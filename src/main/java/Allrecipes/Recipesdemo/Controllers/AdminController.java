package Allrecipes.Recipesdemo.Controllers;
import Allrecipes.Recipesdemo.Recipe.Recipe;
import Allrecipes.Recipesdemo.Service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/recipes/pending")
    public List<Recipe> getPendingRecipes() {
        return adminService.getPendingRecipes();
    }

    @PostMapping("/recipes/{id}/approve")
    public void approveRecipe(@PathVariable Long id) {
        adminService.approveRecipe(id);
    }

    @PostMapping("/recipes/{id}/reject")
    public void rejectRecipe(@PathVariable Long id) {
        adminService.rejectRecipe(id);
    }
    // Also add user management endpoints if needed (e.g., createUser, deleteUser)
}
