package net.geral.lib.filechooser;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;

import org.apache.log4j.Logger;

public class GNFileChooser extends JFileChooser {
  private static final long   serialVersionUID = 1L;
  private static final Logger logger           = Logger
                                                   .getLogger(GNFileChooser.class);

  private static String       textOnlyImages   = "Only Images";

  public static String getTextOnlyImages() {
    return textOnlyImages;
  }

  public static GNFileChooser makeImageChooser() {
    final GNFileChooser fc = new GNFileChooser();
    final FileFilter imageFilter = new GNFileChooser_ImageFilter();
    fc.addChoosableFileFilter(imageFilter);
    fc.setFileFilter(imageFilter);
    fc.setAccessory(new GNFileChooser_ImagePreview(fc));
    return fc;
  }

  public static void setTextImagesOnly(final String s) {
    textOnlyImages = s;
  }

  public GNFileChooser() {
    try {
      final LookAndFeel old = UIManager.getLookAndFeel();
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      SwingUtilities.updateComponentTreeUI(this);
      UIManager.setLookAndFeel(old);
    } catch (final Exception e) {
      logger.warn(e, e);
    }
  }

  public File open(final Component component) {
    final int i = showOpenDialog(component);
    if (i != APPROVE_OPTION) {
      return null;
    }
    return getSelectedFile();
  }

  public File save(final Component component) {
    final int i = showSaveDialog(component);
    if (i != APPROVE_OPTION) {
      return null;
    }
    return getSelectedFile();
  }
}
