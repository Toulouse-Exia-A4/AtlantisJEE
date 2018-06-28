/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atlantis.jee.tests.providers;

import com.atlantis.jee.model.RawMetric;
import com.atlantis.jee.providers.IRawMetricProvider;
import com.atlantis.jee.providers.RawMetricProvider;
import java.io.IOException;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Mockito.times;

/**
 *
 * @author simon
 */
@RunWith(MockitoJUnitRunner.class)
public class RawMetricProviderTest {
    
    IRawMetricProvider rawMetricProvider;
    
    private HttpClient httpClientMock;
    private HttpResponse httpResponseMock;
    
    @Before
    public void setup() throws IOException {
        httpClientMock = Mockito.mock(HttpClient.class);
        httpResponseMock = Mockito.mock(HttpResponse.class);
        Mockito.when(httpClientMock.execute(Mockito.any(HttpGet.class))).thenReturn(httpResponseMock);
        Mockito.when(httpClientMock.execute(Mockito.any(HttpPost.class))).thenReturn(httpResponseMock);
        this.rawMetricProvider = new RawMetricProvider(httpClientMock);
    }
    
    @Test
    public void GivenRawMetricWhenGettingDeviceRawMetricsShouldReturnRawMetricList() throws Exception {
        List<RawMetric> rawMetrics = this.rawMetricProvider.getRawMetricFromDevice("deviceid", System.currentTimeMillis(), 100);
        Mockito.verify(httpClientMock, times(1)).execute(Mockito.any(HttpPost.class));
    }
}
