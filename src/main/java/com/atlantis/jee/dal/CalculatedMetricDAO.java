/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atlantis.jee.dal;

import com.atlantis.jee.model.CalculatedMetric;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import org.bson.Document;

/**
 *
 * @author Methilliev
 */

@Stateless
public class CalculatedMetricDAO implements ICalculatedMetricDAO{
       private static final Logger LOGGER =
      Logger.getLogger(CalculatedMetricDAO.class.getName());
   private final static String HOST = "localhost";
   private final static int PORT = 27017;

   public final static String DATABASE = "JEE";
   public final static String COLLECTION = "CalculatedMetric";
   
   private MongoClient _mongoClient;

   public CalculatedMetricDAO(){
       
   }
   
   public CalculatedMetricDAO(MongoClient mongoclient){
       this._mongoClient = mongoclient;
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
/*
   public List<Candidate> find(String filter) {
      List<Candidate> list = new ArrayList<>();
      MongoClient mongoClient =
         new MongoClient(new ServerAddress(HOST, PORT));
      MongoCollection<Document> collection =
         mongoClient.getDatabase(DATABASE).getCollection(COLLECTION);
      FindIterable<Document> iter;
      if (filter == null || filter.trim().length() == 0) {
         iter = collection.find();
      } else {

         BsonRegularExpression bsonRegex = new
            BsonRegularExpression(filter);
         BsonDocument bsonDoc = new BsonDocument();
         bsonDoc.put("skillSet", bsonRegex);
         iter = collection.find(bsonDoc);

      }
      iter.forEach(new Block<Document>() {
         @Override
         public void apply(Document doc) {
            list.add(new Gson().fromJson(doc.toJson(), Candidate.class));
         }
      });
      return list;
   }*/

    @Override
    public void find(CalculatedMetric calculatedMetric) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
