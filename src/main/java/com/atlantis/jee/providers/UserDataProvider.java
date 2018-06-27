/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atlantis.jee.providers;

import com.atlantis.jee.model.User;
import com.atlantis.jee.model.Device;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import javax.ejb.Stateless;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;

/**
 *
 * @author simon
 */
@Stateless
public class UserDataProvider implements IUserDataProvider {
    private final String baseUrl = "http://localhost:11080"; //tochange
    
    @Override
    public User findUser(String userId) throws Exception {
        String postUrl = this.baseUrl + "/user/" + userId;
        Gson gson = new Gson();
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet(postUrl);
        try {
            get.setHeader("Content-type", "application/json");
            HttpResponse  response = httpClient.execute(get);
            String resp_body = EntityUtils.toString(response.getEntity());
            JsonParser parser = new JsonParser();
            JsonObject jsobj = (JsonObject) parser.parse(resp_body);
            User user = new User();
            user.setId(jsobj.get("id").toString());
            user.setUserId(jsobj.get("userId").toString());
            return user;
        } catch( UnsupportedEncodingException ex) {
            throw ex;
        } catch(IOException ex) {
            throw ex;
        }
    }
    
    @Override
    public User createUser(User user) throws Exception {
        String postUrl = this.baseUrl + "/user";
        Gson gson = new Gson();
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(postUrl);
        try {
            StringEntity postingString = new StringEntity(gson.toJson(user));
            post.setEntity(postingString);
            post.setHeader("Content-type", "application/json");
            HttpResponse  response = httpClient.execute(post);
            String resp_body = EntityUtils.toString(response.getEntity());
            JsonParser parser = new JsonParser();
            JsonObject jsobj = (JsonObject) parser.parse(resp_body);
            user = new User();
            user.setId(jsobj.get("id").toString());
            user.setUserId(jsobj.get("userId").toString());
            return user;
        } catch( UnsupportedEncodingException ex) {
            throw ex;
        } catch(IOException ex) {
            throw ex;
        }
    }
    
    @Override
    public ArrayList<Device> findUserDevices(User user) throws Exception {
        String postUrl = this.baseUrl + "/user/" + user.getUserId() + "/devices";
        Gson gson = new Gson();
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet(postUrl);
        ArrayList<Device> devices = new ArrayList<>();
        try {
            get.setHeader("Content-type", "application/json");
            HttpResponse  response = httpClient.execute(get);
            String resp_body = EntityUtils.toString(response.getEntity());
            JsonParser parser = new JsonParser();
            JsonArray jsarr = (JsonArray) parser.parse(resp_body);
            for (int i = 0; i < jsarr.size(); i++) {
                Device device = new Device();
                JsonObject jsobj = (JsonObject) parser.parse(jsarr.get(i).toString());
                device.setDeviceId(jsobj.get("deviceId").toString());
                device.setType(jsobj.get("type").toString());
                device.setUnit(jsobj.get("unit").toString());
                devices.add(device);
            }
            return devices;
        } catch( UnsupportedEncodingException ex) {
            throw ex;
        } catch(IOException ex) {
            throw ex;
        }
    }
}
