package Allrecipes.Recipesdemo.Exceptions;
public class FavoriteNotFoundException extends RuntimeException {
    public FavoriteNotFoundException(String message) {
        super(message);
    }
}
