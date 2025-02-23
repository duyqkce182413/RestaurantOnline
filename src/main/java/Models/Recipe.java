/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author admin
 */
public class Recipe {
    private int foodID;
    private int ingredientID;
    private int requiredQuantity;

    public Recipe() {
    }

    public Recipe(int foodID, int ingredientID, int requiredQuantity) {
        this.foodID = foodID;
        this.ingredientID = ingredientID;
        this.requiredQuantity = requiredQuantity;
    }

    public int getFoodID() {
        return foodID;
    }

    public void setFoodID(int foodID) {
        this.foodID = foodID;
    }

    public int getIngredientID() {
        return ingredientID;
    }

    public void setIngredientID(int ingredientID) {
        this.ingredientID = ingredientID;
    }

    public int getRequiredQuantity() {
        return requiredQuantity;
    }

    public void setRequiredQuantity(int requiredQuantity) {
        this.requiredQuantity = requiredQuantity;
    }

    @Override
    public String toString() {
        return "Recipe{" + "foodID=" + foodID + ", ingredientID=" + ingredientID + ", requiredQuantity=" + requiredQuantity + '}';
    }
    
    
}
