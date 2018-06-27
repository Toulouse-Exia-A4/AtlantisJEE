/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atlantis.jee.model;

import java.util.ArrayList;

/**
 *
 * @author simon
 */
public class User {
    String _id;
    String UserId;
    ArrayList<Device> Devices;
    
    public User(String UserId) {
        this.UserId = UserId;
    }
    
    public User(String UserId, ArrayList<Device> Devices) {
        this.UserId = UserId;
        this.Devices = Devices;
    }

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String UserId) {
        this.UserId = UserId;
    }

    public ArrayList<Device> getDevices() {
        return Devices;
    }

    public void setDevices(ArrayList<Device> Devices) {
        this.Devices = Devices;
    }
    
    public User() {
        
    }
}
