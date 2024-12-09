package Allrecipes.Recipesdemo.Exceptions;


import lombok.Getter;

@Getter
public class AdminException extends RuntimeException {



    // Constructor that accepts a custom error message
    public AdminException(String message) {
        super(message);
    }

    // Constructor that accepts a custom error message and a cause
    public AdminException(String message, Throwable cause) {
        super(message, cause);
    }

}