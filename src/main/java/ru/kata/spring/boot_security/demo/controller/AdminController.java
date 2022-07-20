package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping()
    public String getAllUsers(@AuthenticationPrincipal UserDetails userDetails, Model model) {

        model.addAttribute("allUsers", userService.getAllUsers());
        model.addAttribute("user", userService.findUserByEmail(userDetails.getUsername()));
        model.addAttribute("newUser", new User());
        model.addAttribute("roleList", roleService.getAllRoles());
        return "admin-page";
    }

    @PostMapping()
    public String createUser(@ModelAttribute("user") User user, @RequestParam(value = "inputRoles", required = false) String[] roles) {
        Set<Role> rolesSet = new HashSet<>();

        if (roles == null) {
            rolesSet.add(roleService.getRoleByName("USER"));
        } else {
            for (String role : roles) {
                rolesSet.add(roleService.getRoleByName(role));
            }
        }
        user.setRoles(rolesSet);
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }

    @PatchMapping("/{id}")
    public String updateUser(@ModelAttribute("user") User user, @RequestParam(value = "inputRoles", required = false) String[] roles) {
        Set<Role> rolesSet = new HashSet<>();

        if (roles == null) {
            rolesSet.add(roleService.getRoleByName("USER"));
        } else {
            for (String role : roles) {
                rolesSet.add(roleService.getRoleByName(role));
            }
        }
        user.setRoles(rolesSet);
        userService.saveUser(user);
        return "redirect:/admin";
    }

}