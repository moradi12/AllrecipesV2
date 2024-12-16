package Allrecipes.Recipesdemo.Exceptions;

public class FavoriteAlreadyExistsException extends RuntimeException {
    public FavoriteAlreadyExistsException(String message) {
        super(message);
    }
}
