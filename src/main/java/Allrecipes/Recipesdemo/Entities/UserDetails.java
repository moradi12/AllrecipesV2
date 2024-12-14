package Allrecipes.Recipesdemo.Entities;

import Allrecipes.Recipesdemo.Entities.Enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDetails {
    private long userId;
    private String userName;
    private String email;
    private UserType userType;
}
