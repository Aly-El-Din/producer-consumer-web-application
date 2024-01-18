package com.producerconsumer.model;
public class Product {
    private String color;
    private String id;

    public Product(String id) {
        this.id = id;
        String[] colors = {"red","blue","orange","purple","black","pink"};
        this.color = colors[(int)(Math.random()*colors.length)];
    }
    public String getColor() {
        return color;
    }
}