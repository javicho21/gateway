/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jarova.mqtt;

/**
 *
 * @author azhang
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import java.net.UnknownHostException;
import java.util.Date;

/**
 *
 * @author vsenger
 */
public class MongoDB {

    public static String DB_NAME = "surfing";
    public static String DB_HOST = "192.168.0.180";
    public static int DB_PORT = 27017;
    static MongoClient mongoClient;
    static DB db;
    static DBCollection coll;
    static boolean initialized = false;
    public static void init() throws UnknownHostException {
        System.out.println("Init connection with MongoDB");
        mongoClient = new MongoClient(DB_HOST, DB_PORT);
        db = mongoClient.getDB(DB_NAME);
        initialized = true;
        System.out.println("MongoDB connection initialized");
    }

    public static String update(String collection) throws UnknownHostException {
        if(!initialized) init();
        DBCursor cursor = db.getCollection(collection).find();
        String lazyReturn = "<html><body>";
        try {
            while (cursor.hasNext()) {
                lazyReturn += "<p>" + cursor.next() + "</p>";
            }
        } finally {
            cursor.close();
        }
        lazyReturn += "</body></html>";
        return lazyReturn;
    }

    public static void save(String key, String value, Date timestamp, String collection) throws UnknownHostException {
        if(!initialized) init();
        System.out.println("MongoDB Saving Data " + value);
        BasicDBObject doc = new BasicDBObject(key, value)
                .append("timestamp", timestamp.toString());
        db.getCollection(collection).insert(doc);
    }


}
