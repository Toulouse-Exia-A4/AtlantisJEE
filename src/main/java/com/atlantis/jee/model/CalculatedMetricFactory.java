/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atlantis.jee.model;

import java.util.Date;
import com.google.gson.JsonObject;

/**
 *
 * @author Methilliev
 */
public class CalculatedMetricFactory {
    public static CalculatedMetric fromJsonObject(JsonObject content){
        return new CalculatedMetric(content.get("DeviceId").getAsString(), new Date(content.get("DateTimeStart").getAsLong()), new Date(content.get("DateTimeEnd").getAsLong()), content.get("Value").getAsDouble(), content.get("DataType").getAsString());
    }
}
