<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Biểu đồ Doanh thu</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css"
              integrity="sha512-Kc323vGBEqzTmouAECnVceyQqyqdsSiqLQISBL29aUW4U/M7pSPA/gEUZQqv1cwx4OnYxTxve5UMg5GT6L4JJg=="
              crossorigin="anonymous" referrerpolicy="no-referrer" />
        <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/3.7.0/chart.min.js"></script>
        <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
        
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
            .card {
                margin-left: 300px;
                margin-right: 50px;
                margin-bottom: 50px;
                }
        </style>
    </head>
    <body class="bg-light">
        <jsp:include page="Dashboard_Header.jsp"></jsp:include>
            <div class="wrapper clearfix">
                <!-- Sidebar -->
                <jsp:include page="Dashboard_Sidebar.jsp"/>
                <div class="card">
                    <div class="card-body">
                        <h1 class="card-title">Biểu đồ Doanh thu</h1>

                        <!-- Date Range Picker -->
                        <form id="dateForm" class="mb-4">
                            <div class="form-row mb-3">
                                <div class="form-group col-md-6">
                                    <label for="startDate">Từ ngày:</label>
                                    <input type="date" id="startDate" name="startDate" 
                                           value="${startDate}"
                                    class="form-control">
                            </div>
                            <div class="form-group col-md-6">
                                <label for="endDate">Đến ngày:</label>
                                <input type="date" id="endDate" name="endDate" 
                                       value="${endDate}"
                                       class="form-control">
                            </div>
                        </div>
                        <button type="button" onclick="updateChart()" 
                                class="btn btn-primary">
                            Cập nhật
                        </button>
                    </form>

                    <!-- Loading Indicator -->
                    <div id="loadingIndicator" class="d-none">
                        <p class="text-center text-secondary">Đang tải dữ liệu...</p>
                    </div>

                    <!-- Combo Chart -->
                    <div class="mb-4">
                        <canvas id="comboChart" style="height: 400px;"></canvas>
                    </div>

                    <!-- Total Revenue -->
                    <div class="alert alert-light">
                        <p class="font-weight-bold">
                            Tổng doanh thu: 
                            <span id="totalRevenue">
                                <fmt:formatNumber value="${totalRevenue}" type="currency" currencySymbol="₫"/>
                            </span>
                        </p>
                    </div>
                </div>
            </div>
        </div>

        <script>
            let comboChart;
            // Initialize chart with data from server
            document.addEventListener('DOMContentLoaded', function() {
            initializeChart();
            });
            function initializeChart() {
            const ctx = document.getElementById('comboChart').getContext('2d');
            const data = {
            labels: [
            <c:forEach items="${revenueData}" var="item" varStatus="status">
            '<fmt:formatDate value="${item.date}" pattern="dd/MM/yyyy"/>'${!status.last ? ',' : ''}
            </c:forEach>
            ],
                    datasets: [
                    {
                    type: 'bar',
                            label: 'Doanh thu',
                            data: [
            <c:forEach items="${revenueData}" var="item" varStatus="status">
                ${item.revenue}${!status.last ? ',' : ''}
            </c:forEach>
                            ],
                            backgroundColor: 'rgba(54, 162, 235, 0.2)',
                            borderColor: 'rgba(54, 162, 235, 1)',
                            borderWidth: 1
                    },
                    {
                    type: 'line',
                            label: 'Doanh thu',
                            data: [
            <c:forEach items="${revenueData}" var="item" varStatus="status">
                ${item.revenue}${!status.last ? ',' : ''}
            </c:forEach>
                            ],
                            fill: false,
                            borderColor: 'rgb(75, 192, 192)',
                            tension: 0.1
                    }
                    ]
            };
            if (comboChart) {
            comboChart.destroy();
            }

            comboChart = new Chart(ctx, {
            type: 'combo',
                    data: data,
                    options: {
                    responsive: true,
                            maintainAspectRatio: false,
                            scales: {
                            y: {
                            beginAtZero: true,
                                    ticks: {
                                    callback: function(value) {
                                    return new Intl.NumberFormat('vi-VN', {
                                    style: 'currency',
                                            currency: 'VND'
                                    }).format(value);
                                    }
                                    }
                            }
                            },
                            plugins: {
                            tooltip: {
                            callbacks: {
                            label: function(context) {
                            return new Intl.NumberFormat('vi-VN', {
                            style: 'currency',
                                    currency: 'VND'
                            }).format(context.raw);
                            }
                            }
                            }
                            }
                    }
            });
            }

            // Function to update chart with new date range
            function updateChart() {
            const startDate = document.getElementById('startDate').value;
            const endDate = document.getElementById('endDate').value;
            if (!startDate || !endDate) {
            alert('Vui lòng chọn khoảng thời gian');
            return;
            }

            // Show loading indicator
            document.getElementById('loadingIndicator').classList.remove('d-none');
            // Build URL with parameters
            const url = new URL('view-statistics', window.location.origin);
            url.searchParams.append('startDate', startDate);
            url.searchParams.append('endDate', endDate);
            // Make AJAX request
            fetch(url, {
            method: 'GET',
                    headers: {
                    'X-Requested-With': 'XMLHttpRequest'
                    }
            })
                    .then(response => {
                    if (!response.ok) {
                    throw new Error('Network response was not ok');
                    }
                    return response.json();
                    })
                    .then(data => {
                    // Update chart data
                    comboChart.data.labels = data.revenueData.map(item => {
                    const date = new Date(item.date);
                    return date.toLocaleDateString('vi-VN');
                    });
                    comboChart.data.datasets[0].data = data.revenueData.map(item => item.revenue);
                    comboChart.data.datasets[1].data = data.revenueData.map(item => item.revenue);
                    comboChart.update();
                    // Update total revenue
                    document.getElementById('totalRevenue').textContent =
                            new Intl.NumberFormat('vi-VN', {
                            style: 'currency',
                                    currency: 'VND'
                            }).format(data.totalRevenue);
                    })
                    .catch(error => {
                    console.error('Error:', error);
                    alert('Có lỗi xảy ra khi tải dữ liệu: ' + error.message);
                    })
                    .finally(() => {
                    // Hide loading indicator
                    document.getElementById('loadingIndicator').classList.add('d-none');
                    });
            }
        </script>
    </body>
</html>
