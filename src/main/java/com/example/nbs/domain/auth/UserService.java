package com.example.nbs.domain.auth;

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
    public void create(String username, String password, String authority, String fullname, String address, String dt) {

        var encodedPassword = passwordEncoder.encode(password);
        userRepository.insert(username, encodedPassword, authority, fullname, address, dt);

    }

    public UserEntity findById(long id) {

        return userRepository.findByUserId(id);

    }

    public long toUserId(String username) {

        Optional<UserEntity> userEntity = userRepository.findByUsername(username);

        return userEntity.get().getId();

    }

    public void updateAuthority(long id, String authority, String dt) {

        userRepository.updateAuthority(id, authority, dt);

    }

    public void updateUsername(long id, String username, String dt) {

        userRepository.updateUsername(id, username, dt);

    }

    public void updatePassword(long id, String password, String dt) {

        var encodedPassword = passwordEncoder.encode(password);
        userRepository.updatePassword(id, encodedPassword, dt);

    }

    public void delete(long id) {

        userRepository.delete(id);

    }

}
