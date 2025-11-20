package com.warehouse_management.controllers;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.warehouse_management.models.Variant;
import com.warehouse_management.service.VariantService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/variants")
public class VariantController {

    @Autowired
    private VariantService variantService;

    @GetMapping
    public ResponseEntity<List<Variant>> getAllVariants() {
        List<Variant> variants = variantService.getAllVariants();
        return ResponseEntity.ok(variants);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Variant> getVariantById(@PathVariable Long id) {
        return variantService.getVariantById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/item/{itemId}")
    public ResponseEntity<List<Variant>> getVariantsByItemId(@PathVariable Long itemId) {
        List<Variant> variants = variantService.getVariantsByItemId(itemId);
        return ResponseEntity.ok(variants);
    }

    @GetMapping("/available")
    public ResponseEntity<List<Variant>> getAvailableVariants() {
        List<Variant> variants = variantService.getAvailableVariants();
        return ResponseEntity.ok(variants);
    }

    @PostMapping("/item/{itemId}")
    public ResponseEntity<Variant> createVariant(
            @PathVariable Long itemId,
            @Valid @RequestBody Variant variant) {
        try {
            Variant createdVariant = variantService.createVariant(itemId, variant);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdVariant);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Variant> updateVariant(
            @PathVariable Long id,
            @Valid @RequestBody Variant variant) {
        try {
            Variant updatedVariant = variantService.updateVariant(id, variant);
            return ResponseEntity.ok(updatedVariant);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteVariant(@PathVariable Long id) {
        try {
            variantService.deleteVariant(id);
            return ResponseEntity.ok(Map.of("message", "Variant deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/reduce-stock")
    public ResponseEntity<Variant> reduceStock(
            @PathVariable Long id,
            @RequestBody Map<String, Integer> request) {
        try {
            Integer quantity = request.get("quantity");
            if (quantity == null || quantity <= 0) {
                return ResponseEntity.badRequest().build();
            }
            Variant updatedVariant = variantService.reduceStock(id, quantity);
            return ResponseEntity.ok(updatedVariant);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{id}/increase-stock")
    public ResponseEntity<Variant> increaseStock(
            @PathVariable Long id,
            @RequestBody Map<String, Integer> request) {
        try {
            Integer quantity = request.get("quantity");
            if (quantity == null || quantity <= 0) {
                return ResponseEntity.badRequest().build();
            }
            Variant updatedVariant = variantService.increaseStock(id, quantity);
            return ResponseEntity.ok(updatedVariant);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

}