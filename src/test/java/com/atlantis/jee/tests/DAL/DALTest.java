/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.atlantis.jee.tests.DAL;

import com.atlantis.jee.model.CalculatedMetric;
import com.atlantis.jee.dal.CalculatedMetricDAO;
import com.atlantis.jee.dal.ICalculatedMetricDAO;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.Document;

import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import org.mockito.runners.MockitoJUnitRunner;

/**
 *
 * @author Methilliev
 */
@RunWith(MockitoJUnitRunner.class)
public class DALTest {
    
   
    ICalculatedMetricDAO calculatedMetric;
    
    private MongoCollection<Document> mongoCollectionMock;
    private MongoDatabase mongoDatabaseMock;
    private MongoClient mongoClientMock;
    
    @Before
    public void setup(){
        mongoClientMock=Mockito.mock(MongoClient.class);
        mongoDatabaseMock=Mockito.mock(MongoDatabase.class);
        mongoCollectionMock=Mockito.mock(MongoCollection.class);
        Mockito.when(mongoClientMock.getDatabase(Mockito.anyString())).thenReturn(mongoDatabaseMock);
        Mockito.when(mongoDatabaseMock.getCollection(Mockito.anyString())).thenReturn(mongoCollectionMock);
        Mockito.doNothing().when(mongoCollectionMock).insertOne(Mockito.any(Document.class));
        //Mockito.when(mongoCollectionMock).updateOne(Mockito.any(Document.class),Mockito.any(Document.class));
        this.calculatedMetric=new CalculatedMetricDAO(mongoClientMock);
        
        
    }
    
    @Test
    public void GivenCalculatedMetricWhenCreatingMetricShouldReturnNothing() throws Exception{
        CalculatedMetric metric = new CalculatedMetric( "test", new Date(), new Date(), 1.2, "average");
        calculatedMetric.create(metric);
        Mockito.verify(mongoCollectionMock, times(1)).insertOne(Mockito.any(Document.class));
    }
    
    @Test
    public void GivenCalculatedMetricWithoutDeviceIdWhenCreatingMetricShouldRaiseError(){
        CalculatedMetric metric = new CalculatedMetric( "", new Date(), new Date(), 1.2, "average");
        try{
            calculatedMetric.create(metric);
            fail();
        } catch (Exception e){
            assert(e.getMessage().equals("Missing DeviceID"));
            Mockito.verify(mongoCollectionMock, times(0)).insertOne(Mockito.any(Document.class));
        }
    }
    
    @Test
    public void GivenCalculatedMetricWithoutDataTypeWhenCreatingMetricShouldRaiseError(){
        CalculatedMetric metric = new CalculatedMetric( "DeviceId", new Date(), new Date(), 1.2, "");
        try{
            calculatedMetric.create(metric);
            fail();
        } catch (Exception e){
            assert(e.getMessage().equals("Missing DataType"));
            Mockito.verify(mongoCollectionMock, times(0)).insertOne(Mockito.any(Document.class));
        }
    }
    
    @Test
    public void GivenCalculatedMetricWithoutDateWhenCreatingMetricShouldRaiseError(){
        CalculatedMetric metric = new CalculatedMetric( "DeviceId", null, new Date(), 1.2, "average");
        try{
            calculatedMetric.create(metric);
        } catch (Exception e){
            assert(e.getMessage().equals("Missing DateTimeStart"));
            Mockito.verify(mongoCollectionMock, times(0)).insertOne(Mockito.any(Document.class));
        }
        
        metric= new CalculatedMetric( "DeviceId",  new Date(),null, 1.2, "average");
        try{
            calculatedMetric.create(metric);
            fail();
        } catch (Exception e){
            assert(e.getMessage().equals("Missing DateTimeEnd"));
            Mockito.verify(mongoCollectionMock, times(0)).insertOne(Mockito.any(Document.class));
        }
    }
    
        @Test
    public void GivenCalculatedMetricWhenUpdatingMetricShouldReturnNothing() throws Exception{
        CalculatedMetric metric = new CalculatedMetric( "test", new Date(), new Date(), 1.2, "average");
        calculatedMetric.update(metric);
        Mockito.verify(mongoCollectionMock, times(1)).updateOne(Mockito.any(Document.class),Mockito.any(Document.class));
    }
    
    @Test
    public void GivenCalculatedMetricWithoutDeviceIdWhenUpdatingMetricShouldRaiseError(){
        CalculatedMetric metric = new CalculatedMetric( "", new Date(), new Date(), 1.2, "average");
        try{
            calculatedMetric.update(metric);
            fail();
        } catch (Exception e){
            assert(e.getMessage().equals("Missing DeviceID"));
            Mockito.verify(mongoCollectionMock, times(0)).updateOne(Mockito.any(Document.class),Mockito.any(Document.class));
        }
    }
    
    @Test
    public void GivenCalculatedMetricWithoutDataTypeWhenUpdatingMetricShouldRaiseError(){
        CalculatedMetric metric = new CalculatedMetric( "DeviceId", new Date(), new Date(), 1.2, "");
        try{
            calculatedMetric.update(metric);
            fail();
        } catch (Exception e){
            assert(e.getMessage().equals("Missing DataType"));
            Mockito.verify(mongoCollectionMock, times(0)).updateOne(Mockito.any(Document.class),Mockito.any(Document.class));
        }
    }
    
    @Test
    public void GivenCalculatedMetricWithoutDateWhenUpdatingMetricShouldRaiseError(){
        CalculatedMetric metric = new CalculatedMetric( "DeviceId", null, new Date(), 1.2, "average");
        try{
            calculatedMetric.update(metric);
            fail();
        } catch (Exception e){
            assert(e.getMessage().equals("Missing DateTimeStart"));
            Mockito.verify(mongoCollectionMock, times(0)).updateOne(Mockito.any(Document.class),Mockito.any(Document.class));
        }
        
        metric= new CalculatedMetric( "DeviceId",  new Date(),null, 1.2, "average");
        try{
            calculatedMetric.update(metric);
            fail();
        } catch (Exception e){
            assert(e.getMessage().equals("Missing DateTimeEnd"));
            Mockito.verify(mongoCollectionMock, times(0)).updateOne(Mockito.any(Document.class),Mockito.any(Document.class));
        }
    }
    
    @Test
    public void GivenCalculatedMetricWhenDeletingMetricShouldReturnNothing() throws Exception{
        CalculatedMetric metric = new CalculatedMetric( "DeviceId", null, new Date(), 1.2, "average");
        metric.setId("15");
        calculatedMetric.delete(metric);
        Mockito.verify(mongoCollectionMock, times(1)).deleteOne(Mockito.any(Document.class));
    }
    
     @Test
    public void GivenCalculatedMetricWithoutIdWhenDeletingMetricShouldReturnError() {
        CalculatedMetric metric = new CalculatedMetric( "DeviceId", null, new Date(), 1.2, "average");
        metric.setId("");
        try{
            calculatedMetric.delete(metric);
            fail();
        } catch (Exception e){
            assert(e.getMessage().equals("Missing ID"));
            Mockito.verify(mongoCollectionMock, times(0)).deleteOne(Mockito.any(Document.class));
        }
    }
    
    @Test
    public void GivenMetricIdWhenFindindMetricShouldReturnMetric(){
        CalculatedMetric metric= new CalculatedMetric();
    }
    
   
    
}
