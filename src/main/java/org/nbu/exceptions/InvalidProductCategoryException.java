package org.nbu.exceptions;

public class InvalidProductCategoryException extends RuntimeException {
    public InvalidProductCategoryException() {
        super("Нехранителните продукти не могат да имат срок на годност.");
    }
}
