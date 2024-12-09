package Allrecipes.Recipesdemo.Service;

import Allrecipes.Recipesdemo.Exceptions.RecipeNotFoundException;
import Allrecipes.Recipesdemo.Recipe.Recipe;
import Allrecipes.Recipesdemo.RecipeStatus;
import Allrecipes.Recipesdemo.Repositories.RecipeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for handling admin-related operations.
 */
@Service
public class AdminService {
    private final RecipeRepository recipeRepository;
    private static final Logger logger = LoggerFactory.getLogger(AdminService.class);

    /**
     * Constructor-based dependency injection for AdminService.
     *
     * @param recipeRepository Repository for Recipe entities.
     */
    public AdminService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    /**
     * Retrieves all recipes that are pending approval.
     *
     * @return A list of pending recipes.
     */
    public List<Recipe> getPendingRecipes() {
        logger.debug("Fetching all pending recipes.");
        return recipeRepository.findByStatus(RecipeStatus.valueOf("PENDING")); // Assuming 'status' field exists
    }

    /**
     * Approves a recipe by its ID.
     *
     * @param id The ID of the recipe to approve.
     */
    public void approveRecipe(Long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Recipe not found with ID: {}", id);
                    return new RecipeNotFoundException("Recipe with ID " + id + " not found.");
                });
        recipe.setStatus(RecipeStatus.valueOf("APPROVED")); // Assuming 'status' field exists
        recipeRepository.save(recipe);
        logger.info("Recipe with ID {} approved.", id);
    }

    /**
     * Rejects a recipe by its ID.
     *
     * @param id The ID of the recipe to reject.
     */
    public void rejectRecipe(Long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Recipe not found with ID: {}", id);
                    return new RecipeNotFoundException("Recipe with ID " + id + " not found.");
                });
        recipe.setStatus(RecipeStatus.valueOf("REJECTED")); // Assuming 'status' field exists
        recipeRepository.save(recipe);
        logger.info("Recipe with ID {} rejected.", id);
    }
}
