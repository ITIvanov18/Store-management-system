package org.nbu.exceptions;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String productName, int requested, int available) {
        super("Недостатъчно количество от продукт: '" + productName + "'. Необходими: " +
                requested + ", налични: " + available);
    }
}
