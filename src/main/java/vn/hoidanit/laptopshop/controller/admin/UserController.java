package vn.hoidanit.laptopshop.controller.admin;

import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.repository.UserRepository;
import vn.hoidanit.laptopshop.services.UserService;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

//MVC pattern
@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/")
    public String getHomePage(Model model) {
        model.addAttribute("hoidanit", "controller with model");
        return "hello";
    }

    @RequestMapping("/admin/user")
    public String getUserPage(Model model) {
        List<User> arrUsers = this.userService.getAllUsers();
        model.addAttribute("users1", arrUsers);
        return "admin/user/show";
    }

    @RequestMapping("/admin/user/create") // Get method
    public String getCreateUserPage(Model model) {
        model.addAttribute("newUser", new User()); // this is the data type we pass
        return "admin/user/create";
    }

    @RequestMapping("/admin/user/{id}") // Get method
    public String getUserDetailPage(Model model, @PathVariable long id) { // this @PathVariable is to dynamically
                                                                          // get individual user id
        User data = this.userService.getUserById(id);
        model.addAttribute("data", data); // passing data into view
        System.out.println("check data: " + data);
        return "admin/user/detail";
    }

    @RequestMapping("/admin/user/update/{id}") // Get method
    public String updateUserPage(Model model, @PathVariable long id) {
        User currentUser = this.userService.getUserById(id);
        model.addAttribute("newUser", currentUser); // we are passing a param newUser that has value of currentUser
        return "admin/user/update";
    }

    @PostMapping("/admin/user/update") // Post method
    public String postUpdateUser(Model model, @ModelAttribute("newUser") User hoidanit) {
        User currentUser = this.userService.getUserById(hoidanit.getId());
        if (currentUser != null) {
            currentUser.setAddress(hoidanit.getAddress());
            currentUser.setFullName(hoidanit.getFullName());
            currentUser.setPhone(hoidanit.getPhone());

            // here we just set it, but not save to db yet, thus
            this.userService.handleSaveUser(currentUser);
        }
        return "redirect:/admin/user";
    }

    @RequestMapping(value = "/admin/user/create", method = RequestMethod.POST)
    public String createUserPage(Model model, @ModelAttribute("newUser") User hoidanit) { // getting value of user in
                                                                                          // view to save in db
        this.userService.handleSaveUser(hoidanit);
        return "redirect:/admin/user";
    }

    @GetMapping("/admin/user/delete/{id}") // Get method
    public String deleteUserPage(Model model, @PathVariable long id) {
        model.addAttribute("id", id);
        User user = new User();
        user.setId(id);
        model.addAttribute("newUser", user); // this modelAttribute here is to pass data from view to controller
        return "admin/user/delete";
    }

    @PostMapping("/admin/user/delete") // Post method
    public String postDeleteUser(Model model, @ModelAttribute("newUser") User hoidanit) {
        this.userService.deleteById(hoidanit.getId());
        return "redirect:/admin/user";
    }
}
