package DAO;

import Models.Address;
import Models.User;
import Utils.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddressDAO extends DBContext {

    public List<Address> getAddressesByUsername(String username) {
        List<Address> addresses = new ArrayList<>();
        String sql = "SELECT a.*"
                + "FROM Address a "
                + "JOIN Users acc ON a.UserID = acc.UserID "
                + "WHERE acc.Username = ? AND IsDeleted = 0";

        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Address address = new Address();
                    address.setAddressID(rs.getInt("AddressID"));
                    address.setName(rs.getString("Name"));
                    address.setAddressLine(rs.getString("AddressLine"));
                    address.setCity(rs.getString("City"));
                    address.setPhoneNumber(rs.getString("PhoneNumber"));
                    address.setIsDefault(rs.getBoolean("IsDefault"));

                    addresses.add(address);
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(AddressDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return addresses;
    }

    public void unsetDefaultAddress(String username) throws SQLException {
        String sql = "UPDATE Address SET IsDefault = 0 "
                + "WHERE UserID = (SELECT UserID FROM Users WHERE Username = ?)";
        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.executeUpdate();
        }
    }

    public void setDefaultAddress(int addressId) throws SQLException {
        String sql = "UPDATE Address SET IsDefault = 1 WHERE AddressID = ?";
        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, addressId);
            ps.executeUpdate();
        }
    }

    public void insertAddress(int userId, String name, String addressLine, String city, String phoneNumber, boolean isDefault, int isDeleted) throws SQLException {
        String sql = "INSERT INTO Address (UserID, Name, AddressLine, City, PhoneNumber, IsDefault, IsDeleted) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, name);
            ps.setString(3, addressLine);
            ps.setString(4, city);
            ps.setString(5, phoneNumber);
            ps.setBoolean(6, isDefault);
            ps.setInt(7, isDeleted);
            ps.executeUpdate();
        }
    }

    public Address getAddressById(int id) throws SQLException {
        String sql = "SELECT a.*, acc.* FROM Address a JOIN Users acc ON a.UserID = acc.UserID WHERE a.AddressID = ?";
        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Address address = new Address();
                    address.setAddressID(rs.getInt("AddressID"));
                    address.setName(rs.getString("Name"));
                    address.setAddressLine(rs.getString("AddressLine"));
                    address.setCity(rs.getString("City"));
                    address.setPhoneNumber(rs.getString("PhoneNumber"));
                    address.setIsDefault(rs.getBoolean("IsDefault"));

                    User user = new User();
                    user.setUserID(rs.getInt("UserID"));
                    user.setUsername(rs.getString("Username"));
                    address.setUser(user);
                    return address;
                }
            }
        }
        return null;
    }

    public void insertNewAddress(int oldAddressId, String name, String addressLine, String city, String phoneNumber, boolean isDefault, int userId) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = getConnection();
            conn.setAutoCommit(false); // Bắt đầu transaction

            // 1. Đánh dấu địa chỉ cũ là đã xóa
            String softDeleteSql = "UPDATE Address SET ISDeleted = 1 WHERE AddressID = ? AND UserID = ?";
            ps = conn.prepareStatement(softDeleteSql);
            ps.setInt(1, oldAddressId);
            ps.setInt(2, userId);
            ps.executeUpdate();
            ps.close();

            // 2. Nếu địa chỉ mới là mặc định, bỏ đánh dấu mặc định của các địa chỉ khác
            if (isDefault) {
                String unsetDefaultSql = "UPDATE Address SET IsDefault = 0 WHERE UserID = ? AND ISDeleted = 0";
                ps = conn.prepareStatement(unsetDefaultSql);
                ps.setInt(1, userId);
                ps.executeUpdate();
                ps.close();
            }

            // 3. Chèn địa chỉ mới
            String insertSql = "INSERT INTO Address (UserID, Name, AddressLine, City, PhoneNumber, IsDefault, ISDeleted) VALUES (?, ?, ?, ?, ?, ?, 0)";
            ps = conn.prepareStatement(insertSql);
            ps.setInt(1, userId);
            ps.setString(2, name);
            ps.setString(3, addressLine);
            ps.setString(4, city);
            ps.setString(5, phoneNumber);
            ps.setBoolean(6, isDefault);
            ps.executeUpdate();
            ps.close();

            conn.commit(); // Xác nhận thay đổi
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback(); // Rollback nếu có lỗi
            }
            e.printStackTrace();
            throw e;
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.setAutoCommit(true);
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    public void deleteAddress(int id, int userId) throws SQLException {
        String sql = "UPDATE Address SET IsDeleted = 1 WHERE AddressID = ? AND UserID = ?";
        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setInt(2, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public Address getDefaultAddressByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM Address WHERE UserID = ? AND IsDefault = 1";
        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Address address = new Address();
                    address.setAddressID(rs.getInt("AddressID"));
                    address.setName(rs.getString("Name"));
                    address.setAddressLine(rs.getString("AddressLine"));
                    address.setCity(rs.getString("City"));
                    address.setPhoneNumber(rs.getString("PhoneNumber"));
                    address.setIsDefault(rs.getBoolean("IsDefault"));
                    return address;
                }
            }
        }
        return null;
    }

    public static void main(String[] args) throws SQLException {
        AddressDAO addressDAO = new AddressDAO();
        List<Address> addresses = addressDAO.getAddressesByUsername("customer1");

        for (Address address : addresses) {
            System.out.println("ID: " + address.getAddressID());
            System.out.println("Name: " + address.getName());
            System.out.println("Address Line: " + address.getAddressLine());
            System.out.println("City: " + address.getCity());
            System.out.println("Phone Number: " + address.getPhoneNumber());
            System.out.println("Is Default: " + address.isDefault());
            System.out.println("--------------------------");
        }
    }

}
