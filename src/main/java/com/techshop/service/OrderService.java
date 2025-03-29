package com.techshop.service;

import com.techshop.model.*;
import com.techshop.repository.OrderItemRepository;
import com.techshop.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @Transactional
    public Order createOrder(String customerName, String address, String phone, String email) {
        // Lấy người dùng hiện tại
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        if (userEmail == null || userEmail.equals("anonymousUser")) {
            throw new IllegalStateException("User must be logged in to place an order");
        }
        User user = (User) userService.loadUserByUsername(userEmail);

        // Lấy giỏ hàng
        List<CartItem> cartItems = cartService.getCartItems();
        if (cartItems.isEmpty()) {
            throw new IllegalStateException("Cart is empty");
        }

        // Tạo đơn hàng
        Order order = new Order();
        order.setUser(user);
        order.setCustomerName(customerName);
        order.setAddress(address);
        order.setPhone(phone);
        order.setEmail(email);
        order.setTotalAmount(cartService.getCartTotal());
        order.setStatus(OrderStatus.PENDING);

        // Tạo các OrderItem từ CartItem
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice());
            order.getOrderItems().add(orderItem);
        }

        // Lưu đơn hàng
        orderRepository.save(order);

        // Xóa giỏ hàng sau khi đặt hàng thành công
        cartService.clearCart();

        return order;
    }

    public List<Order> getUserOrders() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (email == null || email.equals("anonymousUser")) {
            throw new IllegalStateException("User must be logged in to view orders");
        }
        User user = (User) userService.loadUserByUsername(email);
        return orderRepository.findByUser(user);
    }
}