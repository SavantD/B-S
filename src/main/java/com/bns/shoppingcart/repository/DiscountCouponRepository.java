package com.bns.shoppingcart.repository;

import com.bns.shoppingcart.model.DiscountCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DiscountCouponRepository extends JpaRepository<DiscountCoupon, Integer> {

    Optional<DiscountCoupon> findByCouponCodeAndIsClaimed(String couponCode, boolean isClaimed);
}
