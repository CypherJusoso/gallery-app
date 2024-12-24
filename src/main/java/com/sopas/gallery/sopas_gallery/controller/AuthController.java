package com.sopas.gallery.sopas_gallery.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sopas.gallery.sopas_gallery.dto.Response;
import com.sopas.gallery.sopas_gallery.entity.User;
import com.sopas.gallery.sopas_gallery.service.interfac.IAuthService;
import com.sopas.gallery.sopas_gallery.service.interfac.IUserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IAuthService authService;

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {        
        return authService.refreshToken(request, response);
    }
    

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
       return authService.logout(response);
    }
    

  @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody User user, BindingResult result) {
        if(result.hasFieldErrors()){
            return validation(result);
        }
        user.setAdmin(false);
        Response response = userService.register(user);   

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


     private ResponseEntity<?> validation(BindingResult result){
        Map<String, String> errors = new HashMap<>();

        result.getFieldErrors().forEach(err ->{
            errors.put(err.getField(), "The value " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }
}
