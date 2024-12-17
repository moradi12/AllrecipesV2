package Allrecipes.Recipesdemo.Controllers;
import Allrecipes.Recipesdemo.Entities.Enums.UserType;
import Allrecipes.Recipesdemo.Entities.User;
import Allrecipes.Recipesdemo.Entities.UserDetails;
import Allrecipes.Recipesdemo.Recipe.Recipe;
import Allrecipes.Recipesdemo.Recipe.RecipeResponse;
import Allrecipes.Recipesdemo.Request.RecipeCreateRequest;
import Allrecipes.Recipesdemo.Response.UserResponse;
import Allrecipes.Recipesdemo.Security.JWT.JWT;
import Allrecipes.Recipesdemo.Service.AdminService;
import Allrecipes.Recipesdemo.Service.CustomerService;
import Allrecipes.Recipesdemo.Service.RecipeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public ResponseEntity<?> getPendingRecipes(@RequestHeader("Authorization") String authHeader) {
        try {
            jwtUtil.checkUser(authHeader, UserType.ADMIN);
            List<Recipe> pendingRecipes = adminService.getPendingRecipes();
            return ResponseEntity.ok(pendingRecipes);
        } catch (LoginException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Unauthorized: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch pending recipes: " + e.getMessage()));
        }
    }

    // Approve a recipe by ID
    @PutMapping("/recipes/{id}/approve")
    public ResponseEntity<?> approveRecipe(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {
        try {
            jwtUtil.checkUser(authHeader, UserType.ADMIN);
            adminService.approveRecipe(id);
            return ResponseEntity.ok(Map.of("message", "Recipe approved successfully."));
        } catch (LoginException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Unauthorized: " + e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Recipe not found: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to approve recipe: " + e.getMessage()));
        }
    }

    // Reject a recipe by ID
    @PutMapping("/recipes/{id}/reject")
    public ResponseEntity<?> rejectRecipe(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {
        try {
            jwtUtil.checkUser(authHeader, UserType.ADMIN);
            adminService.rejectRecipe(id);
            return ResponseEntity.ok(Map.of("message", "Recipe rejected successfully."));
        } catch (LoginException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Unauthorized: " + e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Recipe not found: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to reject recipe: " + e.getMessage()));
        }
    }

    // Add a new recipe
    @PostMapping("/recipes")
    public ResponseEntity<?> addRecipe(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody RecipeCreateRequest request) {
        try {
            jwtUtil.checkUser(authHeader, UserType.ADMIN);
            UserDetails userDetails = jwtUtil.getUserDetails(authHeader);
            User currentUser = mapToUser(userDetails);
            Recipe recipe = recipeService.createRecipe(request, currentUser);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "Recipe added successfully.", "recipeId", recipe.getId().toString()));
        } catch (LoginException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Unauthorized: " + e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Invalid recipe data: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to add recipe: " + e.getMessage()));
        }
    }

    // Fetch all recipes with pagination and sorting
    @GetMapping("/recipes")
    public ResponseEntity<?> getAllRecipes(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        try {
            jwtUtil.checkUser(authHeader, UserType.ADMIN);
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
            Page<Recipe> recipePage = recipeService.getAllRecipes(pageable);
            Page<RecipeResponse> responsePage = recipePage.map(recipeService::toRecipeResponse);
            return ResponseEntity.ok(responsePage);
        } catch (LoginException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Unauthorized: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch recipes: " + e.getMessage()));
        }
    }

    // Get a recipe by ID
    @GetMapping("/recipes/{id}")
    public ResponseEntity<?> getRecipeById(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {
        try {
            jwtUtil.checkUser(authHeader, UserType.ADMIN);
            Recipe recipe = recipeService.getRecipeById(id);
            return ResponseEntity.ok(recipe);
        } catch (LoginException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Unauthorized: " + e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Recipe not found: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch recipe: " + e.getMessage()));
        }
    }

    // Update a recipe
    @PutMapping("/recipes/{id}")
    public ResponseEntity<?> updateRecipe(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id,
            @RequestBody RecipeCreateRequest request) {
        try {
            jwtUtil.checkUser(authHeader, UserType.ADMIN);
            UserDetails userDetails = jwtUtil.getUserDetails(authHeader);
            User currentUser = mapToUser(userDetails);
            Recipe updatedRecipe = recipeService.updateRecipe(id, request, currentUser);
            return ResponseEntity.ok(Map.of("message", "Recipe updated successfully.", "recipeId", updatedRecipe.getId().toString()));
        } catch (LoginException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Unauthorized: " + e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Recipe not found or invalid data: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to update recipe: " + e.getMessage()));
        }
    }

    // Delete a recipe
    @DeleteMapping("/recipes/{id}")
    public ResponseEntity<?> deleteRecipe(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {
        try {
            jwtUtil.checkUser(authHeader, UserType.ADMIN);
            UserDetails userDetails = jwtUtil.getUserDetails(authHeader);
            User currentUser = mapToUser(userDetails);
            recipeService.deleteRecipe(id, currentUser);
            return ResponseEntity.ok(Map.of("message", "Recipe deleted successfully."));
        } catch (LoginException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Unauthorized: " + e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Recipe not found: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete recipe: " + e.getMessage()));
        }
    }

    // Register a new customer
    @PostMapping("/customers")
    public ResponseEntity<?> registerCustomer(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password) {
        try {
            jwtUtil.checkUser(authHeader, UserType.ADMIN);
            UserResponse newUser = customerService.toUserResponse(customerService.registerUser(username, email, password));
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        } catch (LoginException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Unauthorized: " + e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Invalid user data: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to register customer: " + e.getMessage()));
        }
    }

    // Fetch all customers
    @GetMapping("/customers")
    public ResponseEntity<?> getAllCustomers(@RequestHeader("Authorization") String authHeader) {
        try {
            jwtUtil.checkUser(authHeader, UserType.ADMIN);
            List<UserResponse> customers = customerService.getAllUsers().stream()
                    .map(customerService::toUserResponse)
                    .toList();
            return ResponseEntity.ok(customers);
        } catch (LoginException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Unauthorized: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch customers: " + e.getMessage()));
        }
    }

    // Delete a customer by ID
    @DeleteMapping("/customers/{id}")
    public ResponseEntity<?> deleteCustomer(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {
        try {
            jwtUtil.checkUser(authHeader, UserType.ADMIN);
            customerService.deleteUser(id);
            return ResponseEntity.ok(Map.of("message", "Customer deleted successfully."));
        } catch (LoginException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Unauthorized: " + e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Customer not found: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete customer: " + e.getMessage()));
        }
    }

    // Fetch all recipes publicly
    @GetMapping("/recipes/public")
    public ResponseEntity<?> getAllRecipesPublic() {
        try {
            Pageable pageable = Pageable.unpaged();
            Page<Recipe> recipePage = recipeService.getAllRecipes(pageable);
            List<RecipeResponse> recipes = recipePage.map(recipeService::toRecipeResponse).getContent();
            return ResponseEntity.ok(recipes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch public recipes: " + e.getMessage()));
        }
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
