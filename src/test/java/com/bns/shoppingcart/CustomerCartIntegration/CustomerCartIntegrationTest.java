package com.bns.shoppingcart.CustomerCartIntegration;

import com.bns.shoppingcart.controller.CartController;
import com.bns.shoppingcart.controller.CustomerController;
import com.bns.shoppingcart.dto.CartItemDTO;
import com.bns.shoppingcart.dto.CartRequestDTO;
import com.bns.shoppingcart.dto.CustomerRequestDTO;
import com.bns.shoppingcart.model.CartItem;
import com.bns.shoppingcart.model.Customer;
import com.bns.shoppingcart.model.Product;
import com.bns.shoppingcart.model.ShoppingCart;
import com.bns.shoppingcart.service.CustomerService;
import com.bns.shoppingcart.service.ShoppingCartService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class CustomerCartIntegrationTest {
    @InjectMocks
    CustomerController customerController;
    @Mock
    CustomerService customerService;

    @InjectMocks
    CartController cartController;
    @Mock
    ShoppingCartService cartService;

    CustomerRequestDTO customerDTO1;
    CustomerRequestDTO customerDTO2;
    HttpHeaders header;

    Customer customer1;
    Customer customer2;


    @Before
    public void init() {
        // inntializations for customerDTO1 & customerDTO2 DTO's
        customerDTO1 = new CustomerRequestDTO();
        customerDTO1.setCustomerName("Customer 1");
        customerDTO1.setEmail("customer1@test.com");
        customerDTO1.setPassword("pass1234");
        customerDTO1.setBillingAddress("21, dev street, IT District");
        customerDTO1.setShippingAddress("21, dev street, IT District");
        customerDTO1.setCreditCardInfo("XXXXXXXXXXXXXXXX,name,09/23,123");
        customerDTO1.setPhoneNo(1234567890);

        customerDTO2 = new CustomerRequestDTO();
        customerDTO2.setCustomerName("Customer 2");
        customerDTO2.setEmail("customer2@test.com");
        customerDTO2.setPassword("pass1234");
        customerDTO2.setBillingAddress("21, dev street, IT District");
        customerDTO2.setShippingAddress("21, dev street, IT District");
        customerDTO2.setCreditCardInfo("XXXXXXXXXXXXXXXX,name,09/23,123");
        customerDTO2.setPhoneNo(1234567890);

        // inntializations for customer1 & customer2 Entities.
        customer1 = new Customer();
        customer1.setId(1);
        customer2 = new Customer();
        customer2.setId(2);


        header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);

        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    public void testAddCustomerSuccess() throws Exception {
        ResponseEntity<?> responseEntityMock = new ResponseEntity<>(
                customerDTO1,
                header,
                HttpStatus.CREATED
        );
        doReturn(responseEntityMock).when(customerService).createCustomer(customerDTO1);
        ResponseEntity<Object> responseEntity = (ResponseEntity<Object>) customerController.createSingleCustomer(customerDTO1);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(201);
        assertTrue("responseEntity body isnt equal to the expected DTO object", responseEntity.getBody().equals(customerDTO1));
    }

    @Test
    public void testInvalidAddCustomer() throws Exception {
        ResponseEntity<?> responseEntityMock = new ResponseEntity<>(
                customerDTO1,
                header,
                HttpStatus.CREATED
        );
        doReturn(responseEntityMock).when(customerService).createCustomer(customerDTO1);
        ResponseEntity<Object> responseEntity = (ResponseEntity<Object>) customerController.createSingleCustomer(customerDTO1);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(201);
        assertFalse("responseEntity body is equal to the expected DTO object", responseEntity.getBody().equals(customerDTO2));
    }

    @Test
    public void testAddToCartForCustomer1() throws Exception {
        CartRequestDTO cartDTOOfCustomer1 = new CartRequestDTO();
        cartDTOOfCustomer1.setCustomerId(customer1.getId());
        List<CartItemDTO> cartItemsDTO = new ArrayList<>();
        CartItemDTO item1OfCust1 = new CartItemDTO();
        item1OfCust1.setProductId(1);
        item1OfCust1.setQuantity(10);
        cartItemsDTO.add(item1OfCust1);
        cartDTOOfCustomer1.setCartItems(cartItemsDTO);

        ShoppingCart cartOfCust1 = getCustomersCartForCustomer1();
        List<CartItem> cartItemsOfCust1 = getCartItemsForCustomer1(cartOfCust1);


        doReturn(cartOfCust1).when(cartService).createCart(cartDTOOfCustomer1);
        doReturn(cartItemsOfCust1).when(cartService).addItemsToCart(cartItemsDTO, cartOfCust1);
        doReturn(cartOfCust1).when(cartService).calculateAndUpdateShoppingCart(cartOfCust1, cartItemsOfCust1);
        ShoppingCart actualResult = cartController.addToCart(cartDTOOfCustomer1);

        assertEquals(1, actualResult.getCustomer().getId());
        assertEquals(130, actualResult.getTotalPrice(), 1);
    }

    @Test
    public void testAddToCartForCustomer2() throws Exception {
        CartRequestDTO cartDTOOfCustomer2 = new CartRequestDTO();
        cartDTOOfCustomer2.setCustomerId(customer2.getId());
        List<CartItemDTO> cartItemsDTO = new ArrayList<>();
        CartItemDTO item1OfCust2 = new CartItemDTO();
        item1OfCust2.setProductId(1);
        item1OfCust2.setQuantity(10);
        cartItemsDTO.add(item1OfCust2);
        cartDTOOfCustomer2.setCartItems(cartItemsDTO);


        ShoppingCart cartOfCust2 = getCustomersCartForCustomer2();
        List<CartItem> cartItemsOfCust2 = getCartItemsForCustomer2(cartOfCust2);


        doReturn(cartOfCust2).when(cartService).createCart(cartDTOOfCustomer2);
        doReturn(cartItemsOfCust2).when(cartService).addItemsToCart(cartItemsDTO, cartOfCust2);
        doReturn(cartOfCust2).when(cartService).calculateAndUpdateShoppingCart(cartOfCust2, cartItemsOfCust2);
        ShoppingCart actualResult = cartController.addToCart(cartDTOOfCustomer2);

        assertEquals(2, actualResult.getCustomer().getId());
        assertEquals(52000, actualResult.getTotalPrice(), 1);
    }

    /**
     * mocking the Cart for Customer 1.
     *
     * @return
     */
    public ShoppingCart getCustomersCartForCustomer1() {
        ShoppingCart cartOfCust1 = new ShoppingCart();
        cartOfCust1.setCartId(1);
        cartOfCust1.setShippingCost(0);
        cartOfCust1.setTotalPrice(130);
        cartOfCust1.setCustomer(customer1);
        cartOfCust1.setTotalVat(30);

        return cartOfCust1;
    }

    /**
     * Mocking the Cart Items of Customer 1.
     *
     * @param cust1Cart
     * @return
     */
    public List<CartItem> getCartItemsForCustomer1(ShoppingCart cust1Cart) {
        List<CartItem> cartItemsOfCust1 = new ArrayList<>();
        CartItem cartItem = new CartItem();
        cartItem.setSubTotal(100);
        cartItem.setQuantity(10);
        cartItem.setCart(cust1Cart);
        cartItem.setProduct(new Product());
        cartItemsOfCust1.add(cartItem);

        return cartItemsOfCust1;
    }

    /**
     * mocking the Cart for Customer 2.
     *
     * @return
     */
    public ShoppingCart getCustomersCartForCustomer2() {
        ShoppingCart cartOfCust2 = new ShoppingCart();
        cartOfCust2.setCartId(2);
        cartOfCust2.setShippingCost(0);
        cartOfCust2.setTotalPrice(52000);
        cartOfCust2.setCustomer(customer2);
        cartOfCust2.setTotalVat(12000);

        return cartOfCust2;
    }

    /**
     * Mocking the Cart Items of Customer 2.
     *
     * @param cust2Cart
     * @return
     */
    public List<CartItem> getCartItemsForCustomer2(ShoppingCart cust2Cart) {
        List<CartItem> cartItemsOfCust2 = new ArrayList<>();
        CartItem cartItem1 = new CartItem();
        cartItem1.setSubTotal(2000);
        cartItem1.setQuantity(10);
        cartItem1.setCart(cust2Cart);
        cartItem1.setProduct(new Product());

        CartItem cartItem2 = new CartItem();
        cartItem2.setSubTotal(20000);
        cartItem2.setQuantity(1);
        cartItem2.setCart(cust2Cart);
        cartItem2.setProduct(new Product());

        cartItemsOfCust2.add(cartItem1);
        cartItemsOfCust2.add(cartItem2);

        return cartItemsOfCust2;
    }


}
