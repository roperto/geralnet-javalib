package net.geral.lib.networking;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.Arrays;

public class IPv4 implements Serializable {
  private static final long serialVersionUID = 1L;

  private static final int  BYTES            = 4;

  public static IPv4 toSQL(final String s) {
    return new IPv4(s);
  }

  byte[] ip = new byte[BYTES];

  public IPv4(final byte b1, final byte b2, final byte b3, final byte b4) {
    ip[0] = b1;
    ip[1] = b2;
    ip[2] = b3;
    ip[3] = b4;
  }

  public IPv4(final int b1, final int b2, final int b3, final int b4) {
    this((byte) b1, (byte) b2, (byte) b3, (byte) b4);
  }

  public IPv4(final String sIP) {
    final String[] parts = sIP.split("\\.");
    if (parts.length != 4) {
      throw new InvalidParameterException("Invalid IPv4: " + sIP);
    }
    for (int i = 0; i < BYTES; i++) {
      ip[i] = (byte) Integer.parseInt(parts[i]);
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
    final IPv4 other = (IPv4) obj;
    if (!Arrays.equals(ip, other.ip)) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + Arrays.hashCode(ip);
    return result;
  }

  public String toSQL() {
    return toString();
  }

  @Override
  public String toString() {
    return String.format("%d.%d.%d.%d", ip[0] & 0xFF, ip[1] & 0xFF,
        ip[2] & 0xFF, ip[3] & 0xFF);
  }
}
