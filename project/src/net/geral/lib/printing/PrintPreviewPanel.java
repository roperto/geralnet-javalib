package net.geral.lib.printing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterException;

import javax.swing.JPanel;

import org.apache.log4j.Logger;

public class PrintPreviewPanel extends JPanel {
  private static final long   serialVersionUID     = 1L;
  private static final Color  EMPTY_COLOR          = new Color(200, 200, 255);
  private static final Color  PAGE_SEPARATOR_COLOR = new Color(255, 127, 127);
  private static final Logger logger               = Logger
                                                       .getLogger(PrintPreviewPanel.class);

  private final PrintDocument document;
  private BufferedImage       image;

  public PrintPreviewPanel(final PrintDocument doc) {
    setBackground(Color.WHITE);
    document = doc;
  }

  private void createImage() {
    final int width = (int) document.getFormat().getWidth();
    final double height = document.getFormat().getHeight();
    image = new BufferedImage(width, (int) height, BufferedImage.TYPE_INT_RGB);
    Graphics g = image.createGraphics();
    g.setColor(Color.WHITE);
    g.fillRect(0, 0, image.getWidth(), image.getHeight());
    try {
      document.print(g, document.getFormat(), 0);
      int nPages = document.getNumberOfPages();
      if (nPages > 1) {
        // more than one page, resize and draw again
        image = new BufferedImage(width, (int) (nPages * height),
            BufferedImage.TYPE_INT_RGB);
        g = image.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, image.getWidth(), image.getHeight());
        g.setColor(PAGE_SEPARATOR_COLOR);
        for (int i = 1; i < nPages; i++) {
          g.drawLine(0, (int) (i * height), width, (int) (i * height));
        }
        document.print(g, document.getFormat(), 0);
      } else {
        nPages = 1;
      }
      setPreferredSize(new Dimension(width, (int) (nPages * height)));
    } catch (final PrinterException e) {
      logger.warn(e, e);
    }
  }

  @Override
  public void paint(final Graphics g) {
    super.paint(g);
    g.setColor(EMPTY_COLOR);
    g.fillRect(0, 0, getWidth(), getHeight());
    if (document == null) {
      return;
    }
    if (image == null) {
      createImage();
    }
    g.drawImage(image, 0, 0, null);
  }
}
