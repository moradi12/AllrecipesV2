package Allrecipes.Recipesdemo.Controllers;
import Allrecipes.Recipesdemo.DTOs.AuthRequest;
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
        User user = userService.registerUser(request.getUsernameOrEmail(), request.getUsernameOrEmail(), request.getPassword());
        return ResponseEntity.ok("User registered successfully");
    }

}
