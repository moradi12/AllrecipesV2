package Allrecipes.Recipesdemo.Recipe;

import lombok.Data;

import java.util.Map;

@Data
public class RecipeCreateRequest {
    private String title;
    private String description;
    private Map<Integer, String> ingredients;
    private String preparationSteps;
    private int cookingTime;
    private int servings;
    private String dietaryInfo;
}
