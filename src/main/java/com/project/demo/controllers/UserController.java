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
    public ResponseEntity<UserDto> registration(@RequestBody UserDto userDto) {
        User user = userDto.toEntity();
        user = userService.createUser(user);
        UserDto response = new UserDto(user);

        return new ResponseEntity<UserDto>(response, HttpStatus.OK);
    }


    @GetMapping(value = "/test")
    public ResponseEntity<String> test() {

        return new ResponseEntity<>("Test is done", HttpStatus.OK);
    }

    @GetMapping(value = "/activation/{username}")
    public ResponseEntity<?> activateUser(@PathVariable String username) {
        userService.activateUser(username);
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
