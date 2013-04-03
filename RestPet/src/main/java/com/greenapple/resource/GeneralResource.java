/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.greenapple.resource;

import com.greenapple.model.Tenant;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author leonardo.contreras
 */
@Path("base")
public class GeneralResource {

    @Context
    private UriInfo context;

    public GeneralResource(){}

    @Path("/tenants")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Tenant[] getTenants(){
        Tenant[] tnt = new Tenant[4];
        tnt[0] = new Tenant();
        tnt[0].setId("Uno");
        tnt[0].setName("GreenApple");
        tnt[0].setDescription("Cloud based corporation");
        tnt[1] = new Tenant();
        tnt[1].setId("Dos");
        tnt[1].setName("Globant");
        tnt[1].setDescription("Development services");
        tnt[2] = new Tenant();
        tnt[2].setId("Tres");
        tnt[2].setName("Bulevar");
        tnt[2].setDescription("Cooperative property");
        tnt[3] = new Tenant();
        tnt[3].setId("Cuatro");
        tnt[3].setName("Transmi");
        tnt[3].setDescription("Public transportation");
        return tnt;
    }

}
