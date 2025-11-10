package vn.hoidanit.laptopshop.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import vn.hoidanit.laptopshop.domain.Cart;
import vn.hoidanit.laptopshop.domain.CartDetail;
import vn.hoidanit.laptopshop.domain.Order;
import vn.hoidanit.laptopshop.domain.OrderDetail;
import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.domain.Product_;
import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.repository.CartDetailRepository;
import vn.hoidanit.laptopshop.repository.CartRepository;
import vn.hoidanit.laptopshop.repository.OrderDetailRepository;
import vn.hoidanit.laptopshop.repository.OrderRepository;
import vn.hoidanit.laptopshop.repository.ProductRepository;
import vn.hoidanit.laptopshop.services.specification.ProductSpecs;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;
    private final UserService userService;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    public ProductService(ProductRepository productRepository, CartRepository cartRepository, CartDetailRepository cartDetailRepository, UserService userService, OrderRepository orderRepository, OrderDetailRepository orderDetailRepository) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.cartDetailRepository = cartDetailRepository;
        this.userService = userService;
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
    }

    public Product createProduct(Product product) {
        return this.productRepository.save(product);
    }

    private Specification<Product> nameLike(String name){
        return (root, query, criteriaBuilder) 
            -> criteriaBuilder.like(root.get(Product_.NAME), "%"+name+"%");
      }
      
    public Page<Product> fetchProducts(Pageable page) {
        return this.productRepository.findAll(page);
    }

    // public Page<Product> fetchProductsWithSpec(Pageable page, String name) {
    //     return this.productRepository.findAll(ProductSpecs.nameLike(name), page);
    // }

    //case 1
    // public Page<Product> fetchProductsWithSpec(Pageable page, double min) {
    //     return this.productRepository.findAll(ProductSpecs.minPrice(min), page);
    // }

    //case 2
    // public Page<Product> fetchProductsWithSpec(Pageable page, double min) {
    //     return this.productRepository.findAll(ProductSpecs.maxPrice(min), page);
    // }

    //case 3
    // public Page<Product> fetchProductsWithSpec(Pageable page, String factory) {
    //     return this.productRepository.findAll(ProductSpecs.matchFactory(factory), page);
    // }

    //case 4
    // public Page<Product> fetchProductsWithSpec(Pageable page, List<String> factories) {
    //     return this.productRepository.findAll(ProductSpecs.matchFactories(factories), page);
    // }

    //case 5
    // public Page<Product> fetchProductsWithSpec(Pageable page, String price) {
    //     if (price.equals("UNDER-500")) {
    //         double min = 0;
    //         double max = 500;
    //         return this.productRepository.findAll(ProductSpecs.matchPrice(min, max), page);
    //     } else if (price.equals("FROM-500-TO-1000")) {
    //         double min = 500;
    //         double max = 1000;
    //         return this.productRepository.findAll(ProductSpecs.matchPrice(min, max), page);
    //     } else if (price.equals("FROM-1000-TO-1500")) {
    //         double min = 1000;
    //         double max = 1500;
    //         return this.productRepository.findAll(ProductSpecs.matchPrice(min, max), page);
    //     } else if (price.equals("FROM-1500-TO-2000")) {
    //         double min = 1500;
    //         double max = 2000;
    //         return this.productRepository.findAll(ProductSpecs.matchPrice(min, max), page);
    //     } else {
    //         return this.productRepository.findAll(page);
    //     }
    // }

    //case 6
    // public Page<Product> fetchProductsWithSpec(Pageable page, List<String> price){
    //     Specification<Product> combinedSpec = (root, query, criteriaBuilder) -> criteriaBuilder.disjunction();
    //     int count = 0;
    //     for (String p : price) {
    //         double min = 0;
    //         double max = 0;

    //         //set appropriate min and max based on price range
    //         switch(p){
    //             case "UNDER-500":
    //                 min = 0;
    //                 max = 500;
    //                 count++;
    //                 break;
    //             case "FROM-500-TO-1000":
    //                 min = 500;
    //                 max = 1000;
    //                 count++;
    //                 break;
    //             case "FROM-1000-TO-1500":
    //                 min = 1000;
    //                 max = 1500;
    //                 count++;
    //                 break;
    //             case "FROM-1500-TO-2000":
    //         }

    //         if(min != 0 && max != 0){
    //             Specification<Product> rangeSpec = ProductSpecs.matchMultiplePrice(min, max);
    //             combinedSpec = combinedSpec.or(rangeSpec);
    //         }
    //     }

    //     if (count == 0){
    //         return this.productRepository.findAll(page);
    //     }
    //     return this.productRepository.findAll(combinedSpec, page);
    // }

    public Optional<Product> getProductById(long id) {
        return this.productRepository.findById(id);
    }

    public void deleteProduct(long id) {
        this.productRepository.deleteById(id);
    }

    public Product updateProduct(Product product) {
        return this.productRepository.save(product);
    }

    public void addProductToCart(String email, long productId, HttpSession session, long quantity) {
        User user = this.userService.getUserByEmail(email);
        if(user != null){
            //if user ain't have cart yet -> create new cart
            Cart cart = this.cartRepository.findByUser(user);
            if(cart == null){
                Cart otherCart = new Cart();
                otherCart.setUser(user);
                otherCart.setSum(0);

                cart = this.cartRepository.save(otherCart);
            }
            //save cart_detail
            //find product by id
            Optional<Product> productOptional = this.productRepository.findById(productId);
            if(productOptional.isPresent()){
                Product realProduct = productOptional.get();

                //update total number of items in cart to display on UI
                int sum = (int) (cart.getSum() + quantity);
                cart.setSum(sum);
                this.cartRepository.save(cart);
                session.setAttribute("cartSum", sum);

                //check if product already in cart
                CartDetail productExistsInCart = this.cartDetailRepository.findByCartAndProduct(cart, realProduct);
                //if product already in cart -> update quantity
                if(productExistsInCart != null){
                    productExistsInCart.setQuantity(productExistsInCart.getQuantity() + quantity);
                    this.cartDetailRepository.save(productExistsInCart);
                } else {
                    //if product not in cart -> create new cart_detail row representing anew product in cart
                    CartDetail cartDetail = new CartDetail();
                    cartDetail.setCart(cart);
                    cartDetail.setProduct(realProduct);
                    cartDetail.setQuantity(quantity);
                    cartDetail.setPrice(realProduct.getPrice());
                    this.cartDetailRepository.save(cartDetail);
                }
            }
        }
    }

    public Cart getCartByUser(User user) {
        return this.cartRepository.findByUser(user);
    }

    public void deleteCartItem(long cartItemId, HttpSession session) {
        Optional<CartDetail> cartItemDetail = this.cartDetailRepository.findById(cartItemId);
        if(cartItemDetail.isPresent()){
            CartDetail cartItem = cartItemDetail.get();
            Cart currentCart = cartItem.getCart();
            long numberOfItemsDeleted = cartItem.getQuantity();
            //delete cart item
            this.cartDetailRepository.deleteById(cartItemId);

            //update cart
            if (currentCart.getSum() > 1) {
                //update current cart
                int s = (int) (currentCart.getSum() - numberOfItemsDeleted);
                currentCart.setSum(s);
                session.setAttribute("cartSum", s);
                this.cartRepository.save(currentCart);  
            } else {
                //delete cart if sum cart = 1
                this.cartRepository.deleteById(currentCart.getId());
                session.setAttribute("cartSum", 0);
            }
        } 
    }

    public void updateCartBeforeCheckout(List<CartDetail> cartDetails) {
        for(CartDetail cartDetail : cartDetails){
            Optional<CartDetail> cartDetailOptional = this.cartDetailRepository.findById(cartDetail.getId());
            if(cartDetailOptional.isPresent()){
                CartDetail currentCartDetail = cartDetailOptional.get();
                currentCartDetail.setQuantity(cartDetail.getQuantity());
                this.cartDetailRepository.save(currentCartDetail);
            }
        }
    }

    public void placeOrder(User user, HttpSession session, String receiverName, String receiverPhone, String receiverAddress) {

        //create order
        Order order = new Order();
        order.setUser(user);
        order.setReceiverName(receiverName);
        order.setReceiverPhone(receiverPhone);
        order.setReceiverAddress(receiverAddress);
        order.setStatus("PENDING");
        order =this.orderRepository.save(order);

        //create order details
        //get cart by user so we can get items in cart to save in order details
        Cart cart = this.cartRepository.findByUser(user);
        if(cart != null){
            List<CartDetail> cartDetails = cart.getCartDetails();
            if(cartDetails != null){
                for(CartDetail cartDetail : cartDetails){
                    OrderDetail orderDetail = new OrderDetail();
                    orderDetail.setOrder(order);
                    orderDetail.setProduct(cartDetail.getProduct());
                    orderDetail.setQuantity(cartDetail.getQuantity());
                    orderDetail.setPrice(cartDetail.getPrice());
                    this.orderDetailRepository.save(orderDetail);

                    order.setTotalPrice(order.getTotalPrice() + orderDetail.getPrice() * orderDetail.getQuantity());
                }   

                order.setStatus("COMPLETED");
                //delete cart items and cart
                for(CartDetail cartDetail : cartDetails){
                    this.cartDetailRepository.deleteById(cartDetail.getId());
                }
                this.cartRepository.deleteById(cart.getId());

                //update session
                session.setAttribute("cartSum", 0);
            }
        }

    }
}
