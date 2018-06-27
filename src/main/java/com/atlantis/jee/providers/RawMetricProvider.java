/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atlantis.jee.providers;

import com.atlantis.jee.model.RawMetric;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.ejb.Stateless;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import com.google.gson.JsonObject;

/**
 *
 * @author Methilliev
 */
@Stateless
public class RawMetricProvider implements IRawMetricProvider {

    private static String BASEURL;
    
    private HttpClient _httpClient;
    
    public RawMetricProvider() {
        loadConf();
    }
    
    public RawMetricProvider(HttpClient httpClient) {
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
        BASEURL = configFile.getProperty("DOTNETBASEURL", "https://192.168.1.70") + 
                configFile.getProperty("RAWMETRICSENDPOINT", ":30010/rawmetrics");
    }
    
    private HttpClient getHttpClient() {
        if (this._httpClient != null)
            return this._httpClient;
        else
            return HttpClientBuilder.create().build();
    }
    
    @Override
    public List<RawMetric> getRawMetricFromDevice(String deviceId, Long date, int amount) throws Exception {
        String postUrl = this.BASEURL + "/getRawMetricsFromDevice"
                + "?deviceId=" + deviceId
                + "&date=" + date
                + "&amount=" + amount;
        HttpClient httpClient = this.getHttpClient();
        HttpGet get = new HttpGet(postUrl);
        List<RawMetric> rawMetrics = new ArrayList<>();
        try {
            get.setHeader("Content-type", "application/json");
            HttpResponse  response = httpClient.execute(get);
            HttpEntity entity = response.getEntity();
            if (entity == null)
                return null;
            String resp_body = EntityUtils.toString(entity);
            JsonParser parser = new JsonParser();
            JsonArray jsarr = (JsonArray) parser.parse(resp_body);
            for (int i = 0; i < jsarr.size(); i++) {
                RawMetric rawMetric = new RawMetric();
                JsonObject jsobj = (JsonObject) parser.parse(jsarr.get(i).toString());
                rawMetric.setId(jsobj.get("id").toString());
                rawMetric.setDeviceId(jsobj.get("deviceId").toString());
                rawMetric.setDate(jsobj.get("date").getAsLong());
                rawMetric.setValue(jsobj.get("value").toString());
                rawMetrics.add(rawMetric);
            }
            return rawMetrics;
        } catch( UnsupportedEncodingException ex) {
            throw ex;
        } catch(IOException ex) {
            throw ex;
        }
    }
    
}
