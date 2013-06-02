package net.geral.lib.strings;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.Normalizer;
import java.util.Comparator;

public abstract class GNStrings {
  public static final Comparator<String> Comparator = new Comparator<String>() {
                                                      @Override
                                                      public int compare(
                                                          final String a,
                                                          final String b) {
                                                        return GNStrings
                                                            .compare(a, b);
                                                      }
                                                    };

  public static int compare(final String a, final String b) {
    return GNStrings.trim(removeAccents(a).toUpperCase()).compareTo(
        GNStrings.trim(removeAccents(b).toUpperCase()));
  }

  public static String removeAccents(final String s) {
    return Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll(
        "\\p{InCombiningDiacriticalMarks}+", "");
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
}
