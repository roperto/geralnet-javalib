package net.geral.lib.strings;

import java.io.File;
import java.io.IOException;

//TODO move to
public final class _Conversor {
  private static final char[] TO_HEX = { '0', '1', '2', '3', '4', '5', '6',
      '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

  public static String FileArrayPaths(final File[] files, final String separator) {
    final StringBuilder sb = new StringBuilder();
    boolean first = true;
    for (final File f : files) {
      if (first) {
        first = false;
      } else {
        sb.append(separator);
      }
      sb.append(_Conversor.FilePath(f));
    }
    return sb.toString();
  }

  public static String FilePath(final File f) {
    try {
      return f.getCanonicalPath();
    } catch (final IOException e) {
      return f.getAbsolutePath();
    }
  }

  public static int intOrZero(String s) {
    s = s.replaceAll("[^\\d]", "");
    if (s.length() == 0) {
      return 0;
    }
    return Integer.parseInt(s);
  }

  public static long longOrZero(String s) {
    s = s.replaceAll("[^\\d]", "");
    if (s.length() == 0) {
      return 0L;
    }
    return Long.parseLong(s);
  }

  public static String[] splitOrEmpty(final String toSplit, final String regex) {
    if (toSplit.length() == 0) {
      return new String[0];
    }
    return toSplit.split(regex);
  }

  public static String StringArray(final String[] strings,
      final boolean quotes, final String separator) {
    final StringBuilder sb = new StringBuilder();
    boolean first = true;
    for (final String s : strings) {
      if (first) {
        first = false;
      } else {
        sb.append(separator);
      }
      if (quotes) {
        sb.append('"');
      }
      sb.append(s);
      if (quotes) {
        sb.append('"');
      }
    }
    return sb.toString();
  }

  public static String stringOrEmpty(final String s, final boolean trim) {
    if (s == null) {
      return "";
    }
    return trim ? GNStrings.trim(s) : s;
  }

  public static String toAZ09(String s, final boolean phonetic) {
    s = GNStrings.trim(s).toLowerCase();
    if (s.length() == 0) {
      return "";
    }

    s = s.replaceAll("[æ]", "ae");
    s = s.replaceAll("[äáàâã]", "a");
    s = s.replaceAll("[ëéèê]", "e");
    s = s.replaceAll("[ïíìî]", "i");
    s = s.replaceAll("[öóòôõ]", "o");
    s = s.replaceAll("[üúùû]", "u");
    s = s.replaceAll("[ñ]", "n");
    s = s.replaceAll("[ÿý]", "y");
    s = s.replaceAll("[ç]", phonetic ? "s" : "c");
    s = s.replaceAll("[^a-z0-9 ]", " ");

    return s;
  }

  public static String toHex(final byte[] bytes) {
    final StringBuilder sb = new StringBuilder(2 * bytes.length);
    for (final byte b : bytes) {
      sb.append(TO_HEX[(b >> 4) & 0xF]);
      sb.append(TO_HEX[b & 0xF]);
    }
    return sb.toString();
  }

  public static String toPhonetic(String s) {
    s = toAZ09(s, true);

    s = s.replaceAll("b", "p");
    s = s.replaceAll("ce", "se");
    s = s.replaceAll("ci", "si");
    s = s.replaceAll("cha", "sa");
    s = s.replaceAll("che", "se");
    s = s.replaceAll("chi", "si");
    s = s.replaceAll("cho", "so");
    s = s.replaceAll("chu", "su");
    s = s.replaceAll("c", "k");
    s = s.replaceAll("e", "i");
    s = s.replaceAll("ge", "je");
    s = s.replaceAll("gi", "ji");
    s = s.replaceAll("h", "");
    s = s.replaceAll("l", "u");
    s = s.replaceAll("n", "m");
    s = s.replaceAll("o", "u");
    s = s.replaceAll("que", "ke");
    s = s.replaceAll("qui", "ki");
    s = s.replaceAll("q", "k");
    s = s.replaceAll("w", "v");
    s = s.replaceAll("x", "s");
    s = s.replaceAll("y", "i");
    s = s.replaceAll("z", "s");

    for (int i = 0; i < s.length(); i++) {
      final String c = String.valueOf(s.charAt(i));
      s = s.replaceAll(c + c, c);
    }

    return s;
  }

  // i is the start index of str1, j is the start index of str2
  private _Conversor() {
  }
}
