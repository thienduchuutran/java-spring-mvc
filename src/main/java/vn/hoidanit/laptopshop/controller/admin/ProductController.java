package vn.hoidanit.laptopshop.controller.admin;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.services.ProductService;
import vn.hoidanit.laptopshop.services.UploadService;

@Controller
public class ProductController {
    // private final ProductService productService;
    private final UploadService uploadService;
    private final ProductService productService;

    public ProductController(UploadService uploadService, ProductService productService) {
        this.uploadService = uploadService;
        this.productService = productService;
    }

    @GetMapping("/admin/product")
    public String getProduct(Model model) {
        List<Product> products = this.productService.fetchProducts();
        model.addAttribute("products", products);
        return "/admin/product/show";
    }

    @GetMapping("/admin/product/create")
    public String getCreateProductPage(Model model) {
        model.addAttribute("newProduct", new Product());
        return "/admin/product/create";
    }

    @PostMapping("/admin/product/create")
    public String createProduct(@ModelAttribute("newProduct") @Valid Product newProduct, BindingResult newProductBindingResult,
     @RequestParam("hoidanitFile") MultipartFile file) {
        if (newProductBindingResult.hasErrors()) {
            return "/admin/product/create";
        }
        String image = this.uploadService.handleSaveUploadFile(file, "image");
        newProduct.setImage(image);
        this.productService.createProduct(newProduct);
        return "redirect:/admin/product";
    }
}
