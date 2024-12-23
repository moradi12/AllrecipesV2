import React from "react";
import { Recipe } from "../../Models/Recipe";
import FormInput from "../FormInput/FormInput";

interface RecipeFormProps {
  recipe: Partial<Recipe>;
  onInputChange: (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) => void;
}

const RecipeForm: React.FC<RecipeFormProps> = ({ recipe, onInputChange }) => {
  const handleNumberInputChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) => {
    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    const { value, name } = e.target;
    const numericValue = Number(value);

    // Ensure the value is a positive number or empty string
    if ((!isNaN(numericValue) && numericValue >= 0) || value === "") {
      onInputChange(e);
    }
  };

  return (
    <div className="recipe-form">
      <FormInput
        label="Recipe Name"
        type="text"
        name="name"
        value={recipe.name || ""}
        onChange={onInputChange}
        placeholder="Enter recipe name"
      />
      <FormInput
        label="Title"
        type="text"
        name="title"
        value={recipe.title || ""}
        onChange={onInputChange}
        placeholder="Enter title"
      />
      <FormInput
        label="Description"
        type="textarea"
        name="description"
        value={recipe.description || ""}
        onChange={onInputChange}
        placeholder="Enter a brief description"
        multiline
      />
      <FormInput
        label="Preparation Steps"
        type="textarea"
        name="preparationSteps"
        value={recipe.preparationSteps || ""}
        onChange={onInputChange}
        placeholder="Enter steps"
        multiline
      />
      <FormInput
        label="Cooking Time (mins)"
        type="number"
        name="cookingTime"
        value={recipe.cookingTime?.toString() || ""}
        onChange={handleNumberInputChange}
        placeholder="Enter cooking time in minutes"
      />
      <FormInput
        label="Servings"
        type="number"
        name="servings"
        value={recipe.servings?.toString() || ""}
        onChange={handleNumberInputChange}
        placeholder="Enter number of servings"
      />
    </div>
  );
};

export default RecipeForm;
