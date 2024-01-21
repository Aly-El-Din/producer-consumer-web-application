package com.producerconsumer.model;

import com.producerconsumer.service.SimulationService;

import java.util.ArrayList;
import java.util.List;

public class Queue implements Runnable {
    private List<Product> products;
    private List<Machine> freeMachines;
    private String id;

    public Queue(String id) {
        this.id = id;
        this.freeMachines = new ArrayList<>();
        this.products = new ArrayList<>();
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<Product> getProducts() {
        return products;
    }

    public Product getProduct() {
        return products.remove(0);
    }

    public synchronized void addProduct(Product product) {
        products.add(product);
        SimulationService.sendUpdate(id);
    }

    public String getId() {
        return id;
    }

    public void addMachine(Machine machine) {
        freeMachines.add(machine);
    }

    public synchronized void removeMachine(Machine machine) {
        freeMachines.remove(machine);
    }

    public synchronized Machine getFreeMachine() {
        if (!freeMachines.isEmpty()) {
            return freeMachines.get(0);
        }
        return null;
    }
    @Override
    public void run() {
        while (true) {
            try {
                //wait for random input rate before checking for free machines
                Thread.sleep((int) (Math.random() * 2000) + 2000);
                Machine freeMachine = getFreeMachine();
                if(freeMachine != null && !products.isEmpty()){
                    Product product = getProduct();
                    SimulationService.sendUpdate(id);
                    freeMachine.addProduct(product);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
