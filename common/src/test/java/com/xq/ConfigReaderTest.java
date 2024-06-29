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
    void shouldThrowWhenPropNotAvailable() {
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
        assertEquals(prop, new ConfigReader().readConfig());
    }

    @Test
    void shouldThrowWhenFileNotFound() {
        assertThrows(RuntimeException.class, () -> {
            new ConfigReader().readConfig("non_existing_file.properties");
        }, "config reader does not throw io exception");
    }
}
