package com.techshop.controller;

import com.techshop.model.Product;
import com.techshop.service.CategoryService;
import com.techshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/products")
    public String showProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts(null));
        return "admin/products";
    }

    @GetMapping("/products/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/add-product";
    }

    @PostMapping("/products/add")
    public String addProduct(
            @RequestParam String name,
            @RequestParam Long categoryId,
            @RequestParam double price,
            @RequestParam String imageUrl,
            @RequestParam String description,
            RedirectAttributes redirectAttributes) {
        try {
            Product product = new Product();
            product.setName(name);
            product.setPrice(price);
            product.setImageUrl(imageUrl);
            product.setDescription(description);
            productService.addProduct(product, categoryId);
            redirectAttributes.addFlashAttribute("message", "Sản phẩm đã được thêm thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi thêm sản phẩm!");
        }
        return "redirect:/admin/products";
    }

    @GetMapping("/products/edit")
    public String showEditProductForm(@RequestParam Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Product product = productService.getProductById(id);
            model.addAttribute("product", product);
            model.addAttribute("categories", categoryService.getAllCategories());
            return "admin/edit-product";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Sản phẩm không tồn tại!");
            return "redirect:/admin/products";
        }
    }

    @PostMapping("/products/edit")
    public String updateProduct(
            @RequestParam Long id,
            @RequestParam String name,
            @RequestParam Long categoryId,
            @RequestParam double price,
            @RequestParam String imageUrl,
            @RequestParam String description,
            RedirectAttributes redirectAttributes) {
        try {
            Product product = new Product();
            product.setId(id);
            product.setName(name);
            product.setPrice(price);
            product.setImageUrl(imageUrl);
            product.setDescription(description);
            productService.updateProduct(product, categoryId);
            redirectAttributes.addFlashAttribute("message", "Sản phẩm đã được cập nhật thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi cập nhật sản phẩm!");
        }
        return "redirect:/admin/products";
    }

    @PostMapping("/products/delete")
    public String deleteProduct(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("message", "Sản phẩm đã được xóa thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi xóa sản phẩm!");
        }
        return "redirect:/admin/products";
    }
}