package com.bns.shoppingcart.service;

import com.bns.shoppingcart.dto.OrderItemDTO;
import com.bns.shoppingcart.enums.Global;
import com.bns.shoppingcart.exception.InvalidCouponException;
import com.bns.shoppingcart.exception.InvalidCustomerException;
import com.bns.shoppingcart.exception.InvalidProductException;
import com.bns.shoppingcart.exception.UnauthorizedAccessException;
import com.bns.shoppingcart.model.*;
import com.bns.shoppingcart.repository.DiscountCouponRepository;
import com.bns.shoppingcart.repository.OrderItemRepository;
import com.bns.shoppingcart.repository.OrderRepository;
import com.bns.shoppingcart.util.PriceCalculatorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Handles Order related business logic.
 */
@Service
public class OrderService {

    public static final int MIN_THRESHOLD = 5000;
    public static final double RATE = 0.05;
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    @Autowired
    private PriceCalculatorUtil calcUtil;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private DiscountCouponRepository discountCouponRepository;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ProductService productService;

    /**
     * Gets all the orders.
     *
     * @return List<Order>
     */
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    /**
     * Gets all the orders based on customerId
     *
     * @param customerId
     * @return List<Order>
     */
    public List<Order> getAllOrdersForCustomerId(int customerId) {
        return orderRepository.findOrdersByCustomerId(customerId);
    }

    /**
     * Gets all the order Items based on orderId
     *
     * @param orderId
     * @return List<OrderItem>
     */
    public List<OrderItem> getAllOrderItemsForOrderId(int orderId) {
        return orderItemRepository.findOrderItemsByOrderId(orderId);
    }

    /**
     * Gets the order based on orderId
     *
     * @param orderId
     * @return
     */
    public Order getOrderByOrderId(int orderId) {
        return orderRepository.getById(orderId);
    }

    /**
     * Create a new order.
     *
     * @param customerId
     * @return Order created
     * @throws InvalidCustomerException
     */
    public Order createOrder(int customerId) throws InvalidCustomerException {
        Customer existingCustomer = customerService.getCustomerById(customerId);
        Order persistedOrder;
        if (existingCustomer != null) {
            Order order = new Order();
            order.setCustomer(existingCustomer);
            order.setDateCreated(new Date());
            order.setDateShipped(new Date());
            order.setOrderStatus(Global.OrderStatus.SHIPPED.name());
            persistedOrder = orderRepository.save(order);
            logger.info("Order was saved at : " + order.getDateCreated());
        } else {
            InvalidCustomerException icEx = new InvalidCustomerException("Customer Doesnt Exist.");
            logger.error("Error from createOrder()", icEx.getCause());
            throw icEx;
        }
        return persistedOrder;
    }

    /**
     * Create new order items related to the order.
     *
     * @param requestDTOList
     * @param createdOrder
     * @return
     * @throws InvalidProductException
     */
    public List<OrderItem> createOrderItems(List<OrderItemDTO> requestDTOList, Order createdOrder) throws InvalidProductException {
        List<OrderItem> persistedOrderItems;
        List<OrderItem> orderItemsToBeSaved = new ArrayList<>();
        for (OrderItemDTO item : requestDTOList) {
            Product product = productService.getProductById(item.getProductId());
            if (product != null) {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(createdOrder);
                orderItem.setProduct(product);
                orderItem.setQuantity(item.getQuantity());
                double subTotal = calcUtil.calculateItemSubTotal(product.getPrice(), item.getQuantity(), product.getTax());
                orderItem.setSubTotal(subTotal);

                orderItemsToBeSaved.add(orderItem);

            } else {
                InvalidProductException ipEx = new InvalidProductException("Product Doesnt Exist.");
                logger.error("Error from createOrderItems() ", ipEx.getCause());
                throw ipEx;
            }
        }

        persistedOrderItems = orderItemRepository.saveAll(orderItemsToBeSaved);
        logger.info("OrderItems were saved");
        return persistedOrderItems;
    }

    /**
     * Updates the grandTotal column of the Order table.
     *
     * @param createdOrder
     * @param savedOrderItems
     * @param couponCode
     * @return
     * @throws UnauthorizedAccessException
     * @throws InvalidCouponException
     */
    public Order calculateAndUpdateOrder(Order createdOrder, List<OrderItem> savedOrderItems, String couponCode)
            throws UnauthorizedAccessException, InvalidCouponException {

        double totalOrderPrice = calcUtil.calculateTotalOrderPriceForOrderItems(savedOrderItems);
        createDiscountCouponForOrder(createdOrder, totalOrderPrice);
        double totalVAT = calcUtil.calculateTotalVAT(totalOrderPrice);
        double shippingCost = calcUtil.calculateShippingCost();
        double grandTotal = totalOrderPrice + totalVAT + shippingCost;
        createdOrder.setGrandTotal(grandTotal);
        orderRepository.save(createdOrder);
        if (couponCode != null) {
            grandTotal = applyDiscount(couponCode, createdOrder.getCustomer().getUserId(), grandTotal);
            createdOrder.setGrandTotal(grandTotal);
        }


        return orderRepository.save(createdOrder);
    }

    /**
     * Persists discount data to DiscountCoupon table
     *
     * @param createdOrder
     * @param totalOrderPrice
     */
    public void createDiscountCouponForOrder(Order createdOrder, double totalOrderPrice) {
        if (totalOrderPrice > MIN_THRESHOLD) {
            DiscountCoupon coupon = new DiscountCoupon();
            coupon.setCustomer(createdOrder.getCustomer());
            coupon.setOrder(createdOrder);
            coupon.setCouponCode(UUID.randomUUID().toString());
            coupon.setClaimed(false);
            discountCouponRepository.save(coupon);
            logger.info("createDiscountCouponForOrder() created a discount coupon ");
        }
    }

    /**
     * Applies discount to the total order price.
     *
     * @param couponCode
     * @param customerId
     * @param grandTotal
     * @return
     * @throws UnauthorizedAccessException
     * @throws InvalidCouponException
     */
    public double applyDiscount(String couponCode, int customerId, double grandTotal)
            throws UnauthorizedAccessException, InvalidCouponException {
        boolean couponIsValid = validateCoupon(couponCode, customerId);
        if (couponIsValid) {
            grandTotal -= RATE * grandTotal;
        }
        return grandTotal;
    }

    /**
     * Validates if the provided the discount coupon is valid.
     *
     * @param couponCode
     * @param customerId
     * @return
     * @throws InvalidCouponException
     * @throws UnauthorizedAccessException
     */
    private boolean validateCoupon(String couponCode, int customerId) throws InvalidCouponException, UnauthorizedAccessException {
        DiscountCoupon coupon = discountCouponRepository.findByCouponCodeAndIsClaimed(couponCode, false).orElse(null);
        boolean isValid = false;
        if (coupon != null) {
            if (coupon.getCustomer().getUserId() == customerId) {
                isValid = true;
                coupon.setClaimed(true);
                discountCouponRepository.save(coupon);
            } else {
                UnauthorizedAccessException uaEx = new UnauthorizedAccessException("Customer Not Authorized For Discount");
                logger.error("Error from validateCoupon() ", uaEx.getCause());
                throw uaEx;
            }
        } else {
            InvalidCouponException icEx = new InvalidCouponException("Discount Coupon Is Invalid");
            logger.error("Error from validateCoupon() ", icEx.getCause());
            throw icEx;
        }
        return isValid;
    }
}
