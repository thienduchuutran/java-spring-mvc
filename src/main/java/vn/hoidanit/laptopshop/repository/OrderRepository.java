package vn.hoidanit.laptopshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.hoidanit.laptopshop.domain.Order;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
}
