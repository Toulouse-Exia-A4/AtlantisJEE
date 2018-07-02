/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atlantis.jee.providers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author simon
 */
public class JWTProvider {
    public final String jwtTokenExpiredExceptionMessage = "JWT expired";
    
    public String getUserIdFromToken(String token) throws Exception {
        if (this.checkTokenIsExpired(token)) {
            throw new Exception(this.jwtTokenExpiredExceptionMessage);
        }
        return this.getUserId(token);
    }
    
    public String getUserId(String token) {
        JsonObject decodedJWT = this.decodeJWT(token);
        return decodedJWT.get("sub").toString().replace("\"", "");
    }
    
    public Boolean checkTokenIsExpired(String token) {
        JsonObject decodedJWT = this.decodeJWT(token);
        return decodedJWT.get("exp").getAsLong() < System.currentTimeMillis() / 1000;
    }
    
    public JsonObject decodeJWT(String token){
        String[] split_string = token.split("\\.");
        String base64EncodedBody = split_string[1];

        Base64 base64Url = new Base64(true);

        String body = new String(base64Url.decode(base64EncodedBody));
        JsonParser parser = new JsonParser();
        JsonObject jsobj = (JsonObject) parser.parse(body);
        return jsobj;
    }
}
