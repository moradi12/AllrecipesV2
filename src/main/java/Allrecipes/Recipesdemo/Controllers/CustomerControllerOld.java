//package Allrecipes.Recipesdemo.Controllers;
//import Allrecipes.Recipesdemo.Entities.Enums.UserType;
//import Allrecipes.Recipesdemo.Entities.User;
//import Allrecipes.Recipesdemo.Exceptions.UserNotFoundException;
//import Allrecipes.Recipesdemo.Response.UserResponse;
//import Allrecipes.Recipesdemo.Security.JWT.JWT;
//import Allrecipes.Recipesdemo.Service.CustomerService;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import javax.security.auth.login.LoginException;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@RestController
//@RequestMapping("/api/users")
//public class CustomerControllerOld {
//    private final CustomerService customerService;
//    private final JWT jwtUtil;
//
//    public CustomerControllerOld(CustomerService customerService, JWT jwtUtil) {
//        this.customerService = customerService;
//        this.jwtUtil = jwtUtil;
//    }
//
//    @PostMapping("/register")
//    public ResponseEntity<UserResponse> registerUser(
//            @RequestParam String username,
//            @RequestParam String email,
//            @RequestParam String password) {
//        User newUser = customerService.registerUser(username, email, password);
//        UserResponse response = customerService.toUserResponse(newUser);
//        return new ResponseEntity<>(response, HttpStatus.CREATED);
//    }
//
//
//    @PostMapping("/login")
//    public ResponseEntity<?> login(
//            @RequestParam String usernameOrEmail,
//            @RequestParam String password) {
//
//        User user = customerService.login(usernameOrEmail, password);
//
//        Allrecipes.Recipesdemo.Entities.UserDetails userDetails =
//                new Allrecipes.Recipesdemo.Entities.UserDetails(
//                        user.getId(),
//                        user.getUsername(),
//                        user.getEmail(),
//                        user.getUserType()
//                );
//        String token = jwtUtil.generateToken(userDetails);
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Bearer " + token);
//
//        UserResponse userResponse = customerService.toUserResponse(user);
//
//        Map<String, Object> responseBody = new HashMap<>();
//        responseBody.put("message", "Login successful");
//        responseBody.put("user", userResponse);
//
//        return new ResponseEntity<>(responseBody, headers, HttpStatus.OK);
//    }
//
//    @GetMapping
//    public ResponseEntity<List<UserResponse>> getAllUsers(@RequestHeader("Authorization") String authHeader) throws Exception {
//        // Check if user is Admin
//        jwtUtil.checkUser(authHeader, UserType.ADMIN);
//
//        List<UserResponse> users = customerService.getAllUsers().stream()
//                .map(customerService::toUserResponse)
//                .collect(Collectors.toList());
//
//        return ResponseEntity.ok(users);
//    }
//
//
//    ///@GetMapping
//    //public ResponseEntity<List<UserResponse>> getAllUsers() throws Exception {
//    //    // Temporarily removed JWT validation
//    //    List<UserResponse> users = customerService.getAllUsers().stream()
//    //            .map(customerService::toUserResponse)
//    //            .collect(Collectors.toList());
//    //
//    //    return ResponseEntity.ok(users);
//    //}
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteUser(
//            @RequestHeader("Authorization") String authHeader,
//            @PathVariable Long id) throws Exception {
//        jwtUtil.checkUser(authHeader, UserType.ADMIN);
//        customerService.deleteUser(id);
//        return ResponseEntity.noContent().build();
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<UserResponse> updateUser(
//            @RequestHeader("Authorization") String authHeader,
//            @PathVariable Long id,
//            @RequestParam(required = false) String username,
//            @RequestParam(required = false) String email,
//            @RequestParam(required = false) String password) throws Exception {
//
//        // Validate token
//        String token = authHeader.replace("Bearer ", "");
//        jwtUtil.validateToken(token);
//
//        // Extract user type and ID from token
//        String userType = jwtUtil.getUserType(token);
//        String userEmail = jwtUtil.extractEmail(token);
//        Optional<User> currentUserOpt = customerService.findByUsernameOrEmail(userEmail);
//        User currentUser = currentUserOpt.orElseThrow(() -> new UserNotFoundException("Current user not found."));
//
//        if (!UserType.ADMIN.name().equals(userType) && !currentUser.getId().equals(id)) {
//            throw new LoginException("User not allowed to update another user's data.");
//        }
//
//        customerService.updateUser(id, username, email, password);
//        User updatedUser = customerService.findById(id).orElseThrow(() -> new UserNotFoundException("User not found after update."));
//        UserResponse response = customerService.toUserResponse(updatedUser);
//        return ResponseEntity.ok(response);
//    }
//
//    @PostMapping("/{id}/favorites/{recipeId}")
//    public ResponseEntity<UserResponse> addFavorite(
//            @RequestHeader("Authorization") String authHeader,
//            @PathVariable Long id,
//            @PathVariable Long recipeId) throws Exception {
//
//        String token = authHeader.replace("Bearer ", "");
//        jwtUtil.validateToken(token);
//        String userType = jwtUtil.getUserType(token);
//        String userEmail = jwtUtil.extractEmail(token);
//
//        User currentUser = customerService.findByUsernameOrEmail(userEmail)
//                .orElseThrow(() -> new UserNotFoundException("Current user not found."));
//
//        if (!UserType.ADMIN.name().equals(userType) && !currentUser.getId().equals(id)) {
//            throw new LoginException("User not allowed to modify favorites of another user.");
//        }
//
//        customerService.addFavoriteRecipe(currentUser, recipeId);
//        User updatedUser = customerService.findById(id).orElseThrow(() -> new UserNotFoundException("User not found after adding favorite."));
//        UserResponse response = customerService.toUserResponse(updatedUser);
//        return ResponseEntity.ok(response);
//    }
//
//    @DeleteMapping("/{id}/favorites/{recipeId}")
//    public ResponseEntity<UserResponse> removeFavorite(
//            @RequestHeader("Authorization") String authHeader,
//            @PathVariable Long id,
//            @PathVariable Long recipeId) throws Exception {
//
//        String token = authHeader.replace("Bearer ", "");
//        jwtUtil.validateToken(token);
//        String userType = jwtUtil.getUserType(token);
//        String userEmail = jwtUtil.extractEmail(token);
//
//        User currentUser = customerService.findByUsernameOrEmail(userEmail)
//                .orElseThrow(() -> new UserNotFoundException("Current user not found."));
//
//        if (!UserType.ADMIN.name().equals(userType) && !currentUser.getId().equals(id)) {
//            throw new LoginException("User not allowed to remove favorites of another user.");
//        }
//
//        customerService.removeFavoriteRecipe(currentUser, recipeId);
//        User updatedUser = customerService.findById(id).orElseThrow(() -> new UserNotFoundException("User not found after removing favorite."));
//        UserResponse response = customerService.toUserResponse(updatedUser);
//        return ResponseEntity.ok(response);
//    }
//
//    @PostMapping("/{id}/assignUserType")
//    public ResponseEntity<UserResponse> assignUserType(
//            @RequestHeader("Authorization") String authHeader,
//            @PathVariable Long id,
//            @RequestParam UserType userType) throws Exception {
//
//        jwtUtil.checkUser(authHeader, UserType.ADMIN);
//        customerService.assignUserTypeToUser(id, userType);
//
//        User updatedUser = customerService.findById(id).orElseThrow(() -> new UserNotFoundException("User not found after type assignment."));
//        UserResponse response = customerService.toUserResponse(updatedUser);
//        return ResponseEntity.ok(response);
//    }
//}
