package com.producerconsumer.service;

import com.producerconsumer.model.Machine;
import com.producerconsumer.model.Product;
import com.producerconsumer.model.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SimulationService {
    private int numberOfProducts;
    private static List<Machine> machines;
    private static List<Queue> queues;
    private static SimpMessagingTemplate template;

    // private static  List<Product>products;
    private static CareTaker careTaker;
    @Autowired
    public SimulationService(SimpMessagingTemplate template) {
        SimulationService.template = template;
        machines = new ArrayList<>();
        queues = new ArrayList<>();
        careTaker=new CareTaker();
        //products=new ArrayList<>();
    }

    public SimulationService() {
        machines = new ArrayList<>();
        queues = new ArrayList<>();
        careTaker=new CareTaker();
        //products=new ArrayList<>();
    }
    public void addMachines(int machinesCount) {
        machines.clear();
        for (int i = 1; i <= machinesCount; i++) {
            Machine machine = new Machine("M" + i);
            machines.add(machine);
        }
    }
    public void addQueues(int queuesCount) {
        queues.clear();
        for (int i = 0; i < queuesCount; i++) {
            Queue queue = new Queue("Q" + i);
            queues.add(queue);
        }
    }
    //params: [ "Q0,M1" , "M2,Q4" ]
    public void buildGraph(List<String> arrows) {
        for(String arrow : arrows) {

            String source = arrow.split(">")[0];
            String destination = arrow.split(">")[1];

            if(source.charAt(0) == 'Q'){
                Machine machine = machines.stream()
                        .filter(m -> m.getId().equals(destination)).findFirst().orElse(null);
                Queue queue = queues.stream()
                        .filter(q -> q.getId().equals(source)).findFirst().orElse(null);
                machine.addInputQueue(queue);
            }
            else{
                Machine machine = machines.stream()
                        .filter(m -> m.getId().equals(source)).findFirst().orElse(null);
                Queue queue = queues.stream()
                        .filter(q -> q.getId().equals(destination)).findFirst().orElse(null);
                machine.setOutput(queue);
            }
        }
    }
    public void startSimulation(int numberOfProducts) {
        if(!careTaker.getStateHistory().isEmpty()){
            careTaker.clearHisory();
        }
        Queue q0 = queues.stream().filter(q -> q.getId().equals("Q0")).findFirst().orElse(null);

        for (int i = 0; i < numberOfProducts; i++) {
            Product newProduct=new Product();
            q0.addProduct(newProduct);
            careTaker.captureSnapShots(newProduct.takeSnapShot());
        }

        for (Queue queue : queues) {
            Thread thread = new Thread(queue);
            thread.start();
        }
    }

    public void replay(){
        Queue q0 = queues.stream().filter(q -> q.getId().equals("Q0")).findFirst().orElse(null);
        Queue lastq = queues.stream().filter(q -> q.getId().equals("Q"+(queues.size()-1))).findFirst().orElse(null);
        lastq.getProducts().clear();
        sendUpdate("Q"+(queues.size()-1));
        List<Product.Memento>history=careTaker.getStateHistory();
        for (int i = 0; i <history.size(); i++) {
            Product newProduct=new Product();
            newProduct.setSavedColor(history.get(i).getProductColor());
            q0.addProduct(newProduct);
        }
        //careTaker.clearHisory();
        for (Queue queue : queues) {
            Thread thread = new Thread(queue);
            thread.start();
        }
    }

    public static void  sendUpdate(String id) {
        String updateMessage = id;

        if(id.charAt(0) == 'Q'){
            System.out.println(id);
            Queue queue = queues.stream().filter(q -> q.getId().equals(id)).findFirst().orElse(null);
            updateMessage += "," +  queue.getProducts().size();
            template.convertAndSend("/topic/updates", updateMessage);
        }
        else{
            Machine machine = machines.stream().filter(m -> m.getId().equals(id)).findFirst().orElse(null);
            updateMessage += "," + machine.getColor();
            template.convertAndSend("/topic/updates", updateMessage);
        }
        System.out.println("Update sent: " + updateMessage);
    }
}