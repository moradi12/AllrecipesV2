package Allrecipes.Recipesdemo.Response;

import Allrecipes.Recipesdemo.Entities.Rating;
import Allrecipes.Recipesdemo.Rating.RatingResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Mapper interface for converting between Rating entities and DTOs.
 */
@Mapper(componentModel = "spring")
public interface RatingMapper {

    RatingMapper INSTANCE = Mappers.getMapper(RatingMapper.class);

    @Mapping(source = "recipe.id", target = "recipeId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "recipe.name", target = "recipeName")
    @Mapping(source = "user.username", target = "userName")
    RatingResponse toDto(Rating rating);
}
    // If needed, add reverse mapping or other mappings

