package com.LMS.controller;

import com.LMS.entity.User;
import com.LMS.entity.Role;
import com.LMS.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.LMS.exception.ResourceNotFoundException;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminUserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    public String manageUsers(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "admin-users";
    }


    @PostMapping("/user/delete/{id}")
    @ResponseBody
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "User deleted successfully";
    }

    @PostMapping("/user/role/{id}")
    @ResponseBody
    public String updateUserRole(@PathVariable Long id,
                                 @RequestParam("role") String role) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        user.setRole(Role.valueOf(role));
        userRepository.save(user);

        return "Role updated to " + role;
    }
}

