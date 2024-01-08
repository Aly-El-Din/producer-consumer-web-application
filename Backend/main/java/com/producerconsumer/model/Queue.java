package com.producerconsumer.model;

import java.util.List;

public class Queue {

    private List<Product> products;
    private List<Machine> previousMachines;
    private List<Machine> freeMachines;

    public Product getProduct() {
        while(products.isEmpty()){
            try {
                wait();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return products.remove(0);
    }

    public synchronized void addProduct(Product product) {
        products.add(product);
        notify();
    }

    public void addMachine(Machine machine) {
        freeMachines.add(machine);
    }

    public void removeMachine(Machine machine) {
        freeMachines.remove(machine);
    }

    public synchronized Machine getFreeMachine() {
        if (!freeMachines.isEmpty()) {
            return freeMachines.get(0);
        }
        return null;
    }
}
