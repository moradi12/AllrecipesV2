package Allrecipes.Recipesdemo.Mappers;

import Allrecipes.Recipesdemo.Entities.User;
import Allrecipes.Recipesdemo.DTOs.UserDto;

public class UserMapper {
    /**
     * Convert a User entity to a UserDto.
     */
    public static UserDto toDto(User user) {
        if (user == null) {
            return null;
        }

        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());

        return dto;
    }

    /**
     * Convert a UserDto to a User entity.
     * Again, be cautious if you’re creating new Users or pulling them from persistence.
     * Typically, IDs for new entities are not set at this point.
     */
    public static User toEntity(UserDto dto) {
        if (dto == null) {
            return null;
        }

        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());

        return user;
    }
}
