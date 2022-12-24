package org.cloud.sonic.driver.poco;

import org.cloud.sonic.driver.common.models.WindowSize;
import org.cloud.sonic.driver.common.tool.SonicRespException;
import org.cloud.sonic.driver.poco.enums.PocoEngine;
import org.cloud.sonic.driver.poco.enums.PocoSelector;
import org.cloud.sonic.driver.poco.models.PocoElement;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

public class PocoDriverTest {
    static PocoDriver pocoDriver;

    @BeforeClass
    public static void beforeClass() {
        pocoDriver = new PocoDriver(PocoEngine.UNITY_3D);
        pocoDriver.disableLog();
        pocoDriver.showLog();
    }

    @Test
    public void testPageSource() throws SonicRespException {
//        Assert.assertEquals("Root", pocoDriver.getPageSource().getPayload().getType());
        Assert.assertTrue(pocoDriver.getPageSourceForJsonString().length() > 0);
        Assert.assertNotNull(pocoDriver.getPageSourceForXmlElement().toString());
        System.out.println(pocoDriver.getPageSourceForXmlElement().toString());
    }

    @Test
    public void testFindElement() throws SonicRespException {
        String expression = "poco(\"star\")[3]";
        List<PocoElement> pocoElements = pocoDriver.findElements(PocoSelector.POCO, expression);
        System.out.println(pocoElements.size());
        for (PocoElement pocoElement : pocoElements) {
            System.out.println(pocoElement.getPayload().getPos());
            Assert.assertNotNull(pocoElement.getPayload().getName());
            System.out.println(pocoElement.getPayload().getName());
            Assert.assertNotNull(pocoElement.currentNodeSelector);
            System.out.println(pocoElement.currentNodeSelector);
        }
    }

    @Test
    public void testFreeze() throws SonicRespException {
        String r = pocoDriver.getPageSourceForJsonString();
        pocoDriver.freezeSource();
        try {
            // change page tree operation
            Thread.sleep(5 * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Assert.assertEquals(r, pocoDriver.getPageSourceForJsonString());
        pocoDriver.thawSource();
    }

    @Test
    public void testWindowsSize() throws SonicRespException {
        WindowSize windowSize = pocoDriver.getScreenSize();
        Assert.assertTrue(windowSize.getHeight() > 0);
        Assert.assertTrue(windowSize.getWidth() > 0);
    }

    @Test
    public void testNodeExist() throws SonicRespException {
        String expression = "Root > MEHolo > AnchorManager";
        PocoElement pocoElement = pocoDriver.findElement(PocoSelector.CSS_SELECTOR, expression);
        Assert.assertTrue(pocoElement.currentTheNodeExists());
        // mock node does not exist
        pocoElement.currentNodeSelector = "Root > children > MEHolo > children > AnchorManager222";
        Assert.assertTrue(pocoElement.currentTheNodeExists());
    }

    @Test
    public void testGetParent() throws SonicRespException {
        String expression = "Root > MEHolo >  AnchorManager";
        PocoElement pocoElement = pocoDriver.findElement(PocoSelector.CSS_SELECTOR, expression);
        PocoElement parentPocoElement = pocoElement.getParentNode();
        Assert.assertNotNull(parentPocoElement.getPayload().getName());
        System.out.println(parentPocoElement.getPayload().getName());
        Assert.assertNotNull(parentPocoElement.currentNodeSelector);
        System.out.println(parentPocoElement.currentNodeSelector);
    }

    @Test
    public void testElementGetChild()  throws SonicRespException{
        String expression = "Root > Canvas";
        PocoElement pocoElement = pocoDriver.findElement(PocoSelector.CSS_SELECTOR, expression);
        Assert.assertTrue(!pocoElement.getChildren().isEmpty());
        System.out.println(pocoElement.getChildren().size());
    }

    @Test
    public void testUpdateRootCase() throws SonicRespException{
        String expression = "Root > MEHolo > AnchorManager";
        PocoElement pocoElement = pocoDriver.findElement(PocoSelector.CSS_SELECTOR, expression);
        String lastRootXml = pocoElement.getRootElement().getXmlElement().toString();
        try {
            System.out.println("into blocking,please perform a page tree change operation");
            // change page tree operation
            Thread.sleep(5 * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        pocoDriver.getPageSource();
        Assert.assertEquals(lastRootXml, pocoElement.getRootElement().getXmlElement().toString());
    }

    @Test
    public void testGetAttribute() throws SonicRespException{
        String expression = "poco(\"star\")[3]";
        PocoElement pocoElement = pocoDriver.findElement(PocoSelector.POCO, expression);
        System.out.println(pocoElement.getAttribute("_instanceId"));
        assert pocoElement.getAttribute("_instanceId")!=null;
    }

    @AfterClass
    public static void afterClass() {
        pocoDriver.closeDriver();
    }
}
