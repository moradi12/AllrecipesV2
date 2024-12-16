package Allrecipes.Recipesdemo.Controllers;

import Allrecipes.Recipesdemo.Entities.Category;
import Allrecipes.Recipesdemo.Entities.Enums.UserType;
import Allrecipes.Recipesdemo.Security.JWT.JWT;
import Allrecipes.Recipesdemo.Service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.LoginException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final JWT jwtUtil;

    public CategoryController(CategoryService categoryService, JWT jwtUtil) {
        this.categoryService = categoryService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories(@RequestHeader("Authorization") String authHeader) throws LoginException {
        jwtUtil.checkUser(authHeader, UserType.ADMIN); // Only Admin can access
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }


    @GetMapping("/{name}")
    public ResponseEntity<Category> getCategoryByName(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String name) throws LoginException {
        jwtUtil.checkUser(authHeader, UserType.ADMIN); // Only Admin can access
        Category category = categoryService.getCategoryByName(name);
        if (category == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(category);
    }
    @PostMapping
    public ResponseEntity<Category> createCategory(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Category category) throws LoginException {
        jwtUtil.checkUser(authHeader, UserType.ADMIN); // Only Admin can access
        Category createdCategory = categoryService.createCategory(category.getFoodCategory());
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }
    @DeleteMapping("/{name}")
    public ResponseEntity<Map<String, String>> deleteCategory(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String name) throws LoginException {
        jwtUtil.checkUser(authHeader, UserType.ADMIN); // Only Admin can access
        categoryService.deleteCategoryByName(name);
        return ResponseEntity.ok(Map.of("message", "Category deleted successfully."));
    }
    @GetMapping("/{name}/exists")
    public ResponseEntity<Boolean> checkCategoryExists(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String name) throws LoginException {
        jwtUtil.checkUser(authHeader, UserType.ADMIN); // Only Admin can access
        boolean exists = categoryService.categoryExists(name);
        return ResponseEntity.ok(exists);
    }
}
