package vn.hoidanit.laptopshop.services.specification;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.domain.Product_;

public class ProductSpecs {
    public static Specification<Product> nameLike(String name){
        return (root, query, criteriaBuilder) 
            -> criteriaBuilder.like(root.get(Product_.NAME), "%"+name+"%");
      }

      //case 1
      public static Specification<Product> minPrice(double price){
        return (root, query, criteriaBuilder) 
            -> criteriaBuilder.ge(root.get(Product_.PRICE), price);
      }

    //case 2
    public static Specification<Product> maxPrice(double price){
    return (root, query, criteriaBuilder) 
        -> criteriaBuilder.le(root.get(Product_.PRICE), price);
    }
    
    //case 3
    public static Specification<Product> matchFactory(String factory){
        return (root, query, criteriaBuilder) 
            -> criteriaBuilder.equal(root.get(Product_.FACTORY), factory);
    }

    //case 4
    public static Specification<Product> matchFactories(List<String> factories){
        return (root, query, criteriaBuilder) 
            -> criteriaBuilder.in(root.get(Product_.FACTORY)).value(factories);
    }

    //case 5
    public static Specification<Product> matchPrice(double min, double max){
    return (root, query, criteriaBuilder) -> criteriaBuilder.and(
        criteriaBuilder.gt(root.get(Product_.PRICE), min), criteriaBuilder.le(root.get(Product_.PRICE), max));
    }

    //case 6
    public static Specification<Product> matchMultiplePrice(double min, double max){
    return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get(Product_.PRICE), min, max);
    }
}
