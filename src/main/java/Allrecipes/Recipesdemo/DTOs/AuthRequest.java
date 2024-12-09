package Allrecipes.Recipesdemo.DTOs;

import lombok.Data;

@Data
public class AuthRequest {
    private String usernameOrEmail;
    private String password;
}
