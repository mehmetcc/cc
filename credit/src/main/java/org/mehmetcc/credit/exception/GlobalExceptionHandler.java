package org.mehmetcc.credit.exception;

import org.mehmetcc.credit.commons.user.UserNotActiveException;
import org.mehmetcc.credit.commons.user.UserNotFoundException;
import org.mehmetcc.credit.installment.InstallmentPaymentOutOfBoundsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleUserNotFoundException(UserNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(UserNotActiveException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleUserNotActiveException(UserNotActiveException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(InvalidQueryParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInvalidQueryParameterException(InvalidQueryParameterException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(InstallmentPaymentOutOfBoundsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleInstallmentPaymentOutOfBoundsException(InstallmentPaymentOutOfBoundsException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGeneralException(Exception ex) {
        return "An unexpected error occurred: " + ex.getMessage();
    }
}
