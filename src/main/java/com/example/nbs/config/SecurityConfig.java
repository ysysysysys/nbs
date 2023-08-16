package com.example.nbs.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.formLogin(login -> login
                .loginPage("/login")                                                // ユーザー定義ログインページ
                .defaultSuccessUrl("/")                                             // ログイン成功後の移動ページ
                .failureUrl("/login?error")                       // ログイン失敗後の移動ページ
                .usernameParameter("username")                                      // IDパラメータ名設定
                .passwordParameter("password")                                      // パスワードパラメータ名の設定
                .loginProcessingUrl("/login")                                       // ログインFormActionUrl
        ).logout(logout -> logout
                .logoutSuccessUrl("/")
        ).authorizeHttpRequests(authorize -> authorize
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .requestMatchers("/login/**").permitAll()                                    // /login階層以降のアクセスは認可不要
                .anyRequest().authenticated()                                                         // 全てのURLリクエストは認証されているユーザーしかアクセスできない

        ).csrf(csrf -> csrf
                .ignoringRequestMatchers("/notice/**")           // "/notice/" で始まるリクエストはすべてCSRF保護を除外する
                .ignoringRequestMatchers("/draft/**")
                .ignoringRequestMatchers("/upload/**")
                .ignoringRequestMatchers("/cancel/**")

        );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8();

    }

}
