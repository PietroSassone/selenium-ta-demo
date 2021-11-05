package com.example.selenium.ta.demo.factory;

import static net.lightbody.bmp.proxy.CaptureType.REQUEST_CONTENT;
import static net.lightbody.bmp.proxy.CaptureType.REQUEST_HEADERS;
import static net.lightbody.bmp.proxy.CaptureType.RESPONSE_HEADERS;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;

public class BrowserMobProxyFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(BrowserMobProxyFactory.class);

    private BrowserMobProxy browserMobProxy;

    public BrowserMobProxyFactory() {
        this.createNewBrowserMobProxy();
    }

    public BrowserMobProxy getBrowserMobProxy() {
        return browserMobProxy;
    }

    public void stopBrowserMobProxy() {
        browserMobProxy.stop();
        LOGGER.info("Web traffic recorder proxy stopped.");
    }

    private void createNewBrowserMobProxy() {
        browserMobProxy = new BrowserMobProxyServer();
        browserMobProxy.enableHarCaptureTypes(
            REQUEST_HEADERS,
            REQUEST_CONTENT,
            RESPONSE_HEADERS
        );

        browserMobProxy.start(0);
        LOGGER.info("Web traffic recorder proxy has been started.");
    }
}
