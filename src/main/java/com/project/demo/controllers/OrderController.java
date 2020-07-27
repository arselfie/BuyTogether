package com.project.demo.controllers;

import com.project.demo.dto.OrderDto;
import com.project.demo.entity.order.Order;
import com.project.demo.entity.user.User;
import com.project.demo.services.OrderService;
import com.project.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/api/v1/orders")
public class OrderController extends ControllerAncestor {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;


    @GetMapping(value = "/test")
    public ResponseEntity<String> test() {

        return new ResponseEntity<>("Test is done", HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto orderDto, HttpServletRequest httpServletRequest) {

        User user = getCurrentUser(httpServletRequest);

        Order order = orderDto.toEntity();

        order.setCustomer(user);

        Order response = orderService.createOrder(order);

        return new ResponseEntity<>(new OrderDto(response),HttpStatus.OK);

    }


}
