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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    public Recipe createRecipe(RecipeCreateRequest req, User user) {
        // Validate RecipeCreateRequest (optional)
        if (req.getTitle() == null || req.getTitle().isEmpty()) {
            throw new InvalidRecipeDataException("Recipe title cannot be empty");
        }
        // Add more validation as needed

        Recipe recipe = Recipe.builder()
                .title(req.getTitle())
                .description(req.getDescription())
                .ingredients(Collections.singletonList(req.getIngredients()))
                .preparationSteps(req.getPreparationSteps())
                .cookingTime(req.getCookingTime())
                .servings(req.getServings())
                .dietaryInfo(req.getDietaryInfo())
                .status(RecipeStatus.PENDING_APPROVAL)
                .createdBy(user)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        return recipeRepository.save(recipe);
    }

    public Optional<Recipe> findById(Long id) {
        return recipeRepository.findById(id);
    }

    public Recipe save(Recipe recipe) {
        recipe.setUpdatedAt(LocalDateTime.now());
        return recipeRepository.save(recipe);
    }

    public RecipeResponse toRecipeResponse(Recipe recipe) {
        return RecipeResponse.builder()
                .id(recipe.getId())
                .title(recipe.getTitle())
                .description(recipe.getDescription())
                .ingredients(recipe.getIngredients().toString())
                .preparationSteps(recipe.getPreparationSteps())
                .cookingTime(recipe.getCookingTime())
                .servings(recipe.getServings())
                .dietaryInfo(recipe.getDietaryInfo())
                .status(recipe.getStatus().name())
                .createdByUsername(recipe.getCreatedBy().getUsername())
                .build();
    }

    public Recipe updateRecipe(Long id, RecipeCreateRequest req, User user) {
        Recipe existing = recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException("Recipe not found"));

        if (!existing.getCreatedBy().getId().equals(user.getId())) {
            throw new UnauthorizedActionException("You do not have permission to update this recipe");
        }

        // Optionally, validate the request data
        if (req.getTitle() == null || req.getTitle().isEmpty()) {
            throw new InvalidRecipeDataException("Recipe title cannot be empty");
        }
        // Add more validation as needed

        existing.setTitle(req.getTitle());
        existing.setDescription(req.getDescription());
        existing.setIngredients(Collections.singletonList(req.getIngredients()));
        existing.setPreparationSteps(req.getPreparationSteps());
        existing.setCookingTime(req.getCookingTime());
        existing.setServings(req.getServings());
        existing.setDietaryInfo(req.getDietaryInfo());
        existing.setUpdatedAt(LocalDateTime.now());

        return recipeRepository.save(existing);
    }

    public void deleteRecipe(Long id, User user) {
        Recipe existing = recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException("Recipe not found"));

        // Authorization check
        if (!existing.getCreatedBy().getId().equals(user.getId())) {
            throw new UnauthorizedActionException("You do not have permission to delete this recipe");
        }

        recipeRepository.delete(existing);
    }
}
