package Allrecipes.Recipesdemo.Service;

import Allrecipes.Recipesdemo.Entities.Rating;
import Allrecipes.Recipesdemo.Entities.User;
import Allrecipes.Recipesdemo.Exceptions.ResourceNotFoundException;
import Allrecipes.Recipesdemo.Rating.RatingResponse;
import Allrecipes.Recipesdemo.Rating.RatingUpdateRequest;
import Allrecipes.Recipesdemo.Recipe.Recipe;
import Allrecipes.Recipesdemo.Repositories.RatingRepository;
import Allrecipes.Recipesdemo.Repositories.RecipeRepository;
import Allrecipes.Recipesdemo.Repositories.UserRepository;
import Allrecipes.Recipesdemo.Request.RatingCreateRequest;
import Allrecipes.Recipesdemo.Response.RatingMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer providing CRUD operations for ratings.
 */
@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final RatingMapper ratingMapper;

    /**
     * Creates a new rating.
     *
     * @param request The rating creation request.
     * @return The created rating as a response DTO.
     */
    @Transactional
    public RatingResponse createRating(RatingCreateRequest request) {
        Recipe recipe = recipeRepository.findById(request.getRecipeId())
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found with id: " + request.getRecipeId()));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));

        if (ratingRepository.existsByRecipeIdAndUserId(recipe.getId(), user.getId())) {
            throw new IllegalStateException("User has already rated this recipe.");
        }

        Rating rating = Rating.builder()
                .score(request.getScore())
                .comment(request.getComment())
                .recipe(recipe)
                .user(user)
                .build();

        Rating savedRating = ratingRepository.save(rating);

        return ratingMapper.toDto(savedRating);
    }

    /**
     * Retrieves a rating by its ID.
     *
     * @param id The ID of the rating.
     * @return The rating as a response DTO.
     */
    public RatingResponse getRatingById(Long id) {
        Rating rating = ratingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rating not found with id: " + id));

        return ratingMapper.toDto(rating);
    }

    /**
     * Updates an existing rating.
     *
     * @param id      The ID of the rating to update.
     * @param request The rating update request.
     * @return The updated rating as a response DTO.
     */
    @Transactional
    public RatingResponse updateRating(Long id, RatingUpdateRequest request) {
        Rating rating = ratingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rating not found with id: " + id));

        if (request.getScore() != null) {
            // If desired, you could re-validate the score here.
            rating.setScore(request.getScore());
        }

        if (request.getComment() != null) {
            rating.setComment(request.getComment());
        }

        Rating updatedRating = ratingRepository.save(rating);
        return ratingMapper.toDto(updatedRating);
    }

    /**
     * Deletes a rating by its ID.
     *
     * @param id The ID of the rating to delete.
     */
    @Transactional
    public void deleteRating(Long id) {
        Rating rating = ratingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rating not found with id: " + id));

        ratingRepository.delete(rating);
    }

    /**
     * Retrieves all ratings.
     *
     * @return A list of rating response DTOs.
     */
    public List<RatingResponse> getAllRatings() {
        return ratingRepository.findAll()
                .stream()
                .map(ratingMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all ratings for a specific recipe.
     *
     * @param recipeId The ID of the recipe.
     * @return A list of rating response DTOs.
     */
    public List<RatingResponse> getRatingsByRecipeId(Long recipeId) {
        if (!recipeRepository.existsById(recipeId)) {
            throw new ResourceNotFoundException("Recipe not found with id: " + recipeId);
        }

        return ratingRepository.findByRecipeId(recipeId)
                .stream()
                .map(ratingMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all ratings made by a specific user.
     *
     * @param userId The ID of the user.
     * @return A list of rating response DTOs.
     */
    public List<RatingResponse> getRatingsByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        return ratingRepository.findByUserId(userId)
                .stream()
                .map(ratingMapper::toDto)
                .collect(Collectors.toList());
    }
}
