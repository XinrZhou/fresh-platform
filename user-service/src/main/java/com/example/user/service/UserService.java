package com.example.user.service;

import com.example.common.exception.XException;
import com.example.user.po.User;
import com.example.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public Mono<User> getUser(Long uid) {
        return userRepository.findById(uid);
    }

    public Mono<User> getUser(String number) {
        return userRepository.findByNumber(number);
    }

    @Transactional
    public Mono<User> updateUser(User user) {
        return userRepository.findById(user.getId())
                .flatMap(user1 -> {
                    user1.setName(user.getName());
                    user1.setAvatar(user.getAvatar());
                    return userRepository.save(user1);
                });
    }

    @Transactional
    public Mono<Void> updatePassword(long uid, String pwd) {
        return userRepository.updatePassword(uid, encoder.encode(pwd))
                .doOnSuccess(r -> {
                    if (r == 0) {
                        throw new XException(XException.BAD_REQUEST, "密码重置失败，账号不存在");
                    }
                }).then();
    }
}
