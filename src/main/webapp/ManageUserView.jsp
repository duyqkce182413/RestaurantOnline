<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Admin Dashboard - Account Management</title>
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
                    <h1>Account Management</h1>
                    <!-- Search and Filter Form -->
                    <div class="row mb-3">
                        <div class="col-md-6">
                            <form action="view-users" method="GET" class="d-flex">
                                <input type="text" name="search" class="form-control me-2" 
                                       placeholder="Search by username" value="${param.search}">
                            <button type="submit" class="btn btn-primary">Search</button>
                        </form>
                    </div>
                    <div class="col-md-4">
                        <form action="view-users" method="GET" class="d-flex">
                            <select name="role" class="form-control me-2">
                                <option value="">All Roles</option>
                                <option value="Admin">Admin</option>
                                <option value="Staff">Staff</option>
                                <option value="Customer">Customer</option>
                            </select>
                            <button type="submit" class="btn btn-secondary">Filter</button>
                        </form>
                    </div>
                    <div class="col-md-2">
                        <button type="button" class="btn btn-success" data-bs-toggle="modal" 
                                data-bs-target="#addUserModal">
                            <i class="fas fa-plus"></i> Add New Account
                        </button>
                    </div>
                </div>

                <!-- Account Table -->
                <table class="table table-bordered">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Username</th>
                            <th>Password</th>
                            <th>Email</th>
                            <th>Phone Number</th>
                            <th>Date created</th>
                            <th>Role</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="a" items="${users}">
                            <tr>
                                <td>${a.userID}</td>
                                <td>${a.username}</td>
                                <td>${a.passwordHash}</td>
                                <td>${a.email}</td>
                                <td>${a.phoneNumber}</td>
                                <td>${a.createdAt}</td>
                                <td>${a.role}</td>
                                <td>
                                    <button class="btn btn-info btn-sm" onclick="viewUserDetail(${a.userID})">
                                        <i class="fas fa-eye"></i> View Detail
                                    </button>
                                    <button class="btn btn-warning btn-sm" onclick="editUser(${a.userID})">
                                        <i class="fas fa-edit"></i> Edit
                                    </button>
                                    <c:if test="${a.role != 'Admin'}">

                                        <button class="btn btn-danger btn-sm" onclick="confirmDelete(${a.userID})">
                                            <i class="fas fa-trash"></i> Delete
                                        </button>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
        <!-- Add User Modal -->
        <div class="modal fade" id="addUserModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Add New User</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <form id="addUserForm" action="add-user" method="POST">
                            <div class="mb-3">
                                <label class="form-label">Username</label>
                                <input type="text" class="form-control" name="username" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Full Name</label>
                                <input type="text" class="form-control" name="fullName" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Email</label>
                                <input type="email" class="form-control" name="email" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Password</label>
                                <input type="password" class="form-control" name="password" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Phone Number</label>
                                <input type="tel" class="form-control" name="phoneNumber">
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Date of Birth (yyyy-mm-dd)</label>
                                <input type="" class ="form-control" name ="dateOfBirth">
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Gender</label>
                                <select class="form-control" name="gender">
                                    <option value="Male">Male</option>
                                    <option value="Female">Female</option>
                                    <option value="Other">Other</option>
                                </select>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Role</label>
                                <select class="form-control" name="role">
                                    <option value="Customer">Customer</option>
                                    <option value="Staff">Staff</option>
                                    <option value="Admin">Admin</option>
                                </select>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                        <button type="submit" form="addUserForm" class="btn btn-primary">Save</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- View Detail Modal -->
        <div class="modal fade" id="viewDetailModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">User Details</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <!-- Content will be dynamically populated -->
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Edit User Modal -->
        <div class="modal fade" id="editUserModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Edit User</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <form id="editUserForm" action="${pageContext.request.contextPath}/edit-user" method="POST">
                            <input type="hidden" name="userId" id="editUserId">
                            <div class="mb-3">
                                <label class="form-label">Username</label>
                                <input type="text" class="form-control" name="username" id="editUsername" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Full Name</label>
                                <input type="text" class="form-control" name="fullName" id="editFullName" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Email</label>
                                <input type="email" class="form-control" name="email" id="editEmail" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Phone Number</label>
                                <input type="tel" class="form-control" name="phoneNumber" id="editPhoneNumber">
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Date of Birth (yyyy-mm-dd)</label>
                                <input type="" class="form-control" name="dateOfBirth" id="editDateOfBirth">
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Gender</label>
                                <select class="form-control" name="gender" id="editGender">
                                    <option value="Male">Male</option>
                                    <option value="Female">Female</option>
                                    <option value="Other">Other</option>
                                </select>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Role</label>
                                <select class="form-control" name="role" id="editRole">
                                    <option value="Customer">Customer</option>
                                    <option value="Staff">Staff</option>
                                    <option value="Admin">Admin</option>
                                </select>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Status</label>
                                <select class="form-control" name="status" id="editStatus">
                                    <option value="Active">Active</option>
                                    <option value="Inactive">Inactive</option>
                                    <option value="Banned">Banned</option>
                                </select>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                        <button type="submit" form="editUserForm" class="btn btn-primary">Save Changes</button>
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
                        Are you sure you want to delete this user?
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="button" class="btn btn-danger" onclick="deleteUser()">Delete</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>

        <!-- JavaScript for handling modal operations -->
        <script>
                            let userToDelete = null;

                            function viewUserDetail(userId) {
                                fetch('view-user-detail?id=' + userId)
                                        .then(response => response.json())
                                        .then(user => {
                                            const modalBody = document.getElementById('viewDetailModal').querySelector('.modal-body');
                                            modalBody.innerHTML = `
                            <p><strong>Avatar:</strong> \${user.avatar || 'No Avatar'}</p>                
                            <p><strong>ID:</strong> \${user.userID}</p>                
                            <p><strong>Username:</strong> \${user.username}</p>
                            <p><strong>Password:</strong> \${user.passwordHash}</p>
                            <p><strong>Full Name:</strong> \${user.fullName}</p>
                            <p><strong>Email:</strong> \${user.email}</p>
                            <p><strong>Phone Number:</strong> \${user.phoneNumber}</p>
                            <p><strong>Date of Birth:</strong> \${user.dateOfBirth}</p>
                            <p><strong>Gender:</strong> \${user.gender}</p>
                            <p><strong>Role:</strong> \${user.role}</p>
                            <p><strong>Status:</strong> \${user.status}</p>
                            <p><strong>Created At:</strong> \${user.createdAt}</p>
                        `;
                                            new bootstrap.Modal(document.getElementById('viewDetailModal')).show();
                                        })
                                        .catch(error => console.error('Error:', error));
                            }

                            function editUser(userId) {
                                fetch('view-user-detail?id=' + userId)
                                        .then(response => response.json())
                                        .then(user => {
                                            if (user) {
                                                // Điền dữ liệu vào form
                                                document.getElementById('editUserId').value = userId;
                                                document.getElementById('editUsername').value = user.username || '';
                                                document.getElementById('editFullName').value = user.fullName || '';
                                                document.getElementById('editEmail').value = user.email || '';
                                                document.getElementById('editPhoneNumber').value = user.phoneNumber || '';
                                                // Xử lý ngày tháng
                                                if (user.dateOfBirth) {
                                                    const date = new Date(user.dateOfBirth);
                                                    const formattedDate = date.toISOString().split('T')[0];
                                                    document.getElementById('editDateOfBirth').value = formattedDate;

                                                    // Loại bỏ bất kỳ placeholder nào
                                                    document.getElementById('editDateOfBirth').placeholder = '';
                                                }
                                                document.getElementById('editGender').value = user.gender || 'Male';
                                                document.getElementById('editRole').value = user.role || 'Customer';
                                                document.getElementById('editStatus').value = user.status || 'Active';

                                                // Show modal
                                                new bootstrap.Modal(document.getElementById('editUserModal')).show();
                                            }
                                        })
                                        .catch(error => {
                                            console.error('Error:', error);
                                            alert('Error loading user data');
                                        });
                            }

                            function confirmDelete(userId) {
                                userToDelete = userId;
                                new bootstrap.Modal(document.getElementById('deleteConfirmModal')).show();
                            }

                            function deleteUser() {
                                if (userToDelete) {
                                    window.location.href = 'delete-user?id=' + userToDelete;  // Sử dụng cộng chuỗi
                                }
                            }
        </script>
    </body>
</html>
