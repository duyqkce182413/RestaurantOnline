<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Admin Dashboard - Recipe Management</title>
        <!-- Font Awesome -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css"
              integrity="sha512-Kc323vGBEqzTmouAECnVceyQqyqdsSiqLQISBL29aUW4U/M7pSPA/gEUZQqv1cwx4OnYxTxve5UMg5GT6L4JJg=="
              crossorigin="anonymous" referrerpolicy="no-referrer" />
        <!-- Bootstrap -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css"
              rel="stylesheet">
        <!-- Custom CSS -->
        <link rel="stylesheet" href="./CSS/HeaderAndFooter_CSS.css" />
        <style>
            .sidebar {
                width: 250px;
                background-color: #343a40;
                padding: 20px;
                height: 100%;
                position: fixed;
                top: 0;
                left: 0;
                color: white;
                display: flex;
                flex-direction: column;
                align-items: flex-start;
            }
            .sidebar h3 {
                margin-bottom: 30px;
                text-transform: uppercase;
                font-size: 18px;
            }

            .sidebar a {
                color: white;
                text-decoration: none;
                display: block;
                padding: 10px 15px;
                margin: 5px 0;
                width: 100%;
                text-align: left;
                border-radius: 5px;
            }

            .sidebar a:hover {
                background-color: #495057;
                transition: background-color 0.3s;
            }

            .content {
                margin-left: 250px;
                padding: 20px;
                background-color: #f8f9fa;
                width: calc(100% - 250px);
                float: left;
            }

            .clearfix::after {
                content: "";
                display: table;
                clear: both;
            }

            .is-invalid {
                border-color: red;
            }
            .error-messages .alert-danger {
                padding: 10px;
                margin-bottom: 10px;
                color: #721c24;
                background-color: #f8d7da;
                border: 1px solid #f5c6cb;
                border-radius: 4px;
            }
        </style>
    </head>
    <body>
        <jsp:include page="Dashboard_Header.jsp"></jsp:include>

            <div class="wrapper clearfix row">
                <!-- Sidebar -->
            <jsp:include page="Dashboard_Sidebar.jsp"></jsp:include>

                <!-- Main content -->
                <div class="content col-6">
                    <h1>Recipe Management</h1>
                    <!-- Filter and Add Form -->
                    <div class="row mb-3">
                        <div class="col-md-6">
                            <form action="filter-recipes" method="GET" class="d-flex">
                                <select name="foodName" class="form-control me-2">
                                    <option value="">All Foods</option>
                                <c:forEach var="food" items="${foods}">
                                    <option value="${food[1]}" ${foodName eq food[1] ? 'selected' : ''}>
                                        ${food[1]}
                                    </option>
                                </c:forEach>
                            </select>
                            <button type="submit" class="btn btn-primary">Filter</button>
                        </form>
                    </div>
                    <div class="col-md-2 ms-auto">
                        <button type="button" class="btn btn-success" data-bs-toggle="modal" 
                                data-bs-target="#addRecipeModal">
                            <i class="fas fa-plus"></i> Add New Recipe
                        </button>
                    </div>
                </div>

                <!-- Recipe Table -->
                <!-- Recipe Table -->
                <table class="table table-bordered">
                    <thead>
                        <tr>
                            <th>Food Image</th>
                            <th>Food Name</th>
                            <th>Ingredient Name</th>
                            <th>Required Quantity</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="recipe" items="${recipes}">
                            <tr>
                                <td>
                                    <img src="${pageContext.request.contextPath}/image/${recipe[6] != null ? recipe[6] : 'default-food.jpg'}" 
                                         alt="${recipe[3]}" style="width: 60px; height: 60px; object-fit: cover;">
                                </td>
                                <td>${recipe[3]}</td>
                                <td>${recipe[4]} (${recipe[5]})</td>
                                <td>${recipe[2]}</td>
                                <td>
                                    <button class="btn btn-warning btn-sm" 
                                            onclick="editRecipe(${recipe[0]}, ${recipe[1]}, ${recipe[2]}, '${recipe[3]}', '${recipe[4]}', '${recipe[5]}')">
                                        <i class="fas fa-edit"></i> Edit
                                    </button>
                                    <button class="btn btn-danger btn-sm" 
                                            onclick="confirmDelete(${recipe[0]}, ${recipe[1]}, '${recipe[3]}', '${recipe[4]}')">
                                        <i class="fas fa-trash"></i> Delete
                                    </button>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>

        <!-- Add Recipe Modal -->
        <div class="modal fade" id="addRecipeModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Add New Recipe</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <form id="addRecipeForm" action="add-recipe" method="POST">
                            <div class="mb-3">
                                <label class="form-label">Food</label>
                                <select class="form-control" name="foodId" required>
                                    <c:forEach var="food" items="${foods}">
                                        <option value="${food[0]}">${food[1]}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Ingredient</label>
                                <select class="form-control" name="ingredientId" required>
                                    <c:forEach var="ingredient" items="${ingredients}">
                                        <option value="${ingredient[0]}">${ingredient[1]} (${ingredient[2]})</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Required Quantity</label>
                                <input type="number" class="form-control" name="requiredQuantity" min="1" required>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                        <button type="submit" form="addRecipeForm" class="btn btn-primary">Save</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Edit Recipe Modal -->
        <div class="modal fade" id="editRecipeModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Edit Recipe</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <form id="editRecipeForm" action="edit-recipe" method="POST">
                            <input type="hidden" id="editFoodId" name="foodId">
                            <input type="hidden" id="editIngredientId" name="ingredientId">
                            <div class="mb-3">
                                <label class="form-label">Food</label>
                                <input type="text" class="form-control" id="editFoodName" readonly>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Ingredient</label>
                                <input type="text" class="form-control" id="editIngredientName" readonly>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Required Quantity</label>
                                <input type="number" class="form-control" id="editRequiredQuantity" name="requiredQuantity" min="1" required>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                        <button type="submit" form="editRecipeForm" class="btn btn-primary">Save Changes</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Delete Confirmation Modal -->
        <div class="modal fade" id="deleteConfirmModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Confirm Delete</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <p>Are you sure you want to delete this recipe ingredient?</p>
                        <p id="deleteRecipeInfo"></p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="button" class="btn btn-danger" id="deleteRecipeBtn">Delete</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>

        <!-- JavaScript for handling modal operations -->
        <script>
                                                function editRecipe(foodId, ingredientId, requiredQuantity, foodName, ingredientName, unit) {
                                                    document.getElementById('editFoodId').value = foodId;
                                                    document.getElementById('editIngredientId').value = ingredientId;
                                                    document.getElementById('editFoodName').value = foodName;
                                                    document.getElementById('editIngredientName').value = ingredientName + " (" + unit + ")";
                                                    document.getElementById('editRequiredQuantity').value = requiredQuantity;

                                                    new bootstrap.Modal(document.getElementById('editRecipeModal')).show();
                                                }

                                                function confirmDelete(foodId, ingredientId, foodName, ingredientName) {
                                                    console.log("Confirm delete:", foodId, ingredientId, foodName, ingredientName);
                                                    const deleteBtn = document.getElementById('deleteRecipeBtn');
                                                    deleteBtn.onclick = function () {
                                                        // Add debug logging
                                                        console.log("Redirecting to: delete-recipe?foodId=" + foodId + "&ingredientId=" + ingredientId);
                                                        window.location.href = "delete-recipe?foodId=" + foodId + "&ingredientId=" + ingredientId;
                                                    };

                                                    new bootstrap.Modal(document.getElementById('deleteConfirmModal')).show();
                                                }
        </script>
    </body>
</html>