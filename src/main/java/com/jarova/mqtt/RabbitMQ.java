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
import org.surfthing.Service;

/**
 *
 * @author vsenger
 */
public class RabbitMQ extends Service {

    String USER_NAME = "";
    String PASSWORD = "";
    String VIRTUAL_HOST = "";
    String HOST_NAME = "";
    int PORT_NUMBER;
    public static String QUEUE = "";

    Channel channel;

    public void send(String msg) throws IOException {
        System.out.println("Sending Rabbit MQxxx");
        if(channel==null) {
            System.out.println("Restabelecendo conexao com RabbitMQ");
            fixConnection();
        }else{
            System.out.println("Conexao com RabbitMQ esta OK");
           // fixConnection();
        }
        
        channel.basicPublish("", QUEUE, null, msg.getBytes());
        System.out.println(" [x] Sent '" + msg + "'");
    }

    @Override
    public void start() {
        if (getConfig().getProperty("user.name") != null) {
            USER_NAME = getConfig().getProperty("user.name");
        }
        if (getConfig().getProperty("password") != null) {
            PASSWORD = getConfig().getProperty("password");
        }
        if (getConfig().getProperty("virtual.host") != null) {
            VIRTUAL_HOST = getConfig().getProperty("virtual.host");
        }
        if (getConfig().getProperty("host.name") != null) {
            HOST_NAME = getConfig().getProperty("host.name");
        }
        if (getConfig().getProperty("port.number") != null) {
            PORT_NUMBER = Integer.parseInt(getConfig().getProperty("port.number"));
        }
        if (getConfig().getProperty("queue") != null) {
            QUEUE = getConfig().getProperty("queue");
        }
        fixConnection();
    }

    public void fixConnection() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setUsername(USER_NAME);
            factory.setPassword(PASSWORD);
            factory.setHost(HOST_NAME);
            
            //adicionado para teste recover;
            factory.setAutomaticRecoveryEnabled(true);
            
            Connection connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(QUEUE, true, false, false, null);
            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        } catch (IOException ex) {
            Logger.getLogger(RabbitMQ.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TimeoutException ex) {
            Logger.getLogger(RabbitMQ.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void stop() {
        try {
            channel.close();
        } catch (IOException ex) {
            Logger.getLogger(RabbitMQ.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TimeoutException ex) {
            Logger.getLogger(RabbitMQ.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
    }
}
