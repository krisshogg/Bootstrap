package com.andreyb34rus.JM_Task_3_1_1.controller;

import com.andreyb34rus.JM_Task_3_1_1.model.Role;
import com.andreyb34rus.JM_Task_3_1_1.model.User;
import com.andreyb34rus.JM_Task_3_1_1.repository.RoleRepository;
import com.andreyb34rus.JM_Task_3_1_1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private UserService userService;
    private final RoleRepository roleRepository;

    @Autowired
    public AdminController (UserService userService,
                            RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping()
    public String viewUsers(Principal principal, Model model) {
        User authorizedUser = userService.getUserByName(principal.getName());
        List<User> userListToView = userService.getAllUsers();
        model.addAttribute("authorizedUser", authorizedUser);
        model.addAttribute("users", userListToView);
        model.addAttribute("newUser", new User());
        model.addAttribute("listRoles", userService.getAllRoles());
        return "usersList";
    }

    @GetMapping("/addUser")
    public String newUserPage(@ModelAttribute User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("listRoles", userService.getAllRoles());
        return "newUser";
    }

    @PostMapping("")
    public String createUser(@ModelAttribute("user") User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "newUser";
        }

/*        Set<Role> roles = new HashSet<>();
        for (int roleId : rolesId) {
            roles.add(roleRepository.getById(roleId));
        }

        user.setRoles(roles);*/
        userService.save(user);
        return "redirect:/admin";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("user") User user) {
        userService.update(user);
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        userService.delete(id);
        return "redirect:/admin";
    }

}
