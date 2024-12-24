package com.sopas.gallery.sopas_gallery.security.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sopas.gallery.sopas_gallery.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import static com.sopas.gallery.sopas_gallery.security.TokenJwtConfig.*;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

private AuthenticationManager authenticationManager;


public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
   
    this.authenticationManager = authenticationManager;
}

@Override
public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
        throws AuthenticationException {
            
    User user = null;
    String username = null;
    String password = null;

    try {
        //Convierte el cuerpo JSON de la request a un User
        user = new ObjectMapper().readValue(request.getInputStream(), User.class);
        username = user.getUsername();
        password = user.getPassword();
    } catch (StreamReadException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (DatabindException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

    return authenticationManager.authenticate(authenticationToken);

}

@Override
protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
        Authentication authResult) throws IOException, ServletException {
    
    //Obtengo el nombre y los roles del usuario autenticado
    org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authResult.getPrincipal();
    String username = user.getUsername();
    Collection<? extends GrantedAuthority> roles = authResult.getAuthorities();
    
    boolean rememberMe = Boolean.parseBoolean(request.getParameter("rememberMe"));

    long accessTokenDuration = 3600000; //1 hora
    long refreshTokenDuration = rememberMe ? 2592000000L : 86400000L;

    //Se crea el claim
    Claims claims = Jwts.claims()
    .add("authorities", new ObjectMapper().writeValueAsString(roles))
    .build();

    //JWT crear access token
    String accessToken = Jwts.builder()
        .subject(username) //Identifica al usuario
        .claims(claims)
        .expiration(new Date(System.currentTimeMillis() + accessTokenDuration))
        .issuedAt(new Date())
        .signWith(SECRET_KEY) 
        .compact();

     //JWT crear refresh token
     String refreshToken = Jwts.builder()
     .subject(username) //Identifica al usuario
     .claims(claims)
     .expiration(new Date(System.currentTimeMillis() + refreshTokenDuration))
     .issuedAt(new Date())
     .signWith(SECRET_KEY) 
     .compact();
    
        //Config de accessCookie
        Cookie accessCookie = new Cookie("accessToken", accessToken);
        accessCookie.setHttpOnly(true); //Evita acceso de JS
        accessCookie.setSecure(true); //HTTPS only
        accessCookie.setPath("/"); //Disponible para toda la app
        accessCookie.setMaxAge((int) (accessTokenDuration / 1000));

        //Config de refreshCookie
        Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
        refreshCookie.setHttpOnly(true); //Evita acceso de JS
        refreshCookie.setSecure(true); //HTTPS only
        refreshCookie.setPath("/"); //Disponible para toda la app
        refreshCookie.setMaxAge((int) (refreshTokenDuration/ 1000));
        
        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);

        //Mensaje de respuesta 
        Map<String, String> body = new HashMap<>();
        body.put("username", username);
        body.put("message", String.format("Hello %s you have successfully logged in", username));
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setContentType(CONTENT_TYPE);
        response.setStatus(200);
}

@Override
protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException failed) throws IOException, ServletException {
        Map<String, String> body = new HashMap<>();
        body.put("message", "Error authenticating, username or password incorrect");
        body.put("error", failed.getMessage());

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus(401);
        response.setContentType(CONTENT_TYPE);

    }


}
