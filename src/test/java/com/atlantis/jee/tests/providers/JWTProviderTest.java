/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atlantis.jee.tests.providers;

import com.atlantis.jee.providers.JWTProvider;
import com.google.gson.JsonObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 *
 * @author simon
 */
public class JWTProviderTest {
   
    JWTProvider jwtProvider;
    
    private final String tokenMock = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImtpZCI6Ilg1ZVhrNHh5b2pORnVtMWtsMll0djhkbE5QNC1jNTdkTzZRR1RWQndhTmsifQ" + 
            ".eyJpc3MiOiJodHRwczovL2xvZ2luLmVsaW90YnlsZWdyYW5kLmNvbS8wZDg4MTZkNS0zZTdmLTRjODYtODIyOS02NDUxMzdlMGYyMjIvdjIuMC8iLCJleHAiOjE1MzA" + 
            "1MjQ0MDEsIm5iZiI6MTUzMDUyMDgwMSwiYXVkIjoiY2QyNDYzNGUtYmUyNy00MDU5LThmMDQtMzBmZWI0YjcyYjcxIiwib2lkIjoiYTMxMTY0NTMtYWZkNC00ZGQ1LWJ" +
            "mYzctOTgzZDA0OGFkMDk4Iiwic3ViIjoiYTMxMTY0NTMtYWZkNC00ZGQ1LWJmYzctOTgzZDA0OGFkMDk4IiwidGZwIjoiQjJDXzFfVGhpcmRBcHAtQWNjb3VudExpbmt" +
            "pbmciLCJub25jZSI6IjYzNjY1NTk1MTQxNTM1OTU0MS5NbU5tT0RFNU9EY3RNR1V6WlMwME9EaGtMVGhpTkRrdE1qWTFOR1JrWTJZM1pXRXlPR013WkRNMFpEY3ROell" + 
            "4TkMwME5tVXpMVGd6TmpNdE5qSmtZell6Wm1NMU1Ea3giLCJzY3AiOiJ0b3BvbG9neS5yZWFkIiwiYXpwIjoiOTk5OWQ4MTYtZDUzOS00NjNhLWFmYmQtN2ZjNjYzZGU" +
            "xYzhhIiwidmVyIjoiMS4wIiwiaWF0IjoxNTMwNTIwODAxfQ.Oop4bBvz0g4Eakn_-y8T5dAJaG9esd9E6N_0J7zjgEdCWSJf05UfXIOetWxRQN0fNiCl6bkVxOpflecC" +
            "DoeAyH9U9HyCXTrG1VB9u1x7YuFfjU3j2UesQEPpHCzHMVV8XRX2_tQxJBGibyWtSOYkDRMbsd5BpCJJDCdZ2gXzjPy1qHaFnaUvzbEbH6e1Y3EIVExqUrLwO4jzUHzo" +
            "OC6AsIvxxic_MnQa1UP7Qukb5sA_aQezT2feV0SZv-QoijXaLHfr_Mj8aVhlQghQdqJjAzWCID-Gugla4R6beM3Hulj4XqDZb8JsBEinC3qS9Rt5Ibm81aDi4S2OPaohsGmjgA";
    
    @Before
    public void Setup() throws Exception {
        jwtProvider = new JWTProvider();
    }
    
    @Test
    public void GivenJWTPRoviderWhenCheckingTokenIsNotExpiredShouldReturnFalse() {
        Boolean isExpired = this.jwtProvider.checkTokenIsExpired(tokenMock);
        assert(isExpired);
    }
    
    @Test
    public void GivenJWTProviderWhenGettingUserIdFromTokenShouldReturnString() throws Exception {
        String userId = this.jwtProvider.getUserId(tokenMock);
        assert(userId.equals("a3116453-afd4-4dd5-bfc7-983d048ad098"));
    }
    
}
