package com.bns.shoppingcart.exception;

/**
 * Exception for UnauthorizedAccess when retreiving the discount Coupon
 */
public class UnauthorizedAccessException extends Exception {

    public UnauthorizedAccessException(String msg) {
        super(msg);
    }
}
