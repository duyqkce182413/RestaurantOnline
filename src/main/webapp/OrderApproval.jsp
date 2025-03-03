<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Admin Dashboard - Order Approval</title>
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
            .table-responsive {
                margin-top: 20px;
            }

            .search-filter-form {
                display: flex;
                justify-content: space-between;
                margin-bottom: 20px;
            }
            .search-filter-form input,
            .search-filter-form select {
                margin-right: 10px;
            }

            .btn-add {
                margin-top: 20px;
            }

            .modal-header h5 {
                font-weight: bold;
            }
        </style>
    </head>
    <body>
        <jsp:include page="Dashboard_Header.jsp"></jsp:include>

        <div class="wrapper clearfix row">
            <!-- Sidebar -->
            <jsp:include page="Dashboard_Sidebar.jsp"></jsp:include>

            <!-- Main content -->
            <div class="content col-12">
                <h1 class="text-center mb-4">Order Approval</h1>

                <!-- Order Table -->
                <div class="table-responsive">
                    <table class="table table-bordered table-striped text-center">
                        <thead class="table-dark">
                            <tr>
                                <th>Approval ID</th>
                                <th>Name</th>
                                <th>Order ID</th>
                                <th>Approval Date</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="approval" items="${approvals}">
                                <tr>
                                    <td>${approval.approvalID}</td>
                                    <td>${approval.approvedBy.fullName}</td>
                                    <td>${approval.orderID}</td>
                                    <td>${approval.approvedAt}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>


        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
