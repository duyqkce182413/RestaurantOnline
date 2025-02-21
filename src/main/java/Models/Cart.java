/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author admin
 */
public class Cart {
    private int cartID;
    private int userID;
    private int foodID;
    private int quantity;

    public Cart() {
    }

    public Cart(int cartID, int userID, int foodID, int quantity) {
        this.cartID = cartID;
        this.userID = userID;
        this.foodID = foodID;
        this.quantity = quantity;
    }

    public int getCartID() {
        return cartID;
    }

    public void setCartID(int cartID) {
        this.cartID = cartID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getFoodID() {
        return foodID;
    }

    public void setFoodID(int foodID) {
        this.foodID = foodID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Cart{" + "cartID=" + cartID + ", userID=" + userID + ", foodID=" + foodID + ", quantity=" + quantity + '}';
    }
    
    
}
