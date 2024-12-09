package Allrecipes.Recipesdemo.DTOs;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String email;
    private String username;
}
