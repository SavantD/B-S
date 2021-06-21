package com.bns.shoppingcart.service;

import com.bns.shoppingcart.dto.CustomerRequestDTO;
import com.bns.shoppingcart.enums.Global;
import com.bns.shoppingcart.exception.DuplicateRecordException;
import com.bns.shoppingcart.model.Customer;
import com.bns.shoppingcart.repository.CustomerRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles Customer related business logic.
 */
@Service
public class CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);
    @Autowired
    private CustomerRepository customerRepository;

    /**
     * Gets a customer by ID
     * @param customerId
     * @return Customer
     */
    public Customer getCustomerById(int customerId) {
        return customerRepository.findById(customerId).orElse(null);
    }

    /**
     * Create a customer
     * @param requestDTO
     * @return ResponseEntity<?>
     * @throws DuplicateRecordException
     */
    public ResponseEntity<?> createCustomer(CustomerRequestDTO requestDTO) throws DuplicateRecordException {
        ModelMapper modelMapper = new ModelMapper();
        Customer customer = modelMapper.map(requestDTO, Customer.class);
        customer.setUserType(Global.UserType.CUSTOMER.name());
        customer.setLoggedIn(true);

        Customer persistedCustomer;
        try {
            persistedCustomer = customerRepository.save(customer);
            logger.info("Customer Saved Successfully!");
        } catch (Exception ex) {
            DuplicateRecordException dupEx = new DuplicateRecordException(ex.getMessage());
            logger.error("Duplicate record found when creating the customer ", dupEx.getCause());
            throw dupEx;
        }


        return ResponseEntity.created(URI.create(String.format("/customer/%s", persistedCustomer.getCustomerName())))
                .body(persistedCustomer);
    }

    /**
     * Create multiple customers.
     * @param requestDTOList
     * @return
     * @throws DuplicateRecordException
     */
    public ResponseEntity<?> createCustomerList(List<CustomerRequestDTO> requestDTOList) throws DuplicateRecordException {
        ModelMapper modelMapper = new ModelMapper();
        List<Customer> customerList = new ArrayList<>();
        for (CustomerRequestDTO requestDTO : requestDTOList) {
            Customer customer = modelMapper.map(requestDTO, Customer.class);
            customer.setUserType(Global.UserType.CUSTOMER.name());
            customer.setLoggedIn(true);
            customerList.add(customer);
        }

        List<Customer> persistedCustomers;
        try {
            persistedCustomers = customerRepository.saveAll(customerList);
            logger.info("Customer List Saved Successfully!");
        } catch (Exception ex) {
            DuplicateRecordException dupEx = new DuplicateRecordException(ex.getMessage());
            logger.error("Duplicate record found when creating the customer ", dupEx.getCause());
            throw dupEx;
        }


        return ResponseEntity.created(URI.create("/create-batch"))
                .body(persistedCustomers);
    }
}
