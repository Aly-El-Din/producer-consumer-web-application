package com.producerconsumer.model;
public class Product {
    private String color;

    public Product() {
        String[] colors = {"red","blue","orange","purple","#2f6373","pink","grey","#c08926","#ffd966","#54494f"};
        this.color = colors[(int)(Math.random()*colors.length)];
    }
    public String getColor() {
        return color;
    }
}
