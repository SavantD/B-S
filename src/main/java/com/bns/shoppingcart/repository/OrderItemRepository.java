package com.bns.shoppingcart.repository;

import com.bns.shoppingcart.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

    @Query("SELECT oi FROM OrderItem oi INNER JOIN oi.order ordr WHERE ordr.orderId = ?1")
    List<OrderItem> findOrderItemsByOrderId(int orderId);
}
