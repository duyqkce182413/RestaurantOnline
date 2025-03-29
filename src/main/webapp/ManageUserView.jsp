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
                        <c:if test="${not empty sessionScope.addUserErrors}">
                            <div class="error-messages">
                                <c:forEach var="error" items="${sessionScope.addUserErrors}">
                                    <div class="alert alert-danger">${error}</div>
                                </c:forEach>
                            </div>
                        </c:if>
                        <form id="addUserForm" action="add-user" method="POST">
                            <div class="mb-3">
                                <label class="form-label">Username</label>
                                <input type="text" class="form-control" name="username" required
                                       value="${sessionScope.addUserUsername}">
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Full Name</label>
                                <input type="text" class="form-control" name="fullName" required
                                       value="${sessionScope.addUserFullName}">
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Email</label>
                                <input type="email" class="form-control" name="email" required
                                       value="${sessionScope.addUserEmail}">
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Password</label>
                                <input type="password" class="form-control" name="password" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Phone Number</label>
                                <input type="tel" class="form-control" name="phoneNumber"
                                       value="${sessionScope.addUserPhoneNumber}">
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Date of Birth (dd-MM-yyyy)</label>
                                <input type="date" class="form-control" name="dateOfBirth"
                                       value="${sessionScope.addUserDateOfBirth}" max="2025-03-12" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Gender</label>
                                <select class="form-control" name="gender">
                                    <option value="Male" ${sessionScope.addUserGender == 'Male' ? 'selected' : ''}>Male</option>
                                    <option value="Female" ${sessionScope.addUserGender == 'Female' ? 'selected' : ''}>Female</option>
                                    <option value="Other" ${sessionScope.addUserGender == 'Other' ? 'selected' : ''}>Other</option>
                                </select>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Role</label>
                                <select class="form-control" name="role">                     
                                    <option value="Staff" ${sessionScope.addUserRole == 'Staff' ? 'selected' : ''}>Staff</option>
                                    <option value="Admin" ${sessionScope.addUserRole == 'Admin' ? 'selected' : ''}>Admin</option>
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
                        <c:if test="${not empty sessionScope.editUserErrors}">
                            <div class="error-messages">
                                <c:forEach var="error" items="${sessionScope.editUserErrors}">
                                    <div class="alert alert-danger">${error}</div>
                                </c:forEach>
                            </div>
                        </c:if>
                        <form id="editUserForm" action="${pageContext.request.contextPath}/edit-user" method="POST">
                            <input type="hidden" name="userId" id="editUserId"
                                   value="${sessionScope.editUserId}">
                            <div class="mb-3">
                                <label class="form-label">Username</label>
                                <input type="text" class="form-control" name="username" id="editUsername" required
                                       value="${sessionScope.editUserUsername}">
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Full Name</label>
                                <input type="text" class="form-control" name="fullName" id="editFullName" required
                                       value="${sessionScope.editUserFullName}">
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Email</label>
                                <input type="email" class="form-control" name="email" id="editEmail" required
                                       value="${sessionScope.editUserEmail}">
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Phone Number</label>
                                <input type="tel" class="form-control" name="phoneNumber" id="editPhoneNumber"
                                       value="${sessionScope.editUserPhoneNumber}">
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Date of Birth (dd-MM-yyyy)</label>
                                <input type="date" class="form-control" name="dateOfBirth" id="editDateOfBirth"
                                       value="${sessionScope.editUserDateOfBirth}" max="2025-03-12" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Gender</label>
                                <select class="form-control" name="gender" id="editGender">
                                    <option value="Male" ${sessionScope.editUserGender == 'Male' ? 'selected' : ''}>Male</option>
                                    <option value="Female" ${sessionScope.editUserGender == 'Female' ? 'selected' : ''}>Female</option>
                                    <option value="Other" ${sessionScope.editUserGender == 'Other' ? 'selected' : ''}>Other</option>
                                </select>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Role</label>
                                <select class="form-control" name="role" id="editRole">
                                    <option value="Customer" ${sessionScope.editUserRole == 'Customer' ? 'selected' : ''}>Customer</option>
                                    <option value="Staff" ${sessionScope.editUserRole == 'Staff' ? 'selected' : ''}>Staff</option>
                                    <option value="Admin" ${sessionScope.editUserRole == 'Admin' ? 'selected' : ''}>Admin</option>
                                </select>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Status</label>
                                <select class="form-control" name="status" id="editStatus">
                                    <option value="Active" ${sessionScope.editUserStatus == 'Active' ? 'selected' : ''}>Active</option>
                                    <option value="Inactive" ${sessionScope.editUserStatus == 'Inactive' ? 'selected' : ''}>Inactive</option>
                                    <option value="Banned" ${sessionScope.editUserStatus == 'Banned' ? 'selected' : ''}>Banned</option>
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
        <!-- Cập nhật lại JavaScript -->
        <script>
                            let userToDelete = null;

                            function viewUserDetail(userId) {
                                fetch('view-user-detail?id=' + userId)
                                        .then(response => response.json())
                                        .then(user => {
                                            console.log("Avatar value:", user.avatar); // Kiểm tra giá trị
                                            const contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf('/', 1)) || '/Restaurant_Online';
                                            const avatarUrl = contextPath + '/image/' + (user.avatar || 'default-avatar.jpg');
                                            console.log("Avatar URL:", avatarUrl); // Log đường dẫn để kiểm tra

                                            const modalBody = document.getElementById('viewDetailModal').querySelector('.modal-body');
                                            modalBody.innerHTML = `
                                            <p><strong>Avatar:</strong></p>
                                            <img id="avatarImage" alt="User Avatar" style="max-width: 120px; max-height: 150px; border-radius: 5px; margin-bottom: 10px;">
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
                                            // Gán src sau khi render
                                            const imgElement = document.getElementById('avatarImage');
                                            imgElement.src = avatarUrl;
                                            imgElement.onerror = function () {
                                                console.log('Error loading image:', avatarUrl);
                                                this.src = '/Restaurant_Online/image/default-avatar.jpg'; // Fallback
                                            };
                                            new bootstrap.Modal(document.getElementById('viewDetailModal')).show();
                                        })
                                        .catch(error => console.error('Error:', error));
                            }

                            function editUser(userId) {
                                fetch('view-user-detail?id=' + userId)
                                        .then(response => response.json())
                                        .then(user => {
                                            if (user) {
                                                document.getElementById('editUserId').value = userId;
                                                document.getElementById('editUsername').value = user.username || '';
                                                document.getElementById('editFullName').value = user.fullName || '';
                                                document.getElementById('editEmail').value = user.email || '';
                                                document.getElementById('editPhoneNumber').value = user.phoneNumber || '';
                                                if (user.dateOfBirth) {
                                                    const date = new Date(user.dateOfBirth);
                                                    const formattedDate = date.toISOString().split('T')[0];
                                                    document.getElementById('editDateOfBirth').value = formattedDate;
                                                }
                                                document.getElementById('editGender').value = user.gender || 'Male';
                                                document.getElementById('editRole').value = user.role || 'Customer';
                                                document.getElementById('editStatus').value = user.status || 'Active';

                                                let editModal = new bootstrap.Modal(document.getElementById('editUserModal'));
                                                editModal.show();
                                            }
                                        })
                                        .catch(error => {
                                            console.error('Error:', error);
                                            alert('Error loading user data');
                                        });
                            }

                            // Thêm kiểm tra khi mở addUserModal
                            document.querySelector('[data-bs-target="#addUserModal"]').addEventListener('click', function () {
                                const dateInput = document.querySelector('#addUserForm input[name="dateOfBirth"]');
                                const currentDate = new Date('2025-03-12').toISOString().split('T')[0];
                                if (dateInput.value && dateInput.value > currentDate) {
                                    alert("Date of Birth cannot be greater than current date (2025-03-12).");
                                    event.preventDefault(); // Ngăn không mở modal
                                }
                            });

                            function confirmDelete(userId) {
                                userToDelete = userId;
                                new bootstrap.Modal(document.getElementById('deleteConfirmModal')).show();
                            }

                            function deleteUser() {
                                if (userToDelete) {
                                    window.location.href = 'delete-user?id=' + userToDelete;
                                }
                            }

                            // Hiển thị modal nếu có lỗi
                            document.addEventListener('DOMContentLoaded', function () {
                        <c:if test="${not empty sessionScope.addUserErrors}">
                                let addModal = new bootstrap.Modal(document.getElementById('addUserModal'));
                                addModal.show();
                            <% session.removeAttribute("addUserErrors"); %>
                            <% session.removeAttribute("addUserUsername"); %>
                            <% session.removeAttribute("addUserFullName"); %>
                            <% session.removeAttribute("addUserEmail"); %>
                            <% session.removeAttribute("addUserPhoneNumber"); %>
                            <% session.removeAttribute("addUserDateOfBirth"); %>
                            <% session.removeAttribute("addUserGender"); %>
                            <% session.removeAttribute("addUserRole"); %>
                        </c:if>
                        <c:if test="${not empty sessionScope.editUserErrors}">
                                let editModal = new bootstrap.Modal(document.getElementById('editUserModal'));
                                editModal.show();
                            <% session.removeAttribute("editUserErrors"); %>
                            <% session.removeAttribute("editUserId"); %>
                            <% session.removeAttribute("editUserUsername"); %>
                            <% session.removeAttribute("editUserFullName"); %>
                            <% session.removeAttribute("editUserEmail"); %>
                            <% session.removeAttribute("editUserPhoneNumber"); %>
                            <% session.removeAttribute("editUserDateOfBirth"); %>
                            <% session.removeAttribute("editUserGender"); %>
                            <% session.removeAttribute("editUserRole"); %>
                            <% session.removeAttribute("editUserStatus"); %>
                        </c:if>
                            });
        </script>
    </body>
</html>
