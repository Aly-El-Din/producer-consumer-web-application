package com.producerconsumer.model;
public class Product {
    private String color;

    public Product() {
        String[] colors = {"red","blue","orange","purple","black","pink","#125D63","#7FFF00"};
        this.color = colors[(int)(Math.random()*colors.length)];
    }
    public String getColor() {
        return color;
    }
}