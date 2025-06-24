package net.wiicart.webcli.config;

import net.wiicart.webcli.Debug;
import net.wiicart.webcli.ResourceManager;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.Map;

public final class Configuration {

    public static final File CONFIG_FILE = new File("config.yml");

    private static Configuration instance;

    private final EnumMap<Option.Int, Integer> ints = new EnumMap<>(Option.Int.class);
    private final EnumMap<Option.Text, String> strings = new EnumMap<>(Option.Text.class);
    private final EnumMap<Option.Bool, Boolean> booleans = new EnumMap<>(Option.Bool.class);

    public Configuration() throws IllegalStateException {
        checkInit();
        load();
    }

    private void checkInit() throws IllegalStateException {
        if(instance != null) {
            throw new IllegalStateException("Configuration instance already exists");
        } else {
            instance = this;
        }
    }

    private void load() {
        if(!CONFIG_FILE.exists()) {
            writeDefaultConfig();
        }

        try (FileInputStream stream = new FileInputStream(CONFIG_FILE)) {
            Yaml yaml = new Yaml();
            Map<String, Object> config = yaml.load(stream);

            loadInts(config);
            loadStrings(config);
            loadBooleans(config);
        } catch(final Exception e) {
            Debug.log("Configuration file not found");
            useDefaults();
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void writeDefaultConfig() {
        try(InputStream input = ResourceManager.loadResource("/config.yml")) {
            // Make sure parent directories exist
            File parent = CONFIG_FILE.getParentFile();
            if(parent != null && !parent.exists()) {
                parent.mkdirs();
            }

            try(FileOutputStream out = new FileOutputStream(CONFIG_FILE)) {
                input.transferTo(out);
            }

            Debug.log("Wrote default config.yml to " + CONFIG_FILE.getAbsolutePath());
        } catch(final Exception e) {
            Debug.log("Failed to save default config.yml");
            Debug.log(e.getMessage());
        }
    }

    private void loadInts(@NotNull Map<String, Object> map) {
        for(Option.Int option : Option.Int.values()) {
            Object value = map.get(option.key);
            if(value instanceof Number number) {
                ints.put(option, number.intValue());
            } else {
                ints.put(option, option.defaultValue);
            }
        }
    }

    private void loadStrings(@NotNull Map<String, Object> map) {
        for(Option.Text option : Option.Text.values()) {
            Object value = map.get(option.key);
            if(value instanceof String string) {
                strings.put(option, string);
            } else {
                strings.put(option, option.defaultValue);
            }
        }
    }

    private void loadBooleans(@NotNull Map<String, Object> map) {
        for(Option.Bool bool : Option.Bool.values()) {
            Object value = map.get(bool.key);
            if(value instanceof Boolean booleanValue) {
                booleans.put(bool, booleanValue);
            } else {
                booleans.put(bool, bool.defaultValue);
            }
        }
    }

    // Load in default values from the Enums
    private void useDefaults() {
        for(Option.Int option : Option.Int.values()) {
            ints.put(option, option.defaultValue);
        }

        for(Option.Text option : Option.Text.values()) {
            strings.put(option, option.defaultValue);
        }

        for(Option.Bool bool : Option.Bool.values()) {
            booleans.put(bool, bool.defaultValue);
        }
    }

    public int getInt(@NotNull Option.Int key) {
        return ints.get(key);
    }

    public @NotNull String getString(@NotNull Option.Text key) {
        return strings.get(key);
    }

    public boolean getBoolean(@NotNull Option.Bool key) {
        return booleans.get(key);
    }

}
