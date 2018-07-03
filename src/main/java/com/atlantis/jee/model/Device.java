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
    String DeviceId;
    String Name;
    String Type;
    String Unit;

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String Unit) {
        this.Unit = Unit;
    }
    
    public Device(String DeviceId, String Name, String Type, String Unit) {
        this.DeviceId = DeviceId;
        this.Name = Name;
        this.Type = Type;
        this.Unit = Unit;
    }
    
    public Device() {
        
    }

    public String getDeviceId() {
        return DeviceId;
    }

    public void setDeviceId(String DeviceId) {
        this.DeviceId = DeviceId;
    }
    
    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }
    
}
