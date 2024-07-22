package com.dungeoncrawl.game;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {
    public PropertiesLoader() {
    }

    public static Properties loadProperties(String path) {
        Properties properties = new Properties();

        try {
            InputStream input = PropertiesLoader.class.getResourceAsStream(path);

            try {
                if (input == null) {
                    throw new NullPointerException("inStream parameter is null");
                }

                properties.load(input);
            } catch (Throwable var6) {
                if (input != null) {
                    try {
                        input.close();
                    } catch (Throwable var5) {
                        var6.addSuppressed(var5);
                    }
                }

                throw var6;
            }

            if (input != null) {
                input.close();
            }
        } catch (IOException var7) {
            var7.printStackTrace();
        }

        return properties;
    }
}
