package Allrecipes.Recipesdemo.Recipe;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class RecipeCreateRequest {

    @NotNull(message = "Score is required")
    @Min(value = 1, message = "Score must be at least 1")
    @Max(value = 5, message = "Score must be at most 5")
    private Integer score;

    private String comment;

    @NotNull(message = "Recipe ID is required")
    private Long recipeId;

    @NotNull(message = "User ID is required")
    private Long userId;

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
