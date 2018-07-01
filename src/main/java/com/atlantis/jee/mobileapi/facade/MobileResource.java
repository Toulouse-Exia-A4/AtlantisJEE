/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atlantis.jee.mobileapi.facade;

import com.atlantis.jee.dal.CalculatedMetricDAO;
import com.atlantis.jee.dal.ICalculatedMetricDAO;
import com.atlantis.jee.model.CalculatedMetric;
import com.atlantis.jee.model.Device;
import com.atlantis.jee.model.RawMetric;
import com.atlantis.jee.model.User;
import com.atlantis.jee.providers.IRawMetricProvider;
import com.atlantis.jee.providers.IUserDataProvider;
import com.atlantis.jee.providers.RawMetricProvider;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import com.atlantis.jee.providers.UserDataProvider;
import java.util.List;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import javax.ws.rs.QueryParam;
import org.apache.commons.codec.binary.Base64;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import java.io.StringReader;
import javax.json.Json;
import javax.ws.rs.Consumes;

/**
 * REST Web Service
 *
 * @author Methilliev
 */
@Path("mobile")
public class MobileResource {

    @Context
    private UriInfo context;
    
    private final IUserDataProvider userDataProvider;
    private final IRawMetricProvider rawMetricProvider;
    private final ICalculatedMetricDAO calculatedMetricDAO;
    
    private final String jwtTokenExpiredExceptionMessage = "JWT expired";
    
    public MobileResource() {
        userDataProvider = new UserDataProvider();
        rawMetricProvider = new RawMetricProvider();
        calculatedMetricDAO = new CalculatedMetricDAO();
    }
    
    public MobileResource(IUserDataProvider userDataProvider, IRawMetricProvider rawMetricProvider, ICalculatedMetricDAO calculatedMetricDAO) {
        this.userDataProvider = userDataProvider;
        this.rawMetricProvider = rawMetricProvider;
        this.calculatedMetricDAO = calculatedMetricDAO;
    }

    @Path("getUserDevices")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserDevices(@QueryParam("token") String token) {
        try {
            String userId = this.getUserIdFromToken(token);
            User user = this.getUser(userId);
            List<Device> devices = this.userDataProvider.findUserDevices(user);
            return Response.status(Status.OK).entity(devices)
                    .header("Access-Control-Allow-Origin", "*")
                    .build();
        } catch (Exception ex) {
            if (ex.getMessage().equals(this.jwtTokenExpiredExceptionMessage))
                return Response.status(Status.UNAUTHORIZED).entity(this.jwtTokenExpiredExceptionMessage).header("Access-Control-Allow-Origin", "*").build();
            //return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex).build();
            List<Device> devices = new ArrayList<>();
            devices.add(new Device("deviceId-1", "temp", "°C"));
            devices.add(new Device("deviceId-2", "presence", ""));
            return Response.status(Status.OK).entity(devices)
                    .header("Access-Control-Allow-Origin", "*")
                    .build();
        }
    }
    
    @Path("getDeviceRawMetrics")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDeviceRawMetrics(@QueryParam("token") String token, @QueryParam("deviceId") String deviceId, @QueryParam("timestamp") Long timestamp) {
        try {
            String userId = this.getUserIdFromToken(token);
            //if (!this.checkUserHasDevice(userId, deviceId))
            //    return Response.status(Status.FORBIDDEN).entity("User has no right on device " + deviceId).build();
            List<RawMetric> rawMetrics = this.rawMetricProvider.getRawMetricFromDevice(deviceId, timestamp, 20);
            return Response.status(Status.OK).entity(rawMetrics)
                    .header("Access-Control-Allow-Origin", "*")
                    .build();
        } catch (Exception ex) {
            if (ex.getMessage().equals(this.jwtTokenExpiredExceptionMessage))
                return Response.status(Status.UNAUTHORIZED).entity(this.jwtTokenExpiredExceptionMessage).header("Access-Control-Allow-Origin", "*").build();
            //return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex).build();
            List<RawMetric> rawMetrics = new ArrayList();
            for (int i = 0; i < 20; i++) {
                RawMetric rawMetric = new RawMetric();
                rawMetric.setDeviceId("deviceId-1");
                rawMetric.setValue("20");
                rawMetric.setDate(new Timestamp(System.currentTimeMillis()).getTime() - i * 10000);
                rawMetrics.add(rawMetric);
            }
            return Response.status(Status.OK).entity(rawMetrics)
                    .header("Access-Control-Allow-Origin", "*")
                    .build();
        }
    }
    
    @Path("getDeviceCalcMetrics")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDeviceCalcMetrics(@QueryParam("token") String token, @QueryParam("deviceId") String deviceId, @QueryParam("timestamp") Long timestamp) {
        try {
            String userId = this.getUserIdFromToken(token);
            //if (!this.checkUserHasDevice(userId, deviceId))
            //    return Response.status(Status.FORBIDDEN).entity("User has no right on device " + deviceId).build();
            List<CalculatedMetric> calcMetrics = this.calculatedMetricDAO.findByDeviceId(deviceId);
            return Response.status(Status.OK).entity(calcMetrics)
                    .header("Access-Control-Allow-Origin", "*")
                    .build();
        } catch (Exception ex) {
            if (ex.getMessage().equals(this.jwtTokenExpiredExceptionMessage))
                return Response.status(Status.UNAUTHORIZED).entity(this.jwtTokenExpiredExceptionMessage).header("Access-Control-Allow-Origin", "*").build();
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex).build();
        }
    }
    
    @Path("sendMessageToDevice")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response sendMessageToDevice(String content) {
        try {
            JsonParser parser = new JsonParser();
            JsonObject jsobj = (JsonObject) parser.parse(content);
            String token = jsobj.get("token").toString();
            String deviceId = jsobj.get("deviceId").toString();
            String command = jsobj.get("command").toString();
            String userId = this.getUserIdFromToken(token);
            //if (!this.checkUserHasDevice(userId, deviceId))
            //    return Response.status(Status.FORBIDDEN).entity("User has no right on device " + deviceId).build();
            
            //logic
            
            return Response.status(Status.OK).entity("Your message will be send to and treated by your device shortly").header("Access-Control-Allow-Origin", "*").build();
        } catch (Exception ex) {
            if (ex.getMessage() != null && ex.getMessage().equals(this.jwtTokenExpiredExceptionMessage))
                return Response.status(Status.UNAUTHORIZED).entity(this.jwtTokenExpiredExceptionMessage).header("Access-Control-Allow-Origin", "*").build();
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex).build();
        }
    }
    
    private Boolean checkUserHasDevice(String userId, String deviceId) throws Exception {
        try {
            User user = this.getUser(userId);
            List<Device> devices = this.userDataProvider.findUserDevices(user);
            return devices.stream().anyMatch((device) -> (device.getDeviceId().equals(deviceId)));
        } catch (Exception ex) {
            throw ex;
        }
    }
    
    private User getUser(String userId) throws Exception {
        try {
            User user = this.userDataProvider.findUser(userId);
            if (user == null) {
                user = this.userDataProvider.createUser(new User(userId));
            }
            return user;
        } catch (Exception ex) {
            throw ex;
        }
    }
    
    private String getUserIdFromToken(String token) throws Exception {
        JsonObject decodedJWT = this.decodeJWT(token);
        if (decodedJWT.get("exp").getAsLong() < System.currentTimeMillis() / 1000) {
            throw new Exception(this.jwtTokenExpiredExceptionMessage);
        }
        return decodedJWT.get("sub").toString();
    }
    
    private JsonObject decodeJWT(String token){
        String[] split_string = token.split("\\.");
        String base64EncodedBody = split_string[1];

        Base64 base64Url = new Base64(true);

        String body = new String(base64Url.decode(base64EncodedBody));
        JsonParser parser = new JsonParser();
        JsonObject jsobj = (JsonObject) parser.parse(body);
        return jsobj;
    }
}
