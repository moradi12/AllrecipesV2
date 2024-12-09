package Allrecipes.Recipesdemo.Exceptions;

public class RecipeNotFoundException extends RuntimeException {
    public RecipeNotFoundException(String message) {
        super(message);
    }
}
