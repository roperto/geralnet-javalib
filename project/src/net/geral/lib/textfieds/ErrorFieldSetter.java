package net.geral.lib.textfieds;

import java.awt.Color;
import java.util.HashMap;

import javax.swing.JComponent;
import javax.swing.border.Border;

/**
 * Must be used one per panel panel/container since it will hold references to
 * objects.
 * 
 * @author Roperto
 * 
 */
public class ErrorFieldSetter {
  private static Color  defaultBackground = new Color(255, 200, 200);
  private static Color  defaultForeground = null;
  private static Border defaultBorder     = null;

  public static void initialize(final Color background, final Color foreground,
      final Border border) {
    defaultBackground = background;
    defaultForeground = foreground;
    defaultBorder = border;
  }

  private final HashMap<JComponent, Color>  originalBackground = new HashMap<>();
  private final HashMap<JComponent, Color>  originalForeground = new HashMap<>();
  private final HashMap<JComponent, Border> originalBorder     = new HashMap<>();
  private final Color                       background;
  private final Color                       foreground;
  private final Border                      border;

  public ErrorFieldSetter() {
    this(defaultBackground, defaultForeground, defaultBorder);
  }

  public ErrorFieldSetter(final Color background, final Color foreground,
      final Border border) {
    this.background = background;
    this.foreground = foreground;
    this.border = border;
  }

  public void error(final JComponent c) {
    if (background != null) {
      if (originalBackground.get(c) == null) {
        originalBackground.put(c, c.getBackground());
      }
      c.setBackground(background);
    }

    if (foreground != null) {
      if (originalForeground.get(c) == null) {
        originalForeground.put(c, c.getForeground());
      }
      c.setForeground(foreground);
    }

    if (border != null) {
      if (originalBorder.get(c) == null) {
        originalBorder.put(c, c.getBorder());
      }
      c.setBorder(border);
    }
  }

  public void set(final JComponent c, final boolean isValid) {
    if (isValid) {
      valid(c);
    } else {
      error(c);
    }
  }

  public void valid(final JComponent c) {
    final Color bg = originalBackground.get(c);
    if (bg != null) {
      c.setBackground(bg);
    }

    final Color fg = originalForeground.get(c);
    if (fg != null) {
      c.setForeground(fg);
    }

    final Border b = originalBorder.get(c);
    if (b != null) {
      c.setBorder(b);
    }
  }
}
