package net.geral.lib.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.Normalizer;
import java.util.Comparator;

public class StringUtils {
  private static final char[]             TO_HEX = { '0', '1', '2', '3', '4',
      '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
  private static final Comparator<String> comparator;

  static {
    comparator = new Comparator<String>() {
      @Override
      public int compare(final String a, final String b) {
        return StringUtils.compare(a, b);
      }
    };
  }

  public static Comparator<String> comparator() {
    return comparator;
  }

  public static int compare(String a, String b) {
    a = StringUtils.trim(removeAccents(a).toUpperCase());
    b = StringUtils.trim(removeAccents(b).toUpperCase());
    return a.compareTo(b);
  }

  public static String removeAccents(final String s) {
    return Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll(
        "\\p{InCombiningDiacriticalMarks}+", "");
  }

  public static String toHex(final byte[] bytes) {
    final StringBuilder sb = new StringBuilder(2 * bytes.length);
    for (final byte b : bytes) {
      sb.append(TO_HEX[(b >> 4) & 0xF]);
      sb.append(TO_HEX[b & 0xF]);
    }
    return sb.toString();
  }

  public static int toInt(final String s, final int ifInvalid) {
    try {
      return Integer.parseInt(s);
    } catch (final NumberFormatException e) {
      return ifInvalid;
    }
  }

  public static URL toURL(final String s) throws MalformedURLException {
    if (s.contains("://")) {
      return new URL(s);
    } else {
      return new File(s).toURI().toURL();
    }
  }

  public static String trim(final String s) {
    if (s == null) {
      return null;
    }
    return s.replaceAll("^\\s+", "").replaceAll("\\s+$", "");
  }

  private StringUtils() {
  }
}
