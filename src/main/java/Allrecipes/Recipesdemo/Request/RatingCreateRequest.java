package Allrecipes.Recipesdemo.Request;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Data Transfer Object for creating a new Rating.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RatingCreateRequest {

    /**
     * The rating score provided by the user.
     * Must be between 1 and 5 (inclusive).
     */
    @NotNull(message = "Score is required")
    @Min(value = 1, message = "Score must be at least 1")
    @Max(value = 5, message = "Score must be at most 5")
    private Integer score;

    /**
     * An optional comment provided by the user regarding the rating.
     * This field can be left empty or null if the user chooses not to provide a comment.
     */
    private String comment;

    /**
     * The ID of the recipe being rated.
     * Must correspond to an existing recipe in the system.
     */
    @NotNull(message = "Recipe ID is required")
    private Long recipeId;

    /**
     * The ID of the user who is providing the rating.
     * Must correspond to an existing user in the system.
     */
    @NotNull(message = "User ID is required")
    private Long userId;
}
