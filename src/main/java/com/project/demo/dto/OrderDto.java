package com.project.demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.demo.entity.order.Order;
import com.project.demo.entity.order.OrderStatus;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDto {

    private Long id;

    private UserDto customer;

    private UserDto courier;

    private List<ItemDto> itemList;

    private OrderStatus orderStatus;

    private AddressDto address;

    private Integer rating;

    public OrderDto(Order order) {

        if (order.getCourier() != null) {

            this.courier = new UserDto(order.getCourier());

        }

        this.id = order.getId();
        this.customer = new UserDto(order.getCustomer());
        this.courier = new UserDto(order.getCourier());
        this.itemList = order.getItemList().stream().map(ItemDto::new).collect(Collectors.toList());
        this.orderStatus = order.getOrderStatus();
        this.address = new AddressDto(order.getAddress());
        this.rating = order.getRating();
    }

    public Order toEntity() {
        Order order = new Order();

        if (courier != null) {

            order.setCourier(courier.toEntity());
        }

        order.setId(id);
        order.setCustomer(customer.toEntity());
        order.setItemList(itemList.stream().map(ItemDto::toEntity).collect(Collectors.toList()));
        order.setOrderStatus(orderStatus);
        order.setAddress(address.toEntity());
        order.setRating(rating);


        return order;

    }

}
