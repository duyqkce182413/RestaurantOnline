/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author admin
 */
public class CartItem {
    private Cart cart;
    private Food food;

    public CartItem() {
    }

    public CartItem(Cart cart, Food food) {
        this.cart = cart;
        this.food = food;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    @Override
    public String toString() {
        return "CartItem{" + "cart=" + cart + ", food=" + food + '}';
    }
}
