package com.example.user.service;


import com.example.user.po.User;
import com.example.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class initService {
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;

    @Transactional
    @EventListener(classes = ApplicationReadyEvent.class)
    public Mono<Void> onApplicationEvent() {
        String userName = "admin";
        String pwd = "20021228";
        String phone = "18312345678";
        return userRepository.count()
                .filter(r -> r == 0)
                .flatMap(r -> {
                    User admin = User.builder()
                            .userName(userName)
                            .password(encoder.encode(pwd))
                            .role(User.ADMIN)
                            .phoneNumber(phone)
                            .insertTime(LocalDateTime.now())
                            .updateTime(LocalDateTime.now())
                            .build();
                    return userRepository.save(admin).then();
                });
    }
}
