package Allrecipes.Recipesdemo.Exceptions;

public class InvalidRecipeDataException extends RuntimeException {
    public InvalidRecipeDataException(String message) {
        super(message);
    }
}
