// src/Routes/MainRoute.tsx
import { Route, Routes } from "react-router-dom";
import CreateRecipe from "../../AddRecipe/CreateRecipe";
import RecipeAdminDashboard from "../../Admin/AdminDashboard";
import CategoryDropdown from "../../CategoryDropdown/CategoryDropdown";
import Contact from "../../Contact Page/Contact";
import Dashboard from "../../Dashboard/Dashboard";
import EditRecipe from "../../EditRecipe/EditRecipe";
import EditRecipeAdmin from "../../EditRecipe/EditRecipeAdmin";
import Features from "../../Features/Features";
import FoodHomePage from "../../FoodHomePage/FoodHomePage";
import GetAllRecipes from "../../GetAllRecipes/GetAllRecipes";
import GetSingleRecipe from "../../GetSingleRecipe/GetSingleRecipe";
import HomePage from "../../HomePage/OldHomePage";
import LoginForm from "../../Login/LoginForm";
import { Page404 } from "../../Page404/Page404";
import RegisterForm from "../../Register/RegisterForm";
import SearchRecipes from "../../SearchRecipes/SearchRecipes";
import UpdateRecipe from "../../UpdateRecipe/UpdateRecipe";
import UserPanel from "../../UserPanel/UserPanel";

export function MainRoute(): JSX.Element {
  return (
    <Routes>
      {/* Top-level routes */}
      <Route path="/" element={<HomePage />} />
      <Route path="/food" element={<FoodHomePage />} />
      {/* <Route path="/recipes" element={<RecipesPage />} /> */}
      <Route path="/features" element={<Features />} />
      <Route path="/contact" element={<Contact />} />
      <Route path="/login" element={<LoginForm />} />
      <Route path="/register" element={<RegisterForm />} />
      <Route path="/dashboard" element={<Dashboard />} />
      <Route path="/categories" element={<CategoryDropdown />} />
      <Route path="/create" element={<CreateRecipe />} />
      <Route path="/recipes/:recipeId" element={<GetSingleRecipe />} />
      <Route path="/edit-recipe/:id" element={<EditRecipe />} />

      {/* Existing "All Recipes" route */}
      <Route path="/all/recipes" element={<GetAllRecipes />} />

      <Route path="/admin" element={<RecipeAdminDashboard />} />
      <Route path="/admin/edit-recipe/:id" element={<EditRecipeAdmin />} />

      <Route path="/userpanel" element={<UserPanel />} />

      {/*
        Additional routes for the other controller methods:
        getRecipeById, updateRecipe, deleteRecipe, and searchRecipes.
      */}
      <Route path="/recipes/update" element={<UpdateRecipe />} />
      <Route path="/recipes/search" element={<SearchRecipes />} />

      {/* Catch-all for undefined routes */}
      <Route path="*" element={<Page404 />} />
    </Routes>
  );
}
export default MainRoute;
