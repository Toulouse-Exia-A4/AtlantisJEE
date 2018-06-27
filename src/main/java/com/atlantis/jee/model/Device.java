/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atlantis.jee.model;

/**
 *
 * @author simon
 */
public class Device {
    String _id;
    String DeviceId;
    String Type;
    String Unit;

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String Unit) {
        this.Unit = Unit;
    }
    
    public Device(String DeviceId, String Type, String Unit) {
        this.DeviceId = DeviceId;
        this.Type = Type;
        this.Unit = Unit;
    }
    
    public Device() {
        
    }

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getDeviceId() {
        return DeviceId;
    }

    public void setDeviceId(String DeviceId) {
        this.DeviceId = DeviceId;
    }

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }
    
}
