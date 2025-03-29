package com.techshop.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpServletRequest;  // Thay đổi import này
import jakarta.servlet.http.HttpServletResponse; // Thay đổi import này

@Controller
public class LogoutController {

    @PostMapping("/logout")  // Xử lý logout bằng POST
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        // Xóa thông tin đăng nhập khỏi security context
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());

        // Quay lại trang chủ hoặc có thể điều hướng về trang đăng nhập
        return "redirect:/";  // Điều hướng về trang chủ sau khi đăng xuất
    }
}
