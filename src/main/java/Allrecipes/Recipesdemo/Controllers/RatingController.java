package Allrecipes.Recipesdemo.Controllers;

import Allrecipes.Recipesdemo.Rating.RatingResponse;
import Allrecipes.Recipesdemo.Service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing Ratings.
 */
@RestController
@RequestMapping("/api/ratings")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    /**
     * Creates a new rating.
     *
     * @param request The rating creation request.
     * @return The created rating.
     */
//    @PostMapping
//    public ResponseEntity<RatingResponse> createRating(@Valid @RequestBody RatingCreateRequest request) {
//        RatingResponse response = ratingService.createRating(request);
//        return new ResponseEntity<>(response, HttpStatus.CREATED);
//    }

    /**
     * Retrieves a rating by its ID.
     *
     * @param id The ID of the rating.
     * @return The rating.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RatingResponse> getRatingById(@PathVariable Long id) {
        RatingResponse response = ratingService.getRatingById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Updates an existing rating.
     *
     * @param id      The ID of the rating to update.
     * @param request The rating update request.
     * @return The updated rating.
     */
//    @PutMapping("/{id}")
//    public ResponseEntity<RatingResponse> updateRating(@PathVariable Long id,
//                                                       @Valid @RequestBody RatingUpdateRequest request) {
//        RatingResponse response = ratingService.updateRating(id, request);
//        return ResponseEntity.ok(response);
//    }

    /**
     * Deletes a rating by its ID.
     *
     * @param id The ID of the rating to delete.
     * @return No content.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRating(@PathVariable Long id) {
        ratingService.deleteRating(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves all ratings.
     *
     * @return A list of ratings.
     */
    @GetMapping
    public ResponseEntity<List<RatingResponse>> getAllRatings() {
        List<RatingResponse> ratings = ratingService.getAllRatings();
        return ResponseEntity.ok(ratings);
    }

    /**
     * Retrieves all ratings for a specific recipe.
     *
     * @param recipeId The ID of the recipe.
     * @return A list of ratings.
     */
    @GetMapping("/recipe/{recipeId}")
    public ResponseEntity<List<RatingResponse>> getRatingsByRecipeId(@PathVariable Long recipeId) {
        List<RatingResponse> ratings = ratingService.getRatingsByRecipeId(recipeId);
        return ResponseEntity.ok(ratings);
    }

    /**
     * Retrieves all ratings made by a specific user.
     *
     * @param userId The ID of the user.
     * @return A list of ratings.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RatingResponse>> getRatingsByUserId(@PathVariable Long userId) {
        List<RatingResponse> ratings = ratingService.getRatingsByUserId(userId);
        return ResponseEntity.ok(ratings);
    }
}
