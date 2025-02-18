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
              crossorigin="anony
              mous" referrerpolicy="no-referrer" />
        <!-- Bootstrap -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css"
              rel="stylesheet">
        <!-- Custom CSS -->
        <link rel="stylesheet" href="./CSS/Style.css" />
        <link rel="stylesheet" href="./CSS/HeaderAndFooter_CSS.css" />
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
                margin-left: 250px; /* Space for the sidebar */
                padding: 20px;
                background-color: #f8f9fa;
                width: calc(100% - 250px);
                float: left;
            }

            /* Prevent floating issues */
            .clearfix::after {
                content: "";
                display: table;
                clear: both;
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

                    <!-- Search Form -->
                    <form method="GET" action="admin-search-account" class="mb-3">
                        <div class="row">
                            <div class="col-md-6">
                                <input type="text" name="search" class="form-control" placeholder="Search account by username" value="${param.search}">
                        </div>
                        <div class="col-md-2">
                            <button type="submit" class="btn btn-primary w-100"><i class="fas fa-search"></i> Search</button>
                        </div>
                    </div>
                </form>

                <!-- Add Account Button -->
                <a href="admin_add_account.jsp" class="btn btn-success mb-3"><i class="fas fa-plus"></i> Add New Account</a>

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
                        <c:forEach var="a" items="${accounts}">

                            <tr>
                                <td>${a.id}</td>
                                <td>${a.username}</td>
                                <td>${a.password}</td>
                                <td>${a.email}</td>
                                <td>${a.phone_number}</td>
                                <td>${a.createdAt}</td>
                                <td>${a.isAdmin() ? "Admin" : "User"}</td>

                                <td>
                                    <c:if test="${a.id != sessionScope.acc.id}">
                                        <a href="/admin-edit-account?id=${a.id}" class="btn btn-primary btn-sm"><i class="fas fa-edit"></i> Edit</a>
                                        <a href="/admin-delete-account?id=${a.id}" class="btn btn-danger btn-sm"
                                           onclick="return confirm('Are you sure you want to delete this account?');"><i class="fas fa-trash"></i> Delete</a>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>

        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
