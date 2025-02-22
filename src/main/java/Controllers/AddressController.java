/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import DAO.AddressDAO;
import Models.Address;
import Models.User;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AddressController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getServletPath();

        try {
            switch (action) {
                case "/listAddress":
                    listAddresses(request, response);
                    break;
                case "/setDefaultAddress":
                    setDefaultAddress(request, response);
                    break;
                case "/updateAddress":
                    showEditForm(request, response);
                    break;
                case "/deleteAddress":
                    deleteAddress(request, response);
                    break;
                default:
                    listAddresses(request, response);
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getServletPath();
        try {
            switch (action) {
                case "/insertAddress":
                    insertAddress(request, response);
                    break;
                case "/updateAddress":
                    updateAddress(request, response);
                    break;
                default:
                    listAddresses(request, response);
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void listAddresses(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        AddressDAO addressDAO = new AddressDAO();

        // Get the username from session after login
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String username;
        if (user != null) {
            username = user.getUsername();
        } else {
            response.sendRedirect("LoginView.jsp");
            return;
        }

        List<Address> addresses = addressDAO.getAddressesByUsername(username);
        
        
        
        System.out.println(addresses);
        
        request.setAttribute("listAddresses", addresses);
        RequestDispatcher dispatcher = request.getRequestDispatcher("AddressView.jsp");
        dispatcher.forward(request, response);
    }

    private void setDefaultAddress(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int addressId = Integer.parseInt(request.getParameter("id"));
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user != null) {
            AddressDAO addressDAO = new AddressDAO();

            // Unset all default addresses
            addressDAO.unsetDefaultAddress(user.getUsername());

            // Set the selected address as default
            addressDAO.setDefaultAddress(addressId);

            response.sendRedirect("listAddress");
        } else {
            response.sendRedirect("login.jsp");
        }
    }

    private void insertAddress(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        String name = request.getParameter("name");
        String addressLine = request.getParameter("addressLine");
        String city = request.getParameter("city");
        String phoneNumber = request.getParameter("phoneNumber");
        boolean isDefault = request.getParameter("is_default") != null;

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        AddressDAO addressDAO = new AddressDAO();

        try {
            if (isDefault) {
                addressDAO.unsetDefaultAddress(user.getUsername());
            }

            addressDAO.insertAddress(user.getUserID(), name, addressLine, city, phoneNumber, isDefault);

            response.sendRedirect("listAddress");
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error while adding the address. Please try again.");
            request.getRequestDispatcher("address_management.jsp").forward(request, response);
        }
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int addressId = Integer.parseInt(request.getParameter("id"));

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        AddressDAO addressDAO = new AddressDAO();
        Address address = addressDAO.getAddressById(addressId);

        if (address != null && address.getUser().getUserID() == user.getUserID()) {
            request.setAttribute("updateAddress", address);
            RequestDispatcher dispatcher = request.getRequestDispatcher("edit_address.jsp");
            dispatcher.forward(request, response);
        } else {
            response.sendRedirect("AddressView.jsp");
        }
    }

    private void updateAddress(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String addressLine = request.getParameter("addressLine");
        String city = request.getParameter("city");
        String phoneNumber = request.getParameter("phoneNumber");
        boolean isDefault = request.getParameter("is_default") != null;

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int userId = user.getUserID();
        AddressDAO addressDAO = new AddressDAO();

        try {
            if (isDefault) {
                addressDAO.unsetDefaultAddress(user.getUsername());
            }

            addressDAO.updateAddress(id, name, addressLine, city, phoneNumber, isDefault, userId);

            response.sendRedirect("listAddress");
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error while updating the address. Please try again.");
            request.getRequestDispatcher("edit_address.jsp").forward(request, response);
        }
    }

    private void deleteAddress(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int addressId = Integer.parseInt(request.getParameter("id"));

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        AddressDAO addressDAO = new AddressDAO();

        try {
            addressDAO.deleteAddress(addressId, user.getUserID());
            response.sendRedirect("listAddress");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("address_management.jsp?error=Unable to delete address");
        }
    }

    @Override
    public String getServletInfo() {
        return "Address Controller for managing user addresses";
    }
}
