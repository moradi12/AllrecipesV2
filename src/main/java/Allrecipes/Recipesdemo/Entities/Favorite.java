package Allrecipes.Recipesdemo.Entities;
import Allrecipes.Recipesdemo.Recipe.Recipe;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="favorites")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Favorite {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @ManyToOne
    @JoinColumn(name="recipe_id", nullable=false)
    private Recipe recipe;
}