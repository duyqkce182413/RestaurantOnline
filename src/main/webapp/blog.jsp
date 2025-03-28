<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Blog - Nhà hàng TheKoi</title>
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="./CSS/HeaderAndFooter_CSS.css">
    <link rel="stylesheet" href="./CSS/home.css">
    <script src="./Script/header-script.js"></script>
    <style>
        .blog-section {
            padding: 40px 0;
            background-color: #f9f9f9;
        }

        .blog-title {
            color: #FC6E51;
            font-weight: bold;
            text-align: center;
            margin-bottom: 20px;
        }

        .blog-content {
            font-size: 18px;
            color: #333;
            line-height: 1.6;
        }

        .blog-item {
            padding: 15px;
            border: 1px solid #ddd;
            border-radius: 8px;
            background-color: #fff;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            margin-top: 20px;
        }

        .blog-item img {
            width: 100%;
            height: 200px;
            object-fit: cover;
            border-radius: 8px;
        }

        .blog-title-small {
            color: #FC6E51;
            font-weight: bold;
            margin-top: 10px;
        }
    </style>
</head>

<body>
    <!-- Header -->
    <jsp:include page="Header.jsp"></jsp:include>

    <div class="container blog-section">
        <h1 class="blog-title">Chào mừng đến với Blog Ẩm Thực của TheKoi!</h1>

        <div class="row">
            <div class="col-md-6 blog-content">
                <p>Chúng tôi chia sẻ những bài viết thú vị về ẩm thực, công thức nấu ăn, địa điểm ăn uống hấp dẫn và những xu hướng mới nhất trong ngành ẩm thực.</p>

                <p>TheKoi cam kết mang đến cho bạn những nội dung chất lượng, giúp bạn khám phá và thưởng thức ẩm thực một cách tuyệt vời nhất.</p>
            </div>
            <div class="col-md-6">
                <img src="https://images.unsplash.com/photo-1504674900247-0877df9cc836?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D" alt="Hình ảnh Blog" class="img-fluid rounded">
            </div>
        </div>

        <div class="row mt-5 gap-5 d-flex justify-content-center">
            <div class="col-md-3 blog-item">
                <img src="https://images.unsplash.com/photo-1484723091739-30a097e8f929?q=80&w=1547&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D" alt="Món ăn ngon">
                <h4 class="blog-title-small">Món ăn ngon mỗi ngày</h4>
                <p>Khám phá những công thức nấu ăn thơm ngon, dễ làm và phù hợp với mọi gia đình.</p>
                <a href="#" class="btn btn-primary btn-sm">Đọc tiếp</a>
            </div>
            <div class="col-md-3 blog-item">
                <img src="https://plus.unsplash.com/premium_photo-1675252369719-dd52bc69c3df?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D" alt="Nhà hàng nổi bật">
                <h4 class="blog-title-small">Nhà hàng nổi bật</h4>
                <p>Danh sách các nhà hàng được yêu thích nhất trong tháng này.</p>
                <a href="#" class="btn btn-primary btn-sm">Đọc tiếp</a>
            </div>
            <div class="col-md-3 blog-item">
                <img src="https://plus.unsplash.com/premium_photo-1670333182902-067f29c20107?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D" alt="Cafe Hot Trend">
                <h4 class="blog-title-small">Đồ uống hot</h4>
                <p>Cập nhật những xu hướng đồ uống được yêu thích nhất hiện nay.</p>
                <a href="#" class="btn btn-primary btn-sm">Đọc tiếp</a>
            </div>
        </div>
    </div>

    <!-- Footer -->
    <jsp:include page="Footer.jsp"></jsp:include>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>

</html>
