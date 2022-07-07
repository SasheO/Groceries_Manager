package com.example.groceriesmanager.Models;

public class Ingredient {
    private String food;
    private String measure;
    private String quantity;

    public void setFood(String food){
        this.food = food;
    }
    public void setQuantity(String quantity){
        this.quantity = quantity;
    }
    public void setMeasure(String measure){
        this.measure = measure;
    }
    public String getFood(){
        return this.food;
    }
    public String getMeasure(){
        return this.measure;
    }
    public String getQuantity(){
        return this.quantity;
    }
}
