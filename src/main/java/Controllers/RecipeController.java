package Controllers;

import DAO.RecipeDAO;
import Models.Recipe;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

@WebServlet(name = "RecipeController", urlPatterns = {
    "/manage-recipes",
    "/add-recipe",
    "/edit-recipe",
    "/delete-recipe",
    "/filter-recipes"
})
public class RecipeController extends HttpServlet {

    private RecipeDAO recipeDAO;

    @Override
    public void init() {
        recipeDAO = new RecipeDAO();
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet RecipeController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet RecipeController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getServletPath();

        switch (action) {
            case "/manage-recipes":
                viewRecipes(request, response);
                break;
            case "/delete-recipe":
                deleteRecipe(request, response);
                break;
            case "/filter-recipes":
                filterRecipes(request, response);
                break;
            default:
                viewRecipes(request, response);
                break;
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getServletPath();

        switch (action) {
            case "/add-recipe":
                addRecipe(request, response);
                break;
            case "/edit-recipe":
                editRecipe(request, response);
                break;
            default:
                response.sendRedirect("manage-recipes");
                break;
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private void viewRecipes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Object[]> recipes = recipeDAO.getRecipeDetailsForView();
        List<Object[]> foods = recipeDAO.getAllFoods();
        List<Object[]> ingredients = recipeDAO.getAllIngredients();

        request.setAttribute("recipes", recipes);
        request.setAttribute("foods", foods);
        request.setAttribute("ingredients", ingredients);

        request.getRequestDispatcher("ManageRecipeView.jsp").forward(request, response);
    }

    private void addRecipe(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int foodId = Integer.parseInt(request.getParameter("foodId"));
            int ingredientId = Integer.parseInt(request.getParameter("ingredientId"));
            int requiredQuantity = Integer.parseInt(request.getParameter("requiredQuantity"));

            Recipe recipe = new Recipe(foodId, ingredientId, requiredQuantity);
            recipeDAO.addRecipe(recipe);

            response.sendRedirect("manage-recipes");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("manage-recipes?error=true");
        }
    }

    private void editRecipe(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int foodId = Integer.parseInt(request.getParameter("foodId"));
            int ingredientId = Integer.parseInt(request.getParameter("ingredientId"));
            int requiredQuantity = Integer.parseInt(request.getParameter("requiredQuantity"));

            Recipe recipe = new Recipe(foodId, ingredientId, requiredQuantity);
            recipeDAO.updateRecipe(recipe);

            response.sendRedirect("manage-recipes");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("manage-recipes?error=true");
        }
    }

    private void deleteRecipe(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String foodIdStr = request.getParameter("foodId");
            String ingredientIdStr = request.getParameter("ingredientId");

            if (foodIdStr != null && !foodIdStr.isEmpty() && ingredientIdStr != null && !ingredientIdStr.isEmpty()) {
                int foodId = Integer.parseInt(foodIdStr);
                int ingredientId = Integer.parseInt(ingredientIdStr);

                System.out.println("Deleting recipe - FoodID: " + foodId + ", IngredientID: " + ingredientId);
                boolean deleted = recipeDAO.deleteRecipe(foodId, ingredientId);
                System.out.println("Delete result: " + deleted);
            } else {
                System.out.println("Missing parameters: foodId=" + foodIdStr + ", ingredientId=" + ingredientIdStr);
            }

            response.sendRedirect("manage-recipes");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("manage-recipes?error=true");
        }
    }

    private void filterRecipes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String foodName = request.getParameter("foodName");

        List<Object[]> recipes;
        if (foodName != null && !foodName.isEmpty()) {
            recipes = recipeDAO.getRecipeDetailsByFoodName(foodName);
        } else {
            recipes = recipeDAO.getRecipeDetailsForView();
        }

        List<Object[]> foods = recipeDAO.getAllFoods();
        List<Object[]> ingredients = recipeDAO.getAllIngredients();

        request.setAttribute("recipes", recipes);
        request.setAttribute("foods", foods);
        request.setAttribute("ingredients", ingredients);
        request.setAttribute("foodName", foodName);

        request.getRequestDispatcher("ManageRecipeView.jsp").forward(request, response);
    }
}
