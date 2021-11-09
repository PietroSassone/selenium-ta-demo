package com.example.selenium.ta.demo.util.browserplatform;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Function;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class JsonDeserializerForPlatform extends StdDeserializer<Platform> {

    private static final String SETTINGS_FILES_PATH = "browserplatforms/%s.json";
    private static final String TITLE_NODE_NAME = "title";
    private static final String USER_AGENT_NODE_NAME = "user-agent";
    private static final String SCREEN_NODE_NAME = "screen";
    private static final String WIDTH_NODE_NAME = "width";
    private static final String HEIGHT_NODE_NAME = "height";
    private static final String PIXEL_RATIO_NODE_NAME = "device-pixel-ratio";

    public JsonDeserializerForPlatform() {
        this(null);
    }

    public JsonDeserializerForPlatform(final Class<?> valueClass) {
        super(valueClass);
    }

    @Override
    public Platform deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException {
        final JsonNode platformSettingsJson = jsonParser.getCodec().readTree(jsonParser);
        final JsonNode screenSettingsNode = platformSettingsJson.get(SCREEN_NODE_NAME);

        return new Platform(
            getTextFromJsonNode(TITLE_NODE_NAME).apply(platformSettingsJson),
            new Screen(
                getIntegerValueFromJsonNode(WIDTH_NODE_NAME).apply(screenSettingsNode),
                getIntegerValueFromJsonNode(HEIGHT_NODE_NAME).apply(screenSettingsNode),
                getDoubleValueFromJsonNode(PIXEL_RATIO_NODE_NAME).apply(screenSettingsNode)
            ),
            getTextFromJsonNode(USER_AGENT_NODE_NAME).apply(platformSettingsJson)
        );
    }

    public Platform readJsonFileToPlatform(final String targetPlatform) {
        try (final InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(String.format(SETTINGS_FILES_PATH, targetPlatform))) {
            return new ObjectMapper().readValue(inputStream, Platform.class);

        } catch (IOException e) {
            throw new RuntimeException("Trying to set browser to platform unsupported by the tests: " + targetPlatform);
        }
    }

    private Function<JsonNode, String> getTextFromJsonNode(final String nodeNameToGet) {
        return jsonNode -> jsonNode.get(nodeNameToGet).textValue();
    }

    private Function<JsonNode, Integer> getIntegerValueFromJsonNode(final String nodeNameToGet) {
        return jsonNode -> getNumericValueFromJsonNode(jsonNode, nodeNameToGet).intValue();
    }

    private Function<JsonNode, Double> getDoubleValueFromJsonNode(final String nodeNameToGet) {
        return jsonNode -> getNumericValueFromJsonNode(jsonNode, nodeNameToGet).doubleValue();
    }

    private Number getNumericValueFromJsonNode(final JsonNode jsonNode, final String nodeNameToGet) {
        return jsonNode.get(nodeNameToGet).numberValue();
    }
}
