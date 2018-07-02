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
    String Firstname;
    String Lastname;
    ArrayList<Device> Devices;
    
    public User(String userId) {
        this.UserId = userId;
    }
    
    public User(String userId, String firstname, String lastname) {
        this.UserId = userId;
        this.Firstname = firstname;
        this.Lastname = lastname;
    }
    
    public User(String userId, ArrayList<Device> devices) {
        this.UserId = userId;
        this.Devices = devices;
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
    
    public String getFirstname() {
        return Firstname;
    }

    public void setFirstname(String Firstname) {
        this.Firstname = Firstname;
    }

    public String getLastname() {
        return Lastname;
    }

    public void setLastname(String Lastname) {
        this.Lastname = Lastname;
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
