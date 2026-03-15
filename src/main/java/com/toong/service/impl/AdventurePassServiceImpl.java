package com.toong.service.impl;

import com.toong.modal.dto.PassOrderRequestDto;
import com.toong.modal.entity.AdventurePass;
import com.toong.modal.entity.PassOrder;
import com.toong.repository.AdventurePassRepository;
import com.toong.repository.PassOrderRepository;
import com.toong.service.AdventurePassService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdventurePassServiceImpl implements AdventurePassService {

    private final AdventurePassRepository adventurePassRepository;
    private final PassOrderRepository passOrderRepository;

    @Override
    public List<AdventurePass> getAllPass() {
        return adventurePassRepository.findAll();
    }

    @Override
    public String createPassOrder(PassOrderRequestDto request) {

        AdventurePass pass = adventurePassRepository.findById(request.getPassId())
                .orElseThrow(() -> new RuntimeException("Pass not found"));

        String orderCode = "PASS" + System.currentTimeMillis();

        PassOrder order = new PassOrder();

        order.setOrderCode(orderCode);
        order.setPass(pass);
        order.setFirstName(request.getFirstName());
        order.setLastName(request.getLastName());
        order.setPhone(request.getPhone());
        order.setEmail(request.getEmail());
        order.setPaymentMethod(request.getPaymentMethod());

        passOrderRepository.save(order);

        return orderCode;
    }
}