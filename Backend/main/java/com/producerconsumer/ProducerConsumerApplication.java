package com.producerconsumer;

import com.producerconsumer.model.Machine;
import com.producerconsumer.model.Product;
import com.producerconsumer.model.Queue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class ProducerConsumerApplication {

    public static void main(String[] args) throws InterruptedException {

        SpringApplication.run(ProducerConsumerApplication.class, args);

        Queue q0 = new Queue("q0");
        Queue q1 = new Queue("q1");
        Queue q3 = new Queue("q3");
        Queue q4 = new Queue("q4");
        Queue q5 = new Queue("q5");
        Queue q6 = new Queue("q6");
        Machine m1 = new Machine("m1");
        Machine m2 = new Machine("m2");
        Machine m3 = new Machine("m3");
        Machine m4 = new Machine("m4");
        Machine m5 = new Machine("m5");
        Machine m6 = new Machine("m6");
        Machine m7 = new Machine("m7");


        m1.addInputQueue(q0);
        m1.setOutput(q1);
        m2.addInputQueue(q1);
        m2.setOutput(q3);
        m3.addInputQueue(q1);
        m3.setOutput(q3);
        m5.addInputQueue(q3);
        m5.setOutput(q5);
        m4.addInputQueue(q0);
        m4.setOutput(q4);
        m6.addInputQueue(q4);
        m6.addInputQueue(q5);
        m6.setOutput(q6);
        m7.addInputQueue(q4);
        m7.addInputQueue(q5);
        m7.setOutput(q6);


        Product p1 = new Product("1");
        Product p2 = new Product("2");
        Product p3 = new Product("3");

        List<Product> products = List.of(p1, p2, p3);

        for (Product product : products) {
            q0.addProduct(product);
        }

        List<Queue> queues = List.of(q0, q1, q3, q4, q5, q6);
        for (Queue queue : queues) {
            Thread thread = new Thread(queue);
            thread.start();
        }
        // System.out.println("product 1: " + p1.getColor() + "\nproduct 2: " + p2.getColor() + "\nproduct 3: " + p3.getColor());

        while (true) {
//            System.out.println("m1 :" + m1.getColor() + "\nm2: " + m2.getColor() + "\nm3: " + m3.getColor());
//            System.out.println("====================================");
            if (q6.getProducts().size() == products.size()) {
                System.out.println("All products processed");
                System.exit(0);
            }
        }
    }

}
