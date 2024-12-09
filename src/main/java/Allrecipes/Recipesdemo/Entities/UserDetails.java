package Allrecipes.Recipesdemo.Entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UserDetails {
    private int userId;
    private String userName;
    private String email;
    private String password;
    private UserType userType;

}
