package Controllers;

import DAO.FoodDAO;
import DAO.UserDAO;
import Models.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@WebServlet(name = "UserController", urlPatterns = {
    "/view-users",
    "/add-user",
    "/delete-user",
    "/edit-user",
    "/view-user-detail",
    "/login-with-username",
    "/logout"
})
public class UserController extends HttpServlet {

    private UserDAO userDAO;
    private Gson gson;

    @Override
    public void init() {
        userDAO = new UserDAO();
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getServletPath();

        switch (action) {
            case "/view-users":
                viewUsers(request, response);
                break;
            case "/view-user-detail":
                viewUserDetail(request, response);
                break;
            case "/delete-user":
                deleteUser(request, response);
                break;
            case "/logout":
                logout(request, response);
                break;
            case "user-detail":
                getUser(request, response);
                break;
            default:
                viewUsers(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getServletPath();

        switch (action) {
            case "/login-with-username":
                loginWithUsername(request, response);
                break;
            case "/add-user":
                addUser(request, response);
                break;
            case "/edit-user":
                editUser(request, response);
                break;
            case "/update-user":
                updateUser(request, response);
                break;
        }
    }

    private void viewUsers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String searchUsername = request.getParameter("search");
        String filterRole = request.getParameter("role");
        List<User> users;

        if (searchUsername != null && !searchUsername.isEmpty()) {
            users = userDAO.searchUsers(searchUsername);
        } else if (filterRole != null && !filterRole.isEmpty()) {
            users = userDAO.getUsersByRole(filterRole);
        } else {
            users = userDAO.getAllUsers();
        }

        request.setAttribute("users", users);
        request.getRequestDispatcher("ManageUserView.jsp").forward(request, response);
    }

    private void viewUserDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int userId = Integer.parseInt(request.getParameter("id"));
        User user = userDAO.getUserById(userId);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Cấu hình Gson để xử lý Date
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .create();
        PrintWriter out = response.getWriter();
        out.print(gson.toJson(user));
        out.flush();
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String idStr = request.getParameter("id");
            if (idStr != null && !idStr.trim().isEmpty()) {
                int userId = Integer.parseInt(idStr.trim());
                User userToDelete = userDAO.getUserById(userId);
                if (userToDelete != null && !"Admin".equals(userToDelete.getRole())) {
                    userDAO.deleteUser(userId);
                }
            }
            response.sendRedirect("view-users");
        } catch (NumberFormatException e) {
            response.sendRedirect("view-users");
        }
    }

    private void addUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Lấy thông tin từ form
            String username = request.getParameter("username");
            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");
            String password = request.getParameter("password"); // Nên mã hóa password
            String phoneNumber = request.getParameter("phoneNumber");
            String gender = request.getParameter("gender");
            String role = request.getParameter("role");

            // Parse date
            String dobString = request.getParameter("dateOfBirth");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dateOfBirth = sdf.parse(dobString);

            // Tạo user mới
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setFullName(fullName);
            newUser.setEmail(email);
            newUser.setPasswordHash(password); // Nên mã hóa password
            newUser.setPhoneNumber(phoneNumber);
            newUser.setDateOfBirth(dateOfBirth);
            newUser.setGender(gender);
            newUser.setRole(role);
            newUser.setStatus("Active");
            newUser.setCreatedAt(new Date());

            // Thêm user vào database
            userDAO.addUser(newUser);

            response.sendRedirect("view-users");
        } catch (ParseException e) {
            e.printStackTrace();
            response.sendRedirect("view-users?error=true");
        }
    }

    private void editUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String idStr = request.getParameter("userId");
            System.out.println("Received userId: " + idStr); // Debug log
            System.out.println("UserID received: " + idStr);

            if (idStr != null && !idStr.trim().isEmpty()) {
                int userId = Integer.parseInt(idStr.trim());
                User user = userDAO.getUserById(userId);
                if (user != null) {
                    user.setUsername(request.getParameter("username"));
                    user.setFullName(request.getParameter("fullName"));
                    user.setEmail(request.getParameter("email"));
                    user.setPhoneNumber(request.getParameter("phoneNumber"));

                    String dobString = request.getParameter("dateOfBirth");
                    if (dobString != null && !dobString.isEmpty()) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        user.setDateOfBirth(sdf.parse(dobString));
                    }

                    user.setGender(request.getParameter("gender"));
                    user.setRole(request.getParameter("role"));
                    user.setStatus(request.getParameter("status"));

                    boolean updated = userDAO.updateUser(user);
                    System.out.println("Update result: " + updated); // Debug log
                }
            }
            response.sendRedirect("view-users");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("view-users");
        }
    }

    private void loginWithUsername(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UserDAO dao = new UserDAO();
        User user = dao.loginWithUsername(username, password);
        HttpSession session = request.getSession();
        if (user == null) {
            request.setAttribute("messerror", "Wrong Username or Password");
            request.getRequestDispatcher("LoginView.jsp").forward(request, response);
        } else {
            session.setAttribute("user", user);
            session.setMaxInactiveInterval(1200);

            System.out.println("Session created successfully.");

            // Kiểm tra xem session đã lưu đúng không
            System.out.println("User session: " + session.getAttribute("user"));

            if (user.getRole().equalsIgnoreCase("Admin")) {
                response.sendRedirect("view-users"); 
            } else if (user.getRole().equalsIgnoreCase("Staff")) {
                response.sendRedirect("listAdminOrders");
            } else {
                response.sendRedirect("all");
            }
        }
    }

    private void logout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        if (session != null) {
            session.removeAttribute("user");
            System.out.println("User logged out successfully.");
        } else {
            System.out.println("Session not found.");
        }
        response.sendRedirect("all");
    }

    private void getUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr != null && !idStr.trim().isEmpty()) {
            try {
                int userId = Integer.parseInt(idStr);
                User user = userDAO.getUserById(userId);

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                // Cấu hình Gson để xử lý Date
                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd")
                        .create();
                PrintWriter out = response.getWriter();
                out.print(gson.toJson(user));
                out.flush();
            } catch (NumberFormatException e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "User ID not provided");
        }
    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        int userId = user.getUserID();
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phoneNumber");
        String dateOfBirth = request.getParameter("dateOfBirth");
        String gender = request.getParameter("gender");
        String password = request.getParameter("password"); // Có thể hash mật khẩu nếu cần

        LocalDate date = LocalDate.parse(dateOfBirth, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String formattedDate = date.format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));

        UserDAO userDAO = new UserDAO();
        boolean success = userDAO.updateUser(userId, fullName, email, phoneNumber, formattedDate, gender, password);

        if (success) {
            request.getSession().setAttribute("updateMessage", "Cập nhật thông tin thành công!");
        } else {
            request.getSession().setAttribute("updateMessage", "Cập nhật thất bại. Vui lòng thử lại.");
        }

        User updatedUser = userDAO.getUserById(userId); // Lấy thông tin mới từ DB

        // Cập nhật thông tin người dùng trong session
        session.setAttribute("user", updatedUser);
        response.sendRedirect("UserProfile.jsp");

    }
}
