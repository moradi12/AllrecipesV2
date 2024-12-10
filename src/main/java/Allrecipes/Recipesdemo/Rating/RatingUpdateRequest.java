package Allrecipes.Recipesdemo.Rating;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

/**
 * Data Transfer Object for updating an existing Rating.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RatingUpdateRequest {

    /**
     * The new rating score provided by the user.
     * Must be between 1 and 5 (inclusive).
     */
    @Min(value = 1, message = "Score must be at least 1")
    @Max(value = 5, message = "Score must be at most 5")
    private Integer score;

    /**
     * An optional new comment provided by the user regarding the rating.
     * This field can be left empty or null if the user chooses not to update the comment.
     */
    private String comment;
}
