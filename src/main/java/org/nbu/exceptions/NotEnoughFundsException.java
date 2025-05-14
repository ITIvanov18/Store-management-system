package org.nbu.exceptions;

public class NotEnoughFundsException extends RuntimeException {
    public NotEnoughFundsException() {
        super("Недостатъчно средства за извършване на покупката.");
    }

    public NotEnoughFundsException(String message) {
        super(message);
    }
}
