package Controllers;

import DAO.FoodDAO;
import DAO.UserDAO;
import Models.User;
import Utils.PasswordUtil;
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
import java.util.ArrayList;

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
            case "/register":
                Register(request, response);
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
        response.getWriter().print(gson.toJson(user));
        response.getWriter().flush();
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
            String username = request.getParameter("username");
            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String phoneNumber = request.getParameter("phoneNumber");
            String dobString = request.getParameter("dateOfBirth");
            String gender = request.getParameter("gender");
            String role = request.getParameter("role");

            List<String> errors = new ArrayList<>();

            // Kiểm tra trùng lặp
            if (userDAO.isUsernameExists(username)) {
                errors.add("Username already exists.");
            }
            if (userDAO.isEmailExists(email)) {
                errors.add("Email already exists.");
            }
            if (userDAO.isPhoneExists(phoneNumber)) {
                errors.add("Phone number already exists.");
            }

            // Kiểm tra định dạng
            if (username.contains(" ")) {
                errors.add("Username must not contain spaces.");
            }
            if (!fullName.matches("^[a-zA-Z\\s]+$")) {
                errors.add("Full Name must not contain numbers or special characters.");
            }
            if (!phoneNumber.matches("^[0-9]{10}$")) {
                errors.add("Phone Number must start with 0 and must be 10 digits with no letters or spaces.");
            }
            if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                errors.add("Invalid Email format.");
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            Date dateOfBirth = null;
            try {
                dateOfBirth = sdf.parse(dobString);
            } catch (ParseException e) {
                errors.add("Invalid Date of Birth format (use yyyy-MM-dd).");
            }

            if (!errors.isEmpty()) {
                // Lưu thông tin đã nhập vào session để hiển thị lại
                HttpSession session = request.getSession();
                session.setAttribute("addUserErrors", errors);
                session.setAttribute("addUserUsername", username);
                session.setAttribute("addUserFullName", fullName);
                session.setAttribute("addUserEmail", email);
                session.setAttribute("addUserPhoneNumber", phoneNumber);
                session.setAttribute("addUserDateOfBirth", dobString);
                session.setAttribute("addUserGender", gender);
                session.setAttribute("addUserRole", role);

                // Chuyển hướng về view-users
                response.sendRedirect("view-users");
                return;
            }

            // Tạo user mới nếu không có lỗi
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setFullName(fullName);
            newUser.setEmail(email);
            newUser.setPasswordHash(password);
            newUser.setPhoneNumber(phoneNumber);
            newUser.setDateOfBirth(dateOfBirth);
            newUser.setGender(gender);
            newUser.setRole(role);
            newUser.setStatus("Active");
            newUser.setCreatedAt(new Date());

            userDAO.addUser(newUser);
            response.sendRedirect("view-users");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("view-users?error=true");
        }
    }

    private void editUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String idStr = request.getParameter("userId");
            if (idStr == null || idStr.trim().isEmpty()) {
                response.sendRedirect("view-users?error=true");
                return;
            }

            int userId = Integer.parseInt(idStr.trim());
            User existingUser = userDAO.getUserById(userId);
            if (existingUser == null) {
                response.sendRedirect("view-users?error=true");
                return;
            }

            String username = request.getParameter("username");
            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");
            String phoneNumber = request.getParameter("phoneNumber");
            String dobString = request.getParameter("dateOfBirth");
            String gender = request.getParameter("gender");
            String role = request.getParameter("role");
            String status = request.getParameter("status");

            List<String> errors = new ArrayList<>();

            // Kiểm tra trùng lặp (loại trừ chính user hiện tại)
            if (!username.equals(existingUser.getUsername()) && userDAO.isUsernameExists(username)) {
                errors.add("Username already exists.");
            }
            if (!email.equals(existingUser.getEmail()) && userDAO.isEmailExists(email)) {
                errors.add("Email already exists.");
            }
            if (!phoneNumber.equals(existingUser.getPhoneNumber()) && userDAO.isPhoneExists(phoneNumber)) {
                errors.add("Phone number already exists.");
            }

            // Kiểm tra định dạng
            if (username.contains(" ")) {
                errors.add("Username must not contain spaces.");
            }
            if (!fullName.matches("^[a-zA-Z\\s]+$")) {
                errors.add("Full Name must not contain numbers or special characters.");
            }
            if (!phoneNumber.matches("^[0-9]{10}$")) {
                errors.add("Phone Number must start with 0 and must be 10 digits with no letters or spaces.");
            }
            if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                errors.add("Invalid Email format.");
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            Date dateOfBirth = null;
            try {
                if (dobString != null && !dobString.isEmpty()) {
                    dateOfBirth = sdf.parse(dobString);
                } else {
                    dateOfBirth = existingUser.getDateOfBirth();
                }
            } catch (ParseException e) {
                errors.add("Invalid Date of Birth format (use yyyy-MM-dd).");
            }

            if (!errors.isEmpty()) {
                // Lưu thông tin đã nhập vào session để hiển thị lại
                HttpSession session = request.getSession();
                session.setAttribute("editUserErrors", errors);
                session.setAttribute("editUserId", userId);
                session.setAttribute("editUserUsername", username);
                session.setAttribute("editUserFullName", fullName);
                session.setAttribute("editUserEmail", email);
                session.setAttribute("editUserPhoneNumber", phoneNumber);
                session.setAttribute("editUserDateOfBirth", dobString);
                session.setAttribute("editUserGender", gender);
                session.setAttribute("editUserRole", role);
                session.setAttribute("editUserStatus", status);

                // Chuyển hướng về view-users
                response.sendRedirect("view-users");
                return;
            }

            // Cập nhật user nếu không có lỗi
            existingUser.setUsername(username);
            existingUser.setFullName(fullName);
            existingUser.setEmail(email);
            existingUser.setPhoneNumber(phoneNumber);
            existingUser.setDateOfBirth(dateOfBirth);
            existingUser.setGender(gender);
            existingUser.setRole(role);
            existingUser.setStatus(status);

            userDAO.updateUser(existingUser);
            response.sendRedirect("view-users");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("view-users?error=true");
        }
    }

    private void loginWithUsername(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UserDAO dao = new UserDAO();
        User user = dao.getUserByUsername(username); // Lấy thông tin user từ database
        HttpSession session = request.getSession();

        if (user == null) {
            // Nếu không tìm thấy user
            request.setAttribute("messerror", "Wrong Username or Password");
            request.getRequestDispatcher("LoginView.jsp").forward(request, response);
        } else {
            boolean isPasswordCorrect = false;

            // Kiểm tra xem mật khẩu trong database đã được hash hay chưa
            if (user.getPasswordHash().startsWith("$2a$")) { // Bcrypt hash bắt đầu bằng "$2a$"
                // Nếu đã hash, sử dụng BCrypt để kiểm tra
                isPasswordCorrect = PasswordUtil.checkPassword(password, user.getPasswordHash());
            } else {
                // Nếu chưa hash, so sánh trực tiếp
                isPasswordCorrect = password.equals(user.getPasswordHash());
            }

            if (!isPasswordCorrect) {
                // Nếu mật khẩu không đúng
                request.setAttribute("messerror", "Wrong Username or Password");
                request.getRequestDispatcher("LoginView.jsp").forward(request, response);
            } else {
                // Nếu mật khẩu đúng
                session.setAttribute("user", user);
                session.setMaxInactiveInterval(1200); // Thiết lập thời gian timeout cho session

                // Kiểm tra role của user
                if (user.getRole().equalsIgnoreCase("Admin")) {
                    response.sendRedirect("view-users");
                } else {
                    response.sendRedirect("all");
                }
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

    private void Register(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

        // Xóa thông báo lỗi trước đó
        session.removeAttribute("errorName");
        session.removeAttribute("errorEmail");
        session.removeAttribute("errorPhone");
        session.removeAttribute("errorPassword");
        session.removeAttribute("errorConfirm");
        session.removeAttribute("successMessage");

        String username = request.getParameter("username");
        String fullname = request.getParameter("fullName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String phoneNumber = request.getParameter("phoneNumber");
        String gender = request.getParameter("gender");

        UserDAO dao = new UserDAO();
        boolean hasError = false;

        // Kiểm tra mật khẩu
        if (password == null || password.length() < 8) {
            session.setAttribute("errorPassword", "Password must be at least 8 characters long.");
            hasError = true;
        } else if (!password.equals(confirmPassword)) {
            session.setAttribute("errorConfirm", "Passwords do not match.");
            hasError = true;
        }

        // Kiểm tra username, email, phoneNumber đã tồn tại hay chưa
        if (dao.isUsernameExists(username)) {
            session.setAttribute("errorName", "Username already exists.");
            hasError = true;
        }
        if (dao.isEmailExists(email)) {
            session.setAttribute("errorEmail", "Email already exists.");
            hasError = true;
        }
        if (dao.isPhoneExists(phoneNumber)) {
            session.setAttribute("errorPhone", "Phone number already exists.");
            hasError = true;
        }

        if (hasError) {
            response.sendRedirect("RegisterView.jsp");
            return;
        }

        // Đăng ký tài khoản mới
        String hashedPassword = PasswordUtil.hashPassword(password);
        dao.register(username, fullname, email, hashedPassword, phoneNumber, gender);
        session.setAttribute("successMessage", "Account created successfully.");

        response.sendRedirect("RegisterView.jsp");
    }

}
