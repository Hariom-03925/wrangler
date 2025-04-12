package io.cdap.wrangler.api.parser;

public class ByteSize extends Token {
    private final double value;
    private final String unit;

    public ByteSize(String input) {
        super(input);
        input = input.trim().toUpperCase();
        if (input.endsWith("KB")) {
            value = Double.parseDouble(input.replace("KB", ""));
            unit = "KB";
        } else if (input.endsWith("MB")) {
            value = Double.parseDouble(input.replace("MB", ""));
            unit = "MB";
        } else if (input.endsWith("GB")) {
            value = Double.parseDouble(input.replace("GB", ""));
            unit = "GB";
        } else if (input.endsWith("TB")) {
            value = Double.parseDouble(input.replace("TB", ""));
            unit = "TB";
        } else {
            value = Double.parseDouble(input.replace("B", ""));
            unit = "B";
        }
    }

    public long getBytes() {
        switch (unit) {
            case "KB": return (long)(value * 1024);
            case "MB": return (long)(value * 1024 * 1024);
            case "GB": return (long)(value * 1024 * 1024 * 1024);
            case "TB": return (long)(value * 1024L * 1024 * 1024 * 1024);
            default: return (long)value;
        }
    }
}
