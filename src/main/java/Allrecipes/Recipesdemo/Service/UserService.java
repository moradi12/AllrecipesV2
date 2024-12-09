package Allrecipes.Recipesdemo.Service;

import Allrecipes.Recipesdemo.DTOs.UserResponse;
import Allrecipes.Recipesdemo.DTOs.UserUpdateRequest;
import Allrecipes.Recipesdemo.Entities.Role;
import Allrecipes.Recipesdemo.Entities.User;
import Allrecipes.Recipesdemo.Exceptions.EmailAlreadyTakenException;
import Allrecipes.Recipesdemo.Exceptions.RecipeNotFoundException;
import Allrecipes.Recipesdemo.Exceptions.UserNotFoundException;
import Allrecipes.Recipesdemo.Exceptions.UsernameAlreadyTakenException;
import Allrecipes.Recipesdemo.Recipe.Recipe;
import Allrecipes.Recipesdemo.Repositories.RecipeRepository;
import Allrecipes.Recipesdemo.Repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    private final PasswordEncoder passwordEncoder;

    // Constructor injection
    public UserService(UserRepository userRepository, RecipeRepository recipeRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new user.
     *
     * @param username    The desired username.
     * @param email       The user's email address.
     * @param rawPassword The user's raw password.
     * @return The registered user.
     */
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
                .password(passwordEncoder.encode(rawPassword))
                .role(Role.USER)
                .build();
        return userRepository.save(user);
    }

    /**
     * Validates the registration data.
     *
     * @param username    The desired username.
     * @param email       The user's email address.
     * @param rawPassword The user's raw password.
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
        // Additional validations (e.g., email format) can be added here
    }

    /**
     * Finds a user by username or email.
     *
     * @param usernameOrEmail The username or email to search for.
     * @return An Optional containing the found user or empty if not found.
     */
    public Optional<User> findByUsernameOrEmail(String usernameOrEmail) {
        return userRepository.findByUsername(usernameOrEmail)
                .or(() -> userRepository.findByEmail(usernameOrEmail));
    }

    /**
     * Finds a user by ID.
     *
     * @param id The user ID.
     * @return An Optional containing the found user or empty if not found.
     */
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Retrieves all users.
     *
     * @return A list of all users.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Deletes a user by ID.
     *
     * @param id The user ID.
     */
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User with ID " + id + " not found.");
        }
        userRepository.deleteById(id);
    }

    /**
     * Converts a User entity to a UserResponse DTO.
     *
     * @param user The user entity.
     * @return The UserResponse DTO.
     */
    public UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().name())
                .favorites(
                        user.getFavorites().stream()
                                .map(Recipe::getId)
                                .collect(Collectors.toSet())
                )
                .build();
    }

    /**
     * Checks if the raw password matches the encoded password.
     *
     * @param rawPassword     The raw password.
     * @param encodedPassword The encoded password.
     * @return True if the passwords match, false otherwise.
     */
    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * Updates a user's information.
     *
     * @param user    The user to update.
     * @param request The update request containing new data.
     * @return The updated user.
     */
    public User updateUser(User user, UserUpdateRequest request) {
        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
            if (existingUser.isPresent() && !existingUser.get().getId().equals(user.getId())) {
                throw new EmailAlreadyTakenException("Email '" + request.getEmail() + "' is already in use.");
            }
            user.setEmail(request.getEmail());
        }

        if (request.getUsername() != null && !request.getUsername().isBlank()) {
            Optional<User> existingUser = userRepository.findByUsername(request.getUsername());
            if (existingUser.isPresent() && !existingUser.get().getId().equals(user.getId())) {
                throw new UsernameAlreadyTakenException("Username '" + request.getUsername() + "' is already taken.");
            }
            user.setUsername(request.getUsername());
        }

        // Optionally update other fields like password, etc.
        // Example:
        // if (request.getPassword() != null && !request.getPassword().isBlank()) {
        //     user.setPassword(passwordEncoder.encode(request.getPassword()));
        // }

        return userRepository.save(user);
    }

    /**
     * Adds a favorite recipe to a user's favorites.
     *
     * @param user     The user.
     * @param recipeId The recipe ID to add.
     */
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
     * Removes a favorite recipe from a user's favorites.
     *
     * @param user     The user.
     * @param recipeId The recipe ID to remove.
     */
    public void removeFavoriteRecipe(User user, Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RecipeNotFoundException("Recipe with ID " + recipeId + " not found."));
        if (!user.getFavorites().contains(recipe)) {
            throw new IllegalArgumentException("Recipe is not in favorites.");
        }
        user.getFavorites().remove(recipe);
        userRepository.save(user);
    }
}
