package com.project.demo.controllers;

import com.project.demo.dto.UserDto;
import com.project.demo.entity.User;
import com.project.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        User user = userDto.toEntity();
        user = userService.createUser(user);
        UserDto userDto1=new UserDto(user);

        return new ResponseEntity<UserDto>(userDto1, HttpStatus.OK);

    }


}
