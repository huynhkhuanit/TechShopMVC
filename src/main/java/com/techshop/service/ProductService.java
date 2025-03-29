package com.techshop.service;

import com.techshop.model.Category;
import com.techshop.model.Product;
import com.techshop.repository.CategoryRepository;
import com.techshop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Product> getAllProducts(String sortBy) {
        List<Product> products = productRepository.findAll();
        if (sortBy != null && !sortBy.isEmpty()) {
            if (sortBy.equals("priceAsc")) {
                products.sort(Comparator.comparingDouble(Product::getPrice));
            } else if (sortBy.equals("priceDesc")) {
                products.sort((p1, p2) -> Double.compare(p2.getPrice(), p1.getPrice()));
            }
        }
        return products;
    }

    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    public List<Product> searchProducts(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
    }

    public void addProduct(Product product, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        product.setCategory(category);
        productRepository.save(product);
    }

    public void updateProduct(Product product, Long categoryId) {
        Product existingProduct = productRepository.findById(product.getId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        existingProduct.setName(product.getName());
        existingProduct.setCategory(category);
        existingProduct.setPrice(product.getPrice());
        existingProduct.setImageUrl(product.getImageUrl());
        existingProduct.setDescription(product.getDescription());
        productRepository.save(existingProduct);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        productRepository.delete(product);
    }
}