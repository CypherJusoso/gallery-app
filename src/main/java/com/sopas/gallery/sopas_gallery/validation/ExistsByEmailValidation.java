package com.sopas.gallery.sopas_gallery.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sopas.gallery.sopas_gallery.service.impl.UserService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class ExistsByEmailValidation implements ConstraintValidator<ExistsByEmail, String> {

    @Autowired
    private UserService userService;
    
    
    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (userService == null) {
            return true;
        }
        return !userService.existsByEmail(email);
    }

}
