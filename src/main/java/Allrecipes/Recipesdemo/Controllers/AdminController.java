package Allrecipes.Recipesdemo.Controllers;

import Allrecipes.Recipesdemo.Entities.Enums.UserType;
import Allrecipes.Recipesdemo.Entities.User;
import Allrecipes.Recipesdemo.Entities.UserDetails;
import Allrecipes.Recipesdemo.Exceptions.RecipeNotFoundException;
import Allrecipes.Recipesdemo.Recipe.Recipe;
import Allrecipes.Recipesdemo.Recipe.RecipeCreateRequest;
import Allrecipes.Recipesdemo.Recipe.RecipeResponse;
import Allrecipes.Recipesdemo.Response.UserResponse;
import Allrecipes.Recipesdemo.Security.JWT.JWT;
import Allrecipes.Recipesdemo.Service.AdminService;
import Allrecipes.Recipesdemo.Service.CustomerService;
import Allrecipes.Recipesdemo.Service.RecipeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.LoginException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;
    private final CustomerService customerService;
    private final RecipeService recipeService;
    private final JWT jwtUtil;

    public AdminController(AdminService adminService, CustomerService customerService, RecipeService recipeService, JWT jwtUtil) {
        this.adminService = adminService;
        this.customerService = customerService;
        this.recipeService = recipeService;
        this.jwtUtil = jwtUtil;
    }

    // Fetch all pending recipes
    @GetMapping("/recipes/pending")
    public ResponseEntity<List<Recipe>> getPendingRecipes(@RequestHeader("Authorization") String authHeader) throws LoginException {
        jwtUtil.checkUser(authHeader, UserType.ADMIN);
        List<Recipe> pendingRecipes = adminService.getPendingRecipes();
        return ResponseEntity.ok(pendingRecipes);
    }

    // Approve a recipe by ID
    @PutMapping("/recipes/{id}/approve")
    public ResponseEntity<Map<String, String>> approveRecipe(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) throws LoginException {
        jwtUtil.checkUser(authHeader, UserType.ADMIN);
        adminService.approveRecipe(id);
        return ResponseEntity.ok(Map.of("message", "Recipe approved successfully."));
    }

    // Reject a recipe by ID
    @PutMapping("/recipes/{id}/reject")
    public ResponseEntity<Map<String, String>> rejectRecipe(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) throws LoginException {
        jwtUtil.checkUser(authHeader, UserType.ADMIN);
        adminService.rejectRecipe(id);
        return ResponseEntity.ok(Map.of("message", "Recipe rejected successfully."));
    }

    // Add a new recipe
    @PostMapping("/recipes")
    public ResponseEntity<Map<String, String>> addRecipe(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody RecipeCreateRequest request) throws LoginException {
        jwtUtil.checkUser(authHeader, UserType.ADMIN);
        UserDetails userDetails = jwtUtil.getUserDetails(authHeader);
        User currentUser = mapToUser(userDetails);
        Recipe recipe = recipeService.createRecipe(request, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Recipe added successfully.", "recipeId", recipe.getId().toString()));
    }

    // Fetch all recipes
    @GetMapping("/recipes")
    public ResponseEntity<List<RecipeResponse>> getAllRecipes(@RequestHeader("Authorization") String authHeader) throws LoginException {
        jwtUtil.checkUser(authHeader, UserType.ADMIN);
        return ResponseEntity.ok(recipeService.getAllRecipes());
    }

    // Get a recipe by ID
    @GetMapping("/recipes/{id}")
    public ResponseEntity<Recipe> getRecipeById(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) throws LoginException {
        jwtUtil.checkUser(authHeader, UserType.ADMIN);
        Recipe recipe = recipeService.getRecipeById(id);
        return ResponseEntity.ok(recipe);
    }

    // Update a recipe
    @PutMapping("/recipes/{id}")
    public ResponseEntity<Map<String, String>> updateRecipe(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id,
            @RequestBody RecipeCreateRequest request) throws LoginException {
        jwtUtil.checkUser(authHeader, UserType.ADMIN);
        UserDetails userDetails = jwtUtil.getUserDetails(authHeader);
        User currentUser = mapToUser(userDetails);
        Recipe updatedRecipe = recipeService.updateRecipe(id, request, currentUser);
        return ResponseEntity.ok(Map.of("message", "Recipe updated successfully.", "recipeId", updatedRecipe.getId().toString()));
    }

    // Delete a recipe
    @DeleteMapping("/recipes/{id}")
    public ResponseEntity<Map<String, String>> deleteRecipe(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) throws LoginException {
        jwtUtil.checkUser(authHeader, UserType.ADMIN);
        UserDetails userDetails = jwtUtil.getUserDetails(authHeader);
        User currentUser = mapToUser(userDetails);
        recipeService.deleteRecipe(id, currentUser);
        return ResponseEntity.ok(Map.of("message", "Recipe deleted successfully."));
    }

    @PostMapping("/customers")
    public ResponseEntity<UserResponse> registerCustomer(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password) throws LoginException {
        jwtUtil.checkUser(authHeader, UserType.ADMIN);
        UserResponse newUser = customerService.toUserResponse(customerService.registerUser(username, email, password));
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @GetMapping("/customers")
    public ResponseEntity<List<UserResponse>> getAllCustomers(@RequestHeader("Authorization") String authHeader) throws LoginException {
        jwtUtil.checkUser(authHeader, UserType.ADMIN);
        List<UserResponse> customers = customerService.getAllUsers().stream()
                .map(customerService::toUserResponse)
                .toList();
        return ResponseEntity.ok(customers);
    }

    @DeleteMapping("/customers/{id}")
    public ResponseEntity<Map<String, String>> deleteCustomer(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) throws LoginException {
        jwtUtil.checkUser(authHeader, UserType.ADMIN);
        customerService.deleteUser(id);
        return ResponseEntity.ok(Map.of("message", "Customer deleted successfully."));
    }

    // Helper method to map UserDetails to User
    private User mapToUser(UserDetails userDetails) {
        return User.builder()
                .id(userDetails.getUserId())
                .username(userDetails.getUserName())
                .email(userDetails.getEmail())
                .userType(userDetails.getUserType())
                .build();
    }
}
