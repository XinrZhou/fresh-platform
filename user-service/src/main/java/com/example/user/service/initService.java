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
        return userRepository.count()
                .filter(r -> r == 0)
                .flatMap(r -> {
                    String number = "18312345678";
                    User admin = User.builder()
                            .name("admin")
                            .number(number)
                            .password(encoder.encode(number))
                            .role(User.ADMIN)
                            .insertTime(LocalDateTime.now())
                            .updateTime(LocalDateTime.now())
                            .build();
                    return userRepository.save(admin).then();
                });
    }
}
