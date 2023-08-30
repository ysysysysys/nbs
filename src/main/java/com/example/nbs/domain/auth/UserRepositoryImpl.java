package com.example.nbs.domain.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserRepository userRepository;

    public List<UserEntity> findAll() {

        return userRepository.findAll();

    }

    public UserEntity findByUserId(long id) {

        return userRepository.findByUserId(id);

    }

    public Optional<UserEntity> findByUsername(String username) {

        return userRepository.findByUsername(username);

    }

    public void insert(String username, String password, String authority, String fullname, String address, String dt) {

        userRepository.insert(username, password, authority, fullname, address, dt);

    }

    public void updateAuthority(long id, String authority, String dt) {

        userRepository.updateAuthority(id, authority, dt);

    }

    public void updateBasicInfo(long id, String fullname, String address, String dt) {

        userRepository.updateBasicInfo(id, fullname, address, dt);

    }

    public void updateUsername(long id, String username, String dt) {

        userRepository.updateUsername(id, username, dt);

    }

    public void updatePassword(long id, String password, String dt) {

        userRepository.updatePassword(id, password, dt);

    }

    public void delete(long id) {

        userRepository.delete(id);

    }

}
