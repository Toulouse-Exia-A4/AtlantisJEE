/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atlantis.jee.tests.providers;

import com.atlantis.jee.model.User;
import com.atlantis.jee.providers.IUserDataProvider;
import com.atlantis.jee.providers.UserDataProvider;
import javax.json.JsonArray;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.InjectMocks;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import org.mockito.runners.MockitoJUnitRunner;

/**
 *
 * @author simon
 */
@RunWith(MockitoJUnitRunner.class)
public class UserDataProviderTest {
    
    IUserDataProvider userDataProvider;
    
    private User userMock;
    private WebTarget targetMock;
    private Invocation.Builder builderMock;
    //private MultivaluedMap<String, Object> multivaluedMap;
    //private JsonArray jsonArray;
    private Response responseMock;
    
    @Before
    public void setup() {
        userMock = Mockito.mock(User.class);
        targetMock = Mockito.mock(WebTarget.class);
        builderMock = Mockito.mock(Invocation.Builder.class);
        //multivaluedMap = Mockito.mock(MultivaluedMap.class);
        //jsonArray = Mockito.mock(JsonArray.class);
        responseMock = Mockito.mock(Response.class);
        //Mockito.when(target.request().get()).thenReturn((Response) multivaluedMap);
        //Mockito.when(target.request(MediaType.APPLICATION_JSON).get(JsonArray.class)).thenReturn(jsonArray);
        Mockito.when(targetMock.request()).thenReturn(builderMock);
        Mockito.when(builderMock.post(Mockito.any(Entity.class))).thenReturn(responseMock);
        this.userDataProvider = new UserDataProvider();
    }
    
    @Test
    public void GivenUserDataWhenCreatingUserShouldReturnUser() throws Exception {
        User user = new User("useridmock");
        User returnedUser = userDataProvider.createUser(user);
        //Mockito.verify(returnedUser);
        //Mockito.verify(builderMock, times(1)).post(Entity.entity(Mockito.anyString(), MediaType.APPLICATION_JSON_TYPE));
        Mockito.verify(builderMock, times(1)).post(Mockito.any(Entity.class));
    }
    
    //public void GivenUserDataWhenFindingUserShouldReturnUser() throws Exception {
    //    User user = new User("useridmock");
    //    User returnedUser = userDataProvider.findUser(user);
    //    Mockito.verify(user);
    //}
}
