package Allrecipes.Recipesdemo.Service;

import Allrecipes.Recipesdemo.Entities.User;
import Allrecipes.Recipesdemo.Exceptions.InvalidRecipeDataException;
import Allrecipes.Recipesdemo.Exceptions.RecipeNotFoundException;
import Allrecipes.Recipesdemo.Exceptions.UnauthorizedActionException;
import Allrecipes.Recipesdemo.Recipe.Recipe;
import Allrecipes.Recipesdemo.Recipe.RecipeCreateRequest;
import Allrecipes.Recipesdemo.Recipe.RecipeResponse;
import Allrecipes.Recipesdemo.RecipeStatus;
import Allrecipes.Recipesdemo.Repositories.RecipeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    /**
     * Create a new recipe with the given request data and associate it with the provided user.
     *
     * @param req  The request data for creating a recipe.
     * @param user The user who is creating the recipe.
     * @return The newly created recipe entity.
     */
    public Recipe createRecipe(RecipeCreateRequest req, User user) {
        validateRecipeRequest(req);

        Recipe recipe = Recipe.builder()
                .title(req.getTitle())
                .description(req.getDescription())
                .ingredients(req.getIngredients())
                .preparationSteps(req.getPreparationSteps())
                .cookingTime(req.getCookingTime())
                .servings(req.getServings())
                .dietaryInfo(req.getDietaryInfo())
                .status(RecipeStatus.PENDING_APPROVAL)
                .createdBy(user)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .containsGluten(req.getContainsGlutenOrDefault())
                .build();

        return recipeRepository.save(recipe);
    }

    /**
     * Update an existing recipe. Only the user who created the recipe can update it.
     *
     * @param id   The ID of the recipe to update.
     * @param req  The updated recipe data.
     * @param user The user attempting to update the recipe.
     * @return The updated recipe entity.
     */
    public Recipe updateRecipe(Long id, RecipeCreateRequest req, User user) {
        Recipe existing = recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException("Recipe not found"));

        // Check if the user is authorized to update this recipe
        if (!existing.getCreatedBy().getId().equals(user.getId())) {
            throw new UnauthorizedActionException("You do not have permission to update this recipe");
        }

        validateRecipeRequest(req);

        existing.setTitle(req.getTitle());
        existing.setDescription(req.getDescription());
        existing.setIngredients(req.getIngredients());
        existing.setPreparationSteps(req.getPreparationSteps());
        existing.setCookingTime(req.getCookingTime());
        existing.setServings(req.getServings());
        existing.setDietaryInfo(req.getDietaryInfo());
        existing.setUpdatedAt(LocalDateTime.now());
        existing.setContainsGluten(req.getContainsGlutenOrDefault());

        return recipeRepository.save(existing);
    }

    /**
     * Delete a recipe by its ID. Only the user who created the recipe can delete it.
     *
     * @param id   The ID of the recipe to delete.
     * @param user The user attempting to delete the recipe.
     */
    public void deleteRecipe(Long id, User user) {
        Recipe existing = recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException("Recipe not found"));

        if (!existing.getCreatedBy().getId().equals(user.getId())) {
            throw new UnauthorizedActionException("You do not have permission to delete this recipe");
        }

        recipeRepository.delete(existing);
    }

    /**
     * Validate the fields of the recipe request. Throws an exception if validation fails.
     *
     * @param req The recipe creation/update request.
     */
    private void validateRecipeRequest(RecipeCreateRequest req) {
        if (req.getTitle() == null || req.getTitle().isEmpty()) {
            throw new InvalidRecipeDataException("Recipe title cannot be empty");
        }
        if (req.getIngredients() == null || req.getIngredients().isEmpty()) {
            throw new InvalidRecipeDataException("Recipe must have at least one ingredient");
        }
        for (String ingredient : req.getIngredients()) {
            if (ingredient == null || ingredient.trim().isEmpty()) {
                throw new InvalidRecipeDataException("Invalid ingredient entry");
            }
        }
        if (req.getCookingTime() <= 0) {
            throw new InvalidRecipeDataException("Cooking time must be greater than zero");
        }
        if (req.getServings() <= 0) {
            throw new InvalidRecipeDataException("Servings must be greater than zero");
        }
    }

    /**
     * Retrieve all recipes from the database.
     *
     * @return A list of RecipeResponse DTOs.
     */
    public List<RecipeResponse> getAllRecipes() {
        return recipeRepository.findAll().stream()
                .map(this::toRecipeResponse)
                .collect(Collectors.toList());
    }

    /**
     * Convert a Recipe entity to a RecipeResponse DTO.
     *
     * @param recipe The Recipe entity to convert.
     * @return The corresponding RecipeResponse DTO.
     */
    public RecipeResponse toRecipeResponse(Recipe recipe) {
        return RecipeResponse.builder()
                .id(recipe.getId())
                .title(recipe.getTitle())
                .description(recipe.getDescription())
                .ingredients(formatIngredients(recipe.getIngredients()))
                .preparationSteps(recipe.getPreparationSteps())
                .cookingTime(recipe.getCookingTime())
                .servings(recipe.getServings())
                .dietaryInfo(recipe.getDietaryInfo())
                .status(recipe.getStatus().name())
                .createdByUsername(recipe.getCreatedBy().getUsername())
                .build();
    }

    /**
     * Format a list of ingredients into a single comma-separated string.
     *
     * @param ingredients The list of ingredient strings.
     * @return A comma-separated string of ingredients.
     */
    private String formatIngredients(List<String> ingredients) {
        return String.join(", ", ingredients);
    }
}
