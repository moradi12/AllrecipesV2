package Allrecipes.Recipesdemo.Repositories;

import Allrecipes.Recipesdemo.Entities.RecipeReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeReviewRepository extends JpaRepository<RecipeReview, Long> {
    List<RecipeReview> findByRecipeId(Long recipeId);
    List<RecipeReview> findByUserId(Long userId);
    RecipeReview findByRecipeIdAndUserId(Long recipeId, Long userId);
}
