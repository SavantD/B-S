package com.bns.shoppingcart.controller;

import com.bns.shoppingcart.dto.CustomerRequestDTO;
import com.bns.shoppingcart.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Customer related resource controller
 */
@RestController
@RequestMapping(value = "/customer")
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
    @Autowired
    private CustomerService customerService;

    /**
     * Creates a single customer.
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/create-single", method = RequestMethod.POST)
    public ResponseEntity<?> createSingleCustomer(@RequestBody CustomerRequestDTO request) throws Exception {
        ResponseEntity<?> response;
        response = customerService.createCustomer(request);
        logger.info("Created Customer with name " + request.getCustomerName());

        return response;
    }

    /**
     * Creates multiple customers.
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/create-multiple", method = RequestMethod.POST)
    public ResponseEntity<?> createMultipleCustomer(@RequestBody List<CustomerRequestDTO> request) throws Exception {
        ResponseEntity<?> response;
        response = customerService.createCustomerList(request);

        return response;
    }

}
