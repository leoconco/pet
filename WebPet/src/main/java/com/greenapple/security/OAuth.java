/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.greenapple.security;

import com.google.gdata.client.authn.oauth.OAuthException;
import com.google.gdata.client.authn.oauth.OAuthHelper;
import com.google.gdata.client.authn.oauth.OAuthHmacSha1Signer;
import com.google.gdata.client.authn.oauth.OAuthParameters;

/**
 *
 * @author pgh
 */
public class OAuth {
      /** The Constant REQUEST_TOKEN_ENDPOINT_URL. */
    private static final String REQUEST_TOKEN_ENDPOINT_URL = "https://accounts-staging.autodesk.com/oauth/requesttoken";
    
    /** The Constant ACCESS_TOKEN_ENDPOINT_URL. */
    private static final String ACCESS_TOKEN_ENDPOINT_URL = "https://accounts-staging.autodesk.com/oauth/accesstoken";
    
    /** The Constant AUTHORIZE_TOKEN_ENDPOINT_URL. */
    private static final String AUTHORIZE_TOKEN_ENDPOINT_URL = "https://accounts-staging.autodesk.com/oauth/authorize";
    
    
    public static AccessToken getAccessToken(String consumerKey, String consumerSecret, String requestToken)
    {
        OAuthParameters params = new OAuthParameters() {

            @Override
            public void assertOAuthTokenSecretExists() throws OAuthException {
            }
        };
        params.setOAuthConsumerKey(consumerKey);
        params.setOAuthConsumerSecret(consumerSecret);
        params.setOAuthToken(requestToken);
        params.setOAuthTokenSecret("");
        try {
            getOAuthHelper().getAccessToken(params);
            
            return new AccessToken(params.getOAuthToken(), params.getOAuthTokenSecret());
           
        } catch (Exception e) {

            if (e != null) {
            }

        }
        return null;
    }
            
    private static OAuthHelper getOAuthHelper()
    {
        return new OAuthHelper(REQUEST_TOKEN_ENDPOINT_URL, AUTHORIZE_TOKEN_ENDPOINT_URL,
                ACCESS_TOKEN_ENDPOINT_URL, null, new OAuthHmacSha1Signer());
                   
    }
}
