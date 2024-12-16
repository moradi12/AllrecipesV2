package Allrecipes.Recipesdemo.Testing;

import Allrecipes.Recipesdemo.Entities.User;
import Allrecipes.Recipesdemo.Entities.Category;
import Allrecipes.Recipesdemo.Entities.Comment;
import Allrecipes.Recipesdemo.Entities.Enums.FoodCategories;
import Allrecipes.Recipesdemo.Rating.RatingResponse;
import Allrecipes.Recipesdemo.Request.RatingCreateRequest;
import Allrecipes.Recipesdemo.Request.RecipeCreateRequest;
import Allrecipes.Recipesdemo.Service.CustomerService;
import Allrecipes.Recipesdemo.Service.CategoryService;
import Allrecipes.Recipesdemo.Service.CommentService;
import Allrecipes.Recipesdemo.Service.RatingService;
import Allrecipes.Recipesdemo.Service.RecipeService;
import Allrecipes.Recipesdemo.Recipe.Recipe;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import java.util.List;

@RequiredArgsConstructor
@Component
@Order(7)
public class CustomerTester implements CommandLineRunner {

    private final CustomerService customerService;
    private final CommentService commentService;
    private final RatingService ratingService;
    private final RecipeService recipeService;
    private final CategoryService categoryService;

    private User customer1;
    private User customer2;
    private Recipe testRecipe;

    @Override
    public void run(String... args) {
        System.out.println("\n======== Customer Tester ========\n");

        try {
            addCustomers();
            addTestRecipe();
            printAllCustomers();
            testComments();
            testRatings();
        } catch (Exception e) {
            System.out.println("An unexpected error occurred during CustomerTester: " + e.getMessage());
        }
    }

    /**
     * Adds multiple customers to the system.
     */
    private void addCustomers() {
        System.out.println("\n===== Adding New Customers =====");

        try {
            customer1 = customerService.registerUser("johnDoe", "john.doe@example.com", "password123");
            System.out.println("Added Customer 1: " + customer1);

            customer2 = customerService.registerUser("janeSmith", "jane.smith@example.com", "securePass456");
            System.out.println("Added Customer 2: " + customer2);

        } catch (Exception e) {
            System.out.println("Error adding customers: " + e.getMessage());
        }
    }

    /**
     * Seeds a test recipe for testing purposes with a selected food category.
     */
    private void addTestRecipe() {
        System.out.println("\n===== Adding Test Recipe =====");

        try {
            Category category = categoryService.createCategory(FoodCategories.VEGETARIAN);
            System.out.println("Using Category: " + category);

            RecipeCreateRequest recipeRequest = RecipeCreateRequest.builder()
                    .title("Test Recipe")
                    .description("A simple test recipe.")
                    .ingredients(List.of("Ingredient 1", "Ingredient 2"))
                    .preparationSteps("Step 1: Do this. Step 2: Do that.")
                    .cookingTime(30)
                    .servings(4)
                    .dietaryInfo("Vegetarian")
                    .build();

            testRecipe = recipeService.createRecipe(recipeRequest, customer1);
            System.out.println("Added Test Recipe: " + testRecipe);
        } catch (Exception e) {
            System.out.println("Error adding test recipe: " + e.getMessage());
        }
    }

    /**
     * Prints all customers in the system.
     */
    private void printAllCustomers() {
        System.out.println("\n===== Fetching All Customers =====");

        List<User> customers = customerService.getAllUsers();
        if (customers.isEmpty()) {
            System.out.println("No customers found.");
        } else {
            customers.forEach(customer -> System.out.println("Customer: " + customer));
        }
    }

    /**
     * Tests adding and retrieving comments for the test recipe.
     */
    private void testComments() {
        System.out.println("\n===== Testing Comments =====");

        try {
            // Add a comment to the test recipe
            Comment comment = new Comment();
            comment.setText("This is a test comment for the recipe.");
            comment.setUser(customer1);
            comment.setRecipe(testRecipe);
            Comment createdComment = commentService.createComment(comment);
            System.out.println("Created Comment: " + createdComment);

            // Retrieve the comment by ID
            Comment retrievedComment = commentService.getCommentById(createdComment.getId());
            System.out.println("Retrieved Comment: " + retrievedComment);

        } catch (Exception e) {
            System.out.println("Error testing comments: " + e.getMessage());
        }
    }

    /**
     * Tests creating and retrieving ratings for the test recipe.
     */
    private void testRatings() {
        System.out.println("\n===== Testing Ratings =====");

        try {
            RatingCreateRequest ratingRequest = RatingCreateRequest.builder()
                    .recipeId(testRecipe.getId())
                    .userId(customer1.getId())
                    .score(5)
                    .comment("This is a test rating.")
                    .build();

            RatingResponse createdRating = ratingService.createRating(ratingRequest);
            System.out.println("Created Rating: " + createdRating);

            // Retrieve the rating by ID
            RatingResponse retrievedRating = ratingService.getRatingById(createdRating.getId());
            System.out.println("Retrieved Rating: " + retrievedRating);

        } catch (Exception e) {
            System.out.println("Error testing ratings: " + e.getMessage());
        }
    }
}
