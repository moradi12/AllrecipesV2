package Allrecipes.Recipesdemo.Repositories;

import Allrecipes.Recipesdemo.Entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

     // Existing method
     Optional<Category> findByName(String name);

     // Find categories whose names contain a specific keyword (case insensitive)
     List<Category> findByNameContainingIgnoreCase(String keyword);

     // Find all categories sorted by their name
     List<Category> findAllByOrderByNameAsc();

     // Find categories by their IDs
     List<Category> findByIdIn(List<Long> ids);

     // Check if a category exists by its name
     boolean existsByName(String name);

     // Delete a category by its name
     void deleteByName(String name);

}
