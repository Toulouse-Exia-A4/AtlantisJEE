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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    
    private User userMock;
    private ArrayList<Device> devicesMock;
    private ArrayList<RawMetric> rawMetricsMock;
    private List<CalculatedMetric> calcMetricsMock;
    
    @Before
    public void Setup() throws Exception {
        userDataProviderMock = Mockito.mock(UserDataProvider.class);
        rawMetricProviderMock = Mockito.mock(RawMetricProvider.class);
        calculatedMetricDAOMock = Mockito.mock(CalculatedMetricDAO.class);
        
        userMock = new User("userId");
        devicesMock = new ArrayList();
        devicesMock.add(new Device("deviceId-1", "temp", "°C"));
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
        Mockito.when(userDataProviderMock.findUserDevices(Mockito.any(User.class))).thenReturn(devicesMock);
        Mockito.when(rawMetricProviderMock.getRawMetricFromDevice(Mockito.any(String.class), Mockito.any(Long.class), Mockito.any(int.class))).thenReturn(rawMetricsMock);
        Mockito.when(calculatedMetricDAOMock.findByDeviceId(Mockito.any(String.class))).thenReturn(calcMetricsMock);
                
        mobileResource = new MobileResource(userDataProviderMock, rawMetricProviderMock, calculatedMetricDAOMock);
    }
    
    @Test
    public void GivenMobileResourceWhenGettingUserDevicesShouldReturnResponse() {
        Response response = this.mobileResource.getUserDevices(this.userMock.getUserId());
        assert(response.getStatusInfo() == Status.OK);
    }
    
    @Test
    public void GivenMobileResourceWhenGettingRawMetricsShouldReturnResponse() {
        Response response = this.mobileResource.getDeviceRawMetrics(this.userMock.getUserId(), System.currentTimeMillis());
        assert(response.getStatusInfo() == Status.OK);
    }
    
    public void GivenMobileResourceWhenGettingCalcMetricsShouldReturnResponse() {
        Response response = this.mobileResource.getDeviceCalcMetrics(this.userMock.getUserId(), System.currentTimeMillis());
        assert(response.getStatusInfo() == Status.OK);
    }
    
}
