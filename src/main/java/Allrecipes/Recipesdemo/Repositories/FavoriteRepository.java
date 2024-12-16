package Allrecipes.Recipesdemo.Repositories;

import Allrecipes.Recipesdemo.Entities.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface
FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUserId(Long userId);
    boolean existsByUserIdAndRecipeId(Long userId, Long recipeId);

}
