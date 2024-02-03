package com.example.user.service;

import com.example.user.po.User;
import com.example.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;

    public Mono<Void> addUser(User user) {
        return userRepository.save(user).then();
    }
}
