package vn.hoidanit.laptopshop.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import vn.hoidanit.laptopshop.services.UserService;

@Controller
public class DashboardController {
    private final UserService userService;

    public DashboardController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin")
    public String getDashboard(Model model) {
        model.addAttribute("orderCounts", this.userService.CountOrders());
        model.addAttribute("productCounts", this.userService.CountProducts());
        model.addAttribute("userCounts", this.userService.CountUsers());
        return "admin/dashboard/show";
    }
}
