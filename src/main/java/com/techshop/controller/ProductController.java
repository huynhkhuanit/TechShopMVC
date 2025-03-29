package com.techshop.controller;

import com.techshop.model.Product;
import com.techshop.model.CartItem;
import com.techshop.service.CartService;
import com.techshop.service.CategoryService;
import com.techshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CartService cartService;

    @GetMapping("/products")
    public String showProducts(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String search,
            Model model) {

        List<Product> products;

        if (search != null && !search.isEmpty()) {
            products = productService.searchProducts(search);
            if (categoryId != null) {
                products = products.stream()
                        .filter(p -> p.getCategory().getId().equals(categoryId))
                        .toList();
            }
        } else if (categoryId != null) {
            products = productService.getProductsByCategory(categoryId);
        } else {
            products = productService.getAllProducts(sortBy);
        }

        if (sortBy != null && !sortBy.isEmpty()) {
            if (sortBy.equals("priceAsc")) {
                products = products.stream()
                        .sorted((p1, p2) -> Double.compare(p1.getPrice(), p2.getPrice()))
                        .toList();
            } else if (sortBy.equals("priceDesc")) {
                products = products.stream()
                        .sorted((p1, p2) -> Double.compare(p2.getPrice(), p1.getPrice()))
                        .toList();
            }
        }

        model.addAttribute("products", products);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("selectedCategory", categoryId);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("search", search);

        return "products";
    }

    @GetMapping("/products/{id}")
    public String showProductDetail(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Product product = productService.getAllProducts(null).stream()
                    .filter(p -> p.getId().equals(id))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Product not found"));
            model.addAttribute("product", product);
            return "product-detail";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Sản phẩm không tồn tại!");
            return "redirect:/products";
        }
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam Long productId, @RequestParam(defaultValue = "1") int quantity, RedirectAttributes redirectAttributes) {
        try {
            cartService.addToCart(productId, quantity);
            redirectAttributes.addFlashAttribute("message", "Sản phẩm đã được thêm vào giỏ hàng!");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng đăng nhập để thêm sản phẩm vào giỏ hàng!");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi thêm sản phẩm vào giỏ hàng!");
        }
        return "redirect:/cart";
    }

    @GetMapping("/cart")
    public String showCart(Model model, RedirectAttributes redirectAttributes) {
        try {
            List<CartItem> cartItems = cartService.getCartItems();
            double cartTotal = cartService.getCartTotal();
            model.addAttribute("cartItems", cartItems);
            model.addAttribute("cartTotal", cartTotal);
            return "cart";
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng đăng nhập để xem giỏ hàng!");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi tải giỏ hàng!");
            return "redirect:/products";
        }
    }

    @PostMapping("/cart/update")
    public String updateCart(@RequestParam Long cartItemId, @RequestParam int quantity, RedirectAttributes redirectAttributes) {
        try {
            cartService.updateCartItem(cartItemId, quantity);
            redirectAttributes.addFlashAttribute("message", "Giỏ hàng đã được cập nhật!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi cập nhật giỏ hàng!");
        }
        return "redirect:/cart";
    }

    @PostMapping("/cart/remove")
    public String removeFromCart(@RequestParam Long cartItemId, RedirectAttributes redirectAttributes) {
        try {
            cartService.removeCartItem(cartItemId);
            redirectAttributes.addFlashAttribute("message", "Sản phẩm đã được xóa khỏi giỏ hàng!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi xóa sản phẩm khỏi giỏ hàng!");
        }
        return "redirect:/cart";
    }
}