package Allrecipes.Recipesdemo.Mappers;

import Allrecipes.Recipesdemo.DTOs.RecipeDto;
import Allrecipes.Recipesdemo.Entities.Category;
import Allrecipes.Recipesdemo.Entities.User;
import Allrecipes.Recipesdemo.Recipe.Recipe;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class RecipeMapper {
    /**
     * Convert a Recipe entity to a RecipeDto.
     */
    public static RecipeDto toDto(Recipe recipe) {
        if (recipe == null) {
            return null;
        }

        RecipeDto dto = new RecipeDto();
        dto.setId(recipe.getId());
        dto.setName(recipe.getName());
        dto.setTitle(recipe.getTitle());
        dto.setDescription(recipe.getDescription());
        dto.setIngredients(recipe.getIngredients());
        dto.setPreparationSteps(recipe.getPreparationSteps());
        dto.setCookingTime(recipe.getCookingTime());
        dto.setServings(recipe.getServings());
        dto.setDietaryInfo(recipe.getDietaryInfo());
        dto.setStatus(recipe.getStatus());
        dto.setCreatedAt(recipe.getCreatedAt());
        dto.setUpdatedAt(recipe.getUpdatedAt());
        dto.setContainsGluten(recipe.isContainsGluten());

        // If you only need the user’s ID:
        User createdBy = recipe.getCreatedBy();
        if (createdBy != null) {
            dto.setCreatedByUserId(createdBy.getId());
        }

        // Convert categories to their IDs:
        Set<Long> categoryIds = recipe.getCategories().stream()
                .map(Category::getId)
                .collect(Collectors.toSet());
        dto.setCategoryIds(categoryIds);

        return dto;
    }

    /**
     * Convert a RecipeDto to a Recipe entity.
     * Note: You’ll need to fetch User and Category entities from the database.
     * This method is simplified and assumes you already have them.
     */
    public static Recipe toEntity(RecipeDto dto, User createdBy, Set<Category> categories) {
        if (dto == null) {
            return null;
        }

        // Default categories to empty set if null
        if (categories == null) {
            categories = new HashSet<>();
        }

        return Recipe.builder()
                .id(dto.getId())
                .name(dto.getName())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .ingredients(dto.getIngredients())
                .preparationSteps(dto.getPreparationSteps())
                .cookingTime(dto.getCookingTime())
                .servings(dto.getServings())
                .dietaryInfo(dto.getDietaryInfo())
                .status(dto.getStatus())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .createdBy(createdBy)
                .containsGluten(dto.isContainsGluten())
                .categories(categories)
                .build();
    }
}
