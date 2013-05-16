package net.geral.lib.strings;

import static org.junit.Assert.fail;

import org.junit.Test;

public class GNStringsTest {
  @Test
  public void testRemoveAccents() {
    final String in = "ábcdèfghïjklmnõpqrstüvwxýz";
    final String out = GNStrings.removeAccents(in);
    final String expected = "abcdefghijklmnopqrstuvwxyz";
    if (!out.equals(expected)) {
      fail("Invalid output: " + out);
    }
  }
}
