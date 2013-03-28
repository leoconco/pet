/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.greenapple.security;

/**
 *
 * @author pgh
 */
public class AccessToken {
    
    private String token;
    private String tokenSecret;
    
    public AccessToken(String token, String tokenSecret)
    {
        this.token = token;
        this.tokenSecret = tokenSecret;
    }
    
    public String getToken()
    {
        return this.token;
    }
    
    public String getTokenSecret()
    {
        return this.tokenSecret;
    }
    
}
