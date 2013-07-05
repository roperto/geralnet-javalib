package net.geral.lib.util;

import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;

public class HTTPUtils {
  public static Exception browse(final String url) {
    try {
      return browse(new URI(url));
    } catch (final URISyntaxException e) {
      return e;
    }
  }

  public static Exception browse(final URI uri) {
    if (!Desktop.isDesktopSupported()) {
      return new Exception("Desktop not supported.");
    }
    final Desktop desktop = Desktop.getDesktop();
    if (!desktop.isSupported(Desktop.Action.BROWSE)) {
      return new Exception("Desktop - Browser not supported.");
    }
    try {
      desktop.browse(uri);
      return null;
    } catch (final Exception e) {
      return e;
    }
  }

  private HTTPUtils() {
  }
}
