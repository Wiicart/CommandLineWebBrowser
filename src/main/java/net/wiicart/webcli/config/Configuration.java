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

    private static final String FILE_NAME = "config.yml";

    private static Configuration instance;

    private final EnumMap<Option.Int, Integer> ints = new EnumMap<>(Option.Int.class);
    private final EnumMap<Option.Text, String> strings = new EnumMap<>(Option.Text.class);

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
        File file = new File(FILE_NAME);
        if(!file.exists()) {
            writeDefaultConfig(file);
        }

        try (FileInputStream stream = new FileInputStream(file)) {
            Yaml yaml = new Yaml();
            Map<String, Object> config = yaml.load(stream);

            loadInts(config);
            loadStrings(config);
        } catch(Exception e) {
            Debug.log("Configuration file not found: " + FILE_NAME);
            useDefaults();
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void writeDefaultConfig(@NotNull File file) {
        try(InputStream input = ResourceManager.loadResource("config.yml")) {
            // Make sure parent directories exist
            File parent = file.getParentFile();
            if(!parent.exists()) {
                parent.mkdirs();
            }

            try(FileOutputStream out = new FileOutputStream(file)) {
                input.transferTo(out);
            }

            Debug.log("Wrote default config.yml to " + file.getAbsolutePath());
        } catch(Exception e) {
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

    // Load in default values from the Enums
    private void useDefaults() {
        for(Option.Int option : Option.Int.values()) {
            ints.put(option, option.defaultValue);
        }

        for(Option.Text option : Option.Text.values()) {
            strings.put(option, option.defaultValue);
        }
    }

    public int getInt(@NotNull Option.Int key) {
        return ints.get(key);
    }

    public @NotNull String getString(@NotNull Option.Text key) {
        return strings.get(key);
    }

}
