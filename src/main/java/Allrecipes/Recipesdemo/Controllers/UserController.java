package Allrecipes.Recipesdemo.Controllers;

import Allrecipes.Recipesdemo.Entities.User;
import Allrecipes.Recipesdemo.Entities.UserDetails;
import Allrecipes.Recipesdemo.Entities.UserType;
import Allrecipes.Recipesdemo.Request.LoginRequest;
import Allrecipes.Recipesdemo.Request.RegisterRequest;
import Allrecipes.Recipesdemo.Response.UserResponse;
import Allrecipes.Recipesdemo.Security.JWT.JWT;
import Allrecipes.Recipesdemo.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * UserController handles user-related API endpoints such as user registration,
 * login, retrieving user details, and managing favorite recipes.
 */
@RestController
@CrossOrigin
@RequestMapping("/api/users")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final JWT jwt;

    public UserController(UserService userService, JWT jwt) {
        this.userService = userService;
        this.jwt = jwt;
    }

    /**
     * Registers a new user with the given credentials.
     */
    @Operation(summary = "Register a new user", description = "Create a new user account with provided credentials.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or user already exists")
    })
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
        LOGGER.info("Received registration request for email: {}", request.getEmail());

        // Basic validation
        if (request.getUsername() == null || request.getEmail() == null || request.getPassword() == null) {
            LOGGER.debug("Invalid registration request body: {}", request);
            return ResponseEntity.badRequest().body("Username, email, and password are required.");
        }

        try {
            User user = userService.registerUser(request.getUsername(), request.getEmail(), request.getPassword());
            String token = jwt.generateToken(
                    new UserDetails(user.getId(), user.getUsername(), user.getEmail(), UserType.CUSTOMER));
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);

            LOGGER.info("User {} registered successfully with ID: {}", request.getEmail(), user.getId());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .headers(headers)
                    .body(new ResponseWrapper("User registered successfully", token));
        } catch (Exception e) {
            LOGGER.error("Error registering user: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    /**
     * Authenticates a user and returns a JWT token upon successful login.
     */
    @Operation(summary = "Login a user", description = "Authenticate a user and return a JWT token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        LOGGER.info("Received login request for: {}", request.getUsernameOrEmail());

        if (request.getUsernameOrEmail() == null || request.getPassword() == null) {
            LOGGER.debug("Invalid login request body: {}", request);
            return ResponseEntity.badRequest().body("Username/email and password are required.");
        }

        try {
            User user = userService.login(request.getUsernameOrEmail(), request.getPassword());
            String token = jwt.generateToken(
                    new UserDetails(user.getId(), user.getUsername(), user.getEmail(), UserType.CUSTOMER));
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);

            LOGGER.info("User {} logged in successfully.", request.getUsernameOrEmail());
            return ResponseEntity.ok().headers(headers).body(new ResponseWrapper("Login successful", token));
        } catch (Exception e) {
            LOGGER.error("Error during login for user {}: {}", request.getUsernameOrEmail(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: " + e.getMessage());
        }
    }

    /**
     * Retrieves a user by their ID.
     */
    @Operation(summary = "Get user by ID", description = "Retrieve user details by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(
            @Parameter(description = "User ID") @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {

        LOGGER.debug("Request to get user by ID: {}", id);
        validateToken(authHeader);

        return userService.findById(id)
                .map(user -> {
                    LOGGER.info("User found with ID: {}", id);
                    return ResponseEntity.ok(userService.toUserResponse(user));
                })
                .orElseGet(() -> {
                    LOGGER.warn("User not found with ID: {}", id);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                });
    }

    /**
     * Retrieves all users.
     */
    @Operation(summary = "Get all users", description = "Retrieve a list of all registered users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved")
    })
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers(@RequestHeader("Authorization") String authHeader) {
        LOGGER.debug("Request to get all users");
        validateToken(authHeader);
        List<UserResponse> users = userService.getAllUsers()
                .stream()
                .map(userService::toUserResponse)
                .toList();

        LOGGER.info("Retrieved {} users", users.size());
        return ResponseEntity.ok(users);
    }

    /**
     * Deletes a user by their ID.
     */
    @Operation(summary = "Delete user by ID", description = "Remove a user from the system by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(
            @Parameter(description = "User ID") @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {

        LOGGER.info("Request to delete user by ID: {}", id);
        validateToken(authHeader);
        userService.deleteUser(id);
        LOGGER.info("User {} deleted successfully", id);
        return ResponseEntity.ok("User deleted successfully.");
    }

    /**
     * Adds a recipe to the user's list of favorites.
     */
    @Operation(summary = "Add favorite recipe", description = "Add a specified recipe to the user's favorites.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipe added to favorites"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/favorites/{recipeId}")
    public ResponseEntity<String> addFavoriteRecipe(
            @RequestHeader("Authorization") String authHeader,
            @Parameter(description = "Recipe ID") @PathVariable Long recipeId) {

        LOGGER.debug("Request to add favorite recipe: {}", recipeId);
        UserDetails userDetails = extractUserDetails(authHeader);
        User user = userService.findById(userDetails.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        userService.addFavoriteRecipe(user, recipeId);
        LOGGER.info("User {} added recipe {} to favorites.", user.getId(), recipeId);
        return ResponseEntity.ok("Recipe added to favorites.");
    }

    /**
     * Removes a recipe from the user's list of favorites.
     */
    @Operation(summary = "Remove favorite recipe", description = "Remove a specified recipe from the user's favorites.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipe removed from favorites"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/favorites/{recipeId}")
    public ResponseEntity<String> removeFavoriteRecipe(
            @RequestHeader("Authorization") String authHeader,
            @Parameter(description = "Recipe ID") @PathVariable Long recipeId) {

        LOGGER.debug("Request to remove favorite recipe: {}", recipeId);
        UserDetails userDetails = extractUserDetails(authHeader);
        User user = userService.findById(userDetails.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        userService.removeFavoriteRecipe(user, recipeId);
        LOGGER.info("User {} removed recipe {} from favorites.", user.getId(), recipeId);
        return ResponseEntity.ok("Recipe removed from favorites.");
    }

    private void validateToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            LOGGER.error("Invalid or missing Authorization header");
            throw new RuntimeException("Invalid or missing Authorization header.");
        }
        String token = authHeader.substring(7);
        jwt.validateToken(token);
    }

    private UserDetails extractUserDetails(String authHeader) {
        validateToken(authHeader);
        String token = authHeader.substring(7);
        var claims = jwt.extractAllClaims(token);

        return new UserDetails(
                (Integer) claims.get("id"),
                (String) claims.get("userName"),
                (String) claims.get("email"),
                UserType.valueOf((String) claims.get("userType"))
        );
    }

    /**
     * A simple wrapper class for responses that include a message and optional token.
     */
    private static class ResponseWrapper {
        private final String message;
        private final String token;

        public ResponseWrapper(String message, String token) {
            this.message = message;
            this.token = token;
        }

        public String getMessage() {
            return message;
        }

        public String getToken() {
            return token;
        }
    }
}
