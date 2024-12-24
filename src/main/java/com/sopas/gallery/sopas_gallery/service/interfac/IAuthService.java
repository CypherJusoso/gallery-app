package com.sopas.gallery.sopas_gallery.service.interfac;

import org.springframework.http.ResponseEntity;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface IAuthService {

    ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response);

    ResponseEntity<?> logout(HttpServletResponse response);
}
