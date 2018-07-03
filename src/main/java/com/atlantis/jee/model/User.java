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
    String userId;
    String firstname;
    String lastname;
    ArrayList<Device> Devices;
    
    public User(String userId) {
        this.userId = userId;
    }
    
    public User(String userId, String firstname, String lastname) {
        this.userId = userId;
        this.firstname = firstname;
        this.lastname = lastname;
    }
    
    public User(String userId, ArrayList<Device> devices) {
        this.userId = userId;
        this.Devices = devices;
    }

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String UserId) {
        this.userId = UserId;
    }
    
    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String Firstname) {
        this.firstname = Firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String Lastname) {
        this.lastname = Lastname;
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
