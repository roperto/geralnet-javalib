package net.geral.lib.strings;

import static org.junit.Assert.fail;

import net.geral.lib.util.StringUtils;

import org.junit.Test;

public class GNStringsTest {
  @Test
  public void testRemoveAccents() {
    final String in = "ábcdèfghïjklmnõpqrstüvwxýz";
    final String out = StringUtils.removeAccents(in);
    final String expected = "abcdefghijklmnopqrstuvwxyz";
    if (!out.equals(expected)) {
      fail("Invalid output: " + out);
    }
  }
}
