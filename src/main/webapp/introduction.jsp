<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Giới thiệu - Nhà hàng TheKoi</title>
        <!-- Bootstrap CSS -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css" integrity="sha512-Kc323vGBEqzTmouAECnVceyQqyqdsSiqLQISBL29aUW4U/M7pSPA/gEUZQqv1cwx4OnYxTxve5UMg5GT6L4JJg==" crossorigin="anonymous" referrerpolicy="no-referrer" />
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
        <!--        <link rel="stylesheet" href="./CSS/Style.css">-->
        <link rel="stylesheet" href="./CSS/HeaderAndFooter_CSS.css">
        <script src="./JS/header-script.js"></script>

        <!--CSS rieng-->
        <link rel="stylesheet" href="./CSS/HomeView.css">
        <style>
            .intro-section {
                padding: 40px 0;
                background-color: #f9f9f9;
            }

            .intro-title {
                color: #FC6E51;
                font-weight: bold;
                text-align: center;
                margin-bottom: 20px;
            }

            .intro-content {
                font-size: 18px;
                color: #333;
                line-height: 1.6;
            }

            .feature-item {
                padding: 15px;
                border: 1px solid #ddd;
                border-radius: 8px;
                background-color: #fff;
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
                margin-top: 20px;
            }

            .feature-title {
                color: #FC6E51;
                font-weight: bold;
            }
        </style>
    </head>

    <body>
        <!-- Header -->
        <jsp:include page="Header.jsp"></jsp:include>

            <div class="container intro-section">
                <h1 class="intro-title">Chào mừng đến với TheKoi - Nơi hội tụ tinh hoa ẩm thực!</h1>

                <div class="row">
                    <div class="col-md-6 intro-content">
                        <p>TheKoi không chỉ là một nhà hàng, mà còn là điểm đến lý tưởng cho những ai yêu thích hương vị tinh tế của ẩm thực. Chúng tôi tự hào mang đến những món ăn đặc sắc, kết hợp giữa sự sáng tạo và truyền thống, nhằm mang đến trải nghiệm ẩm thực tuyệt vời nhất cho khách hàng.</p>

                        <p>Tại TheKoi, chúng tôi cam kết sử dụng những nguyên liệu tươi ngon nhất, đảm bảo chất lượng từ khâu lựa chọn đến chế biến. Không gian sang trọng, ấm cúng cùng với dịch vụ chuyên nghiệp chắc chắn sẽ mang lại cho bạn những khoảnh khắc đáng nhớ.</p>
                    </div>
                    <div class="col-md-6">
                        <img src="https://tse3.mm.bing.net/th?id=OIP.IR6hTnZ9adqG3_6e92QdeQHaE8&pid=Api" alt="Không gian nhà hàng TheKoi" class="img-fluid rounded">
                    </div>
                </div>

                <div class="row mt-5 gap-5 d-flex justify-content-center">
                    <div class="col-md-3 feature-item">
                        <h4 class="feature-title">Thực đơn đa dạng</h4>
                        <p>Thưởng thức nhiều món ăn từ các nền ẩm thực khác nhau, được chế biến bởi những đầu bếp tài hoa và tâm huyết.</p>
                    </div>
                    <div class="col-md-3 feature-item">
                        <h4 class="feature-title">Chất lượng hàng đầu</h4>
                        <p>Nguyên liệu tươi ngon, được chọn lọc kỹ lưỡng để đảm bảo mỗi món ăn đều đạt tiêu chuẩn cao nhất về hương vị và vệ sinh an toàn thực phẩm.</p>
                    </div>
                    <div class="col-md-3 feature-item">
                        <h4 class="feature-title">Không gian sang trọng</h4>
                        <p>TheKoi mang đến một không gian ấm cúng, thanh lịch, phù hợp cho các buổi gặp mặt, hẹn hò hay những bữa tiệc quan trọng.</p>
                    </div>
                </div>
            </div>

            <!-- Footer -->
        <jsp:include page="Footer.jsp"></jsp:include>

        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>

</html>
