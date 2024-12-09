package Allrecipes.Recipesdemo.Controllers;


import Allrecipes.Recipesdemo.Entities.User;
import Allrecipes.Recipesdemo.Service.UserService;
import Allrecipes.Recipesdemo.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public UserResponse getCurrentUser(@AuthenticationPrincipal User user) {
        return userService.toUserResponse(user);
    }

    // Update user info, favorites endpoints, etc.
}
