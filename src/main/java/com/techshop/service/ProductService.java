package com.techshop.service;

import com.techshop.model.Product;
import com.techshop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts(String sortBy) {
        if (sortBy != null) {
            if (sortBy.equals("priceAsc")) {
                return productRepository.findAll(Sort.by(Sort.Direction.ASC, "price"));
            } else if (sortBy.equals("priceDesc")) {
                return productRepository.findAll(Sort.by(Sort.Direction.DESC, "price"));
            }
        }
        return productRepository.findAll();
    }

    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    public List<Product> searchProducts(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }
}