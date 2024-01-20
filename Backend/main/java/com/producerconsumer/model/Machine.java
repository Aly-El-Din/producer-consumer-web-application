package com.producerconsumer.model;
import com.producerconsumer.service.SimulationService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Machine implements Runnable{
    private List<Queue> input;
    private Queue output;
    private Product currentProduct;
    private int serviceTime;
    private String id;
    private String color;

    public Machine(String id) {
        Random random = new Random();
        this.serviceTime = random.nextInt(5) + 4;
        this.id = id;
        this.color = "grey";  // default color
        this.input = new ArrayList<>();
    }

    public void setOutput(Queue output) {
        this.output = output;
    }
    public void addInputQueue(Queue q) {
        this.input.add(q);
        q.addMachine(this);
    }

    public String getColor() {
        return color;
    }

    public synchronized void addProduct(Product currentProduct) {
        this.currentProduct = currentProduct;
        notifyInputQueues("busy");
        color = currentProduct.getColor();
        SimulationService.sendUpdate(id);
        //start processing the product
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(serviceTime * 1000L);
            //product processed, add it to next queue and free machine
            System.out.println("Machine " + id + " is processing product " + currentProduct.getColor() + " for " + serviceTime + " seconds");
            output.addProduct(currentProduct);
            notifyInputQueues("free");
            currentProduct = null;
            color = "grey";
            SimulationService.sendUpdate(id);
            System.out.println("Machine " + id + " is free and the output product is " + output.getProducts().get(output.getProducts().size()-1).getColor() +" sent to output queue " + output.getId());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //notify all input queues the state of the machine
    private void notifyInputQueues(String status) {
        if(status.equals("free")){
            for (Queue queue : input) {
                queue.addMachine(this);
            }
        }
        else if(status.equals("busy")){
            for (Queue queue : input) {
                queue.removeMachine(this);
            }
        }
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Machine machine = (Machine) o;
        return Objects.equals(id, machine.id);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}