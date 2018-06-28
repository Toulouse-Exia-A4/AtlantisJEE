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
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import com.atlantis.jee.providers.UserDataProvider;
import java.util.List;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

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

    /**
     * Retrieves representation of an instance of com.atlantis.jee.mobileapi.facade.MobileResource
     * @return an instance of java.lang.String
     */
    @Path("getUserDevices")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserDevices(@PathParam("userId") String userId) {
        try {
            User user = this.getUser(userId);
            List<Device> devices = this.userDataProvider.findUserDevices(user);
            return Response.status(Status.OK).entity(devices)
                    .header("Access-Control-Allow-Origin", "*")
                    .build();
        } catch (Exception ex) {
            //return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex).build();
            List<Device> devices = new ArrayList<Device>();
            devices.add(new Device("deviceId-1", "temp", "°C"));
            devices.add(new Device("deviceId-2", "presence", ""));
            return Response.status(Status.OK).entity(devices)
                    .header("Access-Control-Allow-Origin", "*")
                    .build();
        }
    }
    
    @Path("getDeviceMetrics")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDeviceMetrics(@PathParam("deviceId") String deviceId) {
        try {
            List<RawMetric> rawMetrics = this.rawMetricProvider.getRawMetricFromDevice(deviceId, new Timestamp(System.currentTimeMillis()).getTime(), 20);
            List<CalculatedMetric> calcMetrics = this.calculatedMetricDAO.findByDeviceId(deviceId);
            HashMap<String, Object> response = new HashMap();
            response.put("rawMetrics", rawMetrics);
            response.put("calcMetrics", calcMetrics);
            return Response.status(Status.OK).entity(response)
                    .header("Access-Control-Allow-Origin", "*")
                    .build();
        } catch (Exception ex) {
            //return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex).build();
            
            RawMetric rawMetric1 = new RawMetric();
            rawMetric1.setDeviceId("deviceId-1");
            rawMetric1.setValue("20");
            rawMetric1.setDate(new Timestamp(System.currentTimeMillis()).getTime()-100);
            RawMetric rawMetric2 = new RawMetric();
            rawMetric2.setDeviceId("deviceId-1");
            rawMetric2.setValue("20");
            rawMetric2.setDate(new Timestamp(System.currentTimeMillis()).getTime());
            List<RawMetric> rawMetrics = new ArrayList();
            rawMetrics.add(rawMetric1);
            rawMetrics.add(rawMetric2);
            
            CalculatedMetric calcMetric1 = new CalculatedMetric("deviceId-1", new Date(), new Date(), 20, "moy");
            CalculatedMetric calcMetric2 = new CalculatedMetric("deviceId-1", new Date(), new Date(), 20, "med");
            List<CalculatedMetric> calcMetrics = new ArrayList();
            calcMetrics.add(calcMetric1);
            calcMetrics.add(calcMetric2);
            
            HashMap<String, Object> response = new HashMap();
            response.put("rawMetrics", rawMetrics);
            response.put("calcMetrics", calcMetrics);
            return Response.status(Status.OK).entity(response)
                    .header("Access-Control-Allow-Origin", "*")
                    .build();
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
}
