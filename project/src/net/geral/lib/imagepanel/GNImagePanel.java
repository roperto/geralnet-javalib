package net.geral.lib.imagepanel;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class GNImagePanel extends JPanel {
  private static final long serialVersionUID = 1L;

  private Image             image            = null;

  @Override
  public void paint(final Graphics graphics) {
    final Graphics2D g = (Graphics2D) graphics;
    g.setColor(getBackground());
    g.fillRect(0, 0, getWidth(), getHeight());
    if (image != null) {
      g.drawImage(image, 0, 0, null);
    }
  }

  public void setImage(final Image img) {
    image = (img == null) ? new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB)
        : img;
    final Dimension size = new Dimension(image.getWidth(null),
        image.getHeight(null));
    setPreferredSize(size);
    setSize(size);
    repaint();
  }
}
