package com.bns.shoppingcart.util;

import com.bns.shoppingcart.model.CartItem;
import com.bns.shoppingcart.model.OrderItem;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Utility Class to perform logic pertaining to Price Calculations
 */
@Component
public class PriceCalculatorUtil {

    private static final double SHIPPING_COST = 10;
    private static final double VAT = 0.02;

    public double calculateItemSubTotal(double unitPrice, int quantity, double tax) {
        return (unitPrice + (unitPrice * tax)) * quantity;
    }

    public double calculateTotalCartPriceForCartItems(List<CartItem> savedCartItems) {
        double totalCartPrice = 0.0;
        for (CartItem cartItem : savedCartItems) {
            totalCartPrice += cartItem.getSubTotal();
        }
        return totalCartPrice;
    }

    public double calculateTotalOrderPriceForOrderItems(List<OrderItem> savedOrderItems) {
        double totalOrderPrice = 0.0;
        for (OrderItem orderItem : savedOrderItems) {
            totalOrderPrice += orderItem.getSubTotal();
        }
        return totalOrderPrice;
    }

    public double calculateShippingCost() {
        // some custom calculation logic based on requirement.
        return SHIPPING_COST;
    }

    public double calculateTotalVAT(double totalPriceForItems) {
        return totalPriceForItems * VAT;
    }
}
