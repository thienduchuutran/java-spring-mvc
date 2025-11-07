package vn.hoidanit.laptopshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.hoidanit.laptopshop.domain.OrderDetail;

import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    
}
