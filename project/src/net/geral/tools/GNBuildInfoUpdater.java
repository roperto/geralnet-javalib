package net.geral.tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class GNBuildInfoUpdater {
  private static String forDebug(final String value) {
    if (!value.equals("false")) {
      throw new RuntimeException("Cannot update DEBUG BuildInfo");
    }
    return value;
  }

  private static String forNow(final String value) {
    return "new LocalDateTime(" + System.currentTimeMillis() + "L)";
  }

  private static String forIncrement(final String value) {
    final int old = Integer.parseInt(value);
    return String.valueOf(old + 1);
  }

  public static void main(final String[] args) {
    int errors = 0;
    for (final String arg : args) {
      try {
        updateFile(arg);
      } catch (final Exception e) {
        System.err.println(e);
        errors++;
      }
    }
    System.exit(errors);
  }

  private static void updateFile(final String file) throws IOException {
    final StringBuilder sb = new StringBuilder();
    // read
    System.out.println("<< " + file);
    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
      String line;
      while ((line = br.readLine()) != null) {
        sb.append(updateLine(line));
        sb.append('\n');
      }
    }
    // write
    System.out.println(">> " + file);
    try (PrintWriter out = new PrintWriter(file)) {
      out.print(sb.toString());
    }
  }

  private static String updateLine(final String line) {
    if (!line.contains("[BUILDINFO:")) {
      return line;
    }

    String[] parts;
    final String left;
    String oldValue;
    String newValue;
    final String right;

    parts = line.split("=", 2);
    if (parts.length != 2) {
      return line;
    }
    left = parts[0];
    parts = parts[1].split(";", 2);
    if (parts.length != 2) {
      return line;
    }
    oldValue = parts[0];
    right = parts[1];

    newValue = updateValue(line, oldValue);
    System.out.println(oldValue + " => " + newValue);
    return String.format("%s=%s;%s", left, newValue, right);
  }

  private static String updateValue(final String line, String value) {
    value = value.replaceAll("^\\s+", "").replaceAll("\\s+$", "");
    if (line.contains("[BUILDINFO:DEBUG]")) {
      return forDebug(value);
    }
    if (line.contains("[BUILDINFO:NOW]")) {
      return forNow(value);
    }
    if (line.contains("[BUILDINFO:INCREMENT]")) {
      return forIncrement(value);
    }
    return value;
  }
}
