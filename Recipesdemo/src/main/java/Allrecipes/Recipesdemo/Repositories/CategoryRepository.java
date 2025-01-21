package Allrecipes.Recipesdemo.Repositories;

import Allrecipes.Recipesdemo.Entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

     Optional<Category> findByName(String name);

     List<Category> findByNameContainingIgnoreCase(String keyword);

     List<Category> findAllByOrderByNameAsc();

     List<Category> findByIdIn(List<Long> ids);

     boolean existsByName(String name);

     void deleteByName(String name);

}
