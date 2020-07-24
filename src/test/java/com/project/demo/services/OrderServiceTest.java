package com.project.demo.services;

import com.project.demo.EntityGenerator;
import com.project.demo.entity.*;
import com.project.demo.entity.address.Address;
import com.project.demo.entity.item.Item;
import com.project.demo.entity.order.Order;
import com.project.demo.entity.order.OrderStatus;
import com.project.demo.entity.user.User;
import com.project.demo.exceptions.ValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Test
    void createOrderWhenOrderIsNull() {

        ValidationException exception = assertThrows(ValidationException.class, () -> orderService.createOrder(null));
        assertEquals("Order is null", exception.getMessage());
    }

    @Test
    void checkOrderIsNull1() {

        ValidationException exception = assertThrows(ValidationException.class, () -> orderService.isOrderNull(null));
        assertEquals("Order is null", exception.getMessage());
    }

    @Test
    void createOrder() {

        Order order = EntityGenerator.generateOrder();
        User user = EntityGenerator.generateUser();

        user = userService.createUser(user);
        userService.changeStatus(user.getId(),EntityStatus.ACTIVE);
        order.setCustomer(user);
        order = orderService.createOrder(order);

        assertNotNull(order.getId());
    }

    @Test
    void createOrderWhenCustomerIsNull() {
        Order testOrder = new Order();
        Long id;
        id = 5L;
        testOrder.setId(id);

        User customer = new User();
        testOrder.setCustomer(null);

        User courier = new User();
        testOrder.setCourier(courier);

        List<Item> itemList = new ArrayList<>();
        testOrder.setItemList(itemList);

        testOrder.setOrderStatus(OrderStatus.NEW);

        Address address = new Address();
        testOrder.setAddress(address);

        testOrder.setRating(1);

        ValidationException exception = assertThrows(ValidationException.class, () -> orderService.createOrder(testOrder));

        assertEquals("Customer is null", exception.getMessage());
    }


    @Test
    void updateOrder() {
    }

    @Test
    void assignOrder() {
    }
}