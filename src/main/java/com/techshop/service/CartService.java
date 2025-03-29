package com.techshop.service;

import com.techshop.model.CartItem;
import com.techshop.model.Product;
import com.techshop.model.User;
import com.techshop.repository.CartItemRepository;
import com.techshop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserService userService;

    public void addToCart(Long productId, int quantity) {
        // Lấy người dùng hiện tại
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (email == null || email.equals("anonymousUser")) {
            throw new IllegalStateException("User must be logged in to add items to cart");
        }
        User user = (User) userService.loadUserByUsername(email);

        // Tìm sản phẩm
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
        Optional<CartItem> existingItem = cartItemRepository.findByUserAndProductId(user, productId);
        if (existingItem.isPresent()) {
            // Nếu đã có, tăng số lượng
            CartItem cartItem = existingItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItemRepository.save(cartItem);
        } else {
            // Nếu chưa có, tạo mới
            CartItem cartItem = new CartItem();
            cartItem.setUser(user);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        }
    }

    public List<CartItem> getCartItems() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (email == null || email.equals("anonymousUser")) {
            throw new IllegalStateException("User must be logged in to view cart");
        }
        User user = (User) userService.loadUserByUsername(email);
        return cartItemRepository.findByUser(user);
    }

    public double getCartTotal() {
        List<CartItem> cartItems = getCartItems();
        return cartItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
    }

    public void updateCartItem(Long cartItemId, int quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found"));
        if (quantity <= 0) {
            cartItemRepository.delete(cartItem);
        } else {
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        }
    }

    public void removeCartItem(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found"));
        cartItemRepository.delete(cartItem);
    }

    public void clearCart() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (email == null || email.equals("anonymousUser")) {
            throw new IllegalStateException("User must be logged in to clear cart");
        }
        User user = (User) userService.loadUserByUsername(email);
        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        cartItemRepository.deleteAll(cartItems);
    }
}