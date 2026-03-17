package com.toong.service;

import com.toong.modal.dto.BlogPostRequestDto;
import com.toong.modal.dto.BlogPostResponseDto;
import com.toong.modal.dto.PaginationResponse;

import java.util.List;

public interface BlogPostService {
    List<BlogPostResponseDto> getPublishedPosts(int limit);
    BlogPostResponseDto getPostBySlug(String slug);
    PaginationResponse<BlogPostResponseDto> adminGetAllPosts(String status, int page, int limit);
    BlogPostResponseDto createPost(BlogPostRequestDto request, String authorUsername);
    BlogPostResponseDto updatePost(Long id, BlogPostRequestDto request);
    void deletePost(Long id);
}
