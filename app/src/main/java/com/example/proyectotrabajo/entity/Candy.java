package com.example.proyectotrabajo.entity;

public class Candy {

    private String name;
    private String description;
    private String price;
    private int quantity;

    public Candy(String name, String description, String price) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice(){
        return Double.parseDouble(price) * quantity;
    }
}
