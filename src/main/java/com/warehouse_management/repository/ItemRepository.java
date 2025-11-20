package com.warehouse_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.warehouse_management.models.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    
    List<Item> findByNameContainingIgnoreCase(String name);
    
    List<Item> findByStockGreaterThan(Integer stock);
    
}