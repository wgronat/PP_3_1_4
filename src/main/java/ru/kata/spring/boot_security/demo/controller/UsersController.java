package ru.kata.spring.boot_security.demo.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping()
public class UsersController {

    private final UserService userService;


    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin")
    public String showAllUsers(Model model) {
        List<User> allUsers = userService.getAllUsers();
        model.addAttribute("allUsers", allUsers);
        return "user-list";
    }

    @GetMapping("/admin/create")
    public String createUserForm(@ModelAttribute("user") User user) {
        return "user-create";
    }

    @PostMapping("/admin")
    public String createUser(@ModelAttribute("user") User user) {
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @DeleteMapping("/admin/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }

    @GetMapping("/admin/{id}/user-update")
    public String updateUserForm(@PathVariable("id") long id, Model model) {
        model.addAttribute("user", userService.findUserByID(id));
        return "user-update";
    }

    @PatchMapping("/admin/{id}")
    public String updateUser(@ModelAttribute("user") User user) {
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/user")
    public String userInfo(Principal principal, Model model) {
        User user = userService.findUserByName(principal.getName());
        model.addAttribute("user", user);
        return "user";
    }
}