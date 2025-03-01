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
        String sql = "SELECT a.AddressID, a.Name, a.AddressLine, a.City, a.PhoneNumber, a.IsDefault "
                + "FROM Address a "
                + "JOIN Users acc ON a.UserID = acc.UserID "
                + "WHERE acc.Username = ?";

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

    public void insertAddress(int userId, String name, String addressLine, String city, String phoneNumber, boolean isDefault) throws SQLException {
        String sql = "INSERT INTO Address (UserID, Name, AddressLine, City, PhoneNumber, IsDefault) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, name);
            ps.setString(3, addressLine);
            ps.setString(4, city);
            ps.setString(5, phoneNumber);
            ps.setBoolean(6, isDefault);
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

    public void updateAddress(int id, String name, String addressLine, String city, String phoneNumber, boolean isDefault, int userId) throws SQLException {
        String sql = "UPDATE Address SET Name = ?, AddressLine = ?, City = ?, PhoneNumber = ?, IsDefault = ? "
                + "WHERE AddressID = ? AND UserID = ?";

        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, addressLine);
            ps.setString(3, city);
            ps.setString(4, phoneNumber);
            ps.setBoolean(5, isDefault);
            ps.setInt(6, id);
            ps.setInt(7, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void deleteAddress(int id, int userId) throws SQLException {
        String sql = "DELETE FROM Address WHERE AddressID = ? AND UserID = ?";
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
