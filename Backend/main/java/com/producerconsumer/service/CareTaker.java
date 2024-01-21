package com.producerconsumer.service;

import com.producerconsumer.model.Product;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class CareTaker {
    private List<Product.Memento> stateHistory;

    public CareTaker(){
        stateHistory=new ArrayList<>();
    }

    public void captureSnapShots(Product.Memento machineState){
        stateHistory.add(machineState);
    }
    public List<Product.Memento>getStateHistory(){
        return stateHistory;
    }
    public void clearHisory(){
        stateHistory.clear();
    }
}