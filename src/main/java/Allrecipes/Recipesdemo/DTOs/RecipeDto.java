package Allrecipes.Recipesdemo.DTOs;

import Allrecipes.Recipesdemo.Entities.Enums.RecipeStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class RecipeDto {
    private Long id;
    private String name;
    private String title;
    private String description;
    private List<String> ingredients;
    private String preparationSteps;
    private int cookingTime;
    private int servings;
    private String dietaryInfo;
    private RecipeStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long createdByUserId;
    private boolean containsGluten;
    private Set<Long> categoryIds;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<String> getIngredients() { return ingredients; }
    public void setIngredients(List<String> ingredients) { this.ingredients = ingredients; }

    public String getPreparationSteps() { return preparationSteps; }
    public void setPreparationSteps(String preparationSteps) { this.preparationSteps = preparationSteps; }

    public int getCookingTime() { return cookingTime; }
    public void setCookingTime(int cookingTime) { this.cookingTime = cookingTime; }

    public int getServings() { return servings; }
    public void setServings(int servings) { this.servings = servings; }

    public String getDietaryInfo() { return dietaryInfo; }
    public void setDietaryInfo(String dietaryInfo) { this.dietaryInfo = dietaryInfo; }

    public RecipeStatus getStatus() { return status; }
    public void setStatus(RecipeStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Long getCreatedByUserId() { return createdByUserId; }
    public void setCreatedByUserId(Long createdByUserId) { this.createdByUserId = createdByUserId; }

    public boolean isContainsGluten() { return containsGluten; }
    public void setContainsGluten(boolean containsGluten) { this.containsGluten = containsGluten; }

    public Set<Long> getCategoryIds() { return categoryIds; }
    public void setCategoryIds(Set<Long> categoryIds) { this.categoryIds = categoryIds; }
}
