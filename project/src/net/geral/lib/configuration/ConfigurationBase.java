package net.geral.lib.configuration;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;

import net.geral.lib.strings.GNStrings;
import net.geral.lib.table.GNTableColumnWidth;

import org.apache.log4j.Logger;

public abstract class ConfigurationBase {
  private static final Logger logger = Logger
                                         .getLogger(ConfigurationBase.class);

  public ArrayList<String> parse(final boolean verbose,
      final ArrayList<URL> configFiles) throws ConfigurationException {
    final ArrayList<String> ignored = new ArrayList<>();
    if (verbose) {
      System.out.println("[verbose] Loading configuration files...");
    }
    for (final URL url : configFiles) {
      try (BufferedReader reader = new BufferedReader(new InputStreamReader(url
          .openConnection().getInputStream()))) {
        if (verbose) {
          System.out.println("[verbose] Loading: " + url);
        }
        String line;
        while ((line = reader.readLine()) != null) {
          line = GNStrings.trim(line);
          if (line.length() == 0) {
            if (verbose) {
              System.out.println("[verbose] Ignoring empty line.");
            }
            continue;
          }
          if (!line.matches("^[A-Z].*")) {
            if (verbose) {
              System.out
                  .println("[verbose] Ignoring invalid line (not uppercase start): "
                      + line);
              ignored.add(line);
            }
            continue;
          }
          if (verbose) {
            System.out.println("[verbose] Setting: " + line);
          }
          set(line);
        }
      } catch (final Exception e) {
        final ConfigurationException ce = new ConfigurationException(
            "Cannot process configuration file: " + url, e);
        if (verbose) {
          System.out.println(ce.getMessage());
        }
        throw ce;
      }
    }
    if (verbose) {
      System.out.println("[verbose] Finished configuration loading ("
          + ignored.size() + " ignored).");
    }
    return ignored;
  }

  public void set(String value) throws ConfigurationException {
    if (value == null) {
      throw new ConfigurationException("value cannot be null.");
    }
    final String[] parts = value.split("=", 2);
    final String name = parts[0];
    value = (parts.length == 2) ? parts[1] : "";
    set(name, value);
  }

  public void set(final String name, String value)
      throws ConfigurationException {
    try {
      value = GNStrings.trim(value);
      final Field field = this.getClass().getDeclaredField(name);
      final String type = field.getType().toString();
      if ("".equals(type)) {
        throw new ConfigurationException("Undetected type for: " + name); // should
                                                                          // never
                                                                          // happen!
      } else if ("boolean".equals(type)) {
        field.setBoolean(this, toBoolean(value));
      } else if ("int".equals(type)) {
        field.setInt(this, toInt(value));
      } else if ("class java.lang.String".equals(type)) {
        field.set(this, value);
      } else if ("class java.awt.Dimension".equals(type)) {
        field.set(this, toDimension(value));
      } else if ("class java.awt.Point".equals(type)) {
        field.set(this, toPoint(value));
      } else if ("class java.awt.Color".equals(type)) {
        field.set(this, toColor(value));
      } else if ("class net.geral.lib.table.GNTableColumnWidth".equals(type)) {
        field.set(this, GNTableColumnWidth.fromString(value));
      } else {
        throw new ConfigurationException("Invalid type (" + type + ") for: "
            + name);
      }
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }

  public void set(final String[] values) throws ConfigurationException {
    if (values == null) {
      throw new ConfigurationException("values cannot be null.");
    }
    for (final String s : values) {
      set(s);
    }
  }

  private boolean toBoolean(String value) throws ConfigurationException {
    value = value.toUpperCase();
    if ("TRUE".equals(value)) {
      return true;
    }
    if ("FALSE".equals(value)) {
      return false;
    }
    if ("T".equals(value)) {
      return true;
    }
    if ("F".equals(value)) {
      return false;
    }
    if ("YES".equals(value)) {
      return true;
    }
    if ("NO".equals(value)) {
      return false;
    }
    if ("Y".equals(value)) {
      return true;
    }
    if ("N".equals(value)) {
      return false;
    }
    if ("ON".equals(value)) {
      return true;
    }
    if ("OFF".equals(value)) {
      return false;
    }
    if ("1".equals(value)) {
      return true;
    }
    if ("0".equals(value)) {
      return false;
    }
    throw new ConfigurationException("Invalid boolean: " + value
        + ". Expected: TRUE/T/YES/Y/ON/1 or FALSE/F/NO/N/OFF/0");
  }

  private Object toColor(final String value) throws ConfigurationException {
    if ((value.length() == 0) || ("null".equals(value))) {
      return null;
    }
    final String[] parts = value.split(",");
    if (parts.length != 3) {
      throw new ConfigurationException("Invalid color: " + value
          + ". Expected: R,G,B");
    }
    try {
      final int r = Integer.parseInt(parts[0]);
      final int g = Integer.parseInt(parts[1]);
      final int b = Integer.parseInt(parts[2]);
      return new Color(r, g, b);
    } catch (final NumberFormatException e) {
      throw new ConfigurationException("Not a valid integer for color: "
          + value, e);
    } catch (final IllegalArgumentException e) {
      throw new ConfigurationException("Invalid color: " + value
          + " -- R,G,B must be from 0 to 255.", e);
    }
  }

  private Dimension toDimension(final String value)
      throws ConfigurationException {
    if ((value.length() == 0) || ("null".equals(value))) {
      return null;
    }
    final String[] parts = value.split(",");
    if (parts.length != 2) {
      throw new ConfigurationException("Invalid dimension: " + value
          + ". Expected: [width],[height]");
    }
    try {
      final int w = Integer.parseInt(parts[0]);
      final int h = Integer.parseInt(parts[1]);
      return new Dimension(w, h);
    } catch (final NumberFormatException e) {
      throw new ConfigurationException("Not a valid integer for dimension: "
          + value, e);
    }
  }

  private int toInt(final String value) throws ConfigurationException {
    try {
      return Integer.parseInt(value);
    } catch (final NumberFormatException e) {
      throw new ConfigurationException("Invalid integer: " + value, e);
    }
  }

  private Point toPoint(final String value) throws ConfigurationException {
    if ((value.length() == 0) || ("null".equals(value))) {
      return null;
    }
    final String[] parts = value.split(",");
    if (parts.length != 2) {
      throw new ConfigurationException("Invalid point: " + value
          + ". Expected: [x],[y]");
    }
    try {
      final int x = Integer.parseInt(parts[0]);
      final int y = Integer.parseInt(parts[1]);
      return new Point(x, y);
    } catch (final NumberFormatException e) {
      throw new ConfigurationException("Not a valid integer for point: "
          + value, e);
    }
  }

  @Override
  public String toString() {
    final StringBuilder builder = new StringBuilder();
    for (final Field field : this.getClass().getFields()) {
      builder.append(field.getName());
      builder.append('=');
      try {
        builder.append(toString(field.get(this)));
      } catch (IllegalArgumentException | IllegalAccessException e) {
        logger.warn("Cannot read field: " + field.getName(), e);
      }
      builder.append('\n');
    }
    return builder.toString();
  }

  private Object toString(final Object o) {
    if (o == null) {
      return "";
    }
    if (o instanceof String) {
      return o;
    }
    if (o instanceof Boolean) {
      return o.toString();
    }
    if (o instanceof Integer) {
      return o.toString();
    }
    if (o instanceof Dimension) {
      return ((Dimension) o).width + "," + ((Dimension) o).height;
    }
    if (o instanceof Point) {
      return ((Point) o).x + "," + ((Point) o).y;
    }
    logger.warn("Cannot fetch string for: " + o.getClass().toString());
    return "";
  }
}
