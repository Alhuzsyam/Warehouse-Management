package com.warehouse_management.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.warehouse_management.models.Item;
import com.warehouse_management.models.Variant;
import com.warehouse_management.repository.ItemRepository;
import com.warehouse_management.repository.VariantRepository;

import java.util.List;
import java.util.Optional;

@Service
public class VariantService {

    @Autowired
    private VariantRepository variantRepository;

    @Autowired
    private ItemRepository itemRepository;

    public List<Variant> getAllVariants() {
        return variantRepository.findAll();
    }

    public Optional<Variant> getVariantById(Long id) {
        return variantRepository.findById(id);
    }

    public List<Variant> getVariantsByItemId(Long itemId) {
        return variantRepository.findByItemId(itemId);
    }

    public List<Variant> getAvailableVariants() {
        return variantRepository.findByStockGreaterThan(0);
    }

    @Transactional
    public Variant createVariant(Long itemId, Variant variant) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + itemId));

        variant.setItem(item);
        return variantRepository.save(variant);
    }

    @Transactional
    public Variant updateVariant(Long id, Variant variantDetails) {
        Variant variant = variantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Variant not found with id: " + id));

        variant.setName(variantDetails.getName());
        variant.setDescription(variantDetails.getDescription());
        variant.setPrice(variantDetails.getPrice());
        variant.setStock(variantDetails.getStock());

        return variantRepository.save(variant);
    }

    @Transactional
    public void deleteVariant(Long id) {
        Variant variant = variantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Variant not found with id: " + id));
        variantRepository.delete(variant);
    }

    @Transactional
    public Variant reduceStock(Long id, Integer quantity) {
        Variant variant = variantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Variant not found with id: " + id));

        if (variant.getStock() < quantity) {
            throw new RuntimeException("Insufficient stock. Available: " + variant.getStock() + ", Requested: " + quantity);
        }

        variant.setStock(variant.getStock() - quantity);
        return variantRepository.save(variant);
    }

    @Transactional
    public Variant increaseStock(Long id, Integer quantity) {
        Variant variant = variantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Variant not found with id: " + id));

        variant.setStock(variant.getStock() + quantity);
        return variantRepository.save(variant);
    }

}