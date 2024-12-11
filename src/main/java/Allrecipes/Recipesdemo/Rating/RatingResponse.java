package Allrecipes.Recipesdemo.Rating;

import lombok.*;

/**
 * Data Transfer Object for responding with Rating details.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RatingResponse {

    private Long id;
    private int score;
    private String comment;
    private Long recipeId;
    private Long userId;
    private String recipeName;
    private String userName;
}
