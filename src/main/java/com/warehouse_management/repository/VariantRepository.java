package com.warehouse_management.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.warehouse_management.models.Variant;

import java.util.List;

@Repository
public interface VariantRepository extends JpaRepository<Variant, Long> {
    
    List<Variant> findByItemId(Long itemId);
    
    List<Variant> findByStockGreaterThan(Integer stock);
    
}