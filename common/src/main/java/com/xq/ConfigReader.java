package com.xq;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    public Properties readConfig(String fileName) {
        Properties props = new Properties();
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(fileName)) {
            props.load(in);
        } catch (IOException | NullPointerException e) {
            throw new RuntimeException("can't read config", e.getCause());
        }

        return props;
    }

    public Properties readConfig() throws IOException {
        return readConfig(Constant.DEFAULT_CONFIGURATION_FILE);
    }

    public Config loadConfig(Properties props) {
        Config conf = new Config();
        conf.setSdkVersion(props.getProperty("sdk.version"));
        return conf;
    }
}