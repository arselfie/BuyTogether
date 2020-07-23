package com.project.demo.services;

import com.project.demo.entity.item.Item;
import com.project.demo.exceptions.ValidationException;
import com.project.demo.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {


    @Autowired
    private ItemRepository itemRepository;

    public List<Item> find() {
        return itemRepository.findAll();
    }

    public Item find(Long id) {
        return itemRepository.findById(id).orElse(null);
    }

    public Item createItem(Item item) {
        if (item == null) {
            throw new ValidationException("Item is null");
        }

        if (item.getName() == null) {
            throw new ValidationException("Item's name is null");
        }

        if (item.getDescription() == null) {
            throw new ValidationException("Item's description is null");
        }

        if (item.getItemStatus() == null) {
            throw new ValidationException("Item's status is null");
        }

        return itemRepository.save(item);
    }

    public Item updateItem(Long id, Item item) {
        Item existedItem = find(id);

        if (existedItem != null) {
            existedItem.setName(item.getName());
            existedItem.setDescription(item.getDescription());
            itemRepository.save(existedItem);
        }
        return existedItem;
    }

    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }


}
