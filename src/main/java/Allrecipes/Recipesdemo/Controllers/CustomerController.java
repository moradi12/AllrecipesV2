package Allrecipes.Recipesdemo.Controllers;

import Allrecipes.Recipesdemo.Entities.Enums.UserType;
import Allrecipes.Recipesdemo.Entities.User;
import Allrecipes.Recipesdemo.Exceptions.UserNotFoundException;
import Allrecipes.Recipesdemo.Response.UserResponse;
import Allrecipes.Recipesdemo.Security.JWT.JWT;
import Allrecipes.Recipesdemo.Service.CustomerService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.security.auth.login.LoginException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class CustomerController {
    private final CustomerService customerService;
    private final JWT jwtUtil;

    public CustomerController(CustomerService customerService, JWT jwtUtil) {
        this.customerService = customerService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password) {
        try {
            User newUser = customerService.registerUser(username, email, password);
            UserResponse response = customerService.toUserResponse(newUser);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            // Handle cases like invalid input parameters
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Handle any other unexpected exceptions
            return new ResponseEntity<>("Registration failed due to an unexpected error.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestParam String usernameOrEmail,
            @RequestParam String password) {
        try {
            User user = customerService.login(usernameOrEmail, password);

            Allrecipes.Recipesdemo.Entities.UserDetails userDetails =
                    new Allrecipes.Recipesdemo.Entities.UserDetails(
                            user.getId(),
                            user.getUsername(),
                            user.getEmail(),
                            user.getUserType()
                    );
            String token = jwtUtil.generateToken(userDetails);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);

            UserResponse userResponse = customerService.toUserResponse(user);

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Login successful");
            responseBody.put("user", userResponse);

            return new ResponseEntity<>(responseBody, headers, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            // Handle authentication failures
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            // Handle any other unexpected exceptions
            return new ResponseEntity<>("Login failed due to an unexpected error.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers(@RequestHeader("Authorization") String authHeader) {
        try {
            // Check if user is Admin
            jwtUtil.checkUser(authHeader, UserType.ADMIN);

            List<UserResponse> users = customerService.getAllUsers().stream()
                    .map(customerService::toUserResponse)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(users);
        } catch (UserNotFoundException e) {
            // Handle cases where the user performing the action is not found
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (LoginException e) {
            // Handle unauthorized access
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            // Handle any other unexpected exceptions
            return new ResponseEntity<>("Failed to retrieve users due to an unexpected error.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {
        try {
            jwtUtil.checkUser(authHeader, UserType.ADMIN);
            customerService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (UserNotFoundException e) {
            // Handle cases where the user to be deleted is not found
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (LoginException e) {
            // Handle unauthorized access
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            // Handle any other unexpected exceptions
            return new ResponseEntity<>("Failed to delete user due to an unexpected error.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String password) {
        try {
            // Validate token
            String token = authHeader.replace("Bearer ", "");
            jwtUtil.validateToken(token);

            // Extract user type and email from token
            String userType = jwtUtil.getUserType(token);
            String userEmail = jwtUtil.extractEmail(token);
            Optional<User> currentUserOpt = customerService.findByUsernameOrEmail(userEmail);
            User currentUser = currentUserOpt.orElseThrow(() -> new UserNotFoundException("Current user not found."));

            if (!UserType.ADMIN.name().equals(userType) && !currentUser.getId().equals(id)) {
                throw new LoginException("User not allowed to update another user's data.");
            }

            customerService.updateUser(id, username, email, password);
            User updatedUser = customerService.findById(id)
                    .orElseThrow(() -> new UserNotFoundException("User not found after update."));
            UserResponse response = customerService.toUserResponse(updatedUser);
            return ResponseEntity.ok(response);
        } catch (UserNotFoundException e) {
            // Handle cases where the user is not found
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (LoginException e) {
            // Handle unauthorized access
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (IllegalArgumentException e) {
            // Handle invalid input parameters
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Handle any other unexpected exceptions
            return new ResponseEntity<>("Failed to update user due to an unexpected error.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{id}/favorites/{recipeId}")
    public ResponseEntity<?> addFavorite(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id,
            @PathVariable Long recipeId) {
        try {
            String token = authHeader.replace("Bearer ", "");
            jwtUtil.validateToken(token);
            String userType = jwtUtil.getUserType(token);
            String userEmail = jwtUtil.extractEmail(token);

            User currentUser = customerService.findByUsernameOrEmail(userEmail)
                    .orElseThrow(() -> new UserNotFoundException("Current user not found."));

            if (!UserType.ADMIN.name().equals(userType) && !currentUser.getId().equals(id)) {
                throw new LoginException("User not allowed to modify favorites of another user.");
            }

            customerService.addFavoriteRecipe(currentUser, recipeId);
            User updatedUser = customerService.findById(id)
                    .orElseThrow(() -> new UserNotFoundException("User not found after adding favorite."));
            UserResponse response = customerService.toUserResponse(updatedUser);
            return ResponseEntity.ok(response);
        } catch (UserNotFoundException e) {
            // Handle cases where the user is not found
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (LoginException e) {
            // Handle unauthorized access
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (IllegalArgumentException e) {
            // Handle invalid input parameters
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Handle any other unexpected exceptions
            return new ResponseEntity<>("Failed to add favorite due to an unexpected error.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}/favorites/{recipeId}")
    public ResponseEntity<?> removeFavorite(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id,
            @PathVariable Long recipeId) {
        try {
            String token = authHeader.replace("Bearer ", "");
            jwtUtil.validateToken(token);
            String userType = jwtUtil.getUserType(token);
            String userEmail = jwtUtil.extractEmail(token);

            User currentUser = customerService.findByUsernameOrEmail(userEmail)
                    .orElseThrow(() -> new UserNotFoundException("Current user not found."));

            if (!UserType.ADMIN.name().equals(userType) && !currentUser.getId().equals(id)) {
                throw new LoginException("User not allowed to remove favorites of another user.");
            }

            customerService.removeFavoriteRecipe(currentUser, recipeId);
            User updatedUser = customerService.findById(id)
                    .orElseThrow(() -> new UserNotFoundException("User not found after removing favorite."));
            UserResponse response = customerService.toUserResponse(updatedUser);
            return ResponseEntity.ok(response);
        } catch (UserNotFoundException e) {
            // Handle cases where the user is not found
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (LoginException e) {
            // Handle unauthorized access
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (IllegalArgumentException e) {
            // Handle invalid input parameters
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Handle any other unexpected exceptions
            return new ResponseEntity<>("Failed to remove favorite due to an unexpected error.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{id}/assignUserType")
    public ResponseEntity<?> assignUserType(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id,
            @RequestParam UserType userType) {
        try {
            jwtUtil.checkUser(authHeader, UserType.ADMIN);
            customerService.assignUserTypeToUser(id, userType);

            User updatedUser = customerService.findById(id)
                    .orElseThrow(() -> new UserNotFoundException("User not found after type assignment."));
            UserResponse response = customerService.toUserResponse(updatedUser);
            return ResponseEntity.ok(response);
        } catch (UserNotFoundException e) {
            // Handle cases where the user is not found
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (LoginException e) {
            // Handle unauthorized access
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (IllegalArgumentException e) {
            // Handle invalid input parameters
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Handle any other unexpected exceptions
            return new ResponseEntity<>("Failed to assign user type due to an unexpected error.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
