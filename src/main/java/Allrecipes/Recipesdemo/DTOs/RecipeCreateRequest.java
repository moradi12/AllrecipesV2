package Allrecipes.Recipesdemo.DTOs;

import lombok.Data;

import java.util.List;

@Data
public class RecipeCreateRequest {
    private String title;
    private String description;
    private List<String> ingredients;
    private String preparationSteps;
    private int cookingTime;
    private int servings;
    private String dietaryInfo;
    private Boolean containsGluten; // optional

    public boolean getContainsGlutenOrDefault() {
        return containsGluten == null ? true : containsGluten;
    }
}
