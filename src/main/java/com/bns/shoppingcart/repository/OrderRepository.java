package com.bns.shoppingcart.repository;

import com.bns.shoppingcart.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query("SELECT o FROM Order o INNER JOIN o.customer cust WHERE cust.userId = ?1")
    List<Order> findOrdersByCustomerId(int customerId);
}
