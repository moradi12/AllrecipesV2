package Allrecipes.Recipesdemo.Mappers;

import Allrecipes.Recipesdemo.Entities.RecipeReview;
import Allrecipes.Recipesdemo.Recipe.Recipe;
import Allrecipes.Recipesdemo.Recipe.RecipeResponse;
import Allrecipes.Recipesdemo.Response.RecipeReviewResponse;

public class RecipeMapper {

    public static RecipeResponse toRecipeResponse(Recipe recipe) {
        return RecipeResponse.builder()
                .id(recipe.getId())
                .title(recipe.getTitle())
                .description(recipe.getDescription())
                .ingredients(String.join(", ", recipe.getIngredients()))
                .preparationSteps(recipe.getPreparationSteps())
                .cookingTime(recipe.getCookingTime())
                .servings(recipe.getServings())
                .dietaryInfo(recipe.getDietaryInfo())
                .status(recipe.getStatus().name())
                .createdByUsername(recipe.getCreatedBy().getUsername())
                .build();
    }

    /**
     * Convert a RecipeReview entity to a RecipeReviewResponse DTO.
     */
    public static RecipeReviewResponse toRecipeReviewResponse(RecipeReview review) {
        return RecipeReviewResponse.builder()
                .id(review.getId())
                .score(review.getScore())
                .comment(review.getComment())
                .recipeId(review.getRecipe().getId())
                .userId(review.getUser().getId())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
