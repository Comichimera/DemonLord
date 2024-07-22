package com.dungeoncrawl.game;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

public class LocalizationManager {
    private Properties properties = new Properties();

    public LocalizationManager(Locale locale) {
        String languageCode = locale.getLanguage();

        try {
            InputStream input = this.getClass().getResourceAsStream("/resources/localization/messages_" + languageCode + ".properties");

            try {
                if (input != null) {
                    this.properties.load(input);
                } else {
                    InputStream fallbackInput = this.getClass().getResourceAsStream("/resources/localization/messages_en.properties");

                    try {
                        if (fallbackInput != null) {
                            this.properties.load(fallbackInput);
                        }
                    } catch (Throwable var9) {
                        if (fallbackInput != null) {
                            try {
                                fallbackInput.close();
                            } catch (Throwable var8) {
                                var9.addSuppressed(var8);
                            }
                        }

                        throw var9;
                    }

                    if (fallbackInput != null) {
                        fallbackInput.close();
                    }
                }
            } catch (Throwable var10) {
                if (input != null) {
                    try {
                        input.close();
                    } catch (Throwable var7) {
                        var10.addSuppressed(var7);
                    }
                }

                throw var10;
            }

            if (input != null) {
                input.close();
            }
        } catch (IOException var11) {
            var11.printStackTrace();
        }

    }

    public String getString(String key) {
        return this.properties.getProperty(key, key);
    }
}
