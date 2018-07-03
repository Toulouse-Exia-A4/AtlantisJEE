/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atlantis.jee.providers;

import com.atlantis.jee.model.Device;
import com.atlantis.jee.model.User;
import java.util.ArrayList;
import javax.ejb.Local;
/**
 *
 * @author simon
 */
@Local
public interface IUserDataProvider {
    public User findUser(String userId) throws Exception;
    public User createUser(User user) throws Exception;
}
