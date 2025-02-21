package Controllers;

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

@WebServlet(name = "UserController", urlPatterns = {
    "/view-users",
    "/add-user",
    "/delete-user",
    "/edit-user",
    "/view-user-detail",
    "/login-with-username"
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
            default:
                response.sendRedirect("view-users");
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
            if (user.getRole().equalsIgnoreCase("Admin")) {
                request.getRequestDispatcher("view-users").forward(request, response);
            } else {
                response.sendRedirect("all");
            }
        }
    }
}
