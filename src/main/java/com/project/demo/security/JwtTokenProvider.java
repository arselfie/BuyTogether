package com.project.demo.security;

import com.project.demo.entity.User;
import com.project.demo.exceptions.ValidationException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Autowired
    private JwtUserService jwtUserService;

    @Value("${jwt.secret}")
    private String token;

    @Value("${jwt.expiration}")
    private Long expiration;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostConstruct
    public void init() {

        bCryptPasswordEncoder = new BCryptPasswordEncoder();
        token = Base64.getEncoder().encodeToString(token.getBytes());
    }

    public String createToken(String login) {
        return Jwts.builder()
                .setClaims(Jwts.claims().setSubject(login))
                .setExpiration(Date.from(Instant.now().plusMillis(expiration)))
                .signWith(SignatureAlgorithm.HS256, token)
                .compact();
    }

    public String getUserName(String userToken) {
        return Jwts.parser().setSigningKey(token).parseClaimsJws(userToken).getBody().getSubject();
    }

    public Authentication getAuthentication(String userToken) {
        UserDetails user = jwtUserService.loadUserByUsername(getUserName(userToken));
        return new UsernamePasswordAuthenticationToken(user, "", null);
    }

    public String resolveToken(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getHeader("Authorization");
    }

    public boolean validateToken(String userToken) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(token).parseClaimsJws(userToken);
            return claimsJws.getBody().getExpiration().before(new Date());

        } catch (JwtException | IllegalArgumentException exception) {
            throw new ValidationException("Token's isn't correct");
        }
    }





}
