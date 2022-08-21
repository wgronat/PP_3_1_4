package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/user")
    public String getUserInfo() {
        return "user-page";
    }

    @GetMapping("/admin")
    public String getAllUsers() {
        return "admin-page";
    }
}
