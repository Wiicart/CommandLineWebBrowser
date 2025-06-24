package net.wiicart.webcli.config;

public final class Option {

    private Option() {}

    public enum Int {
        TIMEOUT("timeout", 10000);

        final String key;
        final int defaultValue;

        Int(final String key, final int defaultValue) {
            this.key = key;
            this.defaultValue = defaultValue;
        }
    }

    public enum Text {
        RENDERER("renderer", "primitive");

        final String key;
        final String defaultValue;

        Text(final String key, final String defaultValue) {
            this.key = key;
            this.defaultValue = defaultValue;
        }
    }

    public enum Bool {
        ALLOW_EDITING_LOCAL("allow-edits", false);

        final String key;
        final boolean defaultValue;

        Bool(final String key, final boolean defaultValue) {
            this.key = key;
            this.defaultValue = defaultValue;
        }
    }
}
