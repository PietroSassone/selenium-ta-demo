package com.example.selenium.ta.demo.util.browserplatform;

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
public class Screen {
    private int screenWidth;
    private int screenHeight;
    private double pixelRatio;
}
