package com.example.selenium.ta.demo.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.cucumber.java.Scenario;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.core.har.Har;

/**
 * Utility class to capture web traffic and save them to HTTP archive files.
 */
@Component
public class WebTrafficRecorder {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebTrafficRecorder.class);
    private static final Path CAPTURED_TRAFFIC_FOLDER = Paths.get("target/webtraffic");
    private static final String WHITESPACE_REGEX = "\\s";
    private static final String UNDERSCORE = "_";
    private static final String TARGET_HTTP_ARCHIVE_FILE_NAME_TEMPLATE = "http_archive_demo_%s%s.har";

    @Autowired
    private BrowserMobProxy browserMobProxy;

    public void startRecordingTraffic() {
        browserMobProxy.newHar("demo_traffic_capture");
    }

    public void saveHttpArchiveToFile(final Scenario scenario) {
        final Har capturedHttpArchive = browserMobProxy.getHar();

        final String targetHttpArchiveFileName = String.format(
                TARGET_HTTP_ARCHIVE_FILE_NAME_TEMPLATE,
                scenario.getName(),
                scenario.getLine()
        )
                .replaceAll(WHITESPACE_REGEX, UNDERSCORE);
        try {
            Files.createDirectories(CAPTURED_TRAFFIC_FOLDER);
            capturedHttpArchive.writeTo(CAPTURED_TRAFFIC_FOLDER.resolve(Paths.get(targetHttpArchiveFileName)).toFile());
        } catch (Exception e) {
            LOGGER.warn("Can't save HTTP archive file. Exception: {}", e.getMessage());
        }
    }
}
