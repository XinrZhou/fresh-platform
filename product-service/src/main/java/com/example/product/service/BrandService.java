package com.example.product.service;

import com.example.product.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class BrandService {
    private final BrandRepository brandRepository;
}
