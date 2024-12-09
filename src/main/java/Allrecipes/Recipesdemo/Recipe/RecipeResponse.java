package Allrecipes.Recipesdemo.Recipe;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
@Builder
public class RecipeResponse {
    private Long id;
    private String title;
    private String description;
    private String ingredients;
    private String preparationSteps;
    private int cookingTime;
    private int servings;
    private String dietaryInfo;
    private String status;
    private String createdByUsername;
}