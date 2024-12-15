package Allrecipes.Recipesdemo.Service;

import Allrecipes.Recipesdemo.Recipe.Recipe;
import Allrecipes.Recipesdemo.Response.UserResponse;
import Allrecipes.Recipesdemo.Entities.Enums.UserType;
import Allrecipes.Recipesdemo.Entities.User;
import Allrecipes.Recipesdemo.Exceptions.*;
import Allrecipes.Recipesdemo.Repositories.RecipeRepository;
import Allrecipes.Recipesdemo.Repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService {
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;

    public CustomerService(UserRepository userRepository, RecipeRepository recipeRepository) {
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
    }

    /**
     * Seeds an admin user upon application startup if it doesn't already exist.
     */
    @PostConstruct
    @Transactional
    public void seedAdminUser() {
        if (!userRepository.findByEmail("admin@admin.com").isPresent()) {
            User admin = User.builder()
                    .username("admin")
                    .email("admin@admin.com")
                    .password("admin") // In production, ensure passwords are hashed
                    .userType(UserType.ADMIN)
                    .build();
            userRepository.save(admin);
        }
    }

    /**
     * Authenticates a user based on username/email and password.
     *
     * @param usernameOrEmail The username or email of the user.
     * @param rawPassword     The plain-text password of the user.
     * @return The authenticated User object.
     * @throws UserNotFoundException If the user is not found or the password is incorrect.
     */
    @Transactional(readOnly = true)
    public User login(String usernameOrEmail, String rawPassword) {
        User user = userRepository.findByUsername(usernameOrEmail)
                .or(() -> userRepository.findByEmail(usernameOrEmail))
                .orElseThrow(() -> new UserNotFoundException("User with username or email '" + usernameOrEmail + "' not found."));

        // In production, use a password encoder like BCrypt to verify passwords
        if (!rawPassword.equals(user.getPassword())) {
            throw new UserNotFoundException("Invalid username/email or password.");
        }

        return user;
    }

    /**
     * Registers a new user with the given details.
     *
     * @param username    The desired username.
     * @param email       The user's email address.
     * @param rawPassword The user's plain-text password.
     * @return The registered User object.
     * @throws UsernameAlreadyTakenException If the username is already in use.
     * @throws EmailAlreadyTakenException    If the email is already in use.
     * @throws IllegalArgumentException      If any input data is invalid.
     */
    @Transactional
    public User registerUser(String username, String email, String rawPassword) {
        validateRegistrationData(username, email, rawPassword);

        if (userRepository.findByUsername(username).isPresent()) {
            throw new UsernameAlreadyTakenException("Username '" + username + "' is already taken.");
        }

        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyTakenException("Email '" + email + "' is already in use.");
        }

        User user = User.builder()
                .username(username)
                .email(email)
                .password(rawPassword) // In production, ensure passwords are hashed
                .userType(UserType.CUSTOMER) // Default to CUSTOMER
                .build();
        return userRepository.save(user);
    }

    /**
     * Validates the registration data.
     *
     * @param username    The desired username.
     * @param email       The user's email address.
     * @param rawPassword The user's plain-text password.
     * @throws IllegalArgumentException If any input data is invalid.
     */
    private void validateRegistrationData(String username, String email, String rawPassword) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or empty.");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or empty.");
        }
        if (rawPassword == null || rawPassword.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters long.");
        }
    }

    /**
     * Finds a user by either username or email.
     *
     * @param usernameOrEmail The username or email to search for.
     * @return An Optional containing the User if found, or empty otherwise.
     */
    @Transactional(readOnly = true)
    public Optional<User> findByUsernameOrEmail(String usernameOrEmail) {
        return userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
    }

    /**
     * Finds a user by their ID.
     *
     * @param id The ID of the user.
     * @return An Optional containing the User if found, or empty otherwise.
     */
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Retrieves all users in the system.
     *
     * @return A list of all User objects.
     */
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id The ID of the user to delete.
     * @throws UserNotFoundException If the user with the given ID does not exist.
     */
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User with ID " + id + " not found.");
        }
        userRepository.deleteById(id);
    }

    /**
     * Converts a User entity to a UserResponse DTO.
     *
     * @param user The User entity.
     * @return The UserResponse DTO.
     */
    @Transactional(readOnly = true)
    public UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .userType(user.getUserType().name()) // Reflect the UserType as a string
                .favorites(
                        user.getFavorites().stream()
                                .map(Recipe::getId)
                                .collect(Collectors.toSet())
                )
                .build();
    }

    /**
     * Retrieves all users and maps them to UserResponse DTOs.
     *
     * @return A list of UserResponse DTOs.
     */
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUserResponses() {
        return userRepository.findAll().stream()
                .map(this::toUserResponse)
                .collect(Collectors.toList());
    }

    /**
     * Updates a user's details.
     *
     * @param userId      The ID of the user to update.
     * @param newUsername The new username (optional).
     * @param newEmail    The new email (optional).
     * @param newPassword The new password (optional).
     * @throws UserNotFoundException         If the user with the given ID does not exist.
     * @throws UsernameAlreadyTakenException If the new username is already in use.
     * @throws EmailAlreadyTakenException    If the new email is already in use.
     */
    @Transactional
    public void updateUser(Long userId, String newUsername, String newEmail, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found."));

        if (newUsername != null && !newUsername.isBlank() && !newUsername.equals(user.getUsername())) {
            if (userRepository.findByUsername(newUsername).isPresent()) {
                throw new UsernameAlreadyTakenException("Username '" + newUsername + "' is already taken.");
            }
            user.setUsername(newUsername);
        }

        if (newEmail != null && !newEmail.isBlank() && !newEmail.equals(user.getEmail())) {
            if (userRepository.findByEmail(newEmail).isPresent()) {
                throw new EmailAlreadyTakenException("Email '" + newEmail + "' is already in use.");
            }
            user.setEmail(newEmail);
        }

        if (newPassword != null && newPassword.length() >= 6) {
            user.setPassword(newPassword); // In production, ensure passwords are hashed
        }

        userRepository.save(user);
    }

    /**
     * Adds a recipe to a user's favorites.
     *
     * @param user     The User entity.
     * @param recipeId The ID of the recipe to add.
     * @throws RecipeNotFoundException If the recipe does not exist.
     * @throws IllegalArgumentException If the recipe is already in favorites.
     */
    @Transactional
    public void addFavoriteRecipe(User user, Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RecipeNotFoundException("Recipe with ID " + recipeId + " not found."));
        if (user.getFavorites().contains(recipe)) {
            throw new IllegalArgumentException("Recipe is already in favorites.");
        }
        user.getFavorites().add(recipe);
        userRepository.save(user);
    }

    /**
     * Removes a recipe from a user's favorites.
     *
     * @param user     The User entity.
     * @param recipeId The ID of the recipe to remove.
     * @throws RecipeNotFoundException If the recipe does not exist.
     * @throws IllegalArgumentException If the recipe is not in favorites.
     */
    @Transactional
    public void removeFavoriteRecipe(User user, Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RecipeNotFoundException("Recipe with ID " + recipeId + " not found."));
        if (!user.getFavorites().contains(recipe)) {
            throw new IllegalArgumentException("Recipe is not in favorites.");
        }
        user.getFavorites().remove(recipe);
        userRepository.save(user);
    }

    /**
     * Assigns a new user type to a user.
     *
     * @param userId      The ID of the user.
     * @param newUserType The new UserType to assign.
     * @throws UserNotFoundException If the user with the given ID does not exist.
     */
    @Transactional
    public void assignUserTypeToUser(Long userId, UserType newUserType) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found."));
        user.setUserType(newUserType);
        userRepository.save(user);
    }
}
