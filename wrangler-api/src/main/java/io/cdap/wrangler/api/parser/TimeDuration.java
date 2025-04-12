package io.cdap.wrangler.api.parser;

public class TimeDuration extends Token {
  private final double value;
  private final String unit;

  public TimeDuration(String input) {
    super(input);
    input = input.trim().toLowerCase();

    if (input.endsWith("ms")) {
      value = Double.parseDouble(input.replace("ms", ""));
      unit = "ms";
    } else if (input.endsWith("s") || input.endsWith("sec") || input.endsWith("seconds")) {
      value = Double.parseDouble(input.replaceAll("(s|sec|seconds)$", ""));
      unit = "s";
    } else if (input.endsWith("m") || input.endsWith("min") || input.endsWith("minutes")) {
      value = Double.parseDouble(input.replaceAll("(m|min|minutes)$", ""));
      unit = "m";
    } else {
      throw new IllegalArgumentException("Unsupported time unit in: " + input);
    }
  }

  public long getMillis() {
    switch (unit) {
      case "ms": return (long)(value);
      case "s":  return (long)(value * 1000);
      case "m":  return (long)(value * 1000 * 60);
      default: throw new IllegalStateException("Unknown time unit: " + unit);
    }
  }
}
