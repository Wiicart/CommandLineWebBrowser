package net.wiicart.karatasi;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Offers utility methods to load JAR resources. Does not work for external resources.
 */
public final class ResourceManager {

    private ResourceManager() {}

    // https://stackoverflow.com/questions/20389255/reading-a-resource-file-from-within-jar
    @Contract(pure = true)
    public static @NotNull String loadLocalResource(@NotNull String path) throws Exception {
        StringBuilder builder = new StringBuilder();
        try (InputStream stream = ResourceManager.class.getResourceAsStream(path)) {
            if (stream == null) {
                return "";
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            reader.lines().forEach(builder::append);
        } catch (final IOException e) {
            throw new Exception(e);
        }

        return builder.toString();
    }

    public static @NotNull List<String> loadLocalResourceAsList(@NotNull String path) throws Exception {
        List<String> resource = new ArrayList<>();
        try (InputStream stream = ResourceManager.class.getResourceAsStream(path)) {
            if (stream == null) {
                return resource;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            reader.lines().forEach(resource::add);
        } catch(final IOException e) {
            throw new Exception(e);
        }

        return resource;
    }

    public static @NotNull InputStream loadResource(@NotNull String path) throws IOException {
        InputStream stream = ResourceManager.class.getResourceAsStream(path);
        if (stream == null) {
            throw new IOException("Resource not found: " + path);
        }

        return stream;
    }
}
