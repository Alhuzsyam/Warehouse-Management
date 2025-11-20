package com.warehouse_management.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.warehouse_management.models.Item;
import com.warehouse_management.repository.ItemRepository;

import jakarta.transaction.Transactional;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public Optional<Item> getItemById(Long id) {
        return itemRepository.findById(id);
    }

    public List<Item> searchItemsByName(String name) {
        return itemRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Item> getAvailableItems() {
        return itemRepository.findByStockGreaterThan(0);
    }

    @Transactional
    public Item createItem(Item item) {
        return itemRepository.save(item);
    }

    @Transactional
    public Item updateItem(Long id, Item itemDetails) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));

        item.setName(itemDetails.getName());
        item.setDescription(itemDetails.getDescription());
        item.setPrice(itemDetails.getPrice());
        item.setStock(itemDetails.getStock());

        return itemRepository.save(item);
    }

    @Transactional
    public void deleteItem(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));
        itemRepository.delete(item);
    }

    @Transactional
    public Item reduceStock(Long id, Integer quantity) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));

        if (item.getStock() < quantity) {
            throw new RuntimeException("Insufficient stock. Available: " + item.getStock() + ", Requested: " + quantity);
        }

        item.setStock(item.getStock() - quantity);
        return itemRepository.save(item);
    }

    @Transactional
    public Item increaseStock(Long id, Integer quantity) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));

        item.setStock(item.getStock() + quantity);
        return itemRepository.save(item);
    }

}