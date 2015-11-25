/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.mitfirma.regin;

import dk.mitfirma.dmds.DmdsObject;
import dk.mitfirma.dmds.DmdsObjectEJB;
import dk.mitfirma.dmds.FilEJB;
import dk.mitfirma.dmds.GenstandEJB;
import dk.mitfirma.dmds.SagEJB;
import java.util.List;
import static java.util.stream.Collectors.joining;
import javax.annotation.PostConstruct;
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
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author christian
 */
@Path("/{objType}")
@RequestScoped
public class DmdsObjectsResource {

    @Context
    UriInfo uriInfo;
    @PathParam("objType")
    private String objType;
    @EJB(name = "SagEJB")
    private SagEJB sagEJB;
    @EJB(name = "GenstandEJB")
    private GenstandEJB genstandEJB;
    @EJB(name = "FilEJB")
    private FilEJB filEJB;
    DmdsObjectEJB dmdsService;

    /**
     * Creates a new instance of GenstandsResource
     */
    public DmdsObjectsResource() {}
    
    @PostConstruct
    private void postConstruct() {
        switch (objType) {
            case "genstand": dmdsService = genstandEJB; break;
            case "sag": dmdsService = sagEJB; break;
            case "fil": dmdsService = filEJB; break;
            default: throw new NotFoundException();
        }
    }
    
    /**
     * Retrieves representation of an instance of dk.mitfirma.regin.DmdsObjectsResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/xml")
    public String getXml() {
        String context = uriInfo.getRequestUri().toString();
        String prefix = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<xhtml><head>\n";
        String suffix = "</head><body /></xhtml>";
        List<DmdsObject> L = dmdsService.findAll();
        return L.stream().map(o -> "<link href=\"" + context + "/" + o.getId().toString() + "\" />\n").collect(joining("", prefix, suffix));
    }

    /**
     * POST method for creating an instance of DmdsObjectResource
     * @param content representation for the new resource
     * @return an HTTP response with content of the created resource
     */
    @POST
    @Consumes("application/xml")
    @Produces("application/xml")
    public Response postXml(String content) {
        DmdsObject o = dmdsService.newObject().fromXML(content);
        dmdsService.create(o);
        return Response.created(uriInfo.getRequestUriBuilder().path(o.getId().toString()).build()).entity(o.toXML()).build();
    }

    /**
     * Sub-resource locator method for {id}
     * @param id
     * @return 
     */
    @Path("{id}")
    public DmdsObjectResource getDmdsObjectResource(@PathParam("id") String id) {
        return DmdsObjectResource.getInstance(id, this);
    }
}
