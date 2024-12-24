package com.sopas.gallery.sopas_gallery.service.impl;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.sopas.gallery.sopas_gallery.dto.Response;
import com.sopas.gallery.sopas_gallery.service.interfac.IAuthService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static com.sopas.gallery.sopas_gallery.security.TokenJwtConfig.*;

import java.util.Date;

@Service
public class AuthService implements IAuthService{

    private static final long JWT_EXPIRATION_TIME = 3600000; 
    @Override
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        Response responseBody = new Response();
        //Obtener la cookie con el JWT
        String refreshToken = null;
        if(request.getCookies() != null){
            for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
                if("refreshToken".equals(cookie.getName())){
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        if (refreshToken == null){
            return ResponseEntity.status(401).body("Token not found");

        }
        try {
            //Valido el token con secret key
            Claims claims = Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(refreshToken)
                .getPayload();
            
            String subject = claims.getSubject();

            //Creo un nuevo access token
            String newAccessToken = Jwts.builder()
                .subject(subject)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();

            ResponseCookie accessCookie = ResponseCookie.from("accessToken", newAccessToken)
                .path("/")
                .httpOnly(true)
                .secure(true)
                .maxAge(JWT_EXPIRATION_TIME / 1000)
                .build();

            //Se incluye en el header la respuesta http con el cookie
            response.addHeader("Set-Cookie", accessCookie.toString());
            
            responseBody.setMessage("Token refreshed");
            responseBody.setStatusCode(200);
            return ResponseEntity.ok(responseBody);
        } catch (Exception e) {
            responseBody.setMessage("Invalid refresh token");
            responseBody.setStatusCode(401);
            return ResponseEntity.status(responseBody.getStatusCode()).body(responseBody);
        }
    }
    @Override
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Response responseBody = new Response();
        try {
            ResponseCookie accessCookie = ResponseCookie.from("accessToken",null)
            .path("/")
            .httpOnly(true)
            .secure(true)
            .maxAge(0)
            .build();
            
            ResponseCookie refreshCookie = ResponseCookie.from("refreshToken",null)
            .path("/")
            .httpOnly(true)
            .secure(true)
            .maxAge(0)
            .build();
            
            response.addHeader("Set-Cookie", accessCookie.toString());
            response.addHeader("Set-Cookie", refreshCookie.toString());

            responseBody.setMessage("Logout successful");
            responseBody.setStatusCode(200);
            return ResponseEntity.ok(responseBody);
        } catch (Exception e) {
            responseBody.setStatusCode(500);
            responseBody.setMessage("Logout failed " + e.getMessage());
            return ResponseEntity.status(responseBody.getStatusCode()).body(responseBody);
        }
    }

}
