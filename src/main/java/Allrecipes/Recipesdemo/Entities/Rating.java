package Allrecipes.Recipesdemo.Entities;

import Allrecipes.Recipesdemo.Recipe.Recipe;
import jakarta.persistence.*;
import lombok.*;

/**
 * Entity representing a user rating for a specific recipe.
 */
@Entity
@Table(name = "ratings",
        uniqueConstraints = @UniqueConstraint(columnNames = {"recipe_id", "user_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="recipe_id", nullable=false)
    private Recipe recipe;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @Column(nullable = false)
    private int score;

    @Column(length = 500)
    private String comment;
}
