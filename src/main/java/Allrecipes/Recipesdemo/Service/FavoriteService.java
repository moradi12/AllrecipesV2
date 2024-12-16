package Allrecipes.Recipesdemo.Service;

import Allrecipes.Recipesdemo.Entities.Favorite;
import Allrecipes.Recipesdemo.Entities.User;
import Allrecipes.Recipesdemo.Exceptions.FavoriteAlreadyExistsException;
import Allrecipes.Recipesdemo.Exceptions.FavoriteNotFoundException;
import Allrecipes.Recipesdemo.Recipe.Recipe;
import Allrecipes.Recipesdemo.Repositories.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    public Favorite addFavorite(User user, Recipe recipe) {
        if (favoriteRepository.existsByUserIdAndRecipeId(user.getId(), recipe.getId())) {
            throw new FavoriteAlreadyExistsException("This recipe is already in the user's favorites.");
        }

        Favorite favorite = Favorite.builder()
                .user(user)
                .recipe(recipe)
                .build();
        return favoriteRepository.save(favorite);
    }

    public void removeFavorite(User user, Recipe recipe) {
        List<Favorite> favorites = favoriteRepository.findByUserId(user.getId());
        Favorite favorite = favorites.stream()
                .filter(fav -> fav.getRecipe().getId().equals(recipe.getId()))
                .findFirst()
                .orElseThrow(() -> new FavoriteNotFoundException("This recipe is not in the user's favorites."));

        favoriteRepository.delete(favorite);
    }

    public List<Favorite> getFavoritesByUserId(Long userId) {
        return favoriteRepository.findByUserId(userId);
    }

    public boolean isRecipeFavorite(Long userId, Long recipeId) {
        return favoriteRepository.existsByUserIdAndRecipeId(userId, recipeId);
    }
}
