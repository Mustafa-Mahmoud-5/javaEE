package org.example.models;

import org.example.errors.ApiException;

public class Product {
    private int id;
    private String name;
    private double price;
    private int quantity;


    // important for deserializing by jackson
    public Product() {

    }

    public Product(int id, String name, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }


    public double getPrice() { return price; }

    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }

    public void setQuantity(int quantity) { this.quantity = quantity; }

    public void validate() {
        if(name == null || name.isEmpty()) {
            throw new ApiException(400, "name must not be null");
        }

        if(price <= 0) {
            throw new ApiException(400, "Price must be greater than 0");
        }

        if(quantity <= 0) {
            throw new ApiException(400, "Quantity must be greater than 0");
        }
    }
}
