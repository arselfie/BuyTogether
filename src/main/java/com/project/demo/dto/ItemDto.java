package com.project.demo.dto;

import com.project.demo.entity.ItemStatus;
import com.project.demo.entity.Order;

public class ItemDto {

    private Long id;

    private String name;

    private String description;

    private ItemStatus itemStatus;

    private OrderDto order;
}
