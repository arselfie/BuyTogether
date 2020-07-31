package com.project.demo.security;

import com.project.demo.entity.token.TokenType;
import com.project.demo.exceptions.ValidationException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.util.Pair;
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

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @PostConstruct
    public void init() {
        token = Base64.getEncoder().encodeToString(token.getBytes());
    }

    public String createToken(String login, TokenType tokenType) {
        return Jwts.builder()
                .claim("login", login)
                .claim("type", tokenType)
                .setExpiration(Date.from(Instant.now().plusMillis(expiration)))
                .signWith(SignatureAlgorithm.HS256, token)
                .compact();
    }

    public Pair<String, TokenType> getLogin(String userToken) {
        Claims claims = Jwts.parser().setSigningKey(token).parseClaimsJws(userToken).getBody();
        String login = claims.get("login", String.class);
        TokenType tokenType = TokenType.valueOf(claims.get("type", String.class));
        return Pair.of(login, tokenType);
    }

    public Authentication getAuthentication(String userToken) {
        Pair<String, TokenType> tokenPair = getLogin(userToken);
        UserDetails user = jwtUserService.loadUserByUsername(tokenPair.getFirst());
        if (tokenPair.getSecond() != TokenType.USER_AUTHENTICATION) {
            throw new ValidationException("Token is invalid");
        }

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
