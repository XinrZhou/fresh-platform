package com.example.user.service;

import com.example.user.po.User;
import com.example.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Iterator;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SupplierService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public Mono<User> addUser(User user) {
        user.setRole(User.SUPPLIER);
        user.setPassword(encoder.encode(user.getNumber()));
        return userRepository.save(user);
    }

    public Mono<List<User>> listSuppliers(int page, int pageSize) {
        return userRepository.findByRole(User.SUPPLIER, (page -1) * pageSize, pageSize).collectList();
    }

    public Mono<Integer> getSuppliersCount() {
        return userRepository.findCountByRole(User.SUPPLIER);
    }

    public Mono<Void> deleteSupplier(Long sid) {
        return userRepository.deleteById(sid).then();
    }
}
