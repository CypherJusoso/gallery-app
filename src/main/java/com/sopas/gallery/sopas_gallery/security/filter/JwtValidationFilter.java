package com.sopas.gallery.sopas_gallery.security.filter;

import static com.sopas.gallery.sopas_gallery.security.TokenJwtConfig.CONTENT_TYPE;
import static com.sopas.gallery.sopas_gallery.security.TokenJwtConfig.HEADER_AUTHORIZATION;
import static com.sopas.gallery.sopas_gallery.security.TokenJwtConfig.PREFIX_TOKEN;
import static com.sopas.gallery.sopas_gallery.security.TokenJwtConfig.SECRET_KEY;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sopas.gallery.sopas_gallery.security.SimpleGrantedAuthorityJsonCreator;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtValidationFilter extends BasicAuthenticationFilter{

    public JwtValidationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
                
                //Obtener la cookie jwt   
                String token = extractJwtFromCookies(request.getCookies());
                if(token == null){
                    chain.doFilter(request, response);
                    return;
                }

                try {
                    Claims claims = Jwts.parser()
                        .verifyWith(SECRET_KEY)
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();
                    
                    //Extraer datos del token
                    String username = claims.getSubject();
                    Object authoritiesClaims = claims.get("authorities");
                    
                    //Convertir los roles a objetos GrantedAuthority
                    Collection<? extends GrantedAuthority> authorities = Arrays.asList(new ObjectMapper()
                        .addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityJsonCreator.class)
                        .readValue(authoritiesClaims.toString()
                        .getBytes(), SimpleGrantedAuthority[].class));

                    UsernamePasswordAuthenticationToken  authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                } catch (JwtException e) {
                    log.error("Invalid JWT: {}", e.getMessage());
                    Map<String, String> body = new HashMap<>();
                    body.put("error", e.getMessage());
                    body.put("message", "The JWT token is not valid.");
            
                    response.getWriter().write(new ObjectMapper().writeValueAsString(body));
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType(CONTENT_TYPE);
                    return;
                }
                chain.doFilter(request, response);

        }

        private String extractJwtFromCookies(Cookie[] cookies){
            if(cookies == null) return null;
            for (Cookie cookie : cookies) {
                //Busca cookie llamada jwt
                if("accessToken".equals(cookie.getName())){
                    //Devuelve su valor
                    return cookie.getValue();
                }
            }
            return null;
        }
}
