package Models;

public class Recipe {

    private int foodID;
    private int ingredientID;
    private double requiredQuantity; // Thay từ int sang double

    public Recipe() {
    }

    public Recipe(int foodID, int ingredientID, double requiredQuantity) { // Thay int sang double
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

    public double getRequiredQuantity() { // Thay int sang double
        return requiredQuantity;
    }

    public void setRequiredQuantity(double requiredQuantity) { // Thay int sang double
        this.requiredQuantity = requiredQuantity;
    }

    @Override
    public String toString() {
        return "Recipe{" + "foodID=" + foodID + ", ingredientID=" + ingredientID + ", requiredQuantity=" + requiredQuantity + '}';
    }
}
