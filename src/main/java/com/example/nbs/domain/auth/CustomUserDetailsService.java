package com.example.nbs.domain.auth;

import com.example.nbs.web.Global;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        toUserId(username);

        return userRepository.findByUsername(username)
                .map(
                        user -> new CustomUserDetails(
                                user.getUsername(),
                                user.getPassword(),
                                toGrantedAuthorityList(user.getAuthority())
                        )
                )

                .orElseThrow(
                        () -> new UsernameNotFoundException(
                                "Given username is not found. (username = '" + username + "'"
                        )
                );

    }

    private List<GrantedAuthority> toGrantedAuthorityList(UserEntity.Authority authority) {

        return Collections.singletonList(new SimpleGrantedAuthority(authority.name()));

    }


    private void toUserId(String username) {

        Optional<UserEntity> userEntity = userRepository.findByUsername(username);
        Global.userId = userEntity.get().getId();

    }

}
