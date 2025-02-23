package Controllers;

import DAO.IngredientDAO;
import Models.Ingredient;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "IngredientController", urlPatterns = {
    "/view-ingredients",
    "/add-ingredient",
    "/edit-ingredient",
    "/delete-ingredient"
})
public class IngredientController extends HttpServlet {

    private final IngredientDAO ingredientDAO = new IngredientDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getServletPath();
        
        switch (action) {
            case "/view-ingredients":
                String searchQuery = request.getParameter("search");
                List<Ingredient> ingredientList;
                if (searchQuery != null && !searchQuery.trim().isEmpty()) {
                    ingredientList = ingredientDAO.searchIngredients(searchQuery);
                } else {
                    ingredientList = ingredientDAO.getAllIngredients();
                }
                request.setAttribute("ingredientList", ingredientList);
                request.getRequestDispatcher("ManageIngredientView.jsp").forward(request, response);
                break;
            
            case "/delete-ingredient":
                int ingredientId = Integer.parseInt(request.getParameter("id"));
                ingredientDAO.deleteIngredient(ingredientId);
                response.sendRedirect("view-ingredients?success=Ingredient deleted successfully");
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
            case "/add-ingredient":
                addIngredient(request, response);
                break;
            
            case "/edit-ingredient":
                editIngredient(request, response);
                break;
            
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }

    private void addIngredient(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String ingredientName = request.getParameter("ingredientName");
        int stockQuantity = Integer.parseInt(request.getParameter("stockQuantity"));
        String unit = request.getParameter("unit");
        
        if (ingredientName == null || ingredientName.trim().isEmpty()) {
            response.sendRedirect("view-ingredients?error=Ingredient name is required");
            return;
        }
        
        ingredientDAO.addIngredient(new Ingredient(0, ingredientName, stockQuantity, unit));
        response.sendRedirect("view-ingredients?success=Ingredient added successfully");
    }

    private void editIngredient(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int ingredientId = Integer.parseInt(request.getParameter("ingredientId"));
            String ingredientName = request.getParameter("ingredientName");
            int stockQuantity = Integer.parseInt(request.getParameter("stockQuantity"));
            String unit = request.getParameter("unit");
            
            if (ingredientName == null || ingredientName.trim().isEmpty()) {
                response.sendRedirect("view-ingredients?error=Ingredient name is required");
                return;
            }
            
            ingredientDAO.updateIngredient(new Ingredient(ingredientId, ingredientName, stockQuantity, unit));
            response.sendRedirect("view-ingredients?success=Ingredient updated successfully");
        } catch (NumberFormatException e) {
            response.sendRedirect("view-ingredients?error=Invalid ingredient ID");
        }
    }
}
