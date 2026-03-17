package com.toong.controller;

import com.toong.modal.ApiResponse;
import com.toong.modal.dto.BlogPostRequestDto;
import com.toong.modal.dto.BlogPostResponseDto;
import com.toong.modal.dto.PaginationResponse;
import com.toong.service.BlogPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BlogPostController {

    private final BlogPostService blogPostService;

    /** PUBLIC */
    @GetMapping("/api/v1/blog-posts")
    public ResponseEntity<ApiResponse<List<BlogPostResponseDto>>> getPublished(
            @RequestParam(defaultValue = "6") int limit) {
        return ResponseEntity.ok(ApiResponse.success(blogPostService.getPublishedPosts(limit)));
    }

    @GetMapping("/api/v1/blog-posts/{slug}")
    public ResponseEntity<ApiResponse<BlogPostResponseDto>> getBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(ApiResponse.success(blogPostService.getPostBySlug(slug)));
    }

    /** ADMIN */
    @GetMapping("/api/v1/admin/blog-posts")
    public ResponseEntity<ApiResponse<PaginationResponse<BlogPostResponseDto>>> adminGetAll(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit) {
        return ResponseEntity.ok(ApiResponse.success(blogPostService.adminGetAllPosts(status, page, limit)));
    }

    @PostMapping("/api/v1/admin/blog-posts")
    public ResponseEntity<ApiResponse<BlogPostResponseDto>> create(
            @RequestBody BlogPostRequestDto request, Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(blogPostService.createPost(request, username)));
    }

    @PutMapping("/api/v1/admin/blog-posts/{id}")
    public ResponseEntity<ApiResponse<BlogPostResponseDto>> update(
            @PathVariable Long id, @RequestBody BlogPostRequestDto request) {
        return ResponseEntity.ok(ApiResponse.success(blogPostService.updatePost(id, request)));
    }

    @DeleteMapping("/api/v1/admin/blog-posts/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {
        blogPostService.deletePost(id);
        return ResponseEntity.ok(ApiResponse.success("Bài viết đã được xóa."));
    }
}
