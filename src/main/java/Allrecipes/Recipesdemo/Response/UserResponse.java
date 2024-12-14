package Allrecipes.Recipesdemo.Response;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String userType;
    private Set<Long> favorites;
}
