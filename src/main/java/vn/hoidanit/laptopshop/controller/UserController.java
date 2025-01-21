package vn.hoidanit.laptopshop.controller;

import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.laptopshop.services.UserService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

//MVC pattern
@Controller
public class UserController {

    @RequestMapping("/")
    public String getHomePage() {
        return "duc.html";
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
