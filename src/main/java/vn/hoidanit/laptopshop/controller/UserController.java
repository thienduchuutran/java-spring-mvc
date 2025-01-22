package vn.hoidanit.laptopshop.controller;

import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.laptopshop.services.UserService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

//MVC pattern
@Controller
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/")
    public String getHomePage(Model model) {
        String test = this.userService.handleHello();
        model.addAttribute("eric", test);
        model.addAttribute("hoidanit", "controller with model");
        // return this.userService.handleHello();
        return "hello";
    }

    @RequestMapping("/admin/user")
    public String createNewUser(Model model) {
        return "admin/user/create";
    }
}
// @RestController
// public class UserController {

// // Dependency Injection pattern
// private UserService userService;

// public UserController(UserService userService) {
// this.userService = userService;
// }

// @GetMapping()
// public String getHomePage() {
// return "duc.html";
// }
// }
