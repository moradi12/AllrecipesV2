package Allrecipes.Recipesdemo.Controllers;
    import Allrecipes.Recipesdemo.DTOs.UserCreateRequest;
import Allrecipes.Recipesdemo.DTOs.UserResponse;
import Allrecipes.Recipesdemo.DTOs.UserUpdateRequest;
import Allrecipes.Recipesdemo.Entities.User;
import Allrecipes.Recipesdemo.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserCreateRequest request) {
        User user = userService.registerUser(request.getUsername(), request.getEmail(), request.getPassword());
        UserResponse response = userService.toUserResponse(user);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Update user info
    @PutMapping("/me")
    public UserResponse updateCurrentUser(@AuthenticationPrincipal User user, @RequestBody UserUpdateRequest request) {
        User updated = userService.updateUser(user, request);
        return userService.toUserResponse(updated);
    }

    // Favorites management (assuming you have methods in userService)
    @PostMapping("/me/favorites/{recipeId}")
    public UserResponse addFavorite(@AuthenticationPrincipal User user, @PathVariable Long recipeId) {
        userService.addFavoriteRecipe(user, recipeId);
        return userService.toUserResponse(userService.findById(user.getId()).orElseThrow());
    }

    @DeleteMapping("/me/favorites/{recipeId}")
    public UserResponse removeFavorite(@AuthenticationPrincipal User user, @PathVariable Long recipeId) {
        userService.removeFavoriteRecipe(user, recipeId);
        return userService.toUserResponse(userService.findById(user.getId()).orElseThrow());
    }
}
