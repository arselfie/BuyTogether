package com.project.demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.demo.entity.item.Item;
import com.project.demo.entity.item.ItemStatus;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemDto {


    private Long id;

    private String name;

    private String description;

    private ItemStatus itemStatus;

    public ItemDto() {
    }

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
