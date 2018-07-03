/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atlantis.jee.tests.providers;

import com.atlantis.jee.model.User;
import com.atlantis.jee.model.Device;
import com.atlantis.jee.providers.IUserDataProvider;
import com.atlantis.jee.providers.UserDataProvider;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
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
    
    private HttpClient httpClientMock;
    private HttpResponse httpResponseMock;
    
    @Before
    public void setup() throws IOException {
        httpClientMock = Mockito.mock(HttpClient.class);
        httpResponseMock = Mockito.mock(HttpResponse.class);
        Mockito.when(httpClientMock.execute(Mockito.any(HttpGet.class))).thenReturn(httpResponseMock);
        Mockito.when(httpClientMock.execute(Mockito.any(HttpPost.class))).thenReturn(httpResponseMock);
        this.userDataProvider = new UserDataProvider(httpClientMock);
    }
    
    @Test
    public void GivenUserDataWhenGettingUserShouldReturnUser() throws Exception {
        String userId = "userid";
        User returnedUser = userDataProvider.findUser(userId);
        Mockito.verify(httpClientMock, times(1)).execute(Mockito.any(HttpGet.class));
    }
    
    @Test
    public void GivenUserDataWhenCreatingUserShouldReturnUser() throws Exception {
        User user = new User("userid");
        User returnedUser = userDataProvider.createUser(user);
        Mockito.verify(httpClientMock, times(1)).execute(Mockito.any(HttpPost.class));
    }
}
