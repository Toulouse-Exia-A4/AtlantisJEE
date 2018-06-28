/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atlantis.jee.dal;

import com.atlantis.jee.model.CalculatedMetric;
import com.google.gson.Gson;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import org.bson.BsonDocument;
import org.bson.BsonRegularExpression;
import org.bson.Document;

/**
 *
 * @author Methilliev
 */

@Stateless
@LocalBean
public class CalculatedMetricDAO implements ICalculatedMetricDAO{
       private static final Logger LOGGER =
      Logger.getLogger(CalculatedMetricDAO.class.getName());
   private static String HOST;
   private static int PORT;

   public static String DATABASE ;
   public static String COLLECTION ;
   
   private MongoClient _mongoClient;

   public CalculatedMetricDAO(){
       loadConf();
   }

   
   public CalculatedMetricDAO(MongoClient mongoclient){
       this._mongoClient = mongoclient;
       loadConf();
   }
   
   private void loadConf(){
        Properties  configFile = new java.util.Properties();
	try {
	  configFile.load(this.getClass().getClassLoader().
	  getResourceAsStream("config.cfg"));
	}catch(Exception eta){
	    eta.printStackTrace();
	}
        HOST=configFile.getProperty("HOST", "localhost");
        PORT= Integer.parseInt(configFile.getProperty("PORT",  "27017"));
        DATABASE=configFile.getProperty("DATABASE", "JEE");
        COLLECTION=configFile.getProperty("COLLECTION", "CalculatedMetric");
   
   }

   public MongoClient getMongoClient(){
       if(_mongoClient!=null)
           return _mongoClient;
       _mongoClient=new MongoClient(new ServerAddress(HOST, PORT));
       return _mongoClient;
   }
   
   
    @Override
   public void create(CalculatedMetric calculatedMetric) throws Exception {
       
      MongoClient mongoClient = getMongoClient();
    MongoCollection<Document> collection =
         mongoClient.getDatabase(DATABASE).getCollection(COLLECTION);
      if  (calculatedMetric!=null) {
          
          if(calculatedMetric.getDeviceId() == null || calculatedMetric.getDeviceId().equals(""))
              throw new Exception("Missing DeviceID");
          
          if(calculatedMetric.getDateTimeEnd() == null )
              throw new Exception("Missing DateTimeEnd");
          
          if(calculatedMetric.getDateTimeStart() == null )
              throw new Exception("Missing DateTimeStart");
          
          if(calculatedMetric.getDataType()== null || calculatedMetric.getDataType().equals(""))
              throw new Exception("Missing DataType");

         Document d = new Document().append("deviceId", calculatedMetric.getDeviceId())
            .append("dateTimeStart", calculatedMetric.getDateTimeStart())
            .append("dateTimeEnd", calculatedMetric.getDateTimeEnd())
            .append("value", calculatedMetric.getValue())
            .append("type", calculatedMetric.getDataType());
         collection.insertOne(d);
      }


   }

    @Override
   public void update(CalculatedMetric calculatedMetric) throws Exception {
      MongoClient mongoClient = getMongoClient();
      MongoCollection<Document> collection =
         mongoClient.getDatabase(DATABASE).getCollection(COLLECTION);
      Document d = new Document();
      
      if(calculatedMetric.getDeviceId() == null || calculatedMetric.getDeviceId().equals(""))
              throw new Exception("Missing DeviceID");
          
          if(calculatedMetric.getDateTimeEnd() == null )
              throw new Exception("Missing DateTimeEnd");
          
          if(calculatedMetric.getDateTimeStart() == null )
              throw new Exception("Missing DateTimeStart");
          
          if(calculatedMetric.getDataType()== null || calculatedMetric.getDataType().equals(""))
              throw new Exception("Missing DataType");
          
      d.append("deviceId", calculatedMetric.getDeviceId())
            .append("dateTimeStart", calculatedMetric.getDateTimeStart())
            .append("dateTimeEnd", calculatedMetric.getDateTimeEnd())
            .append("value", calculatedMetric.getValue())
            .append("type", calculatedMetric.getDataType());
      collection.updateOne(new Document("id", calculatedMetric.getId()),
         new Document("$set", d));
   }

    @Override
   public void delete(CalculatedMetric c) throws Exception {
      MongoClient mongoClient = getMongoClient();
      
      if(c.getId()==null || c.getId().equals(""))
          throw new Exception("Missing ID");
      
      MongoCollection<Document> collection =
         mongoClient.getDatabase(DATABASE).getCollection(COLLECTION);
      collection.deleteOne(new Document("id", c.getId()));
   }

   @Override
   public List<CalculatedMetric> findByDeviceId(String id) throws Exception {
      List<CalculatedMetric> list = new ArrayList<>();
      MongoClient mongoClient = getMongoClient();
      MongoCollection<Document> collection =
         mongoClient.getDatabase(DATABASE).getCollection(COLLECTION);
      FindIterable<Document> iter;
      if (id == null || id.trim().length() == 0) {
         throw new Exception("No Id specified");
      } else {

         BsonRegularExpression bsonRegex = new
            BsonRegularExpression(id);
         BsonDocument bsonDoc = new BsonDocument();
         bsonDoc.put("DeviceId", bsonRegex);
         iter = collection.find(bsonDoc);

      }
      iter.forEach(new Block<Document>() {
         @Override
         public void apply(Document doc) {
            list.add(new Gson().fromJson(doc.toJson(), CalculatedMetric.class));
         }
      });
      return list;
   }
   


}
