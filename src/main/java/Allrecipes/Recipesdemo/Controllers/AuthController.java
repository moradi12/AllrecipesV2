package Allrecipes.Recipesdemo.Controllers;

import Allrecipes.Recipesdemo.Security.JWT.JWT;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/booked-rooms")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {

    private final JWT jwt;
}
