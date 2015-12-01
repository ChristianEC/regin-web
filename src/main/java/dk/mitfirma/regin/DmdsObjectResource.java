/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.mitfirma.regin;

import dk.mitfirma.dmds.DmdsObject;
import dk.mitfirma.dmds.DmdsObjectEJB;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * REST Web Service
 *
 * @author christian
 */
@RequestScoped
public class DmdsObjectResource {

    private final String id;
    final DmdsObjectEJB dmdsService;
    private final UriInfo uriInfo;
    DmdsObject object;
    
    /**
     * Creates a new instance of GenstandResource
     * @param id
     * @param r
     */
    public DmdsObjectResource(String id, DmdsObjectsResource r) {
        this.id = id;
        uriInfo = r.uriInfo;
        dmdsService = r.dmdsService;
        object = dmdsService.findById(id);
    }

    /**
     * Get instance of the DmdsObjectResource
     * @param id
     * @param r
     * @return 
     */
    public static DmdsObjectResource getInstance(String id, DmdsObjectsResource r) {
        return new DmdsObjectResource(id, r);
    }

    /**
     * Retrieves representation of an instance of dk.mitfirma.regin.DmdsObjectResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/xml")
    public String getXml() throws NotFoundException {   
        if (object == null) throw new NotFoundException();
        return object.toXML();
    }

    /**
     * PUT method for updating or creating an instance of DmdsObjectResource
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/xml")
    @Produces("application/xml")
    public Response putXml(String content) {
        Response response;
        
        if (object == null) {
            object = dmdsService.newObject().fromXML(content);
            object.setId(id);
            dmdsService.create(object);
            response = Response.created(uriInfo.getRequestUri()).entity(object.toXML()).build();
        } else {
            object = dmdsService.update(object.fromXML(content));
            response = Response.ok(object.toXML()).build();
       }
       return response;
    }

    /**
     * DELETE method for resource DmdsObjectResource
     * @return an HTTP response with content of the deleted resource
     */
    @DELETE
    @Produces("application/xml")
    public String delete() throws NotFoundException {
        object = dmdsService.delete(id);
        if (object == null)
            throw new NotFoundException();
        else
            return object.toXML();
    }
}
