package com.example.nbs.domain.auth;

import com.example.nbs.web.Global;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @PreAuthorize("hasAuthority('ADMIN')")
    public List<UserEntity> findAll() {

        return userRepository.findAll();

    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public void create(String username, String password, String authority) {

        var encodedPassword = passwordEncoder.encode(password);
        userRepository.insert(username, encodedPassword, authority);

    }

    public long toUserId(String username) {

        Optional<UserEntity> userEntity = userRepository.findByUsername(username);

        return userEntity.get().getId();

    }

}
