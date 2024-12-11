package Allrecipes.Recipesdemo.Service;

import Allrecipes.Recipesdemo.Entities.Comment; // Hypothetical entity; adjust as needed
import Allrecipes.Recipesdemo.Exceptions.ResourceNotFoundException;
import Allrecipes.Recipesdemo.Repositories.CommentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }
    public Comment getCommentById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + id));
    }
    public Comment updateComment(Long id, Comment updated) {
        Comment existing = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + id));

        if (updated.getText() != null) {
            existing.setText(updated.getText());
        }
        return commentRepository.save(existing);
    }
    public void deleteComment(Long id) {
        Comment existing = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + id));

        commentRepository.delete(existing);
    }
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }
}
