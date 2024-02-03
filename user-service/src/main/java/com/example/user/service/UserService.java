package com.example.user.service;


import com.example.user.po.User;
import com.example.user.repository.UserRepository;
import com.example.user.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public Mono<User> getUser(Long uid) {
        return userRepository.findById(uid);
    }

    public Mono<User> getUser(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    public Mono<Void> addUser(User user) {
        if(user.getRole() == User.CONSUMER) {
            user.setBalance(new BigDecimal(0));
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user).then();
    }
}
