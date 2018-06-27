/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atlantis.jee.model;

import java.util.Date;

/**
 *
 * @author Methilliev
 */
public class CalculatedMetric {
    String _id;
    String DeviceId;
    Date dateTimeStart;
    Date dateTimeEnd;
    double value;
    String dataType;

    public CalculatedMetric(String DeviceId, Date dateTimeStart, Date dateTimeEnd, double value, String dataType) {
        this.DeviceId = DeviceId;
        this.dateTimeStart = dateTimeStart;
        this.dateTimeEnd = dateTimeEnd;
        this.value = value;
        this.dataType = dataType;
    }

    public CalculatedMetric() {
        
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String getDeviceId() {
        return DeviceId;
    }

    public void setDeviceId(String DeviceId) {
        this.DeviceId = DeviceId;
    }

    public Date getDateTimeStart() {
        return dateTimeStart;
    }

    public void setDateTimeStart(Date dateTimeStart) {
        this.dateTimeStart = dateTimeStart;
    }

    public Date getDateTimeEnd() {
        return dateTimeEnd;
    }

    public void setDateTimeEnd(Date dateTimeEnd) {
        this.dateTimeEnd = dateTimeEnd;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
    
    
}
