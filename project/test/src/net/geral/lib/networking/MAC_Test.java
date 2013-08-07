package net.geral.lib.networking;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MAC_Test {
  @Test
  public void testMAC() {
    final MAC m1 = new MAC("01:23:45:67:89:AB");
    final MAC m2 = new MAC(0x01, 0x23, 0x45, 0x67, 0x89, 0xAB);
    assertEquals(m1, m2);
    assertEquals(m1.toString(), "01:23:45:67:89:AB");
    assertEquals(m1.toSQL(), "0123456789AB");
  }
}
