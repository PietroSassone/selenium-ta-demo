package com.example.selenium.ta.demo.util.browserplatform;

import org.openqa.selenium.Dimension;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@EqualsAndHashCode
@JsonDeserialize(using = JsonDeserializerForPlatform.class)
public class Platform {
    private static final String DESKTOP = "desktop";

    private String platformName;
    private Screen screenSettings;
    private String userAgent;

    public Dimension getBrowserWindowSize() {
        return new Dimension(screenSettings.getScreenWidth(), screenSettings.getScreenHeight());
    }

    public boolean isDevice() {
        return !DESKTOP.equals(platformName);
    }
}
