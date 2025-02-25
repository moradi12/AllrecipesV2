// ...
import React, { useCallback, useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";

import { Category } from "../../Models/Category";
import RecipeService from "../../Service/RecipeService";
import { notify } from "../../Utiles/notif";

import useRecipeForm from "../Redux/Hooks/useRecipeForm";

import CategorySelect from "./CategorySelect";
import "./CreateRecipe.css";
import ErrorMessages from "./ErrorMessages";
import IngredientsList from "./IngredientsList";
import PhotoUploader from "./PhotoUploader";

interface AuthState {
  isLogged: boolean;
}

interface RootState {
  auth: AuthState;
}

const CreateRecipe: React.FC = () => {
  const auth = useSelector((state: RootState) => state.auth);
  const navigate = useNavigate();

  const {
    form,
    setForm,
    errors,
    isSubmitting,
    handleChange,
    addIngredient,
    removeIngredient,
    handleIngredientChange,
  } = useRecipeForm();

  const [categories, setCategories] = useState<Category[]>([]);
  const [selectedCategoryId, setSelectedCategoryId] = useState<number | "">("");
  const [photoPreview, setPhotoPreview] = useState<string | null>(null);

  useEffect(() => {
    const token = sessionStorage.getItem("jwt");
    if (!auth.isLogged && !token) {
      notify.error("You must log in to create a recipe.");
      navigate("/login");
    }
  }, [auth.isLogged, navigate]);

  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const response = await RecipeService.getAllCategories();
        setCategories(response.data);
      } catch (err) {
        console.error("Error fetching categories:", err);
        setCategories([]);
      }
    };
    fetchCategories();
  }, []);

  const handleCategoryChange = useCallback((value: string) => {
    const parsedValue = Number(value);
    if (!isNaN(parsedValue)) {
      setSelectedCategoryId(parsedValue);
    } else {
      setSelectedCategoryId("");
    }
  }, []);

  const handleFormSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (selectedCategoryId === "") {
      notify.error("Please select a valid category.");
      return;
    }

    const finalForm = {
      ...form,
      categoryIds: [selectedCategoryId],
    };

    const token = sessionStorage.getItem("jwt");
    if (!token) {
      notify.error("Please log in to create a recipe.");
      navigate("/login");
      return;
    }

    try {
      const response = await RecipeService.createRecipe(finalForm, token);
      console.log("Created Recipe Response:", response.data);
      notify.success("Recipe created successfully!");
      navigate("/all/recipes");
    } catch (error) {
      console.error("Error creating recipe:", error);
      notify.error("Failed to create recipe. Please try again.");
    }
  };

  const handlePhotoChange = (file: File) => {
    if (!file) return;
    const reader = new FileReader();
    reader.onload = () => {
      if (typeof reader.result === "string") {
        const fullResult = reader.result;
        const base64Part = fullResult.split(",")[1] || "";

        setForm((prev) => ({
          ...prev,
          photo: base64Part,
        }));
        setPhotoPreview(fullResult);
      }
    };
    reader.readAsDataURL(file);
  };

  if (!auth.isLogged && !sessionStorage.getItem("jwt")) {
    return null;
  }

  return (
    <div className="create-recipe-container">
      <h2>Create a New Recipe</h2>

      <ErrorMessages errors={errors} />

      <form onSubmit={handleFormSubmit}>
        {/* Title */}
        <div className="form-group">
          <label htmlFor="title">
            <p>Title*</p>
          </label>
          <input
            type="text"
            id="title"
            value={form.title}
            onChange={handleChange}
            required
          />
          {errors.title && <span className="error-text">{errors.title}</span>}
        </div>

        {/* Description */}
        <div className="form-group">
          <label htmlFor="description">
            <p>Description*</p>
          </label>
          <textarea
            id="description"
            value={form.description}
            onChange={handleChange}
            required
          />
          {errors.description && (
            <span className="error-text">{errors.description}</span>
          )}
        </div>

        {/* Ingredients List */}
        <IngredientsList
          ingredients={form.ingredients}
          onIngredientChange={handleIngredientChange}
          onRemoveIngredient={removeIngredient}
          onAddIngredient={addIngredient}
          error={errors.ingredients}
        />

        {/* Preparation Steps */}
        <div className="form-group">
          <label htmlFor="preparationSteps">
            <p>Preparation Steps*</p>
          </label>
          <textarea
            id="preparationSteps"
            value={form.preparationSteps}
            onChange={handleChange}
            required
          />
          {errors.preparationSteps && (
            <span className="error-text">{errors.preparationSteps}</span>
          )}
        </div>

        {/* Cooking Time */}
        <div className="form-group">
          <label htmlFor="cookingTime">
            <p>Cooking Time (minutes)*</p>
          </label>
          <input
            type="number"
            id="cookingTime"
            value={form.cookingTime}
            onChange={handleChange}
            min="1"
            required
          />
          {errors.cookingTime && (
            <span className="error-text">{errors.cookingTime}</span>
          )}
        </div>

        {/* Servings */}
        <div className="form-group">
          <label htmlFor="servings">
            <p>Servings*</p>
          </label>
          <input
            type="number"
            id="servings"
            value={form.servings}
            onChange={handleChange}
            min="1"
            required
          />
          {errors.servings && (
            <span className="error-text">{errors.servings}</span>
          )}
        </div>

        {/* Dietary Info */}
        <div className="form-group">
          <label htmlFor="dietaryInfo">
            <p>Dietary Information</p>
          </label>
          <input
            type="text"
            id="dietaryInfo"
            value={form.dietaryInfo}
            placeholder="e.g. vegan, vegetarian..."
            onChange={handleChange}
          />
        </div>

        {/* Category Select */}
        <CategorySelect
          categories={categories}
          selectedCategoryId={selectedCategoryId}
          onChange={handleCategoryChange}
          error={errors.category}
        />

        {/* Photo Uploader */}
        <PhotoUploader
          onPhotoChange={handlePhotoChange}
          photoPreview={photoPreview}
        />

        {/* Contains Gluten */}
        <div className="form-group checkbox-group">
          <label htmlFor="containsGluten">
            <p>Contains Gluten</p>
          </label>
          <input
            type="checkbox"
            id="containsGluten"
            checked={form.containsGluten}
            onChange={handleChange}
          />
        </div>

        {/* Submit Errors */}
        {errors.submit && (
          <div className="error-text submit-error">{errors.submit}</div>
        )}

        {/* Submit Button */}
        <button type="submit" className="submit-button" disabled={isSubmitting}>
          {isSubmitting ? "Creating..." : "Create Recipe"}
        </button>
      </form>
    </div>
  );
};

export default CreateRecipe;
