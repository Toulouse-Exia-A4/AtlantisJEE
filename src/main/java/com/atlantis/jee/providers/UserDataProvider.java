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
import com.google.gson.JsonNull;
import org.apache.http.HttpEntity;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Response;

/**
 *
 * @author simon
 */
@Stateless
public class UserDataProvider implements IUserDataProvider {
    private static String BASEURL;
    
    private HttpClient _httpClient;
    
    private Logger logger;
    
    public UserDataProvider() {
        loadConf();
    }
    
    public UserDataProvider(HttpClient httpClient) {
        this._httpClient = httpClient;
        loadConf();
    }
    
    private void loadConf() {
        Properties  configFile = new java.util.Properties();
	try {
	  configFile.load(this.getClass().getClassLoader().
	  getResourceAsStream("config.cfg"));
	}catch(Exception eta){
	    eta.printStackTrace();
	}
        BASEURL = configFile.getProperty("DOTNETBASEURL", "https://192.168.1.87") + 
                configFile.getProperty("USERDATAENDPOINT", ":30010/userdata");
        this.logger = Logger.getLogger(UserDataProvider.class.getName());
    }
    
    private HttpClient getHttpClient() {
        if (this._httpClient != null)
            return this._httpClient;
        else
            return HttpClientBuilder.create().build();
    }
    
    @Override
    public User findUser(String userId) throws Exception {
        String postUrl = this.BASEURL + "/users/" + userId;
        HttpClient httpClient = this.getHttpClient();
        HttpGet get = new HttpGet(postUrl);
        try {
            get.setHeader("Content-type", "application/json");
            HttpResponse response = httpClient.execute(get);
            HttpEntity entity = response.getEntity();
            if (entity == null)
                return null;
            String resp_body = EntityUtils.toString(entity);
            JsonParser parser = new JsonParser();
            if (parser.parse(resp_body) instanceof JsonNull) {
                return null;
            }
            JsonObject jsobj = (JsonObject) parser.parse(resp_body);
            User user = new User();
            user.setId(jsobj.get("id").toString());
            user.setUserId(jsobj.get("userId").toString());
            user.setFirstname(jsobj.get("firstname").toString());
            user.setLastname(jsobj.get("lastname").toString());
            return user;
        } catch( UnsupportedEncodingException ex) {
            throw ex;
        } catch(IOException ex) {
            throw ex;
        }
    }
    
    @Override
    public User createUser(User user) throws Exception {
        String postUrl = this.BASEURL + "/users/create";
        Gson gson = new Gson();
        HttpClient httpClient = this.getHttpClient();
        HttpPost post = new HttpPost(postUrl);
        try {
            StringEntity postingString = new StringEntity(gson.toJson(user));
            logger.log(Level.WARNING, "Requéte envoyée: "+ gson.toJson(user).toString());
            logger.log(Level.WARNING, "posting string: "+ postingString.toString());
            post.setEntity(postingString);
            post.setHeader("Content-type", "application/json");
            logger.log(Level.WARNING, "Httppost: "+ post.toString());
            HttpResponse  response = httpClient.execute(post);
            HttpEntity entity = response.getEntity();
            if (entity == null)
                return null;
            String resp_body = EntityUtils.toString(entity);
            JsonParser parser = new JsonParser();
            logger.log(Level.WARNING, "Body de la réponse: "+ resp_body);
            logger.log(Level.WARNING, "Body de la réponse parsé: "+  parser.parse(resp_body));
            JsonObject jsobj = (JsonObject) parser.parse(resp_body);
            jsobj = jsobj.getAsJsonObject("AddUserResult");
            user = new User();
            user.setUserId(jsobj.get("userId").toString());
            user.setFirstname(jsobj.get("firstname").toString());
            user.setLastname(jsobj.get("lastname").toString());
            return user;
        } catch( UnsupportedEncodingException ex) {
            throw ex;
        } catch(IOException ex) {
            throw ex;
        }
    }
    
    @Override
    public ArrayList<Device> findUserDevices(User user) throws Exception {
        String postUrl = this.BASEURL + "/users/" + user.getUserId() + "/devices";
        HttpClient httpClient = this.getHttpClient();
        HttpGet get = new HttpGet(postUrl);
        ArrayList<Device> devices = new ArrayList<>();
        try {
            get.setHeader("Content-type", "application/json");
            HttpResponse response = httpClient.execute(get);
            HttpEntity entity = response.getEntity();
            if (entity == null)
                return null;
            String resp_body = EntityUtils.toString(entity);
            JsonParser parser = new JsonParser();
            JsonArray jsarr = (JsonArray) parser.parse(resp_body);
            for (int i = 0; i < jsarr.size(); i++) {
                Device device = new Device();
                JsonObject jsobj = (JsonObject) parser.parse(jsarr.get(i).toString());
                device.setId(jsobj.get("id").toString());
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
