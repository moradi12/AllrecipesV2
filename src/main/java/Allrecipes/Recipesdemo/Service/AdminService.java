package Allrecipes.Recipesdemo.Service;

import Allrecipes.Recipesdemo.Exceptions.RecipeNotFoundException;
import Allrecipes.Recipesdemo.Recipe.Recipe;
import Allrecipes.Recipesdemo.Entities.Enums.RecipeStatus;
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

    public AdminService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public List<Recipe> getPendingRecipes() {
        logger.debug("Fetching all pending recipes.");
        return recipeRepository.findByStatus(RecipeStatus.valueOf("PENDING")); // Assuming 'status' field exists
    }


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
