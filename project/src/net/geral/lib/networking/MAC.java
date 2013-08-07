package net.geral.lib.networking;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.Arrays;

public class MAC implements Serializable {
  private static final long serialVersionUID = 1L;
  private static final int  BYTES            = 6;

  public static MAC fromSQL(final String s) {
    return new MAC(s);
  }

  private final byte[]     mac   = new byte[BYTES];
  private transient String cache = null;

  public MAC(final byte b6, final byte b5, final byte b4, final byte b3,
      final byte b2, final byte b1) {
    mac[0] = b1;
    mac[1] = b2;
    mac[2] = b3;
    mac[3] = b4;
    mac[4] = b5;
    mac[5] = b6;
  }

  public MAC(final int b6, final int b5, final int b4, final int b3,
      final int b2, final int b1) {
    this((byte) b6, (byte) b5, (byte) b4, (byte) b3, (byte) b2, (byte) b1);
  }

  public MAC(final String sMAC) {
    final String m = sMAC.toUpperCase().replaceAll("[^A-F0-9]", "");
    if (m.length() != 12) {
      throw new InvalidParameterException("Invalid MAC: " + sMAC);
    }

    for (int i = 0; i < BYTES; i++) {
      final int from = i * 2;
      final int to = from + 2;
      final String part = m.substring(from, to);
      mac[BYTES - i - 1] = (byte) Integer.parseInt(part, 16);
    }
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final MAC other = (MAC) obj;
    if (!Arrays.equals(mac, other.mac)) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + Arrays.hashCode(mac);
    return result;
  }

  public String toSQL() {
    return String.format("%02X%02X%02X%02X%02X%02X", mac[5] & 0xFF,
        mac[4] & 0xFF, mac[3] & 0xFF, mac[2] & 0xFF, mac[1] & 0xFF,
        mac[0] & 0xFF);
  }

  @Override
  public String toString() {
    if (cache == null) {
      cache = String.format("%02X:%02X:%02X:%02X:%02X:%02X", mac[5] & 0xFF,
          mac[4] & 0xFF, mac[3] & 0xFF, mac[2] & 0xFF, mac[1] & 0xFF,
          mac[0] & 0xFF);
    }
    return cache;
  }
}
