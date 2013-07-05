package net.geral.lib.util;

//TODO improve (localization), maybe move to essomerie?
public final class PhoneticUtils {
  public static String toAZ09(String s, final boolean phonetic) {
    s = StringUtils.trim(s).toLowerCase();
    if (s.length() == 0) {
      return "";
    }

    s = s.replaceAll("[ז]", "ae");
    s = s.replaceAll("[הבאגד]", "a");
    s = s.replaceAll("[כיטך]", "e");
    s = s.replaceAll("[ןםלמ]", "i");
    s = s.replaceAll("[צףעפץ]", "o");
    s = s.replaceAll("[üתשû]", "u");
    s = s.replaceAll("[ס]", "n");
    s = s.replaceAll("[ÿ‎]", "y");
    s = s.replaceAll("[ח]", phonetic ? "s" : "c");
    s = s.replaceAll("[^a-z0-9 ]", " ");

    return s;
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
  private PhoneticUtils() {
  }
}
