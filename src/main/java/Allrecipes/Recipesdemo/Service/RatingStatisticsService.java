package Allrecipes.Recipesdemo.Service;

import Allrecipes.Recipesdemo.Exceptions.ResourceNotFoundException;
import Allrecipes.Recipesdemo.Repositories.RatingRepository;
import Allrecipes.Recipesdemo.Repositories.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RatingStatisticsService {

    private final RatingRepository ratingRepository;
    private final RecipeRepository recipeRepository;

    /**
     * Returns the average rating score for a given recipe.
     * If there are no ratings for the recipe, returns null.
     *
     * @param recipeId The ID of the recipe.
     * @return The average score, or null if none exist.
     */

    /**
     * Returns the total number of ratings for a given recipe.
     *
     * @param recipeId The ID of the recipe.
     * @return The total number of ratings.
     */

    /**
     * Helper method to ensure the given recipe exists.
     */
    private void validateRecipeExists(Long recipeId) {
        if (!recipeRepository.existsById(recipeId)) {
            throw new ResourceNotFoundException("Recipe not found with id: " + recipeId);
        }
    }
}
