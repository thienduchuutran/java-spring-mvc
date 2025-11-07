package vn.hoidanit.laptopshop.controller.client;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import vn.hoidanit.laptopshop.domain.Cart;
import vn.hoidanit.laptopshop.domain.CartDetail;
import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.services.ProductService;

@Controller
public class ItemController {

    private final ProductService productService;

    public ItemController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/product/{id}")
    public String getProductPage(Model model, @PathVariable long id) {
        Product product = this.productService.getProductById(id).get(); //we use .get() because in productService.getProductById(id) we use Optional<Product>, so .get() gets any object that Optional returns, just a method of Optional class
        model.addAttribute("product", product);
        model.addAttribute("id", id);
        return "client/product/detail";
    }

    @PostMapping("/add-product-to-cart/{id}")
    public String addProductToCart(@PathVariable long id, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        long productId = id;
        String email = (String) session.getAttribute("email");
        this.productService.addProductToCart(email, productId, session);
        return "redirect:/";
    }

    @GetMapping("/cart")
    public String getCartPage(Model model, HttpServletRequest request) {
        User currentUser = new User();
        HttpSession session = request.getSession(false);
        long userId = (long) session.getAttribute("id");
        currentUser.setId(userId);

        Cart cart = this.productService.getCartByUser(currentUser);
        List<CartDetail> cartDetails = cart == null ? new ArrayList<CartDetail>() : cart.getCartDetails();

        double totalPrice = 0;
        for(CartDetail cartDetail : cartDetails){
            totalPrice += cartDetail.getPrice() * cartDetail.getQuantity();
        }

        model.addAttribute("cartDetails", cartDetails);
        model.addAttribute("totalPrice", totalPrice);
        return "client/cart/show";
    }

    @PostMapping("/delete-cart-item/{id}")
    public String deleteCartItem(@PathVariable long id, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        long cartItemId = id;
        this.productService.deleteCartItem(cartItemId, session);
        return "redirect:/cart";
    }
}
