package Allrecipes.Recipesdemo.Service;

import Allrecipes.Recipesdemo.Entities.RecipeReview;
import Allrecipes.Recipesdemo.Entities.User;
import Allrecipes.Recipesdemo.Exceptions.InvalidRecipeDataException;
import Allrecipes.Recipesdemo.Exceptions.RecipeNotFoundException;
import Allrecipes.Recipesdemo.Recipe.Recipe;
import Allrecipes.Recipesdemo.Repositories.RecipeReviewRepository;
import Allrecipes.Recipesdemo.Repositories.RecipeRepository;
import Allrecipes.Recipesdemo.Request.RecipeReviewRequest;
import Allrecipes.Recipesdemo.Response.RecipeReviewResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeReviewService {

    private final RecipeReviewRepository recipeReviewRepository;
    private final RecipeRepository recipeRepository;

    public RecipeReviewService(RecipeReviewRepository recipeReviewRepository,
                               RecipeRepository recipeRepository) {
        this.recipeReviewRepository = recipeReviewRepository;
        this.recipeRepository = recipeRepository;
    }

    /**
     * Add a review to a recipe.
     *
     * @param request The review creation request.
     * @param user    The user submitting the review.
     * @return The saved RecipeReview entity.
     */
    public RecipeReview addReview(RecipeReviewRequest request, User user) {
        Recipe recipe = recipeRepository.findById(request.getRecipeId())
                .orElseThrow(() -> new RecipeNotFoundException("Recipe not found with ID: " + request.getRecipeId()));

        // Optionally, enforce one review per user per recipe
        RecipeReview existingReview = recipeReviewRepository.findByRecipeIdAndUserId(recipe.getId(), user.getId());
        if (existingReview != null) {
            throw new InvalidRecipeDataException("User has already reviewed this recipe");
        }

        RecipeReview review = RecipeReview.builder()
                .score(request.getScore())
                .comment(request.getComment())
                .recipe(recipe)
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();

        return recipeReviewRepository.save(review);
    }

    public List<RecipeReviewResponse> getReviewsByRecipeId(Long recipeId) {
        List<RecipeReview> reviews = recipeReviewRepository.findByRecipeId(recipeId);
        return reviews.stream()
                .map(this::toRecipeReviewResponse)
                .collect(Collectors.toList());
    }

    public RecipeReviewResponse toRecipeReviewResponse(RecipeReview review) {
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
