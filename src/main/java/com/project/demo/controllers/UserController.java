package com.project.demo.controllers;

import com.project.demo.dto.UserDto;
import com.project.demo.entity.user.User;
import com.project.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/auth")
public class UserController extends ControllerAncestor {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/registration")
    public ResponseEntity<?> registration(@RequestBody UserDto userDto) {
        User user = userDto.toEntity();
        userService.createUser(user);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/activation/{token}")
    public ResponseEntity<?> activateUser(@PathVariable String token) {
        userService.activateUser(token);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<UserDto> login(@RequestBody UserDto userDto) {
        String token = userService.login(userDto.getLogin(), userDto.getPassword());
        UserDto response = new UserDto();
        response.setToken(token);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
