package Allrecipes.Recipesdemo.Repositories;

import Allrecipes.Recipesdemo.Entities.Rating;
import Allrecipes.Recipesdemo.Entities.User;
import Allrecipes.Recipesdemo.Recipe.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    Rating findByUserAndRecipe(User user, Recipe recipe);

    @Query("SELECT AVG(r.ratingValue) FROM Rating r WHERE r.recipe.id = :recipeId")
    Double findAverageRatingByRecipe(Long recipeId);
}
