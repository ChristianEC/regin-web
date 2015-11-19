/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.mitfirma.regin;

import dk.mitfirma.dmds.Genstand;
import dk.mitfirma.dmds.GenstandEJB;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author christian
 */
@Path("/genstand")
@RequestScoped
public class GenstandsResource {

    @Context
    private UriInfo uriInfo;
    @EJB(name = "GenstandEJB")
    private GenstandEJB dmdsService;

    /**
     * Creates a new instance of GenstandsResource
     */
    public GenstandsResource() {}

    /**
     * Retrieves representation of an instance of dk.mitfirma.regin.GenstandsResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/xml")
    public String getXml() {
        List<Genstand> L = dmdsService.findAll();
        String xml = "<?xml version=\"1.0\"?>\n<uriList>\n";
        String context = uriInfo.getRequestUri().toString();
        for (Genstand g : L) {
            xml = xml.concat("<uri>" + context + "/" + g.getId().toString() + "</uri>\n");
        }
        xml = xml.concat("</uriList>");
        return xml;
    }

    /**
     * POST method for creating an instance of GenstandResource
     * @param content representation for the new resource
     * @return an HTTP response with content of the created resource
     */
    @POST
    @Consumes("application/xml")
    @Produces("application/xml")
    public Response postXml(String content) {
        Genstand genstand = new Genstand();
        genstand.fromXML(content);
        dmdsService.create(genstand);        
        return Response.created(uriInfo.getRequestUriBuilder().path(genstand.getId().toString()).build()).entity(genstand.toXML()).build();
    }

    /**
     * Sub-resource locator method for {id}
     * @param id
     * @return 
     */
    @Path("{id}")
    public GenstandResource getGenstandResource(@PathParam("id") String id) {
        return GenstandResource.getInstance(id, uriInfo, dmdsService);
    }
}
