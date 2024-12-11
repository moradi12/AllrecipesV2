package Allrecipes.Recipesdemo.Request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Request DTO for creating a new rating.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RatingCreateRequest {

    @NotNull(message = "Score is required")
    @Min(value = 1, message = "Score must be at least 1")
    @Max(value = 5, message = "Score must be at most 5")
    private Integer score;

    private String comment;

    @NotNull(message = "Recipe ID is required")
    private Long recipeId;

    @NotNull(message = "User ID is required")
    private Long userId;
}
