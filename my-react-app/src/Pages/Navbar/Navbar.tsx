// src/Navbar/Navbar.tsx
import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { NavLink, useNavigate } from "react-router-dom";
import { logoutAction } from "../../Pages/Redux/AuthReducer"; // <-- Adjust path if necessary
import { AppDispatch, RootState } from "../../Pages/Redux/store"; // <-- Adjust path if necessary
import styles from "./Navbar.module.css";

const Navbar: React.FC = () => {
  // Redux
  const dispatch = useDispatch<AppDispatch>();
  const isLogged = useSelector((state: RootState) => state.auth.isLogged);

  // Local state for hamburger menu
  const [isOpen, setIsOpen] = useState(false);

  // React Router
  const navigate = useNavigate();

  // Toggle the mobile menu
  const toggleMenu = () => {
    setIsOpen(!isOpen);
  };

  const handleLogout = () => {
    dispatch(logoutAction());
    sessionStorage.removeItem("jwt");
    navigate("/login");
    setIsOpen(false);
  };

  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      const navbar = document.querySelector(`.${styles.navbar}`);
      if (navbar && !navbar.contains(event.target as Node)) {
        setIsOpen(false);
      }
    };
    document.addEventListener("click", handleClickOutside);
    return () => {
      document.removeEventListener("click", handleClickOutside);
    };
  }, []);

  return (
    <nav className={styles.navbar}>
      <div className={styles.logo}>
        <NavLink to="/" onClick={() => setIsOpen(false)}>
          Dessert Delights
        </NavLink>
      </div>

      <div className={styles.hamburger} onClick={toggleMenu}>
        <span className={styles.bar} />
        <span className={styles.bar} />
        <span className={styles.bar} />
      </div>

      <ul className={`${styles.navLinks} ${isOpen ? styles.active : ""}`}>
        {/* Existing Links */}
        <li>
          <NavLink to="/" onClick={() => setIsOpen(false)}>
            Home
          </NavLink>
        </li>
        <li>
          <NavLink to="/food" onClick={() => setIsOpen(false)}>
            Food
          </NavLink>
        </li>
        <li>
          <NavLink to="/recipes" onClick={() => setIsOpen(false)}>
            Recipes
          </NavLink>
        </li>
        <li>
          <NavLink to="/features" onClick={() => setIsOpen(false)}>
            Features
          </NavLink>
        </li>
        <li>
          <NavLink to="/contact" onClick={() => setIsOpen(false)}>
            Contact
          </NavLink>
        </li>

        {/* Add recipe pages */}
        <li>
          <NavLink to="/create" onClick={() => setIsOpen(false)}>
            Create Recipe
          </NavLink>
        </li>
        <li>
          <NavLink to="/all/recipes" onClick={() => setIsOpen(false)}>
            All Recipes
          </NavLink>
        </li>


      

        {/* Auth Links (Login / Register vs Logout) */}
        {!isLogged ? (
          <>
            <li>
              <NavLink to="/login" onClick={() => setIsOpen(false)}>
                Login
              </NavLink>
            </li>
            <li>
              <NavLink to="/register" onClick={() => setIsOpen(false)}>
                Register
              </NavLink>
            </li>
          </>
        ) : (
          <li>
            <button className={styles.logoutButton} onClick={handleLogout}>
              Logout
            </button>
          </li>
        )}

        <li className={styles.divider}>|</li>
        <li>
          <NavLink to="/dashboard" onClick={() => setIsOpen(false)}>
            Dashboard
          </NavLink>
        </li>
      </ul>
    </nav>
  );
};

export default Navbar;
