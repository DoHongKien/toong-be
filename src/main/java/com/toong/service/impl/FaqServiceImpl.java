package com.toong.service.impl;

import com.toong.modal.dto.FaqRequestDto;
import com.toong.modal.dto.FaqResponseDto;
import com.toong.modal.entity.GeneralFaq;
import com.toong.repository.GeneralFaqRepository;
import com.toong.service.FaqService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FaqServiceImpl implements FaqService {

    private final GeneralFaqRepository faqRepository;

    @Override
    public List<FaqResponseDto> getAllFaqs() {
        return faqRepository.findAllByOrderBySortOrderAsc()
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public FaqResponseDto createFaq(FaqRequestDto request) {
        GeneralFaq faq = GeneralFaq.builder()
                .question(request.getQuestion()).answer(request.getAnswer())
                .sortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0).build();
        return toDto(faqRepository.save(faq));
    }

    @Override
    public FaqResponseDto updateFaq(Long id, FaqRequestDto request) {
        GeneralFaq faq = faqRepository.findById(id).orElseThrow(() -> new RuntimeException("FAQ not found"));
        if (request.getQuestion() != null) faq.setQuestion(request.getQuestion());
        if (request.getAnswer() != null) faq.setAnswer(request.getAnswer());
        if (request.getSortOrder() != null) faq.setSortOrder(request.getSortOrder());
        return toDto(faqRepository.save(faq));
    }

    @Override
    public void deleteFaq(Long id) {
        if (!faqRepository.existsById(id)) throw new RuntimeException("FAQ not found");
        faqRepository.deleteById(id);
    }

    private FaqResponseDto toDto(GeneralFaq f) {
        return FaqResponseDto.builder()
                .id(f.getId()).question(f.getQuestion()).answer(f.getAnswer()).sortOrder(f.getSortOrder()).build();
    }
}
