<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <title>Admin Dashboard</title>

        <!-- Font Awesome -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css">

        <!-- Bootstrap -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">

        <!-- Custom CSS -->
        <style>
            /* Sidebar Styles */
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

            /* Main content */
            .content {
                margin-left: 250px;
                padding: 20px;
                background-color: #f8f9fa;
                width: calc(100% - 250px);
                float: left;
            }
        </style>
    </head>
    <body>
        <jsp:include page="Dashboard_Header.jsp"/>
        <div class="wrapper clearfix row">
            <jsp:include page="Dashboard_Sidebar.jsp"/>
            <div class="content col-9">
                <h1>Manage Ingredients</h1>
                <!-- Search Form -->
                <form action="view-ingredients" method="GET" class="d-flex mb-3">
                    <input type="text" name="search" class="form-control me-2" placeholder="Search by Ingredient Name" value="${param.search}">
                    <button type="submit" class="btn btn-primary">Search</button>
                </form>
                <!-- Add Ingredient Button -->
                <button type="button" class="btn btn-success mb-3" data-bs-toggle="modal" data-bs-target="#addIngredientModal">
                    <i class="fas fa-plus"></i> Add New Ingredient
                </button>
                <!-- Ingredient Table -->
                <table class="table table-bordered">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Ingredient Name</th>
                            <th>Stock Quantity</th>
                            <th>Unit</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="ingredient" items="${ingredientList}">
                            <tr>
                                <td>${ingredient.ingredientID}</td>
                                <td>${ingredient.ingredientName}</td>
                                <td>${ingredient.stockQuantity}</td>
                                <td>${ingredient.unit}</td>
                                <td>
                                    <button class="btn btn-warning btn-sm" onclick="editIngredient('${ingredient.ingredientID}', '${ingredient.ingredientName}', '${ingredient.stockQuantity}', '${ingredient.unit}')">
                                        <i class="fas fa-edit"></i> Edit
                                    </button>


                                    <button class="btn btn-danger btn-sm" onclick="confirmDelete(${ingredient.ingredientID})">
                                        <i class="fas fa-trash"></i> Delete
                                    </button>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>

        <div class="modal fade" id="editIngredientModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Edit Ingredient</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <form id="editIngredientForm" action="edit-ingredient" method="POST">
                            <input type="hidden" name="ingredientId" id="editIngredientId">
                            <div class="mb-3">
                                <label class="form-label">Ingredient Name</label>
                                <input type="text" class="form-control" name="ingredientName" id="editIngredientName" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Stock Quantity</label>
                                <input type="number" class="form-control" name="stockQuantity" id="editStockQuantity" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Unit</label>
                                <input type="text" class="form-control" name="unit" id="editUnit" required>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                        <button type="submit" form="editIngredientForm" class="btn btn-primary">Save Changes</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Add Ingredient Modal -->
        <div class="modal fade" id="addIngredientModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Add New Ingredient</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <form id="addIngredientForm" action="add-ingredient" method="POST">
                            <div class="mb-3">
                                <label class="form-label">Ingredient Name</label>
                                <input type="text" class="form-control" name="ingredientName" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Stock Quantity</label>
                                <input type="number" class="form-control" name="stockQuantity" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Unit</label>
                                <input type="text" class="form-control" name="unit" required>
                            </div>
                            <button type="submit" class="btn btn-primary">Save</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <script>
            function editIngredient(ingredientID, ingredientName, stockQuantity, unit) {
                let modal = document.getElementById("editIngredientModal");
                if (!modal) {
                    console.error("Error: Modal element not found!");
                    return;
                }

                document.getElementById("editIngredientId").value = ingredientID;
                document.getElementById("editIngredientName").value = ingredientName;
                document.getElementById("editStockQuantity").value = stockQuantity;
                document.getElementById("editUnit").value = unit;

                let bootstrapModal = new bootstrap.Modal(modal);
                bootstrapModal.show();
            }



            function confirmDelete(ingredientId) {
                if (confirm("Are you sure you want to delete this ingredient?")) {
                    window.location.href = 'delete-ingredient?id=' + ingredientId;
                }
            }
        </script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
