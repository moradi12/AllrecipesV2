package Allrecipes.Recipesdemo.Request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeCreateRequest {

    @NotBlank(message = "Title is mandatory")
    private String title;

    @NotBlank(message = "Description is mandatory")
    private String description;

    @NotEmpty(message = "Ingredients cannot be empty")
    private List<@NotBlank(message = "Ingredient cannot be blank") String> ingredients;

    @NotBlank(message = "Preparation steps are mandatory")
    private String preparationSteps;

    @Positive(message = "Cooking time must be positive")
    private int cookingTime;

    @Positive(message = "Servings must be positive")
    private int servings;

    private String dietaryInfo;

    private Boolean containsGluten; // Optional

    private Set<Long> categoryIds;

    // Default value handling
    public boolean getContainsGlutenOrDefault() {
        return containsGluten == null ? true : containsGluten;
    }
}
