/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jarova.mqtt;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author azhang
 */
public class RabbitMQConnector {

    String userName = "azhang";
    String password = "Queens.NY1";
    String virtualHost = "192.168.0.170";
    String hostName = "192.168.0.170";
    int portNumber = 5672;
    public static String QUEUE = "javier/board1";

    private static RabbitMQConnector instance;
    Channel channel;

    public static RabbitMQConnector getInstance() {
        if (instance == null) {
            try {
                instance = new RabbitMQConnector();
            } catch (IOException ex) {
                Logger.getLogger(RabbitMQConnector.class.getName()).log(Level.SEVERE, null, ex);
            } catch (TimeoutException ex) {
                Logger.getLogger(RabbitMQConnector.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return instance;
    }

    public RabbitMQConnector() throws IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("azhang");
        factory.setPassword("Queens.NY1");
        factory.setHost(hostName);
        Connection connection = factory.newConnection();
        channel = connection.createChannel();
        channel.queueDeclare(QUEUE, false, false, false, null);

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
    }

    public void send(String msg) throws IOException {
        System.out.println("Sending Rabbit MQ");

        channel.basicPublish("", QUEUE, null, msg.getBytes());
        System.out.println(" [x] Sent '" + msg + "'");
    }

    public static void main(String[] args) throws IOException, TimeoutException {
        new RabbitMQConnector();
    }
}
