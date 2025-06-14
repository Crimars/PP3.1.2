package ru.kata.spring.boot_security.demo.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserServiceImpl userServiceImpl;

    @Autowired
    public AdminController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }


    @GetMapping
    public String adminHome() {
        return "index";
    }


    @GetMapping("/users")
    @ResponseBody
    public List<User> getAllUsers() {
        return userServiceImpl.getAllUsers();
    }


    @GetMapping("/users/{id}")
    @ResponseBody
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userServiceImpl.findUserById(id);
        if (user != null && user.getId() != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/users")
    @ResponseBody
    public ResponseEntity<String> createUser(@RequestBody User user) {
        boolean success = userServiceImpl.saveUser(user);
        if (success) {
            return ResponseEntity.ok("User created successfully");
        } else {
            return ResponseEntity.badRequest().body("Username already exists");
        }
    }


    @DeleteMapping("/users/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        boolean success = userServiceImpl.deleteUser(id);
        if (success) {
            return ResponseEntity.ok("User deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/update")
    public String updateUser(@Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "admin";
        }

        if (!userServiceImpl.updateUser(user)) {
            model.addAttribute("error", "Пользователь не найден");
            return "admin";
        }

        model.addAttribute("success", "Пользователь успешно обновлён");
        return "admin";
    }
}