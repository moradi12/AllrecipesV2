package Allrecipes.Recipesdemo.Controllers;

import Allrecipes.Recipesdemo.DTOs.UserCreateRequest;
import Allrecipes.Recipesdemo.DTOs.UserResponse;
import Allrecipes.Recipesdemo.DTOs.UserUpdateRequest;
import Allrecipes.Recipesdemo.Entities.User;
import Allrecipes.Recipesdemo.Exceptions.UserNotFoundException;
import Allrecipes.Recipesdemo.Service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "Operations related to users")
public class UserController {

    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    @Operation(summary = "Get Current User", description = "Retrieves the authenticated user's information.")
    public ResponseEntity<UserResponse> getCurrentUser(@AuthenticationPrincipal User user) {
        logger.debug("Fetching current user: {}", user.getUsername());
        UserResponse response = userService.toUserResponse(user);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    @Operation(summary = "Register User", description = "Registers a new user with the provided details.")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserCreateRequest request) {
        logger.debug("Registering user with username: {}", request.getUsername());
        User user = userService.registerUser(request.getUsername(), request.getEmail(), request.getPassword());
        UserResponse response = userService.toUserResponse(user);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Update user info
    @PutMapping("/me")
    @Operation(summary = "Update Current User", description = "Updates the authenticated user's information.")
    public ResponseEntity<UserResponse> updateCurrentUser(@AuthenticationPrincipal User user, @RequestBody UserUpdateRequest request) {
        logger.debug("Updating user: {}", user.getUsername());
        User updated = userService.updateUser(user, request);
        UserResponse response = userService.toUserResponse(updated);
        return ResponseEntity.ok(response);
    }

    // Favorites management (assuming you have methods in userService)
    @PostMapping("/me/favorites/{recipeId}")
    @Operation(summary = "Add Favorite Recipe", description = "Adds a recipe to the authenticated user's favorites.")
    public ResponseEntity<UserResponse> addFavorite(@AuthenticationPrincipal User user, @PathVariable Long recipeId) {
        logger.debug("Adding favorite recipe with ID: {} for user: {}", recipeId, user.getUsername());
        userService.addFavoriteRecipe(user, recipeId);
        User updatedUser = userService.findById(user.getId()).orElseThrow(() -> new UserNotFoundException("User not found after adding favorite."));
        UserResponse response = userService.toUserResponse(updatedUser);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/me/favorites/{recipeId}")
    @Operation(summary = "Remove Favorite Recipe", description = "Removes a recipe from the authenticated user's favorites.")
    public ResponseEntity<UserResponse> removeFavorite(@AuthenticationPrincipal User user, @PathVariable Long recipeId) {
        logger.debug("Removing favorite recipe with ID: {} for user: {}", recipeId, user.getUsername());
        userService.removeFavoriteRecipe(user, recipeId);
        User updatedUser = userService.findById(user.getId()).orElseThrow(() -> new UserNotFoundException("User not found after removing favorite."));
        UserResponse response = userService.toUserResponse(updatedUser);
        return ResponseEntity.ok(response);
    }
}
