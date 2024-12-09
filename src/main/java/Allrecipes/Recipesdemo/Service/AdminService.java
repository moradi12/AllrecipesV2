package Allrecipes.Recipesdemo.Service;

import Allrecipes.Recipesdemo.Recipe.Recipe;
import Allrecipes.Recipesdemo.RecipeStatus;
import Allrecipes.Recipesdemo.Repositories.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private RecipeRepository recipeRepository;

    public List<Recipe> getPendingRecipes() {
        return recipeRepository.findByStatus(RecipeStatus.PENDING_APPROVAL);
    }

    public void approveRecipe(Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));
        recipe.setStatus(RecipeStatus.APPROVED);
        recipeRepository.save(recipe);
    }

    public void rejectRecipe(Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));
        recipe.setStatus(RecipeStatus.REJECTED);
        recipeRepository.save(recipe);
    }
}