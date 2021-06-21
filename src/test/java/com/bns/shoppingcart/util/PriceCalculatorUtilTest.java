package com.bns.shoppingcart.util;

import com.bns.shoppingcart.model.CartItem;
import com.bns.shoppingcart.model.OrderItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class PriceCalculatorUtilTest {

    PriceCalculatorUtil priceCalculatorUtil;

    @BeforeEach
    void init() {
        priceCalculatorUtil = new PriceCalculatorUtil();
    }

    @Test
    public void testCalculateItemSubTotal() {
        double unitPrice = 10.0;
        int quantity = 5;
        double tax = 0.2;
        double expected = 60;
        double actual = priceCalculatorUtil.calculateItemSubTotal(unitPrice, quantity, tax);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testCalculateShippingCost() {
        double expected = 10;
        double actual = priceCalculatorUtil.calculateShippingCost();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testCalculateTotalVAT() {
        double priceForItems = 45;
        double expected = 0.9;
        double actual = priceCalculatorUtil.calculateTotalVAT(priceForItems);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testCalculateTotalCartPriceForCartItems() {
        List<CartItem> savedCartItems = new ArrayList<>();
        CartItem item1 = new CartItem();
        item1.setSubTotal(150.0);
        CartItem item2 = new CartItem();
        item2.setSubTotal(50.0);
        savedCartItems.add(item1);
        savedCartItems.add(item2);
        double expected = 200;
        double actual = priceCalculatorUtil.calculateTotalCartPriceForCartItems(savedCartItems);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testCalculateTotalOrderPriceForOrderItems() {
        List<OrderItem> savedOrderItems = new ArrayList<>();
        OrderItem item1 = new OrderItem();
        item1.setSubTotal(150.0);
        OrderItem item2 = new OrderItem();
        item2.setSubTotal(150.0);
        savedOrderItems.add(item1);
        savedOrderItems.add(item2);
        double expected = 300;
        double actual = priceCalculatorUtil.calculateTotalOrderPriceForOrderItems(savedOrderItems);

        Assertions.assertEquals(expected, actual);
    }

}
