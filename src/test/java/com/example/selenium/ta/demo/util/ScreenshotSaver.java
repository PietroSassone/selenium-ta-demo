package com.example.selenium.ta.demo.util;

import static org.openqa.selenium.OutputType.FILE;

import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.imageio.ImageIO;

import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.cucumber.java.Scenario;

/**
 * Utility class to capture screenshots from the automated browsers and add them to the test report.
 */
@Component
public class ScreenshotSaver {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScreenshotSaver.class);

    public void addScreenshotToCucumberScenario(final Scenario scenario, final WebDriver driver) {

        try (ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream()) {

            ImageIO.write(ImageIO.read(takeScreenshot(driver)), "png", arrayOutputStream);

            scenario.attach(
                    arrayOutputStream.toByteArray(),
                    "image/png",
                    "test_result image"
            );

            LOGGER.info("Screenshot was attached to the failed cucumber scenario result.");
        } catch (Exception e) {
            LOGGER.warn("Can't add screenshot to the scenario. Exception: " + e.getMessage());
        }
    }

    private File takeScreenshot(final WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(FILE);
    }
}
