/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Models.User;
import Utils.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author admin
 */
public class UserDAO extends DBContext {

    public User loginWithUsername(String username, String password) {
        String query = "select * from Users\n"
                + "where Username = ?\n"
                + "and PasswordHash = ?";
        try {
            Connection connect = new DBContext().getConnection();
            PreparedStatement ps = connect.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return new User(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getDate(7),
                        rs.getString(8),
                        rs.getString(9),
                        rs.getDate(10),
                        rs.getString(11),
                        rs.getString(12));
            }

            return null;
        } catch (Exception e) {
        }
        return null;
    }

    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        String query = "SELECT * FROM Users";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new User(
                        rs.getInt("UserID"),
                        rs.getString("Username"),
                        rs.getString("FullName"),
                        rs.getString("Email"),
                        rs.getString("PasswordHash"),
                        rs.getString("PhoneNumber"),
                        rs.getDate("DateOfBirth"),
                        rs.getString("Gender"),
                        rs.getString("Avatar"),
                        rs.getDate("CreatedAt"),
                        rs.getString("Status"),
                        rs.getString("Role")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<User> searchUsers(String username) {
        List<User> list = new ArrayList<>();
        String query = "SELECT * FROM Users WHERE Username LIKE ?";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, "%" + username + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new User(
                        rs.getInt("UserID"),
                        rs.getString("Username"),
                        rs.getString("FullName"),
                        rs.getString("Email"),
                        rs.getString("PasswordHash"),
                        rs.getString("PhoneNumber"),
                        rs.getDate("DateOfBirth"),
                        rs.getString("Gender"),
                        rs.getString("Avatar"),
                        rs.getDate("CreatedAt"),
                        rs.getString("Status"),
                        rs.getString("Role")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<User> getUsersByRole(String role) {
        List<User> list = new ArrayList<>();
        String query = "SELECT * FROM Users WHERE Role = ?";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, role);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new User(
                        rs.getInt("UserID"),
                        rs.getString("Username"),
                        rs.getString("FullName"),
                        rs.getString("Email"),
                        rs.getString("PasswordHash"),
                        rs.getString("PhoneNumber"),
                        rs.getDate("DateOfBirth"),
                        rs.getString("Gender"),
                        rs.getString("Avatar"),
                        rs.getDate("CreatedAt"),
                        rs.getString("Status"),
                        rs.getString("Role")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public User getUserById(int userId) {
        String query = "SELECT * FROM Users WHERE UserID = ?";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("UserID"),
                        rs.getString("Username"),
                        rs.getString("FullName"),
                        rs.getString("Email"),
                        rs.getString("PasswordHash"),
                        rs.getString("PhoneNumber"),
                        rs.getDate("DateOfBirth"),
                        rs.getString("Gender"),
                        rs.getString("Avatar"),
                        rs.getDate("CreatedAt"),
                        rs.getString("Status"),
                        rs.getString("Role")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public User getUserByUsername(String username) {
        String query = "SELECT * FROM Users WHERE Username = ?";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("UserID"),
                        rs.getString("Username"),
                        rs.getString("FullName"),
                        rs.getString("Email"),
                        rs.getString("PasswordHash"),
                        rs.getString("PhoneNumber"),
                        rs.getDate("DateOfBirth"),
                        rs.getString("Gender"),
                        rs.getString("Avatar"),
                        rs.getDate("CreatedAt"),
                        rs.getString("Status"),
                        rs.getString("Role")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean deleteUser(int userId) {
        String query = "DELETE FROM Users WHERE UserID = ? AND Role != 'Admin'";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateUser(User user) {
        String query = "UPDATE Users SET Username=?, FullName=?, Email=?, PhoneNumber=?, DateOfBirth=?, Gender=?, [Status]=?, [Role]=? WHERE UserID=?";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query);

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getFullName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPhoneNumber());
            ps.setDate(5, new java.sql.Date(user.getDateOfBirth().getTime()));
            ps.setString(6, user.getGender());
            ps.setString(7, user.getStatus());
            ps.setString(8, user.getRole());
            ps.setInt(9, user.getUserID());

            int rowsAffected = ps.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);

            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Thêm vào UserDAO.java
    public boolean addUser(User user) {
        String query = "INSERT INTO Users (Username, FullName, Email, PasswordHash, PhoneNumber, "
                + "DateOfBirth, Gender, Status, Role, CreatedAt) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getFullName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPasswordHash());
            ps.setString(5, user.getPhoneNumber());
            ps.setDate(6, new java.sql.Date(user.getDateOfBirth().getTime()));
            ps.setString(7, user.getGender());
            ps.setString(8, user.getStatus());
            ps.setString(9, user.getRole());
            ps.setTimestamp(10, new java.sql.Timestamp(user.getCreatedAt().getTime()));

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateUser(int userId, String fullName, String email, String phoneNumber, String dateOfBirth, String gender, String password) {
        String sql = "UPDATE Users SET FullName = ?, Email = ?, PhoneNumber = ?, DateOfBirth = ?, Gender = ?, PasswordHash = ? WHERE UserID = ?";
        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, fullName);
            ps.setString(2, email);
            ps.setString(3, phoneNumber);
            ps.setString(4, dateOfBirth);
            ps.setString(5, gender);
            ps.setString(6, password); // Giữ nguyên mật khẩu, có thể hash nếu cần
            ps.setInt(7, userId);

            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Kiểm tra username có tồn tại hay không
    public boolean isUsernameExists(String username) {
        String sql = "SELECT 1 FROM Users WHERE Username = ?";
        try ( PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Nếu có kết quả -> Username đã tồn tại
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Kiểm tra email có tồn tại hay không
    public boolean isEmailExists(String email) {
        String sql = "SELECT 1 FROM Users WHERE Email = ?";
        try ( PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Nếu có kết quả -> Email đã tồn tại
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Kiểm tra số điện thoại có tồn tại hay không
    public boolean isPhoneExists(String phoneNumber) {
        String sql = "SELECT 1 FROM Users WHERE PhoneNumber = ?";
        try ( PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, phoneNumber);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Nếu có kết quả -> Phone đã tồn tại
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Đăng ký tài khoản mới
    public void register(String username, String fullname, String email, String password, String phoneNumber, String gender) {
        String sql = "INSERT INTO Users (Username, FullName, Email, PasswordHash, PhoneNumber, Gender) VALUES (?, ?, ?, ?, ?, ?)";
        try ( PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, fullname);
            stmt.setString(3, email);
            stmt.setString(4, password); // Lưu ý: Password đã được hash trước khi gọi hàm này
            stmt.setString(5, phoneNumber);
            stmt.setString(6, gender);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User getUserByEmail(String email) {
        String query = "SELECT * FROM Users WHERE Email = ?";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("UserID"),
                        rs.getString("Username"),
                        rs.getString("FullName"),
                        rs.getString("Email"),
                        rs.getString("PasswordHash"),
                        rs.getString("PhoneNumber"),
                        rs.getDate("DateOfBirth"),
                        rs.getString("Gender"),
                        rs.getString("Avatar"),
                        rs.getDate("CreatedAt"),
                        rs.getString("Status"),
                        rs.getString("Role")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void createGoogleUser(String email, String fullName, String avatar) {
        String sql = "INSERT INTO Users (Email, FullName, Avatar, CreatedAt, Status, Role) VALUES (?, ?, ?, GETDATE(), 'Active', 'Customer')";
        try ( PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, fullName);
            stmt.setString(3, avatar); // Thêm avatar vào câu lệnh SQL
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
