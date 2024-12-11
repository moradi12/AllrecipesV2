package Allrecipes.Recipesdemo.Repositories;

import Allrecipes.Recipesdemo.Entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Comment entities.
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * If your Comment entity has a relationship to Recipe or User,
     * you can add query methods here, for example:
     *
     * List<Comment> findByRecipeId(Long recipeId);
     * List<Comment> findByUserId(Long userId);
     *
     * Adjust these methods based on your actual Comment entity fields.
     */
}
