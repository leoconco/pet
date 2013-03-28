/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.greenapple.security;

import java.util.HashMap;

/**
 *
 * @author pgh
 */
public class AuthContext {
    
    private HashMap<String, Object> properties;
    
    public AuthContext()
    {
        properties = new HashMap<String, Object>();
    }
    
    public Object getProperty(String name)
    {
        if (properties.containsKey(name))
            return properties.get(name);
        return null;
    }
    
    public void putProperty(String name, Object value)
    {
        properties.put(name, value);
    }
    
    public AccessToken getAccessToken()
    {
        if (properties.containsKey(Constants.OAUTH_ACCESS_TOKEN))
            return (AccessToken) properties.get(Constants.OAUTH_ACCESS_TOKEN);
        return null;
    }
    
    public String getDOB()
    {
        if (properties.containsKey(Constants.DOB_ATTRIBUTE_NAME))
            return (String) properties.get(Constants.DOB_ATTRIBUTE_NAME);
        return null;
    }
    
    public String getCountry()
    {
        if (properties.containsKey(Constants.COUNTRY_ATTRIBUTE_NAME))
            return (String) properties.get(Constants.COUNTRY_ATTRIBUTE_NAME);
        return null;
    }

    public String getEMail()
    {
        if (properties.containsKey(Constants.EMAIL_ATTRIBUTE_NAME))
            return (String) properties.get(Constants.EMAIL_ATTRIBUTE_NAME);
        return null;
    }
 
    public String getFirstName()
    {
        if (properties.containsKey(Constants.FIRSTNAME_ATTRIBUTE_NAME))
            return (String) properties.get(Constants.FIRSTNAME_ATTRIBUTE_NAME);
        return null;
    }
    
    public String getLastName()
    {
        if (properties.containsKey(Constants.LASTNAME_ATTRIBUTE_NAME))
            return (String) properties.get(Constants.LASTNAME_ATTRIBUTE_NAME);
        return null;
    }
    
    public String getFullName()
    {
        if (properties.containsKey(Constants.FULLNAME_ATTRIBUTE_NAME))
            return (String) properties.get(Constants.FULLNAME_ATTRIBUTE_NAME);
        return null;
    }    

    public String getGender()
    {
        if (properties.containsKey(Constants.GENDER_ATTRIBUTE_NAME))
            return (String) properties.get(Constants.GENDER_ATTRIBUTE_NAME);
        return null;
    }   

    public String getLanguage()
    {
        if (properties.containsKey(Constants.LANGUAGE_ATTRIBUTE_NAME))
            return (String) properties.get(Constants.LANGUAGE_ATTRIBUTE_NAME);
        return null;
    }   

    public String getNickName()
    {
        if (properties.containsKey(Constants.NICKNAME_ATTRIBUTE_NAME))
            return (String) properties.get(Constants.NICKNAME_ATTRIBUTE_NAME);
        return null;
    } 
    
    public String getPostalCode()
    {
        if (properties.containsKey(Constants.POSTCODE_ATTRIBUTE_NAME))
            return (String) properties.get(Constants.POSTCODE_ATTRIBUTE_NAME);
        return null;
    } 

    public String getTimeZone()
    {
        if (properties.containsKey(Constants.TIMEZONE_ATTRIBUTE_NAME))
            return (String) properties.get(Constants.TIMEZONE_ATTRIBUTE_NAME);
        return null;
    } 
 
    public String getTenantId()
    {
        if (properties.containsKey(Constants.TENANT_ATTRIBUTE_ID))
            return (String) properties.get(Constants.TENANT_ATTRIBUTE_ID);
        return null;
    }

    public String getTenantName()
    {
        if (properties.containsKey(Constants.TENANT_ATTRIBUTE_LABEL))
            return (String) properties.get(Constants.TENANT_ATTRIBUTE_LABEL);
        return null;
    }
}
