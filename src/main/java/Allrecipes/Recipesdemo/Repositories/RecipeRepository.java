package Allrecipes.Recipesdemo.Repositories;

import Allrecipes.Recipesdemo.Recipe.Recipe;
import Allrecipes.Recipesdemo.Entities.Enums.RecipeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findByStatus(RecipeStatus status);
    List<Recipe> findByCreatedById(Long userId);
    List<Recipe> findByStatus(String status); // Duplicate method
    List<Recipe> findByTitleContainingIgnoreCase(String title);
}
