package Allrecipes.Recipesdemo.Controllers;

import Allrecipes.Recipesdemo.DTOs.AuthRequest;
import Allrecipes.Recipesdemo.DTOs.AuthResponse;
import Allrecipes.Recipesdemo.Entities.User;
import Allrecipes.Recipesdemo.Security.JWT.JwtTokenProvider;
import Allrecipes.Recipesdemo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest request) {
        // request.getUsernameOrEmail() is used for both username and email here,
        // consider splitting username and email fields in AuthRequest
        User user = userService.registerUser(request.getUsernameOrEmail(), request.getUsernameOrEmail(), request.getPassword());
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        User user = userService.findByUsernameOrEmail(request.getUsernameOrEmail())
                .orElseThrow(() -> new RuntimeException("Invalid username/email or password"));

        // Verify the password
        if (!userService.checkPassword(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username/email or password");
        }

        // Generate JWT token
        String token = tokenProvider.generateToken(user.getUsername(), user.getRole().name());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
