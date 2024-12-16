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
    public List<Category> searchCategoriesByName(String keyword) {
        return categoryRepository.findByNameContainingIgnoreCase(keyword);
    }
    public List<Category> getCategoriesSortedByName() {
        return categoryRepository.findAllByOrderByNameAsc();
    }
    public boolean categoryExists(String name) {
        return categoryRepository.existsByName(name);
    }
    public void deleteCategoryByName(String name) {
        if (categoryExists(name)) {
            categoryRepository.deleteByName(name);
        } else {
            throw new IllegalArgumentException("Category with name " + name + " does not exist.");
        }
    }
    public List<Category> getCategoriesByIds(List<Long> ids) {
        return categoryRepository.findByIdIn(ids);
    }
}
