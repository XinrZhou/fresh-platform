package com.example.configuration.service;

import com.example.configuration.po.Page;
import com.example.configuration.repository.PageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class PageService {
    private final PageRepository pageRepository;

    public Mono<Page> addPage(Page page) {
        return pageRepository.save(page);
    }

    public Mono<Page> getPage(String name) {
        return pageRepository.findFirstByName(name);
    }
}
