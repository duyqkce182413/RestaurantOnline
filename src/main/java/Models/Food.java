/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author admin
 */
public class Food {
    private int foodID;
    private String foodName;
    private double price;
    private int categoryID;
    private String description;
    private String image;
    private boolean available;

    public Food() {
    }

    public Food(int foodID, String foodName, double price, int categoryID, String description, String image, boolean available) {
        this.foodID = foodID;
        this.foodName = foodName;
        this.price = price;
        this.categoryID = categoryID;
        this.description = description;
        this.image = image;
        this.available = available;
    }

    public int getFoodID() {
        return foodID;
    }

    public void setFoodID(int foodID) {
        this.foodID = foodID;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return "Food{" + "foodID=" + foodID + ", foodName=" + foodName + ", price=" + price + ", categoryID=" + categoryID + ", description=" + description + ", image=" + image + ", available=" + available + '}';
    }
    
    
}
