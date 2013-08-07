package net.geral.lib.networking;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class IPv4_Test {
  @Test
  public void testIPv4() {
    final IPv4 ip1 = new IPv4("192.168.00.1");
    final IPv4 ip2 = new IPv4(192, 168, 0, 1);
    assertEquals(ip1, ip2);
    assertEquals(ip1.toString(), "192.168.0.1");
    assertEquals(ip1.toSQL(), ip1.toString());
  }
}
