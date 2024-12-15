package Allrecipes.Recipesdemo.Controllers;

import Allrecipes.Recipesdemo.Entities.RecipeReview;
import Allrecipes.Recipesdemo.Entities.User;
import Allrecipes.Recipesdemo.Entities.UserDetails;
import Allrecipes.Recipesdemo.Recipe.Recipe;
import Allrecipes.Recipesdemo.Recipe.RecipeResponse;
import Allrecipes.Recipesdemo.Request.RecipeCreateRequest;
import Allrecipes.Recipesdemo.Request.RecipeReviewRequest;
import Allrecipes.Recipesdemo.Response.RecipeReviewResponse;
import Allrecipes.Recipesdemo.Security.JWT.JWT;
import Allrecipes.Recipesdemo.Service.RecipeService;
import Allrecipes.Recipesdemo.Service.RecipeReviewService;
import Allrecipes.Recipesdemo.Repositories.CategoryRepository;
import Allrecipes.Recipesdemo.Repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import javax.security.auth.login.LoginException;
import java.util.List;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeService recipeService;
    private final RecipeReviewService recipeReviewService;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final JWT jwtUtil;

    @Autowired
    public RecipeController(RecipeService recipeService,
                            RecipeReviewService recipeReviewService,
                            UserRepository userRepository,
                            CategoryRepository categoryRepository,
                            JWT jwtUtil) {
        this.recipeService = recipeService;
        this.recipeReviewService = recipeReviewService;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Helper method to extract UserDetails from the JWT token in the Authorization header.
     */
    private UserDetails getUserDetailsFromRequest(HttpServletRequest request) throws LoginException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Missing or invalid Authorization header");
        }
        String token = authHeader.substring(7); // Remove "Bearer " prefix
        return jwtUtil.getUserDetails(token);
    }

    /**
     * Create a new recipe.
     */
    @PostMapping
    public ResponseEntity<RecipeResponse> createRecipe(
            @Valid @RequestBody RecipeCreateRequest request,
            HttpServletRequest httpRequest) throws LoginException {
        UserDetails userDetails = getUserDetailsFromRequest(httpRequest);
        User user = userRepository.findById(userDetails.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        Recipe recipe = recipeService.createRecipe(request, user);
        RecipeResponse response = recipeService.toRecipeResponse(recipe);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Retrieve all recipes with pagination and sorting.
     */
    @GetMapping
    public ResponseEntity<Page<RecipeResponse>> getAllRecipes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        Page<Recipe> recipePage = recipeService.getAllRecipes(pageable);
        Page<RecipeResponse> dtoPage = recipePage.map(recipeService::toRecipeResponse);
        return ResponseEntity.ok(dtoPage);
    }

    /**
     * Retrieve a recipe by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RecipeResponse> getRecipeById(@PathVariable Long id) {
        Recipe recipe = recipeService.getRecipeById(id);
        RecipeResponse response = recipeService.toRecipeResponse(recipe);
        return ResponseEntity.ok(response);
    }

    /**
     * Update an existing recipe.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RecipeResponse> updateRecipe(
            @PathVariable Long id,
            @Valid @RequestBody RecipeCreateRequest request,
            HttpServletRequest httpRequest) throws LoginException {
        UserDetails userDetails = getUserDetailsFromRequest(httpRequest);
        User user = userRepository.findById(userDetails.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        Recipe updatedRecipe = recipeService.updateRecipe(id, request, user);
        RecipeResponse response = recipeService.toRecipeResponse(updatedRecipe);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete a recipe.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id, HttpServletRequest httpRequest) throws LoginException {
        UserDetails userDetails = getUserDetailsFromRequest(httpRequest);
        User user = userRepository.findById(userDetails.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
        recipeService.deleteRecipe(id, user);
        return ResponseEntity.noContent().build();
    }

    /**
     * Search recipes by title.
     */
    @GetMapping("/search")
    public ResponseEntity<List<RecipeResponse>> searchRecipes(@RequestParam String title) {
        List<RecipeResponse> recipes = recipeService.searchRecipesByTitle(title);
        return ResponseEntity.ok(recipes);
    }

    /**
     * Retrieve recipes created by the authenticated user.
     */
    @GetMapping("/my-recipes")
    public ResponseEntity<List<RecipeResponse>> getMyRecipes(HttpServletRequest httpRequest) throws LoginException {
        UserDetails userDetails = getUserDetailsFromRequest(httpRequest);
        Long userId = userDetails.getUserId();
        List<RecipeResponse> recipes = recipeService.getRecipesByUserId(userId);
        return ResponseEntity.ok(recipes);
    }

    /**
     * Add a review to a recipe.
     */
    @PostMapping("/{recipeId}/reviews")
    public ResponseEntity<RecipeReviewResponse> addReview(
            @PathVariable Long recipeId,
            @Valid @RequestBody RecipeReviewRequest request,
            HttpServletRequest httpRequest) throws LoginException {
        // Ensure the recipeId in the path matches the one in the request body
        if (!recipeId.equals(request.getRecipeId())) {
            throw new IllegalArgumentException("Recipe ID in path and body must match");
        }

        UserDetails userDetails = getUserDetailsFromRequest(httpRequest);
        User user = userRepository.findById(userDetails.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        RecipeReview review = recipeReviewService.addReview(request, user);
        RecipeReviewResponse response = recipeReviewService.toRecipeReviewResponse(review);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Retrieve all reviews for a specific recipe.
     */
    @GetMapping("/{recipeId}/reviews")
    public ResponseEntity<List<RecipeReviewResponse>> getReviewsByRecipeId(@PathVariable Long recipeId) {
        List<RecipeReviewResponse> reviews = recipeReviewService.getReviewsByRecipeId(recipeId);
        return ResponseEntity.ok(reviews);
    }
}
