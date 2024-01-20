package com.producerconsumer.controller;

import com.producerconsumer.service.SimulationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class SimulationController {
    private SimulationService simulationService;

    public SimulationController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @PostMapping("/start")
    public void startSimulation(@RequestParam int machines, @RequestParam int queues,
                                @RequestParam ArrayList<String> arrows, @RequestParam int numberofProducts ) {

        simulationService.addMachines(machines);
        simulationService.addQueues(queues);
        simulationService.buildGraph(arrows);
        simulationService.startSimulation(numberofProducts);
    }

}
