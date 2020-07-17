package com.project.demo.dto;

import com.project.demo.entity.Address;
import com.project.demo.entity.Item;
import com.project.demo.entity.OrderStatus;
import com.project.demo.entity.User;
import lombok.Data;

import java.util.List;

@Data
public class OrderDto {

    private Long id;

    private UserDto customer;

    private UserDto courier;

    private List<ItemDto> itemList;

    private OrderStatus orderStatus;

    private AddressDto address;

    private Integer rating;

}
