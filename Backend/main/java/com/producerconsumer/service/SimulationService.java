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

    @Autowired
    public SimulationService(SimpMessagingTemplate template) {
        SimulationService.template = template;
        machines = new ArrayList<>();
        queues = new ArrayList<>();
    }

    public SimulationService() {
        machines = new ArrayList<>();
        queues = new ArrayList<>();
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
        Queue q0 = queues.stream().filter(q -> q.getId().equals("Q0")).findFirst().orElse(null);

        for (int i = 0; i < numberOfProducts; i++) {
            q0.addProduct(new Product());
        }
        for (Queue queue : queues) {
            Thread thread = new Thread(queue);
            thread.start();
        }
    }
    public static void  sendUpdate(String id) {
        String updateMessage = id;

        if(id.charAt(0) == 'Q'){
            Queue queue = queues.stream().filter(q -> q.getId().equals(id)).findFirst().orElse(null);
            updateMessage += "," + queue.getProducts().size();
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
