/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.mitfirma.regin;

import dk.mitfirma.dmds.DmdsObject;
import dk.mitfirma.dmds.Genstand;
import dk.mitfirma.dmds.GenstandEJB;
import java.net.URI;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 * @author christian
 */
public class DmdsObjectResourceTest {
    private DmdsObjectResource instance;
    private static final DmdsObjectsResource r = new DmdsObjectsResource();
    private static final Genstand genstand = new Genstand();
    private static final String content =
            "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
            "<genstand>\n" +
            "<genstandsnummer>12345</genstandsnummer>\n" +
            "<navn>Erling Bøgh Andersen</navn>\n" +
            "<beskrivelse>blah bleh bleu</beskrivelse>\n" +
            "</genstand>";  
    
    public DmdsObjectResourceTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        r.uriInfo = mock(UriInfo.class);
        r.dmdsService = mock(GenstandEJB.class);
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getXml method, of class DmdsObjectResource.
     */
    @Test
    public void getXmlReturnsXmlWhenObjectFound() {
        when(r.dmdsService.findById("1")).thenReturn(genstand);
        instance = new DmdsObjectResource("1", r);
        String expResult = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><genstand id=\"null\" parentId=\"null\"><genstandsnummer>null</genstandsnummer><navn>null</navn><beskrivelse>null</beskrivelse></genstand>";
        String result = instance.getXml();
        assertEquals(expResult, result);
    }

    @Test(expected=NotFoundException.class)
    public void getXmlThrows404WhenObjectNotFound() {
        when(r.dmdsService.findById("1")).thenReturn(null);
        instance = new DmdsObjectResource("1", r);
        String result = instance.getXml();
    }

    /**
     * Test of putXml method, of class DmdsObjectResource.
     */
    @Test
    public void putXmlUpdatesObjectWhenObjectFound() {
        when(r.dmdsService.findById("1")).thenReturn(genstand);
        when(r.dmdsService.update(isA(DmdsObject.class))).thenReturn(genstand);
        instance = new DmdsObjectResource("1", r);
        String expResult = "OutboundJaxrsResponse{status=200, reason=OK, hasEntity=true, closed=false, buffered=false}";
        Response result = instance.putXml(content);
        verify(instance.dmdsService, times(1)).update(genstand);
        assertEquals(expResult, result.toString());
    }

    @Test
    public void putXmlCreatesObjectWhenObjectNotFound() {
        when(r.dmdsService.findById("1")).thenReturn(null);
        when(r.dmdsService.newObject()).thenReturn(genstand);
        when(r.uriInfo.getRequestUri()).thenReturn(URI.create(""));
        instance = new DmdsObjectResource("1", r);
        String expResult = "OutboundJaxrsResponse{status=201, reason=Created, hasEntity=true, closed=false, buffered=false}";
        Response result = instance.putXml(content);
        verify(instance.dmdsService, times(1)).create(genstand);
        assertEquals(expResult, result.toString());
    }

    /**
     * Test of delete method, of class DmdsObjectResource.
     */
    @Test
    public void deleteDeletesObjectWhenObjectFound() {
        when(r.dmdsService.delete("1")).thenReturn(genstand);
        instance = new DmdsObjectResource("1", r);
        String expResult = genstand.toXML();
        String result = instance.delete();
        verify(instance.dmdsService, times(1)).delete("1");
        assertEquals(expResult, result);
    }

    @Test(expected=NotFoundException.class)
    public void deleteThrows404WhenObjectNotFound() {
        when(r.dmdsService.delete("1")).thenReturn(null);
        instance = new DmdsObjectResource("1", r);
        String result = instance.delete();
    }
}
