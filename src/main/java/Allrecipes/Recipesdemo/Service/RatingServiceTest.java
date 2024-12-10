package Allrecipes.Recipesdemo.Service;

import Allrecipes.Recipesdemo.Entities.Rating;
import Allrecipes.Recipesdemo.Entities.User;
import Allrecipes.Recipesdemo.Exceptions.ResourceNotFoundException;
import Allrecipes.Recipesdemo.Rating.RatingResponse;
import Allrecipes.Recipesdemo.Recipe.Recipe;
import Allrecipes.Recipesdemo.Repositories.RatingRepository;
import Allrecipes.Recipesdemo.Repositories.RecipeRepository;
import Allrecipes.Recipesdemo.Repositories.UserRepository;
import Allrecipes.Recipesdemo.Request.RatingCreateRequest;
import Allrecipes.Recipesdemo.Response.RatingMapper;


import java.util.Optional;


@ExtendWith(MockitoExtension.class) // Integrates Mockito with JUnit 5
class RatingServiceTest {

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RatingMapper ratingMapper;

    @InjectMocks
    private RatingService ratingService;

    private Recipe recipe;
    private User user;
    private Rating rating;
    private RatingCreateRequest createRequest;
    private RatingResponse ratingResponse;

    @BeforeEach
    void setUp() {
        // Initialize test data
        recipe = Recipe.builder()
                .id(1L)
                .name("Spaghetti Bolognese")
                .build();

        user = User.builder()
                .id(1L)
                .username("john_doe")
                .build();

        rating = Rating.builder()
                .id(1L)
                .score(5)
                .comment("Excellent recipe!")
                .recipe(recipe)
                .user(user)
                .build();

        createRequest = RatingCreateRequest.builder()
                .score(5)
                .comment("Excellent recipe!")
                .recipeId(1L)
                .userId(1L)
                .build();

        ratingResponse = RatingResponse.builder()
                .id(1L)
                .score(5)
                .comment("Excellent recipe!")
                .recipeId(1L)
                .userId(1L)
                .recipeName("Spaghetti Bolognese")
                .userName("john_doe")
                .build();
    }

    @Test
    void createRating_Success() {
        // Define behavior of mocks
        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(ratingRepository.existsByRecipeIdAndUserId(1L, 1L)).thenReturn(false);
        when(ratingRepository.save(any(Rating.class))).thenReturn(rating);
        when(ratingMapper.toDto(rating)).thenReturn(ratingResponse);

        // Execute the service method
        RatingResponse response = ratingService.createRating(createRequest);

        // Assertions
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(5, response.getScore());
        assertEquals("Excellent recipe!", response.getComment());
        assertEquals(1L, response.getRecipeId());
        assertEquals(1L, response.getUserId());
        assertEquals("Spaghetti Bolognese", response.getRecipeName());
        assertEquals("john_doe", response.getUserName());

        // Verify interactions
        verify(recipeRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(1L);
        verify(ratingRepository, times(1)).existsByRecipeIdAndUserId(1L, 1L);
        verify(ratingRepository, times(1)).save(any(Rating.class));
        verify(ratingMapper, times(1)).toDto(rating);
    }

    @Test
    void createRating_RecipeNotFound() {
        // Define behavior of mocks
        when(recipeRepository.findById(1L)).thenReturn(Optional.empty());

        // Execute and assert exception
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            ratingService.createRating(createRequest);
        });

        assertEquals("Recipe not found with id: 1", exception.getMessage());

        // Verify interactions
        verify(recipeRepository, times(1)).findById(1L);
        verify(userRepository, times(0)).findById(anyLong());
        verify(ratingRepository, times(0)).existsByRecipeIdAndUserId(anyLong(), anyLong());
        verify(ratingRepository, times(0)).save(any(Rating.class));
        verify(ratingMapper, times(0)).toDto(any(Rating.class));
    }

    @Test
    void createRating_UserNotFound() {
        // Define behavior of mocks
        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Execute and assert exception
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            ratingService.createRating(createRequest);
        });

        assertEquals("User not found with id: 1", exception.getMessage());

        // Verify interactions
        verify(recipeRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(1L);
        verify(ratingRepository, times(0)).existsByRecipeIdAndUserId(anyLong(), anyLong());
        verify(ratingRepository, times(0)).save(any(Rating.class));
        verify(ratingMapper, times(0)).toDto(any(Rating.class));
    }

    @Test
    void createRating_UserAlreadyRated() {
        // Define behavior of mocks
        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(ratingRepository.existsByRecipeIdAndUserId(1L, 1L)).thenReturn(true);

        // Execute and assert exception
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            ratingService.createRating(createRequest);
        });

        assertEquals("User has already rated this recipe.", exception.getMessage());

        // Verify interactions
        verify(recipeRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(1L);
        verify(ratingRepository, times(1)).existsByRecipeIdAndUserId(1L, 1L);
        verify(ratingRepository, times(0)).save(any(Rating.class));
        verify(ratingMapper, times(0)).toDto(any(Rating.class));
    }
}
