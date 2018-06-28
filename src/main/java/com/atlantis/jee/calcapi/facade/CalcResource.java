/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atlantis.jee.calcapi.facade;

import com.atlantis.jee.dal.ICalculatedMetricDAO;
import com.atlantis.jee.model.CalculatedMetricFactory;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author Methilliev
 */
@Path("calc")
public class CalcResource {

    @Context
    private UriInfo context;
    
    @EJB
    ICalculatedMetricDAO calc;

    /**
     * Creates a new instance of CalcResource
     */
    public CalcResource() {
    }

    /**
     * Retrieves representation of an instance of com.atlantis.jee.calcapi.facade.CalcResource
     * @return an instance of java.lang.String
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void getJson(String content) throws Exception {
        JsonParser parser = new JsonParser();
        JsonArray array = (JsonArray) parser.parse(content);
        for(JsonElement obj : array ){
            
            calc.create(CalculatedMetricFactory.fromJsonObject((JsonObject)obj));
        }
        
    }

    /**
     * PUT method for updating or creating an instance of CalcResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }
}
