package com.toong.service;

import com.toong.modal.dto.PassOrderRequestDto;
import com.toong.modal.entity.AdventurePass;

import java.util.List;

public interface AdventurePassService {

    List<AdventurePass> getAllPass();

    String createPassOrder(PassOrderRequestDto request);

}