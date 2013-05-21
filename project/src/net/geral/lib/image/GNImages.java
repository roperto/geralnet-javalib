package net.geral.lib.image;

import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

public class GNImages {
  public static final Logger logger = Logger.getLogger(GNImages.class);

  public static final byte[] toByteArray(final Image img) {
    try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
      ImageIO.write((RenderedImage) img, "png", baos);
      baos.flush();
      return baos.toByteArray();
    } catch (final Exception e) {
      logger.warn(e, e);
      return null;
    }
  }

  private GNImages() {
  }
}
