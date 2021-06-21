package com.bns.shoppingcart.service;

import com.bns.shoppingcart.dto.CartItemDTO;
import com.bns.shoppingcart.dto.CartRequestDTO;
import com.bns.shoppingcart.exception.InvalidCustomerException;
import com.bns.shoppingcart.exception.InvalidProductException;
import com.bns.shoppingcart.model.CartItem;
import com.bns.shoppingcart.model.Customer;
import com.bns.shoppingcart.model.Product;
import com.bns.shoppingcart.model.ShoppingCart;
import com.bns.shoppingcart.repository.CartItemRepository;
import com.bns.shoppingcart.repository.CartRepository;
import com.bns.shoppingcart.util.PriceCalculatorUtil;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles ShoppingCart related business logic.
 */
@Service
public class ShoppingCartService {
    private static final Logger logger = LoggerFactory.getLogger(ShoppingCartService.class);
    @Autowired
    CartItemRepository cartItemRepository;
    @Autowired
    PriceCalculatorUtil calcUtil;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ProductService productService;

    /**
     * Create a Cart for a purticular Customer id.
     * @param requestDTO
     * @return
     * @throws InvalidCustomerException
     */
    public ShoppingCart createCart(CartRequestDTO requestDTO) throws InvalidCustomerException {
        ModelMapper modelMapper = new ModelMapper();
        ShoppingCart cart = modelMapper.map(requestDTO, ShoppingCart.class);

        Customer customer = customerService.getCustomerById(requestDTO.getCustomerId());
        ShoppingCart persistedCart;
        if (customer != null) {
            cart.setCustomer(customer);
            persistedCart = cartRepository.save(cart);
        } else {
            InvalidCustomerException icEx = new InvalidCustomerException("Customer Doesnt Exist.");
            logger.error("Error in createCart() ", icEx.getCause());
            throw icEx;
        }
        return persistedCart;
    }

    /**
     * Adds items to the created cart
     * @param cartItemList
     * @param createdCart
     * @return
     * @throws InvalidProductException
     */
    public List<CartItem> addItemsToCart(List<CartItemDTO> cartItemList, ShoppingCart createdCart) throws InvalidProductException {
        List<CartItem> cartItemsToSave = new ArrayList<>();
        for (CartItemDTO item : cartItemList) {
            Product product = productService.getProductById(item.getProductId());
            if (product != null) {
                CartItem cartItem = new CartItem();
                cartItem.setCart(createdCart);
                cartItem.setProduct(product);
                cartItem.setQuantity(item.getQuantity());
                double subTotal = calcUtil.calculateItemSubTotal(product.getPrice(), item.getQuantity(), product.getTax());
                cartItem.setSubTotal(subTotal);

                cartItemsToSave.add(cartItem);

            } else {
                InvalidProductException ipEx = new InvalidProductException("Product Doesnt Exist.");
                logger.error("Error in addItemsToCart() ", ipEx.getCause());
                throw ipEx;
            }
        }
        List<CartItem> persistedCartItems = cartItemRepository.saveAll(cartItemsToSave);
        return persistedCartItems;
    }

    /**
     * Updates the cart after doing calculations.
     * @param createdCart
     * @param savedCartItems
     * @return
     */
    public ShoppingCart calculateAndUpdateShoppingCart(ShoppingCart createdCart, List<CartItem> savedCartItems) {
        double totalCartPrice = calcUtil.calculateTotalCartPriceForCartItems(savedCartItems);
        double totalVAT = calcUtil.calculateTotalVAT(totalCartPrice);
        double grandTotal = totalCartPrice + totalVAT;
        double shippingCost = calcUtil.calculateShippingCost();
        createdCart.setTotalPrice(grandTotal);
        createdCart.setTotalVat(totalVAT);
        createdCart.setShippingCost(shippingCost);
        return cartRepository.save(createdCart);

    }
}
