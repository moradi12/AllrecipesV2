package Allrecipes.Recipesdemo.Exceptions;

public class UsernameAlreadyTakenException extends RuntimeException {
    public UsernameAlreadyTakenException(String message) {
        super(message);
    }
}
