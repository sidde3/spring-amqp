package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;

import java.util.UUID;

@SpringBootApplication
@EnableJms
public class SpringAmqBrokerApplication implements CommandLineRunner {
    @Autowired
    private JmsTemplate jmsTemplate;

    public static void main(String[] args) {
        SpringApplication.run(SpringAmqBrokerApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        while (true) {
            sendMessage();
        }
    }

    public void sendMessage() {
		new Thread(() -> {
            String text = UUID.randomUUID().toString();
            System.out.println(String.format("Sending '%s'", text));
            this.jmsTemplate.convertAndSend("test-queue", text);
        }
		).start();

    }

    @JmsListener(destination = "test-queue")
    public void receiveMessage(String text) {
        System.err.println(String.format("Received '%s'", text));
    }
}
