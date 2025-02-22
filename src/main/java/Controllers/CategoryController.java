package Controllers;

import DAO.CategoryDAO;
import Models.Category;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "CategoryController", urlPatterns = {
    "/view-categories",
    "/add-category",
    "/edit-category",
    "/delete-category"
})
public class CategoryController extends HttpServlet {

    private final CategoryDAO categoryDAO = new CategoryDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getServletPath();

        switch (action) {
            case "/view-categories":
                request.setAttribute("categoryList", categoryDAO.getAllCategories());
                request.getRequestDispatcher("/ManageCategoryView.jsp").forward(request, response);
                break;

            case "/delete-category":
                int categoryId = Integer.parseInt(request.getParameter("id"));
                categoryDAO.deleteCategory(categoryId);
                response.sendRedirect("view-categories");
                break;

            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String action = request.getServletPath();

        switch (action) {
            case "/add-category":
                addCategory(request, response);
                break;

            case "/edit-category":
                editCategory(request, response);
                break;

            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }

    private void addCategory(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String categoryName = request.getParameter("categoryName");
        String description = request.getParameter("description");

        if (categoryName == null || categoryName.trim().isEmpty()) {
            response.sendRedirect("view-categories?error=Category name is required");
            return;
        }

        categoryDAO.addCategory(categoryName, description);
        response.sendRedirect("view-categories?success=Category added successfully");
    }

    private void editCategory(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int categoryId = Integer.parseInt(request.getParameter("categoryId"));
            String categoryName = request.getParameter("categoryName");
            String description = request.getParameter("description");

            if (categoryName == null || categoryName.trim().isEmpty()) {
                response.sendRedirect("view-categories?error=Category name is required");
                return;
            }

            categoryDAO.updateCategory(categoryId, categoryName, description);
            response.sendRedirect("view-categories?success=Category updated successfully");
        } catch (NumberFormatException e) {
            response.sendRedirect("view-categories?error=Invalid category ID");
        }
    }
}
