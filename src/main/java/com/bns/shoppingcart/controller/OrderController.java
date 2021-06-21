package com.bns.shoppingcart.controller;

import com.bns.shoppingcart.dto.OrderRequestDTO;
import com.bns.shoppingcart.exception.InvalidCouponException;
import com.bns.shoppingcart.exception.InvalidCustomerException;
import com.bns.shoppingcart.exception.InvalidProductException;
import com.bns.shoppingcart.exception.UnauthorizedAccessException;
import com.bns.shoppingcart.model.Order;
import com.bns.shoppingcart.model.OrderItem;
import com.bns.shoppingcart.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Order related resource controller
 */
@RestController
@RequestMapping(value = "/order")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    @Autowired
    private OrderService orderService;


    /**
     * Gets all the Orders.
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/fetch-all", method = RequestMethod.GET)
    public List<Order> fetchAllOrders() throws Exception {
        List<Order> response;
        try {
            response = orderService.getAllOrders();
            logger.info("Retreived order count: " + response.size());
        } catch (Exception ex) {
            logger.error("Exception when fetching all orders (order/fetch-all)", ex.getMessage());
            throw new Exception("Error fetchAllOrders() method", ex);
        }

        return response;
    }

    /**
     * Gets all the Categories based on customerId
     *
     * @param customerId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/fetch", method = RequestMethod.GET)
    public List<Order> fetchOrdersByCustomerId(@RequestParam(name = "customer_id", required = true) Integer customerId)
            throws Exception {
        List<Order> response;
        try {
            response = orderService.getAllOrdersForCustomerId(customerId);
            logger.info("Retreived orders for customer id: " + customerId);
        } catch (Exception ex) {
            logger.error("Exception when fetching order by customer id (order/fetch)", ex.getMessage());
            throw new Exception("Error fetchOrdersByCustomerId() method", ex);
        }

        return response;
    }

    /**
     * Performs the placing of the order.
     *
     * @param requestDTO
     * @return
     * @throws InvalidCustomerException
     * @throws InvalidProductException
     * @throws UnauthorizedAccessException
     * @throws InvalidCouponException
     */
    @RequestMapping(value = "/place-order", method = RequestMethod.POST)
    public Order placeOrder(@RequestBody OrderRequestDTO requestDTO)
            throws InvalidCustomerException, InvalidProductException, UnauthorizedAccessException, InvalidCouponException {
        List<OrderItem> orderItems;
        Order order = orderService.createOrder(requestDTO.getCustomerId());
        logger.info("Created Order for Customer ID:" + requestDTO.getCustomerId());
        orderItems = orderService.createOrderItems(requestDTO.getOrderItems(), order);
        logger.info("Saved the Order Items for Order ID:" + order.getOrderId());
        Order response = orderService.calculateAndUpdateOrder(order, orderItems, requestDTO.getCouponCode());
        logger.info("Order Updated " + order.getOrderId());

        return response;
    }
}
