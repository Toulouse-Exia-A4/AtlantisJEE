/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atlantis.jee.providers;

import com.atlantis.jee.model.RawMetric;
import java.util.List;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Methilliev
 */
@Stateless
public class RawMetricProvider implements IRawMetricProvider {

    private String NetEndpoint="";
    
    @Override
    public List<RawMetric> getRawMetricFromDevice(String deviceId, int quantity) {
        JsonObjectBuilder requestBuilder = Json.createObjectBuilder();
        JsonObject requestObject = requestBuilder.add("deviceId",deviceId).add("quantity",quantity).build();
        
        Client client= ClientBuilder.newClient();
        WebTarget target = client.target(NetEndpoint+"");
        Response resp = target.request().post(Entity.entity(requestObject.toString(),MediaType.APPLICATION_JSON_TYPE));
        
        return null;
    }
    
}
