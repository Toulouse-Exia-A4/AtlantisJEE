package com.atlantis.jee.tests.mobileapi;

import com.atlantis.jee.dal.ICalculatedMetricDAO;
import com.atlantis.jee.dal.CalculatedMetricDAO;
import com.atlantis.jee.mobileapi.facade.MobileResource;
import com.atlantis.jee.model.CalculatedMetric;
import com.atlantis.jee.model.Device;
import com.atlantis.jee.model.RawMetric;
import com.atlantis.jee.model.User;
import com.atlantis.jee.providers.IRawMetricProvider;
import com.atlantis.jee.providers.RawMetricProvider;
import com.atlantis.jee.providers.IUserDataProvider;
import com.atlantis.jee.providers.UserDataProvider;
import com.atlantis.jee.providers.JWTProvider;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author simon
 */
@RunWith(MockitoJUnitRunner.class)
public class MobileResourceTest {
    
    MobileResource mobileResource;
    
    private IUserDataProvider userDataProviderMock;
    private IRawMetricProvider rawMetricProviderMock;
    private ICalculatedMetricDAO calculatedMetricDAOMock;
    private JWTProvider jwtProviderMock;
    
    private final String tokenMock = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImtpZCI6Ilg1ZVhrNHh5b2pORnVtMWtsMll0djhkbE5QNC1jNTdkTzZRR1RWQndhTmsifQ" + 
            ".eyJpc3MiOiJodHRwczovL2xvZ2luLmVsaW90YnlsZWdyYW5kLmNvbS8wZDg4MTZkNS0zZTdmLTRjODYtODIyOS02NDUxMzdlMGYyMjIvdjIuMC8iLCJleHAiOjE1MzA" + 
            "1MjQ0MDEsIm5iZiI6MTUzMDUyMDgwMSwiYXVkIjoiY2QyNDYzNGUtYmUyNy00MDU5LThmMDQtMzBmZWI0YjcyYjcxIiwib2lkIjoiYTMxMTY0NTMtYWZkNC00ZGQ1LWJ" +
            "mYzctOTgzZDA0OGFkMDk4Iiwic3ViIjoiYTMxMTY0NTMtYWZkNC00ZGQ1LWJmYzctOTgzZDA0OGFkMDk4IiwidGZwIjoiQjJDXzFfVGhpcmRBcHAtQWNjb3VudExpbmt" +
            "pbmciLCJub25jZSI6IjYzNjY1NTk1MTQxNTM1OTU0MS5NbU5tT0RFNU9EY3RNR1V6WlMwME9EaGtMVGhpTkRrdE1qWTFOR1JrWTJZM1pXRXlPR013WkRNMFpEY3ROell" + 
            "4TkMwME5tVXpMVGd6TmpNdE5qSmtZell6Wm1NMU1Ea3giLCJzY3AiOiJ0b3BvbG9neS5yZWFkIiwiYXpwIjoiOTk5OWQ4MTYtZDUzOS00NjNhLWFmYmQtN2ZjNjYzZGU" +
            "xYzhhIiwidmVyIjoiMS4wIiwiaWF0IjoxNTMwNTIwODAxfQ.Oop4bBvz0g4Eakn_-y8T5dAJaG9esd9E6N_0J7zjgEdCWSJf05UfXIOetWxRQN0fNiCl6bkVxOpflecC" +
            "DoeAyH9U9HyCXTrG1VB9u1x7YuFfjU3j2UesQEPpHCzHMVV8XRX2_tQxJBGibyWtSOYkDRMbsd5BpCJJDCdZ2gXzjPy1qHaFnaUvzbEbH6e1Y3EIVExqUrLwO4jzUHzo" +
            "OC6AsIvxxic_MnQa1UP7Qukb5sA_aQezT2feV0SZv-QoijXaLHfr_Mj8aVhlQghQdqJjAzWCID-Gugla4R6beM3Hulj4XqDZb8JsBEinC3qS9Rt5Ibm81aDi4S2OPaohsGmjgA";
    private User userMock;
    private Device deviceMock;
    private ArrayList<Device> devicesMock;
    private ArrayList<RawMetric> rawMetricsMock;
    private List<CalculatedMetric> calcMetricsMock;
    
    @Before
    public void Setup() throws Exception {
        userDataProviderMock = Mockito.mock(UserDataProvider.class);
        rawMetricProviderMock = Mockito.mock(RawMetricProvider.class);
        calculatedMetricDAOMock = Mockito.mock(CalculatedMetricDAO.class);
        jwtProviderMock = Mockito.mock(JWTProvider.class);
        
        userMock = new User("userId", "Simon", "Todeschini");
        deviceMock = new Device("deviceId-1", "temp", "°C");
        devicesMock = new ArrayList();
        devicesMock.add(deviceMock);
        devicesMock.add(new Device("deviceId-2", "temp", "°C"));
        RawMetric rawMetric1 = new RawMetric();
        rawMetric1.setDeviceId("deviceId-1");
        rawMetric1.setValue("20");
        rawMetric1.setDate(new Timestamp(System.currentTimeMillis()).getTime()-100);
        RawMetric rawMetric2 = new RawMetric();
        rawMetric2.setDeviceId("deviceId-1");
        rawMetric2.setValue("20");
        rawMetric2.setDate(new Timestamp(System.currentTimeMillis()).getTime());
        rawMetricsMock = new ArrayList();
        rawMetricsMock.add(rawMetric1);
        rawMetricsMock.add(rawMetric2);
        
        CalculatedMetric calcMetric1 = new CalculatedMetric("deviceId-1", new Date(), new Date(), 20, "moy");
        CalculatedMetric calcMetric2 = new CalculatedMetric("deviceId-1", new Date(), new Date(), 20, "med");
        calcMetricsMock = new ArrayList();
        calcMetricsMock.add(calcMetric1);
        calcMetricsMock.add(calcMetric2);
        
        Mockito.when(userDataProviderMock.findUser(Mockito.any(String.class))).thenReturn(userMock);
        Mockito.when(userDataProviderMock.createUser(Mockito.any(User.class))).thenReturn(userMock);
        Mockito.when(rawMetricProviderMock.getRawMetricFromDevice(Mockito.any(String.class), Mockito.any(Long.class), Mockito.any(int.class))).thenReturn(rawMetricsMock);
        Mockito.when(calculatedMetricDAOMock.findByDeviceId(Mockito.any(String.class))).thenReturn(calcMetricsMock);
        Mockito.when(jwtProviderMock.checkTokenIsExpired(Mockito.any(String.class))).thenReturn(true);
                
        mobileResource = new MobileResource(userDataProviderMock, rawMetricProviderMock, calculatedMetricDAOMock, jwtProviderMock);
    }
    
    //@Test
    public void GivenMobileResourceWhenGettingRawMetricsShouldReturnResponse() {
        Response response = this.mobileResource.getDeviceRawMetrics(this.tokenMock, this.deviceMock.getDeviceId(), System.currentTimeMillis());
        assert(response.getStatusInfo() == Status.OK);
    }
    
    //@Test
    public void GivenMobileResourceWhenGettingCalcMetricsShouldReturnResponse() {
        Response response = this.mobileResource.getDeviceCalcMetrics(this.tokenMock, this.deviceMock.getDeviceId(), System.currentTimeMillis());
        assert(response.getStatusInfo() == Status.OK);
    }
    
    @Test
    public void GivenMobileResourceWhenGettingUserShouldReturnResponse() {
        Response response = this.mobileResource.getUser(this.tokenMock);
        assert(response.getStatusInfo() == Status.OK);
    }
    
    @Test
    public void GivenMobileResourceWhenCreatingUserShouldReturnResponse() {
        Map map = new HashMap();
        map.put("token", this.tokenMock);
        map.put("firstname", this.userMock.getFirstname());
        map.put("lastname", this.userMock.getLastname());
        Gson gson = new Gson();
        String content = gson.toJson(map);
        Response response = this.mobileResource.createUser(content);
        assert(response.getStatusInfo() == Status.OK);
    }
    
}
