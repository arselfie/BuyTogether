package com.project.demo.controllers;

import com.project.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/orders")
public class OrderController extends ControllerAncestor {

    @Autowired
    private UserService userService;


    @GetMapping(value = "/test")
    public ResponseEntity<String> test() {

        return new ResponseEntity<>("Test is done", HttpStatus.OK);
    }




}
