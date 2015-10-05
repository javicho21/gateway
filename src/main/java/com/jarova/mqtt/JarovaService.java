/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jarova.mqtt;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import org.surfthing.service.mqtt.MQTTController;

/**
 *
 * @author vsenger
 */
public class JarovaService extends MQTTController {
    public String LOG_QUEUE = MQTT_QUEUE + "/log";
    @Inject
    CSVPersistence persistence;
    
    
    @Override
    public void processMessage(String msg) {
        System.out.println("--->Write Message to TXT log " + msg);
        persistence.saveLog(msg);
        System.out.println("--->Log OK");

        System.out.println("--> Sending Message to Rabbit Queue " + msg);
        try {
            RabbitMQConnector.getInstance().send(msg);
        } catch (IOException ex) {
            Logger.getLogger(JarovaService.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("--> Rabbit OK");
        System.out.println("--> Sending Message to MongoB " + msg);
        try {
            MongoDB.save("sensors", msg, new Date(), "database1");
        } catch (UnknownHostException ex) {
            Logger.getLogger(JarovaService.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("--> MongoB OK");
    }

}
