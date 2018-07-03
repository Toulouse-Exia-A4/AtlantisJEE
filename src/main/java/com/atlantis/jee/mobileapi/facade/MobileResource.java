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
import com.atlantis.jee.providers.JMSProvider;
import com.atlantis.jee.providers.JWTProvider;
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
import java.util.Arrays;
import javax.ws.rs.Consumes;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


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
    private final JWTProvider jwtProvider;
    private final JMSProvider jmsProvider;
    
    private final String userDoesNotExistExceptionMessage = "User does not exist";
    
    private final Logger logger;
    
    public MobileResource() {
        userDataProvider = new UserDataProvider();
        rawMetricProvider = new RawMetricProvider();
        calculatedMetricDAO = new CalculatedMetricDAO();
        jwtProvider = new JWTProvider();
        jmsProvider = new JMSProvider();
        logger = Logger.getLogger(MobileResource.class.getName());
    }
    
    public MobileResource(IUserDataProvider userDataProvider, IRawMetricProvider rawMetricProvider, ICalculatedMetricDAO calculatedMetricDAO, JWTProvider jwtProvider) {
        this.userDataProvider = userDataProvider;
        this.rawMetricProvider = rawMetricProvider;
        this.calculatedMetricDAO = calculatedMetricDAO;
        this.jwtProvider = jwtProvider;
        this.jmsProvider = new JMSProvider();
        logger = Logger.getLogger(MobileResource.class.getName());
    }

    @Path("getUserDevices")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserDevices(@QueryParam("token") String token) {
        try {
            String userId = this.jwtProvider.getUserIdFromToken(token);
            User user = this.getUserFromUserDataWebService(userId);
            List<Device> devices = this.userDataProvider.findUserDevices(user);
            return Response.status(Status.OK).entity(devices)
                    .header("Access-Control-Allow-Origin", "*")
                    .build();
        } catch (Exception ex) {
            logger.log(Level.WARNING, ex.toString());
            logger.log(Level.WARNING, Arrays.toString(ex.getStackTrace()));
            if (ex.getMessage().equals(this.userDoesNotExistExceptionMessage))
                return Response.status(Status.NOT_FOUND).entity(this.userDoesNotExistExceptionMessage).header("Access-Control-Allow-Origin", "*").build();
            if (ex.getMessage().equals(this.jwtProvider.jwtTokenExpiredExceptionMessage))
                return Response.status(Status.UNAUTHORIZED).entity(this.jwtProvider.jwtTokenExpiredExceptionMessage).header("Access-Control-Allow-Origin", "*").build();
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex).build();
        }
    }
    
    @Path("getDeviceRawMetrics")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDeviceRawMetrics(@QueryParam("token") String token, @QueryParam("deviceId") String deviceId, @QueryParam("timestamp") Long timestamp) {
        try {
            String userId = this.jwtProvider.getUserIdFromToken(token);
            if (!this.checkUserHasDevice(userId, deviceId))
                return Response.status(Status.FORBIDDEN).entity("User has no right on device " + deviceId).build();
            List<RawMetric> rawMetrics = this.rawMetricProvider.getRawMetricFromDevice(deviceId, timestamp, 20);
            return Response.status(Status.OK).entity(rawMetrics)
                    .header("Access-Control-Allow-Origin", "*")
                    .build();
        } catch (Exception ex) {
            logger.log(Level.WARNING, ex.toString());
            logger.log(Level.WARNING, Arrays.toString(ex.getStackTrace()));
            if (ex.getMessage().equals(this.jwtProvider.jwtTokenExpiredExceptionMessage))
                return Response.status(Status.UNAUTHORIZED).entity(this.jwtProvider.jwtTokenExpiredExceptionMessage).header("Access-Control-Allow-Origin", "*").build();
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex).build();
        }
    }
    
    @Path("getDeviceCalcMetrics")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDeviceCalcMetrics(@QueryParam("token") String token, @QueryParam("deviceId") String deviceId, @QueryParam("timestamp") Long timestamp) {
        try {
            String userId = this.jwtProvider.getUserIdFromToken(token);
            if (!this.checkUserHasDevice(userId, deviceId))
                return Response.status(Status.FORBIDDEN).entity("User has no right on device " + deviceId).build();
            List<CalculatedMetric> calcMetrics = this.calculatedMetricDAO.findByDeviceId(deviceId);
            return Response.status(Status.OK).entity(calcMetrics)
                    .header("Access-Control-Allow-Origin", "*")
                    .build();
        } catch (Exception ex) {
            logger.log(Level.WARNING, ex.toString());
            logger.log(Level.WARNING, Arrays.toString(ex.getStackTrace()));
            if (ex.getMessage().equals(this.jwtProvider.jwtTokenExpiredExceptionMessage))
                return Response.status(Status.UNAUTHORIZED).entity(this.jwtProvider.jwtTokenExpiredExceptionMessage).header("Access-Control-Allow-Origin", "*").build();
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex).build();
        }
    }
    
    @Path("sendMessageToDevice")
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response sendMessageToDevice(String content) {
        try {
            JsonParser parser = new JsonParser();
            JsonObject jsobj = (JsonObject) parser.parse(content);
            String token = jsobj.get("token").toString();
            String deviceId = jsobj.get("deviceId").toString().replace("\"", "");
            String command = jsobj.get("command").toString().replace("\"", "");
            String userId = this.jwtProvider.getUserIdFromToken(token);
            if (!this.checkUserHasDevice(userId, deviceId))
                return Response.status(Status.FORBIDDEN).entity("User has no right on device " + deviceId).build();
            
            this.jmsProvider.sendMessage(content);
            
            Map responseBody = new HashMap();
            responseBody.put("message", "Your message will be send to and treated by your device shortly");
            return Response.status(Status.OK).entity(responseBody).header("Access-Control-Allow-Origin", "*").build();
        } catch (Exception ex) {
            logger.log(Level.WARNING, ex.toString());
            logger.log(Level.WARNING, Arrays.toString(ex.getStackTrace()));
            if (ex.getMessage() != null && ex.getMessage().equals(this.jwtProvider.jwtTokenExpiredExceptionMessage))
                return Response.status(Status.UNAUTHORIZED).entity(this.jwtProvider.jwtTokenExpiredExceptionMessage).header("Access-Control-Allow-Origin", "*").build();
            System.out.println(ex);
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex).build();
        }
    }
    
    @Path("getUser")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@QueryParam("token") String token) {
        try {
            String userId = this.jwtProvider.getUserIdFromToken(token);
            User user = this.getUserFromUserDataWebService(userId);
            return Response.status(Status.OK).entity(user).header("Access-Control-Allow-Origin", "*").build();
        } catch (Exception ex) {
            logger.log(Level.WARNING, ex.toString());
            for(StackTraceElement e : ex.getStackTrace()) 
            { 
                logger.log(Level.WARNING, e.toString()); 
            } 
            if (ex.getMessage() != null && ex.getMessage().equals(this.userDoesNotExistExceptionMessage))
                return Response.status(Status.NOT_FOUND).entity(this.userDoesNotExistExceptionMessage).header("Access-Control-Allow-Origin", "*").build();
            if (ex.getMessage() != null && ex.getMessage().equals(this.jwtProvider.jwtTokenExpiredExceptionMessage))
                return Response.status(Status.UNAUTHORIZED).entity(this.jwtProvider.jwtTokenExpiredExceptionMessage).header("Access-Control-Allow-Origin", "*").build();
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex).build();
        }
    }
    
    @Path("createUser")
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(String content) {
        try {
            JsonParser parser = new JsonParser();
            JsonObject jsobj = (JsonObject) parser.parse(content);
            String token = jsobj.get("token").toString();
            String firstname = jsobj.get("firstname").toString().replace("\"", "");
            String lastname = jsobj.get("lastname").toString().replace("\"", "");
            String userId = this.jwtProvider.getUserIdFromToken(token);
            User user = new User();
            user.setUserId(userId);
            user.setFirstname(firstname);
            user.setLastname(lastname);
            user = this.userDataProvider.createUser(user);
            return Response.status(Status.OK).entity(user).header("Access-Control-Allow-Origin", "*").build();
        } catch (Exception ex) {
            logger.log(Level.WARNING, ex.toString());
            logger.log(Level.WARNING, Arrays.toString(ex.getStackTrace()));
            if (ex.getMessage() != null && ex.getMessage().equals(this.jwtProvider.jwtTokenExpiredExceptionMessage))
                return Response.status(Status.UNAUTHORIZED).entity(this.jwtProvider.jwtTokenExpiredExceptionMessage).header("Access-Control-Allow-Origin", "*").build();         
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex).build();
        }
    }
    
    
    private Boolean checkUserHasDevice(String userId, String deviceId) throws Exception {
        try {
            User user = this.getUserFromUserDataWebService(userId);
            List<Device> devices = this.userDataProvider.findUserDevices(user);
            return devices.stream().anyMatch((device) -> (device.getDeviceId().equals(deviceId)));
        } catch (Exception ex) {
            throw ex;
        }
    }
    
    private User getUserFromUserDataWebService(String userId) throws Exception {
        try {
            User user = this.userDataProvider.findUser(userId);
            if (user == null) {
                throw new Exception(this.userDoesNotExistExceptionMessage);
            }
            return user;
        } catch (Exception ex) {
            throw ex;
        }
    }
}
