# Producer/Consumer simulation project 
# Running process

---

The attached Pom.xml, Main.Js, vue.config.js, and index.html files serve the purpose of instantiating the required libraries for our project.
After Setting up the project using the Uploaded Source Code:

- It is advised to Modify the Pom.xml file to match the versions used on your machine.
- Add this dependency to Pom.xml file to activate web socket:

```xml
<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-websocket</artifactId>
</dependency>
```

- Install web socket packages to your VUE app

```bash
npm install stomp/stompjs
npm install sockjs-client
npm install primevue
npm install konva
```

# UML

---

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/1d8335ab-63af-492c-be05-7e4ce08a9bdb/86fbdba2-1060-427b-b873-fbe4f335441f/Untitled.svg)

# Design patterns

---

## Snapshot (Memento)

**usage**

It is used to restore state of an object to a previous state.

It uses three actor classes.
▪      Memento contains state of an object to be restored.
▪      Originator creates and stores states in Memento objects
▪      Caretaker object is responsible to restore object state from Memento.

**UML:**

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/1d8335ab-63af-492c-be05-7e4ce08a9bdb/25eb959a-879b-4d96-9784-0e5aaf7bf633/Untitled.png)

**How it’s used in producer-consumer app?**

The app involves a *replay* feature, thereby we need to save machine states (randomized colors) throughout the simulation as we would need them when we replay the simulation.

Memento class is constructed in **Product class** and product color is saved into a memento object, consequently the care taker state history keeps track with the saved memento where all states are saved into state history list.

**Consequences of using snapshot pattern?**

Capability of saving states in an encapsulated memento objects.

## Observer Design pattern

**usage**

defines a one-to-many dependency between objects so that when one object changes state, all its dependents are notified and updated automatically.

**How it’s used in producer-consumer app?**

- When a product is added to a machine (**`addProduct`** method), the machine is marked as "busy," and the input queues are notified accordingly.
- The product is then processed in a separate thread , simulating the processing time specified by **`serviceTime`**.
- After processing, the machine is marked as "free," and the product is added to the output queue.

## Producer Consumer Design pattern

**usage**

T used to solve the problem of coordinating the activities of two types of threads: producers(Queues) , which generate data, and consumers(Machines), which process that data

**How it’s used in producer-consumer app?**

- We used producer-consumer concurrency design pattern to
to synchronize functionalities of queues and machines.
- Products are moving to machines through queue and it waits till
machine permits them to be processed.
- Machines takes  products from queue when it is free to used

### producer(Queue)

```java
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
```

### Consumer(Machines)

```java
@Override
    public void run() {
        try {
            Thread.sleep(serviceTime * 1000L);
            //product processed, add it to next queue and free machine
            output.addProduct(currentProduct);
            notifyInputQueues("free");
            currentProduct = null;
            color = "#22d431";
            SimulationService.sendUpdate(id);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
```

---

# Snapshots of UI

---

![1000112778.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/1d8335ab-63af-492c-be05-7e4ce08a9bdb/99f05472-a29f-4c84-916e-b6a31fe8cad5/1000112778.png)

# **Producer-Consumer Simulation User Guide**

Welcome to the Producer-Consumer Simulation! This application simulates the flow of products through machines and queues in a manufacturing process. This user guide will help you understand the functionalities and how to interact with the simulation.

## **Table of Contents**

1. [Introduction](https://chat.openai.com/c/7cd9046e-92b4-4fbb-b94e-323ecac384a3#introduction)
2. [Getting Started](https://chat.openai.com/c/7cd9046e-92b4-4fbb-b94e-323ecac384a3#getting-started)
3. [Simulation Controls](https://chat.openai.com/c/7cd9046e-92b4-4fbb-b94e-323ecac384a3#simulation-controls)
    - [Start Simulation](https://chat.openai.com/c/7cd9046e-92b4-4fbb-b94e-323ecac384a3#start-simulation)
    - [Replay Simulation](https://chat.openai.com/c/7cd9046e-92b4-4fbb-b94e-323ecac384a3#replay-simulation)
4. [Understanding the Simulation](https://chat.openai.com/c/7cd9046e-92b4-4fbb-b94e-323ecac384a3#understanding-the-simulation)
    - [Machines](https://chat.openai.com/c/7cd9046e-92b4-4fbb-b94e-323ecac384a3#machines)
    - [Queues](https://chat.openai.com/c/7cd9046e-92b4-4fbb-b94e-323ecac384a3#queues)
5. [Troubleshooting](https://chat.openai.com/c/7cd9046e-92b4-4fbb-b94e-323ecac384a3#troubleshooting)
6. [Conclusion](https://chat.openai.com/c/7cd9046e-92b4-4fbb-b94e-323ecac384a3#conclusion)

## **1. Introduction**

The Producer-Consumer Simulation is designed to demonstrate a manufacturing process where products move through machines and queues. The application is built using Java and Spring Framework.

## **2. Getting Started**

To use the simulation, follow these steps:

- Ensure you have Java and Spring Framework installed.
- Clone the project repository to your local machine.
- Open the project in your preferred Java IDE.
- Run the application, and it will start a server on the specified port.

## **3. Simulation Controls**

### **Start Simulation**

To start the simulation:

- Make a POST request to **`/start`** endpoint with the following parameters:
    - **`machines`**: Number of machines in the simulation.
    - **`queues`**: Number of queues in the simulation.
    - **`arrows`**: List of machine-queue connections (e.g., **`["Q0,M1", "M2,Q4"]`**).
    - **`numberofProducts`**: Number of products to initiate the simulation.

### **Replay Simulation**

To replay the simulation:

- Make a POST request to the **`/replay`** endpoint.

## **4. Understanding the Simulation**

### **Machines**

- Machines are represented by the **`Machine`** class.
- Each machine has a unique ID, service time, and color.
- Service time is the time it takes for a machine to process a product.
- The color represents the current status of the machine (e.g., "busy" or "free").

### **Queues**

- Queues are represented by the **`Queue`** class.
- Each queue has a unique ID and a list of products.
- Products move through queues and machines during the simulation.
- Queues can have machines connected to them, influencing the flow of products.

## **5. Troubleshooting**

If you encounter any issues or unexpected behavior during the simulation, consider the following:

- Check the console output for any error messages or stack traces.
- Ensure that the required parameters are provided correctly when starting the simulation.
- If the simulation freezes or hangs, restart the application and try again.

## **6. Conclusion**

Congratulations! You have successfully explored the Producer-Consumer Simulation. If you have any questions or feedback, please refer to the project documentation or reach out to the application developer. Thank you for using our simulation
