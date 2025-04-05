<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Admin Dashboard - Food Management</title>
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
                    <h1>Food Management</h1>
                <c:if test="${not empty error}">
                    <div class="alert alert-danger" role="alert">
                        ${error}
                    </div>
                </c:if>
                <!-- Search and Filter Form -->
                <div class="row mb-3">
                    <div class="col-md-6">
                        <form action="manage-foods" method="GET" class="d-flex">
                            <input type="text" name="search" class="form-control me-2" 
                                   placeholder="Search by Food Name" value="${param.search}">
                            <button type="submit" class="btn btn-primary">Search</button>
                        </form>
                    </div>
                    <div class="col-md-4">
                        <form action="manage-foods" method="GET" class="d-flex">
                            <select name="categoryid" class="form-control me-2">
                                <option value="">All Categories</option>
                                <c:forEach items="${categories}" var="c">
                                    <option value="${c.categoryID}" ${param.categoryid == c.categoryID ? 'selected' : ''}>
                                        ${c.categoryName}
                                    </option>
                                </c:forEach>
                            </select>
                            <button type="submit" class="btn btn-secondary">Filter</button>
                        </form>
                    </div>
                    <div class="col-md-2">
                        <button type="button" class="btn btn-success" data-bs-toggle="modal" 
                                data-bs-target="#addFoodModal">
                            <i class="fas fa-plus"></i> Add New Food
                        </button>
                    </div>
                </div>

                <!-- Food Table -->
                <table class="table table-bordered">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Image</th>
                            <th>Food Name</th>
                            <th>Description</th>                            
                            <th>Price</th>
                            <th>Category</th>
                            <th>Available</th>
                            <th>Quantity</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${foods}" var="f">
                            <tr>
                                <td>${f.foodID}</td>
                                <td>
                                    <img src="${pageContext.request.contextPath}/image/${f.image}" 
                                         alt="${f.foodName}" 
                                         style="width: 50px; height: 50px; object-fit: cover; border-radius: 5px;" 
                                         onerror="this.src='${pageContext.request.contextPath}/image/default-food.jpg'">
                                </td>
                                <td>${f.foodName}</td>
                                <td>${f.description}</td>
                                <td>${f.price}</td>
                                <td>
                                    <c:forEach items="${categories}" var="c">
                                        ${f.categoryID == c.categoryID ? c.categoryName : ''}
                                    </c:forEach>
                                </td>
                                <td>${f.available ? 'Yes' : 'No'}</td>
                                <td>${f.quantity}</td>
                                <td>
                                    <button class="btn btn-warning btn-sm" onclick="editFood(${f.foodID})">
                                        <i class="fas fa-edit"></i> Edit
                                    </button>
                                    <button class="btn btn-danger btn-sm" onclick="confirmDelete(${f.foodID})">
                                        <i class="fas fa-trash"></i> Delete
                                    </button>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>

        <!-- Add Food Modal -->
        <div class="modal fade" id="addFoodModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Add New Food</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <form id="addFoodForm" action="add-food" method="POST">
                            <div class="mb-3">
                                <label class="form-label">Food Name</label>
                                <input type="text" class="form-control" name="foodName" pattern="[A-Za-zÀ-ỹ\s]+" title="Vui lòng chỉ nhập chữ cái" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Price</label>
                                <input type="number" step="0.01" class="form-control" name="price" min="1" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Category</label>
                                <select class="form-control" name="categoryId" required>
                                    <c:forEach items="${categories}" var="c">
                                        <option value="${c.categoryID}">${c.categoryName}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Description</label>
                                <textarea class="form-control" name="description" rows="3"></textarea>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Image URL</label>
                                <input type="text" class="form-control" name="image">
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Quantity</label>
                                <input type="number" class="form-control" name="quantity" min="1" required>
                            </div>
                            <div class="mb-3 form-check">
                                <input type="checkbox" class="form-check-input" name="available" value="true" checked>
                                <label class="form-check-label">Available</label>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                        <button type="submit" form="addFoodForm" class="btn btn-primary">Save</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Edit Food Modal -->
        <div class="modal fade" id="editFoodModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Edit Food</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <form id="editFoodForm" action="edit-food" method="POST">
                            <input type="hidden" name="foodId" id="editFoodId">
                            <div class="mb-3">
                                <label class="form-label">Food Name</label>
                                <input type="text" class="form-control" name="foodName" id="editFoodName" pattern="[A-Za-zÀ-ỹ\s]+" title="Vui lòng chỉ nhập chữ cái" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Price</label>
                                <input type="number" step="0.01" class="form-control" name="price" id="editPrice" min="1" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Category</label>
                                <select class="form-control" name="categoryId" id="editCategoryId" required>
                                    <c:forEach items="${categories}" var="c">
                                        <option value="${c.categoryID}">${c.categoryName}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Description</label>
                                <textarea class="form-control" name="description" id="editDescription" rows="3"></textarea>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Image URL</label>
                                <input type="text" class="form-control" name="image" id="editImage">
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Quantity</label>
                                <input type="number" class="form-control" name="quantity" id="editQuantity" min="0" required>
                            </div>
                            <div class="mb-3 form-check">
                                <input type="checkbox" class="form-check-input" name="available" id="editAvailable" value="true">
                                <label class="form-check-label">Available</label>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                        <button type="submit" form="editFoodForm" class="btn btn-primary">Save Changes</button>
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
                        Are you sure you want to delete this food?
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="button" class="btn btn-danger" onclick="deleteFood()">Delete</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>

        <!-- JavaScript for handling modal operations -->
        <script>
                            let foodToDelete = null;
                            function editFood(foodId) {
                                fetch('view-food-detail?foodID=' + foodId + '&format=json')
                                        .then(response => response.json())
                                        .then(data => {
                                            const food = data.food; // Lấy đối tượng food từ dữ liệu trả về
                                            document.getElementById('editFoodId').value = food.foodID;
                                            document.getElementById('editFoodName').value = food.foodName;
                                            document.getElementById('editPrice').value = food.price;
                                            document.getElementById('editCategoryId').value = food.categoryID;
                                            document.getElementById('editDescription').value = food.description;
                                            document.getElementById('editImage').value = food.image;
                                            document.getElementById('editQuantity').value = food.quantity;
                                            document.getElementById('editAvailable').checked = food.available;

                                            new bootstrap.Modal(document.getElementById('editFoodModal')).show();
                                        })
                                        .catch(error => {
                                            console.error('Error:', error);
                                            alert('Error loading food data');
                                        });
                            }

                            function confirmDelete(foodId) {
                                foodToDelete = foodId;
                                new bootstrap.Modal(document.getElementById('deleteConfirmModal')).show();
                            }

                            function deleteFood() {
                                if (foodToDelete) {
                                    window.location.href = 'delete-food?id=' + foodToDelete;
                                }
                            }
        </script>
    </body>
</html>