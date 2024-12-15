package Allrecipes.Recipesdemo.Recipe;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecipeResponse {
    private Long id;
    private String title;
    private String description;
    private String ingredients; // Comma-separated string
    private String preparationSteps;
    private int cookingTime;
    private int servings;
    private String dietaryInfo;
    private String status;
    private String createdByUsername;
}
