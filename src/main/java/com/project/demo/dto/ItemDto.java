package com.project.demo.dto;

import com.project.demo.entity.Address;
import com.project.demo.entity.Item;
import com.project.demo.entity.ItemStatus;
import com.project.demo.entity.Order;

public class ItemDto {

    private Long id;

    private String name;

    private String description;

    private ItemStatus itemStatus;

    private OrderDto order;

    public ItemDto(Item item) {
        this.id = item.getId();
        this.name = item.getName();
        this.description = item.getDescription();
        this.itemStatus=item.getItemStatus();

    }

    public Item toEntity() {
        Item item = new Item();

        item.setId(id);
        item.setName(name);
        item.setDescription(description);
        item.setItemStatus(itemStatus);

        return item;

    }
}
