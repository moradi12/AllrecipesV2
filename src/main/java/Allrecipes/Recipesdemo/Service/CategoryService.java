package Allrecipes.Recipesdemo.Service;

import Allrecipes.Recipesdemo.Entities.Category;
import Allrecipes.Recipesdemo.Entities.Enums.FoodCategories;
import Allrecipes.Recipesdemo.Repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name).orElse(null);
    }

    /**
     * Creates a new category if it doesn't already exist.
     *
     * @param foodCategory the FoodCategories enum value
     * @return the created or existing Category entity
     */
    public Category createCategory(FoodCategories foodCategory) {
        return categoryRepository.findByName(foodCategory.name())
                .orElseGet(() -> {
                    Category category = Category.builder()
                            .name(foodCategory.name())
                            .foodCategory(foodCategory)
                            .build();
                    return categoryRepository.save(category);
                });
    }
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}
