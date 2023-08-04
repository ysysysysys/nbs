package com.example.nbs.web.validation;

import com.example.nbs.domain.auth.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername,String> {

    private final UserRepository userRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

         // DBに今回入力されたユーザー名が存在しない場合は、TRUE判定
        return userRepository.findByUsername(value).isEmpty();

    }

}
