package com.ifarmr.exception;


import com.ifarmr.exception.customExceptions.AccountNotVerifiedException;
import com.ifarmr.exception.customExceptions.EmailAlreadyExistsException;
import com.ifarmr.exception.customExceptions.InvalidPasswordException;
import com.ifarmr.exception.customExceptions.InvalidTokenException;
import com.ifarmr.payload.response.AuthResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CentralizedExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<AuthResponse> handleEmailAlreadyExists(EmailAlreadyExistsException ex) {
        AuthResponse response = AuthResponse.builder()
                .responseCode("400")
                .responseMessage(ex.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Handle AccountNotVerifiedException
    @ExceptionHandler(AccountNotVerifiedException.class)
    public ResponseEntity<AuthResponse> handleAccountNotVerified(AccountNotVerifiedException ex) {
        AuthResponse response = AuthResponse.builder()
                .responseCode("400")
                .responseMessage(ex.getMessage())
                .build();
        return new ResponseEntity<>(response,HttpStatus.FORBIDDEN);
    }

    // Handle InvalidTokenException
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<?> handleInvalidToken(InvalidTokenException ex) {
        AuthResponse response = AuthResponse.builder()
                .responseCode("400")
                .responseMessage(ex.getMessage())
                .build();
        return new ResponseEntity<>(response,HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<AuthResponse> handleInvalidPasswordException(InvalidPasswordException ex) {
        AuthResponse response = AuthResponse.builder()
                .responseCode("400")
                .responseMessage(ex.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<AuthResponse> handleGenericException(Exception ex) {
        AuthResponse response = AuthResponse.builder()
                .responseCode("500")
                .responseMessage("An unexpected error occurred: " + ex.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }




}
