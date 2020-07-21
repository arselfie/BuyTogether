package com.project.demo;

import com.project.demo.entity.*;

import java.util.ArrayList;
import java.util.List;

public class EntityGenerator {

    public static Item generateItem() {
        Item item = new Item();
        item.setItemStatus(ItemStatus.NEW);
        item.setDescription("Apple");
        item.setName("Phone");

        return item;
    }

    public static Address generateAddress() {
        Address address = new Address();
        address.setCity("Minsk");
        address.setPostalCode(53002);
        address.setStreet("Jindrisska");

        return address;

    }

    public static Order generateOrder() {
        Order order = new Order();

        order.setAddress(generateAddress());
        order.setOrderStatus(OrderStatus.NEW);

        List<Item> itemList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            itemList.add(generateItem());
        }
        order.setItemList(itemList);

        return order;
    }

    public static User generateUser() {

        User user= new User();
        user.setName("Max");
        user.setEmail("max@upce.cz");
        user.setLogin("Max");
        user.setPassword("123");
        user.setUserType(UserType.CUSTOMER);


        return user;

    }


}