package Allrecipes.Recipesdemo.Recipe;

import lombok.Data;

@Data
public class RecipeCreateRequest {
    private String title;
    private String description;
    private String ingredients;
    private String preparationSteps;
    private int cookingTime;
    private int servings;
    private String dietaryInfo;
}
