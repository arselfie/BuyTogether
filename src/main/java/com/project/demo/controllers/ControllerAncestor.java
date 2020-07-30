package com.project.demo.controllers;

import com.project.demo.entity.user.User;
import com.project.demo.exceptions.ValidationException;
import com.project.demo.security.JwtTokenProvider;
import com.project.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

public class ControllerAncestor {

    @Autowired
    private UserService userService;


    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    protected User getCurrentUser(HttpServletRequest httpServletRequest) {
        String token = jwtTokenProvider.resolveToken(httpServletRequest);

        if (token == null) {
            throw new ValidationException("Token is null");
        }

        String username = jwtTokenProvider.getAuthentication(token).getPrincipal().toString();

        if (username == null) {
            throw new ValidationException("Username is null");
        }

        User user = userService.findByLogin(username);

        if (user==null){
            throw new ValidationException("User not exist");
        }

        return user;
    }

}
