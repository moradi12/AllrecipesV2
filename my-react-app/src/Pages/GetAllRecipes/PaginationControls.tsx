// src/Pages/GetAllRecipes/PaginationControls.tsx

import React from "react";

interface Pagination {
  page: number;
  size: number;
  totalPages: number;
}

interface PaginationControlsProps {
  pagination: Pagination;
  setPagination: React.Dispatch<React.SetStateAction<Pagination>>;
  loading: boolean;
}

const PaginationControls: React.FC<PaginationControlsProps> = ({
  pagination,
  setPagination,
  loading,
}) => {
  const { page, totalPages } = pagination;

  return (
    <div className="pagination">
      <button
        onClick={() =>
          setPagination((prev) => ({ ...prev, page: prev.page - 1 }))
        }
        disabled={page === 0 || loading}
      >
        Previous
      </button>
      <span>
        Page {page + 1} of {totalPages}
      </span>
      <button
        onClick={() =>
          setPagination((prev) => ({ ...prev, page: prev.page + 1 }))
        }
        disabled={page >= totalPages - 1 || loading}
      >
        Next
      </button>
    </div>
  );
};

export default PaginationControls;
