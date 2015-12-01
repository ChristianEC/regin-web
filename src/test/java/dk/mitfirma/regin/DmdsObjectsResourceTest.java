/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.mitfirma.regin;

import dk.mitfirma.dmds.Genstand;
import dk.mitfirma.dmds.GenstandEJB;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 * @author christian
 */
public class DmdsObjectsResourceTest {
    private DmdsObjectsResource instance;
    private static final Genstand genstand = new Genstand();
    private static final List<Genstand> genstandList = Arrays.asList(genstand, genstand);
    
    public DmdsObjectsResourceTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        genstand.setId("1");
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        instance = new DmdsObjectsResource();
        instance.uriInfo = mock(UriInfo.class);
        instance.dmdsService = mock(GenstandEJB.class);
        when(instance.uriInfo.getRequestUri()).thenReturn(URI.create(""));
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getXml method, of class DmdsObjectsResource.
     */
    @Test
    public void getXmlReturnsValidXmlForEmptyList() {
        String expResult = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n<html xmlns=\"http://www.w3.org/1999/xhtml\"><head><title>DMDS resources</title>\n</head><body /></html>";
        String result = instance.getXml();
        assertEquals(expResult, result);
    }

    @Test
    public void getXmlBuildsCorrectUriList() {
        when(instance.dmdsService.findAll()).thenReturn(genstandList);
        String expResult = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n<html xmlns=\"http://www.w3.org/1999/xhtml\"><head><title>DMDS resources</title>\n<link href=\"/1\" />\n<link href=\"/1\" />\n</head><body /></html>";
        String result = instance.getXml();
        assertEquals(expResult, result);    
    }
    
    /**
     * Test of postXml method, of class DmdsObjectsResource.
     */
    @Test
    public void postXmlCreatesNewObject() {
        when(instance.uriInfo.getRequestUriBuilder()).thenReturn(UriBuilder.fromPath(""));
        when(instance.dmdsService.newObject()).thenReturn(genstand);
        String content = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
            "<genstand>\n" +
            "<genstandsnummer>12345</genstandsnummer>\n" +
            "<navn>Erling Bøgh Andersen</navn>\n" +
            "<beskrivelse>blah bleh bleu</beskrivelse>\n" +
            "</genstand>";        
        String expResult = "OutboundJaxrsResponse{status=201, reason=Created, hasEntity=true, closed=false, buffered=false}";
        Response result = instance.postXml(content);
        verify(instance.dmdsService, times(1)).create(genstand);
        assertEquals(expResult, result.toString());
    }

    /**
     * Test of getDmdsObjectResource method, of class DmdsObjectsResource.
     */
    @Test
    public void getDmdsObjectResourceReturnsNotNull() {
        DmdsObjectResource result = instance.getDmdsObjectResource("9380234234243");
        assertNotNull(result);
    }
}
