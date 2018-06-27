/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atlantis.jee.providers;

import com.atlantis.jee.model.User;
import com.atlantis.jee.model.Device;
import java.util.ArrayList;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

/**
 *
 * @author simon
 */
@Rest
@Stateless
public class UserDataProvider implements IUserDataProvider {
    private final String baseUrl = "http://localhost:11080"; //tochange
    
    @Override
    public User findUser(String userId) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(this.baseUrl + "/user/" + userId);
        
        Response resp = target.request().get();
        
        MultivaluedMap<String, Object> responseBody = resp.getMetadata();
        
        User user = new User();
        user.setId(responseBody.getFirst("id").toString());
        user.setUserId(responseBody.getFirst("userId").toString());
        
        resp.close();
        client.close();
        return user;
    }
    
    @Override
    public User createUser(User user) {
        JsonObjectBuilder userBuilder = Json.createObjectBuilder();
        JsonObject userObject = userBuilder
                .add("userId", user.getUserId())
                .build();
        
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(this.baseUrl + "/user");
        
        Response resp = target.request().post(Entity.entity(userObject.toString(), MediaType.APPLICATION_JSON_TYPE));
        
        MultivaluedMap<String, Object> responseBody = resp.getMetadata();
        
        user = new User();
        user.setId(responseBody.getFirst("id").toString());
        user.setUserId(responseBody.getFirst("userId").toString());
        
        resp.close();
        client.close();
        return user;
    }
    
    @Override
    public ArrayList<Device> findUserDevices(User user) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(this.baseUrl + "/user/" + user.getUserId() + "/devices");
        
        JsonArray response = target.request(MediaType.APPLICATION_JSON).get(JsonArray.class);
        
        ArrayList<Device> devices = new ArrayList<>();
        
        for (int i = 0; i < response.size(); i++) {
            Device device = new Device();
            device.setDeviceId(response.getJsonObject(i).getString("deviceId"));
            device.setType(response.getJsonObject(i).getString("type"));
            device.setUnit(response.getJsonObject(i).getString("unit"));
            devices.add(device);
        }
        
        client.close();
        return devices;
    }
}
