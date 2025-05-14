package org.nbu.exceptions;

public class ClosedCashRegisterException extends RuntimeException {
    public ClosedCashRegisterException(int registerId) {
        super("Касата с ID " + registerId + " е затворена и не може да бъде използвана.");
    }
}