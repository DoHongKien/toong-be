package com.toong.controller;

import com.toong.modal.ApiResponse;
import com.toong.modal.dto.BlogPostResponseDto;
import com.toong.service.BlogPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/blog-posts")
@RequiredArgsConstructor
public class BlogPostController {

    private final BlogPostService blogPostService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<BlogPostResponseDto>>> getPublished(
            @RequestParam(defaultValue = "6") int limit) {
        return ResponseEntity.ok(ApiResponse.success(blogPostService.getPublishedPosts(limit)));
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<BlogPostResponseDto>> getBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(ApiResponse.success(blogPostService.getPostBySlug(slug)));
    }
}
