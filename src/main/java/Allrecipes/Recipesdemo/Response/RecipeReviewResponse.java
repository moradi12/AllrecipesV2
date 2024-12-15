package Allrecipes.Recipesdemo.Response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class RecipeReviewResponse {
    private Long id;
    private Integer score;
    private String comment;
    private Long recipeId;
    private Long userId;
    private LocalDateTime createdAt;
}
