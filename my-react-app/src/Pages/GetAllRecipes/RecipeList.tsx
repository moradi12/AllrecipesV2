import React from "react";
import { useNavigate } from "react-router-dom";
import { RecipeResponse } from "../../Models/RecipeResponse";
import "./RecipeList.css";

interface RecipeListProps {
  recipes: RecipeResponse[];
  onDeleteRecipe: (id: number) => void;
  onEditRecipe: (id: number) => void;
}

const RecipeList: React.FC<RecipeListProps> = ({
  recipes,
  onDeleteRecipe,
  onEditRecipe,
}) => {
  const navigate = useNavigate();

  if (!recipes.length) {
    return <p>No recipes found.</p>;
  }

  return (
    <ul className="recipe-list">
      {recipes.map((recipe) => (
        <li key={recipe.id} className="recipe-item">
          <h3>{recipe.title || "No Title"}</h3>
          <p>
            <strong>Description:</strong> {recipe.description || "No Description"}
          </p>
          <p>
            <strong>Cooking Time:</strong> {recipe.cookingTime} minutes
          </p>
          <p>
            <strong>Servings:</strong> {recipe.servings}
          </p>
          <p>
            <strong>Dietary Info:</strong> {recipe.dietaryInfo || "N/A"}
          </p>
          <p>
            <strong>Contains Gluten:</strong>{" "}
            {recipe.containsGluten ? "Yes" : "No"}
          </p>
          <p>
            <strong>Status:</strong> {recipe.status || "Unknown"}
          </p>
          <p>
            <strong>Categories:</strong>{" "}
            {recipe.categories && recipe.categories.length > 0
              ? recipe.categories.join(", ")
              : "Uncategorized"}
          </p>
          <p>
            <strong>Preparation Steps:</strong>{" "}
            {recipe.preparationSteps || "None"}
          </p>
          <p>
            <strong>Created By:</strong>{" "}
            {recipe.createdByUsername || "Unknown"}
          </p>
          {recipe.photo && (
            <div className="photo-preview">
              <img
                src={`data:image/png;base64,${recipe.photo}`}
                alt="Recipe"
                className="preview-image"
                style={{ maxWidth: "200px" }}
              />
            </div>
          )}

          <h4>Ingredients:</h4>
          <ul>
            {recipe.ingredients.map((ingredient, idx) => (
              <li key={idx}>{ingredient}</li>
            ))}
          </ul>

          <button
            className="button button-primary"
            onClick={() => onEditRecipe(recipe.id)}
          >
            Edit
          </button>
          <button
            className="button button-danger"
            onClick={() => onDeleteRecipe(recipe.id)}
          >
            Delete
          </button>
          <button
            className="button button-primary"
            onClick={() => navigate(`/recipes/${recipe.id}`)}
          >
            View
          </button>
        </li>
      ))}
    </ul>
  );
};

export default RecipeList;
