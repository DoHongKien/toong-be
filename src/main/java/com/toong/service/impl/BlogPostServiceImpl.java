package com.toong.service.impl;

import com.toong.modal.dto.BlogPostRequestDto;
import com.toong.modal.dto.BlogPostResponseDto;
import com.toong.modal.dto.PaginationResponse;
import com.toong.modal.entity.BlogPost;
import com.toong.modal.entity.Employee;
import com.toong.repository.BlogPostRepository;
import com.toong.repository.EmployeeRepository;
import com.toong.service.BlogPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlogPostServiceImpl implements BlogPostService {

    private final BlogPostRepository blogPostRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public List<BlogPostResponseDto> getPublishedPosts(int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "publishedAt"));
        return blogPostRepository.findByStatus("published", pageable)
                .getContent().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public BlogPostResponseDto getPostBySlug(String slug) {
        BlogPost post = blogPostRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Blog post not found"));
        return toDto(post);
    }

    @Override
    public PaginationResponse<BlogPostResponseDto> adminGetAllPosts(String status, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<BlogPost> postPage = (status != null)
                ? blogPostRepository.findByStatus(status, pageable)
                : blogPostRepository.findAll(pageable);

        return PaginationResponse.<BlogPostResponseDto>builder()
                .data(postPage.getContent().stream().map(this::toDto).collect(Collectors.toList()))
                .pagination(PaginationResponse.PaginationMeta.builder()
                        .page(page).limit(limit).total(postPage.getTotalElements()).build())
                .build();
    }

    @Override
    public BlogPostResponseDto createPost(BlogPostRequestDto request, String authorUsername) {
        Employee author = employeeRepository.findByUsername(authorUsername)
                .orElseThrow(() -> new RuntimeException("Author not found"));

        BlogPost post = BlogPost.builder()
                .author(author).title(request.getTitle()).slug(request.getSlug())
                .content(request.getContent()).thumbnail(request.getThumbnail())
                .status(request.getStatus() != null ? request.getStatus() : "draft")
                .createdAt(LocalDateTime.now()).build();

        if ("published".equals(post.getStatus())) post.setPublishedAt(LocalDateTime.now());

        return toDto(blogPostRepository.save(post));
    }

    @Override
    public BlogPostResponseDto updatePost(Long id, BlogPostRequestDto request) {
        BlogPost post = blogPostRepository.findById(id).orElseThrow(() -> new RuntimeException("Blog post not found"));
        if (request.getTitle() != null) post.setTitle(request.getTitle());
        if (request.getSlug() != null) post.setSlug(request.getSlug());
        if (request.getContent() != null) post.setContent(request.getContent());
        if (request.getThumbnail() != null) post.setThumbnail(request.getThumbnail());
        if (request.getStatus() != null) {
            post.setStatus(request.getStatus());
            if ("published".equals(request.getStatus()) && post.getPublishedAt() == null) {
                post.setPublishedAt(LocalDateTime.now());
            }
        }
        return toDto(blogPostRepository.save(post));
    }

    @Override
    public void deletePost(Long id) {
        if (!blogPostRepository.existsById(id)) throw new RuntimeException("Blog post not found");
        blogPostRepository.deleteById(id);
    }

    private BlogPostResponseDto toDto(BlogPost p) {
        String authorName = p.getAuthor() != null ? p.getAuthor().getFullName() : null;
        return BlogPostResponseDto.builder()
                .id(p.getId()).title(p.getTitle()).slug(p.getSlug()).content(p.getContent())
                .thumbnail(p.getThumbnail()).status(p.getStatus())
                .publishedAt(p.getPublishedAt()).createdAt(p.getCreatedAt()).author(authorName).build();
    }
}
