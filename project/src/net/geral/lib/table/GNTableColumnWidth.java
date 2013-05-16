package net.geral.lib.table;

import javax.swing.table.TableColumn;

public class GNTableColumnWidth {
  public static GNTableColumnWidth fromString(final String value) {
    try {
      final String parts[] = value.split(";");
      if (parts.length == 1) {
        return new GNTableColumnWidth(Integer.valueOf(parts[0]));
      }
      if (parts.length != 3) {
        throw new IllegalArgumentException("Invalid format: " + value
            + " -- Accepted: [all] or [min];[pref];[max]");
      }
      final int min = Integer.valueOf(parts[0]);
      final int pref = Integer.valueOf(parts[1]);
      final int max = Integer.valueOf(parts[2]);
      return new GNTableColumnWidth(min, pref, max);
    } catch (final NumberFormatException e) {
      throw new IllegalArgumentException("Invalid integer for: " + value, e);
    }
  }

  private final int minimum;
  private final int preferred;

  private final int maximum;

  public GNTableColumnWidth(final int allWidths) {
    this(allWidths, allWidths, allWidths);
  }

  public GNTableColumnWidth(final int min, final int pref, final int max) {
    minimum = min;
    preferred = pref;
    maximum = max;
  }

  public void apply(final TableColumn c) {
    c.setMinWidth(minimum);
    c.setMaxWidth(maximum);
    c.setPreferredWidth(preferred);
    c.setResizable(isResizable());
  }

  public int getWidthMax() {
    return maximum;
  }

  public int getWidthMin() {
    return minimum;
  }

  public int getWidthPref() {
    return preferred;
  }

  public boolean isResizable() {
    return ((minimum <= 0) || (minimum != preferred) || (preferred != maximum));
  }

  public GNTableColumnWidth withMax(final int max) {
    return new GNTableColumnWidth(minimum, preferred, max);
  }

  public GNTableColumnWidth withMin(final int min) {
    return new GNTableColumnWidth(min, preferred, maximum);
  }

  public GNTableColumnWidth withMinMax(final int w) {
    return withMinPref(w, w);
  }

  public GNTableColumnWidth withMinMax(final int min, final int max) {
    return new GNTableColumnWidth(min, preferred, max);
  }

  public GNTableColumnWidth withMinPref(final int w) {
    return withMinPref(w, w);
  }

  public GNTableColumnWidth withMinPref(final int min, final int pref) {
    return new GNTableColumnWidth(min, pref, maximum);
  }

  public GNTableColumnWidth withPref(final int pref) {
    return new GNTableColumnWidth(minimum, pref, maximum);
  }

  public GNTableColumnWidth withPrefMax(final int w) {
    return withMinPref(w, w);
  }

  public GNTableColumnWidth withPrefMax(final int pref, final int max) {
    return new GNTableColumnWidth(minimum, pref, max);
  }
}
