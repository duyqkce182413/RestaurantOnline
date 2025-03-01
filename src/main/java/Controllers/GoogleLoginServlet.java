package Controllers;

import DAO.UserDAO;
import Models.User;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.MemoryDataStoreFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

//@WebServlet("/GoogleLoginServlet")
public class GoogleLoginServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(GoogleLoginServlet.class.getName());
    private static final String CLIENT_ID = "127240927862-ovg89s7bgqd52hppkv89dq7i1ch9oc9m.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "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";
    private static final String REDIRECT_URI = "http://localhost:8080/Restaurant_Online/GoogleLoginServlet";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String code = request.getParameter("code");

            if (code == null || code.isEmpty()) {
                // Nếu không có mã xác thực, chuyển hướng người dùng đến trang xác thực Google
                GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                        GoogleNetHttpTransport.newTrustedTransport(),
                        JSON_FACTORY,
                        CLIENT_ID,
                        CLIENT_SECRET,
                        Arrays.asList("openid", "email", "profile")) // Thêm scope "openid"
                        .setDataStoreFactory(new MemoryDataStoreFactory())
                        .setAccessType("offline")
                        .build();

                String authUrl = flow.newAuthorizationUrl()
                        .setRedirectUri(REDIRECT_URI)
                        .build();

                response.sendRedirect(authUrl);
                return;
            }

            // Nếu có mã xác thực, yêu cầu token từ Google
            GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JSON_FACTORY,
                    CLIENT_ID,
                    CLIENT_SECRET,
                    Arrays.asList("openid", "email", "profile")) // Thêm scope "openid"
                    .setDataStoreFactory(new MemoryDataStoreFactory())
                    .setAccessType("offline")
                    .build();

            TokenResponse tokenResponse = flow.newTokenRequest(code)
                    .setRedirectUri(REDIRECT_URI)
                    .execute();

            // Lấy access token từ tokenResponse
            String accessToken = tokenResponse.getAccessToken();

            // Gọi Google API để lấy thông tin người dùng
            URL url = new URL("https://www.googleapis.com/oauth2/v2/userinfo?access_token=" + accessToken);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Đọc phản hồi từ Google API
            Scanner scanner = new Scanner(conn.getInputStream());
            StringBuilder jsonResponse = new StringBuilder();
            while (scanner.hasNext()) {
                jsonResponse.append(scanner.nextLine());
            }
            scanner.close();

            // Parse JSON để lấy thông tin người dùng
            JSONObject jsonObject = new JSONObject(jsonResponse.toString());
            String email = jsonObject.getString("email");
            String name = jsonObject.getString("name");
            String avatar = jsonObject.getString("picture"); // Lấy URL hình ảnh đại diện

            // Xử lý đăng nhập hoặc tạo người dùng mới
            UserDAO userDAO = new UserDAO();
            User user = userDAO.getUserByEmail(email);

            // In ra jsonObject để debug
            System.out.println("Parsed JSON Object: " + jsonObject.toString());

            // In ra các trường cụ thể
            System.out.println("Email: " + jsonObject.getString("email"));
            System.out.println("Name: " + jsonObject.getString("name"));
            System.out.println("Avatar: " + jsonObject.getString("picture"));

            if (user == null) {
                userDAO.createGoogleUser(email, name, avatar); // Truyền thêm avatar
                user = userDAO.getUserByEmail(email);
            }

            // Đăng nhập người dùng và chuyển hướng đến trang chủ
            request.getSession().setAttribute("user", user);
            response.sendRedirect("all");

        } catch (GeneralSecurityException e) {
            LOGGER.log(Level.SEVERE, "Google Login error", e);
            response.sendRedirect("LoginView.jsp?error=" + e.getClass().getSimpleName() + ": " + e.getMessage());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Google Login error", e);
            response.sendRedirect("LoginView.jsp?error=" + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }
}
