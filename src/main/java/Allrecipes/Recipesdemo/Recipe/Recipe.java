package Allrecipes.Recipesdemo.Recipe;

import Allrecipes.Recipesdemo.Entities.Category;
import Allrecipes.Recipesdemo.Entities.User;
import Allrecipes.Recipesdemo.Entities.Enums.RecipeStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "recipes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(nullable = false)
    private String title;

    @Lob
    private String description;

    @ElementCollection
    @CollectionTable(name = "recipe_ingredients", joinColumns = @JoinColumn(name = "recipe_id"))
    @Column(name = "ingredient")
    private List<String> ingredients = new ArrayList<>();

    @Lob
    private String preparationSteps;

    private int cookingTime;
    private int servings;
    private String dietaryInfo;

    @Enumerated(EnumType.STRING)
    private RecipeStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @Column(nullable = false)
    private boolean containsGluten = true;


    @ManyToMany
//    @JoinTable(
//            name = "recipe_categories",
//            joinColumns = @JoinColumn(name = "recipe_id"),
//            inverseJoinColumns = @JoinColumn(name = "category_id")
//    )
    @Builder.Default
    private Set<Category> categories = new HashSet<>();

    public String formatCookingTime(int minutes) {
        int hours = minutes / 60;
        int remainingMinutes = minutes % 60;
        return hours > 0 ? hours + " hrs " + remainingMinutes + " mins" : remainingMinutes + " mins";
    }

}
