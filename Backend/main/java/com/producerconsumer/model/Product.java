package com.producerconsumer.model;

import com.producerconsumer.service.CareTaker;

public class Product extends CareTaker {
    private String color;

    public Product() {
        String[] colors = {"red","blue","orange","purple","#2f6373","pink","grey","#c08926","#ffd966","#54494f"};
        this.color = colors[(int)(Math.random()*colors.length)];
    }
    public String getColor() {
        return color;
    }
    public void setSavedColor(String color){
        this.color=color;
    }
    public Memento takeSnapShot(){
        return new Memento(color);
    }
    public static class Memento {
        private final String color;

        private Memento(String color){this.color=color;}

        public String getProductColor(){return color;}
    }
}