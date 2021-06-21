package com.bns.shoppingcart.exception;

/**
 * Exception DuplicateRecord in DB
 */
public class DuplicateRecordException extends Exception {

    public DuplicateRecordException(String msg) {
        super(msg);
    }
}