package com.toong.controller;

import com.toong.modal.ApiResponse;
import com.toong.modal.entity.Tour;
import com.toong.repository.TourRepository;
import com.toong.service.MinioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Endpoint migration: download ảnh từ URL ngoài, upload lên MinIO theo cấu trúc
 *   tours/{region}/{slug}/hero.jpg
 *   tours/{region}/{slug}/card.jpg
 * rồi cập nhật DB luôn.
 *
 * Endpoint được mở public (không cần JWT) — chỉ dùng nội bộ khi seeding data.
 * POST /api/v1/migrate/tour-images
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/migrate")
@RequiredArgsConstructor
public class MigrationController {

    private final TourRepository tourRepository;
    private final MinioService minioService;

    /**
     * Migrate ảnh của tất cả tours:
     *  - Với mỗi tour, nếu heroImage / cardImage là URL ngoài (bắt đầu bằng http)
     *    → download rồi upload lên MinIO theo path: tours/{region}/{slug}/hero.jpg
     *  - Update DB với MinIO object path mới
     *
     * Query param:
     *   dryRun=true  → chỉ liệt kê, không thực sự upload/update
     *   dryRun=false → thực hiện migration thật (mặc định)
     */
    @PostMapping("/tour-images")
    public ResponseEntity<ApiResponse<Map<String, Object>>> migrateTourImages(
            @RequestParam(defaultValue = "false") boolean dryRun) {

        List<Tour> tours = tourRepository.findAll();
        log.info("[Migration] Bắt đầu migrate ảnh cho {} tours. dryRun={}", tours.size(), dryRun);

        List<Map<String, Object>> results = new ArrayList<>();
        int successCount = 0;
        int skipCount = 0;
        int failCount = 0;

        for (Tour tour : tours) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", tour.getId());
            item.put("slug", tour.getSlug());
            item.put("region", tour.getRegion());

            String region = tour.getRegion() != null ? tour.getRegion() : "unknown";
            String slug   = tour.getSlug()   != null ? tour.getSlug()   : "tour-" + tour.getId();
            String basePath = "tours/" + region + "/" + slug;

            boolean changed = false;

            // ── Hero image ──────────────────────────────────────────────────
            String heroResult = processImage(
                    tour.getHeroImage(), basePath + "/hero.jpg", dryRun);
            item.put("hero", heroResult);
            if (heroResult.startsWith("OK:")) {
                if (!dryRun) tour.setHeroImage(heroResult.substring(3));
                changed = true;
                successCount++;
            } else if (heroResult.startsWith("SKIP")) {
                skipCount++;
            } else {
                failCount++;
            }

            // ── Card image ──────────────────────────────────────────────────
            String cardResult = processImage(
                    tour.getCardImage(), basePath + "/card.jpg", dryRun);
            item.put("card", cardResult);
            if (cardResult.startsWith("OK:")) {
                if (!dryRun) tour.setCardImage(cardResult.substring(3));
                changed = true;
                successCount++;
            } else if (cardResult.startsWith("SKIP")) {
                skipCount++;
            } else {
                failCount++;
            }

            // ── Lưu DB nếu có thay đổi ─────────────────────────────────────
            if (!dryRun && changed) {
                tourRepository.save(tour);
                item.put("dbUpdated", true);
            } else {
                item.put("dbUpdated", false);
            }

            results.add(item);
        }

        Map<String, Object> summary = new HashMap<>();
        summary.put("total_tours", tours.size());
        summary.put("success", successCount);
        summary.put("skipped", skipCount);
        summary.put("failed", failCount);
        summary.put("dryRun", dryRun);
        summary.put("details", results);

        log.info("[Migration] Done. success={}, skip={}, fail={}", successCount, skipCount, failCount);
        return ResponseEntity.ok(ApiResponse.success(summary));
    }

    /**
     * Upload một ảnh từ URL lên MinIO.
     *
     * @return "OK:{objectPath}"  nếu upload thành công
     *         "SKIP:{lý do}"    nếu không cần upload (đã là MinIO path hoặc null)
     *         "FAIL:{message}"  nếu lỗi
     */
    private String processImage(String currentUrl, String targetPath, boolean dryRun) {
        if (currentUrl == null || currentUrl.isBlank()) {
            return "SKIP:empty";
        }
        // Nếu currentUrl không bắt đầu bằng http → đang là MinIO object path rồi
        if (!currentUrl.startsWith("http")) {
            return "SKIP:already_minio:" + currentUrl;
        }
        if (dryRun) {
            return "OK:" + targetPath + " [DRY_RUN - not actually uploaded]";
        }
        try {
            String savedPath = minioService.uploadImageFromUrl(currentUrl, targetPath);
            return "OK:" + savedPath;
        } catch (Exception e) {
            log.error("[Migration] Lỗi upload {} → {}: {}", currentUrl, targetPath, e.getMessage());
            return "FAIL:" + e.getMessage();
        }
    }
}
