package com.xq;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ConfigReaderTest {
    @Test
    void shouldReturnCorrectConfig() throws Exception {
        ConfigReader reader = new ConfigReader();
        Properties prop = new Properties();
        prop.setProperty("sdk.version", "1.1.1");
        Config config = reader.loadConfig(prop);
        assert config.getSdkVersion().equals("1.1.1");
    }

    @Test
    void shouldReturnCorrectDefaultConfig() throws Exception {
        ConfigReader reader = new ConfigReader();
        Config config = reader.loadConfig();
        assertEquals("1.1.1", config.getSdkVersion());
        assertEquals("localhost", config.getApiGateway());
    }

    @Test
    void shouldThrowWhenPropNotAvailable() {
        assertThrows(RuntimeException.class, () -> {
            ConfigReader reader = new ConfigReader();
            reader.readConfigValue(new Properties(), "abc");
        }, "config reader does not throw runtime exception");
    }

    @Test
    void shouldReturnValueWhenKeyExists() {
        ConfigReader reader = new ConfigReader();
        Properties properties = new Properties();
        properties.setProperty("abc", "1.1.1");
        reader.readConfigValue(properties, "abc");
    }

    @Test
    void shouldThrowWhenKeyNotFound() {
        assertThrows(RuntimeException.class, () -> {
            ConfigReader reader = new ConfigReader();
            reader.readConfig("config2.properties");
        }, "config reader does not throw runtime exception");
    }

    @Test
    void shouldReturnCorrectProp() throws IOException {
        Properties prop = new Properties();
        prop.setProperty("sdk.version", "1.1.1");
        prop.setProperty("sdk.version2", "");
        prop.setProperty("api.gateway", "localhost");
        assertEquals(prop, new ConfigReader().readConfig());
    }

    @Test
    void shouldThrowWhenFileNotFound() {
        assertThrows(RuntimeException.class, () -> {
            new ConfigReader().readConfig("non_existing_file.properties");
        }, "config reader does not throw io exception");
    }
}
