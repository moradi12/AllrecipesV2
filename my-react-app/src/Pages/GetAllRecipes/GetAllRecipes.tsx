import React, { useCallback, useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { Category } from "../../Models/Category";
import { RecipeResponse } from "../../Models/RecipeResponse";
import { RecipeStatus } from "../../Models/RecipeStatus";
import RecipeService, { PaginatedRecipes } from "../../Service/RecipeService";
import { checkData } from "../../Utiles/checkData";
import { notify } from "../../Utiles/notif";
import { recipeSystem } from "../Redux/store";
import CategoryFilter from "./CategoryFilter";
import "./GetAllRecipes.css";
import PaginationControls from "./PaginationControls";
import RecipeList from "./RecipeList";

const GetAllRecipes: React.FC = () => {
  const [recipes, setRecipes] = useState<RecipeResponse[]>([]);
  const [categories, setCategories] = useState<Category[]>([]);
  const [filterCategory, setFilterCategory] = useState<string>("");
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  const auth = useSelector((state: any) => state.auth);
  const [pagination, setPagination] = useState({
    page: 0,
    size: 10,
    totalPages: 0,
  });
  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<string>("");
  const navigate = useNavigate();

  /* --------------------------------
   * Helpers: define user privileges
   * -------------------------------- */
  const canEdit = auth.userType === "ADMIN" || auth.userType === "EDITOR";
  const canApproveOrReject = auth.userType === "ADMIN";
  const canDelete = auth.userType === "ADMIN";

  useEffect(() => {
    checkData();
    console.log("Checked user authentication");
  }, []);

  const fetchCategories = useCallback(async () => {
    try {
      const response = await RecipeService.getAllCategories();
      console.log("Fetched categories:", response.data);
      setCategories(response.data);
    } catch (err) {
      notify.error("Failed to load categories.");
      console.error("Error fetching categories:", err);
    }
  }, []);

  const normalizeRecipes = (recipes: RecipeResponse[]): RecipeResponse[] => {
    return recipes.map((recipe) => ({
      ...recipe,
      categories:
        recipe.categories && recipe.categories.length > 0
          ? recipe.categories
          : ["Uncategorized"],
    }));
  };

  const fetchRecipes = useCallback(async () => {
    try {
      setLoading(true);
      setError("");

      const categoryParam = filterCategory ? Number(filterCategory) : undefined;
      console.log("Fetching recipes with:", {
        page: pagination.page,
        size: pagination.size,
        category: categoryParam || "All",
      });

      const response = await RecipeService.getAllRecipesPaginated(
        pagination.page,
        pagination.size,
        categoryParam
      );
      const data: PaginatedRecipes = response.data;
      const normalized = normalizeRecipes(data.content);

      setRecipes(normalized);
      setPagination((prev) => ({
        ...prev,
        totalPages: data.totalPages,
      }));
    } catch (err: unknown) {
      let errorMessage = "Failed to load recipes.";
      if (err instanceof Error) {
        errorMessage = err.message || errorMessage;
      }
      console.error("Error fetching recipes:", err);
      setError(errorMessage);
      notify.error(errorMessage);
    } finally {
      setLoading(false);
    }
  }, [pagination.page, pagination.size, filterCategory]);

  /* --------------------------------
   * Approve a recipe (ADMIN only)
   * -------------------------------- */
  const handleApproveRecipe = useCallback(
    async (id: number) => {
      const token = recipeSystem.getState().auth.token;
      if (!token || token.length < 10) {
        notify.error("Missing authorization token. Please log in again.");
        return;
      }
      try {
        await RecipeService.approveRecipe(id, token);
        console.log("Recipe approved successfully:", id);
        notify.success("Recipe approved successfully!");
        fetchRecipes(); // Refresh the list
      } catch (err: unknown) {
        let errorMessage = "Failed to approve recipe.";
        if (err instanceof Error) {
          errorMessage = err.message || errorMessage;
        }
        console.error("Error approving recipe:", err);
        notify.error(errorMessage);
      }
    },
    [fetchRecipes]
  );

  /* --------------------------------
   * Reject a recipe (ADMIN only)
   * -------------------------------- */
  const handleRejectRecipe = useCallback(
    async (id: number) => {
      const token = recipeSystem.getState().auth.token;
      if (!token || token.length < 10) {
        notify.error("Missing authorization token. Please log in again.");
        return;
      }
      try {
        await RecipeService.rejectRecipe(id, token);
        console.log("Recipe rejected successfully:", id);
        notify.success("Recipe rejected successfully!");
        fetchRecipes(); // Refresh the list
      } catch (err: unknown) {
        let errorMessage = "Failed to reject recipe.";
        if (err instanceof Error) {
          errorMessage = err.message || errorMessage;
        }
        console.error("Error rejecting recipe:", err);
        notify.error(errorMessage);
      }
    },
    [fetchRecipes]
  );

  /* --------------------------------
   * Delete a recipe (ADMIN only)
   * -------------------------------- */
  const handleDeleteRecipe = useCallback(
    async (id: number) => {
      const state = recipeSystem.getState();
      const token = state.auth.token;
      if (!token || token.length < 10) {
        notify.error("Missing authorization token. Please log in again.");
        return;
      }
      if (window.confirm("Are you sure you want to delete this recipe?")) {
        try {
          await RecipeService.deleteRecipe(id, token);
          console.log("Recipe deleted successfully:", id);
          notify.success("Recipe deleted successfully!");
          fetchRecipes();
        } catch (err: unknown) {
          let errorMessage = "An unexpected error occurred.";
          if (err instanceof Error) {
            errorMessage = err.message || errorMessage;
          }
          console.error("Error deleting recipe:", err);
          notify.error(errorMessage);
        }
      }
    },
    [fetchRecipes]
  );

  /* --------------------------------
   * Update a recipe status (ADMIN or CREATOR logic)
   * This example shows how a non-admin might update
   * a recipe’s status. Adjust accordingly.
   * -------------------------------- */
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  const handleChangeRecipeStatus = useCallback(
    async (id: number, newStatus: RecipeStatus) => {
      const token = recipeSystem.getState().auth.token;
      if (!token || token.length < 10) {
        notify.error("Missing authorization token. Please log in again.");
        return;
      }
      try {
        await RecipeService.updateRecipeStatus(id, newStatus, token);
        console.log("Recipe status updated:", id, newStatus);
        notify.success("Recipe status updated successfully!");
        fetchRecipes();
      } catch (err: unknown) {
        let errorMessage = "Failed to update recipe status.";
        if (err instanceof Error) {
          errorMessage = err.message || errorMessage;
        }
        console.error("Error updating recipe status:", err);
        notify.error(errorMessage);
      }
    },
    [fetchRecipes]
  );

  /* --------------------------------
   * Edit a recipe (ADMIN, EDITOR)
   * -------------------------------- */
  const handleEditRecipe = useCallback(
    (recipeId: number) => {
      // This navigates to an EditRecipe form for the given recipeId
      navigate(`/edit-recipe/${recipeId}`);
    },
    [navigate]
  );

  // Fetch categories on mount
  useEffect(() => {
    fetchCategories();
  }, [fetchCategories]);

  // Fetch recipes whenever filter/pagination changes
  useEffect(() => {
    fetchRecipes();
  }, [fetchRecipes]);

  return (
    <div className="get-all-recipes">
      <h2>
        All Recipes{" "}
        {pagination.totalPages > 0 &&
          `(Page ${pagination.page + 1} of ${pagination.totalPages})`}
      </h2>

      {loading && <p>Loading...</p>}
      {error && <p className="error-message">{error}</p>}

      {/* Category Filter */}
      <CategoryFilter
        categories={categories}
        filterCategory={filterCategory}
        setFilterCategory={setFilterCategory}
        disabled={loading}
      />

      {/* Recipe List */}
      <RecipeList
        recipes={recipes}
        onEditRecipe={canEdit ? handleEditRecipe : undefined}
        onApproveRecipe={canApproveOrReject ? handleApproveRecipe : undefined}
        onRejectRecipe={canApproveOrReject ? handleRejectRecipe : undefined}
        onDeleteRecipe={canDelete ? handleDeleteRecipe : undefined}
      />

      {/* Pagination */}
      {recipes.length > 0 && (
        <PaginationControls
          pagination={pagination}
          setPagination={setPagination}
          loading={loading}
        />
      )}
    </div>
  );
};

export default GetAllRecipes;
