package Allrecipes.Recipesdemo.Controllers;


import Allrecipes.Recipesdemo.Entities.User;
import Allrecipes.Recipesdemo.Recipe.Recipe;
import Allrecipes.Recipesdemo.Recipe.RecipeCreateRequest;
import Allrecipes.Recipesdemo.Recipe.RecipeResponse;
import Allrecipes.Recipesdemo.Service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @PostMapping
    public RecipeResponse createRecipe(@AuthenticationPrincipal User user, @RequestBody RecipeCreateRequest req) {
        Recipe recipe = recipeService.createRecipe(req, user);
        return recipeService.toRecipeResponse(recipe);
    }

    @GetMapping("/{id}")
    public RecipeResponse getRecipe(@PathVariable Long id) {
        Recipe recipe = recipeService.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));
        return recipeService.toRecipeResponse(recipe);
    }

    // Similar methods for update and delete
}
