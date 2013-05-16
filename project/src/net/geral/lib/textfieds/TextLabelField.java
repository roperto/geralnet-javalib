package net.geral.lib.textfieds;

import java.awt.Color;

import javax.swing.JTextField;
import javax.swing.border.Border;

public class TextLabelField extends JTextField {
  private static final long serialVersionUID   = 1L;
  private Border            previousBorder     = getBorder();
  private Color             previousBackground = getBackground();
  private boolean           previousOpaque     = isOpaque();
  private Color             previousForeground = getForeground();

  @Override
  public void setEditable(final boolean yn) {
    if (isEditable() == yn) {
      return;
    }
    if (yn) {
      setBorder(previousBorder);
      setBackground(previousBackground);
      setOpaque(previousOpaque);
      setForeground(previousForeground);
    } else {
      previousBorder = getBorder();
      previousBackground = getBackground();
      previousOpaque = isOpaque();
      previousForeground = getForeground();
      setBorder(null);
      setBackground(new Color(0, 0, 0, 0));
      setOpaque(false);
      setForeground(Color.BLACK);
    }
    super.setEditable(yn);
  }
}
