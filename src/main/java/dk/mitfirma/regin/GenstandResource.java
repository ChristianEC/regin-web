/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.mitfirma.regin;

import dk.mitfirma.dmds.Genstand;
import dk.mitfirma.dmds.GenstandEJB;
import javax.annotation.Resource;
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
public class GenstandResource {

    private final String id;
    private final GenstandEJB dmdsService;
    private final UriInfo uriInfo;
    private Genstand genstand;
    
    /**
     * Creates a new instance of GenstandResource
     * @param id
     * @param uriInfo
     * @param dmdsService
     */
    public GenstandResource(String id, UriInfo uriInfo, GenstandEJB dmdsService) {
        this.id = id;
        this.uriInfo = uriInfo;
        this.dmdsService = dmdsService;
        genstand = dmdsService.findById(id);
    }

    /**
     * Get instance of the GenstandResource
     * @param id
     * @param uriInfo
     * @param dmdsService
     * @return 
     */
    public static GenstandResource getInstance(String id, UriInfo uriInfo, GenstandEJB dmdsService) {
        // The user may use some kind of persistence mechanism
        // to store and restore instances of GenstandResource class.
        return new GenstandResource(id, uriInfo, dmdsService);
    }

    /**
     * Retrieves representation of an instance of dk.mitfirma.regin.GenstandResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/xml")
    public String getXml() throws NotFoundException {   
        if (genstand == null) throw new NotFoundException();
        return genstand.toXML();
    }

    /**
     * PUT method for updating or creating an instance of GenstandResource
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/xml")
    @Produces("application/xml")
    public Response putXml(String content) {
        Response response;
        
        if (genstand == null) {
            genstand = new Genstand();
            genstand.setId(id);
            genstand.fromXML(content);
            dmdsService.create(genstand);
            response = Response.created(uriInfo.getRequestUri()).entity(genstand.toXML()).build();
        } else {
            genstand.setId(id);
            genstand.fromXML(content);
            genstand = dmdsService.update(genstand);
            response = Response.ok(genstand.toXML()).build();
       }
       return response;
    }

    /**
     * DELETE method for resource GenstandResource
     * @return an HTTP response with content of the deleted resource
     */
    @DELETE
    @Produces("application/xml")
    public String delete() throws NotFoundException{
        genstand = dmdsService.delete(id);
        if (genstand == null)
            throw new NotFoundException();
        else
            return genstand.toXML();
    }
}
