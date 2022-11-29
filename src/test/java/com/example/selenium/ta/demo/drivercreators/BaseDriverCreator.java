package com.example.selenium.ta.demo.drivercreators;

import static org.openqa.selenium.remote.CapabilityType.PROXY;

import java.util.function.Function;

import org.openqa.selenium.remote.AbstractDriverOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.client.ClientUtil;

public class BaseDriverCreator {

    public static final String USER_AGENT_OPTION = "--user-agent=";
    public static final String IGNORE_CERTIFICATE_ERRORS_OPTION = "--ignore-certificate-errors";

    @Value("${headless:false}")
    protected Boolean headless;

    @Autowired
    protected BrowserMobProxy browserMobProxy;

    protected final Function<AbstractDriverOptions<?>, Void> setProxyForWetTrafficRecording = options -> {
        options.setCapability(PROXY, ClientUtil.createSeleniumProxy(browserMobProxy));
        return null;
    };

}
