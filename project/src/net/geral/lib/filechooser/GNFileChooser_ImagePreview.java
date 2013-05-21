package net.geral.lib.filechooser;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;

public class GNFileChooser_ImagePreview extends JComponent implements
    PropertyChangeListener {
  private static final long      serialVersionUID = 1L;
  private static final Dimension PREFERRED_SIZE   = new Dimension(200, 100);

  private ImageIcon              icon             = null;
  private File                   file             = null;

  public GNFileChooser_ImagePreview(final GNFileChooser fc) {
    setPreferredSize(PREFERRED_SIZE);
    fc.addPropertyChangeListener(this);
  }

  @Override
  protected void paintComponent(final Graphics g) {
    if (icon == null) {
      reload();
    }
    if (icon != null) {
      int x = (getWidth() / 2) - (icon.getIconWidth() / 2);
      int y = (getHeight() / 2) - (icon.getIconHeight() / 2);

      if (y < 0) {
        y = 0;
      }

      if (x < 5) {
        x = 5;
      }
      icon.paintIcon(this, g, x, y);
    }
  }

  @Override
  public void propertyChange(final PropertyChangeEvent evt) {
    final String prop = evt.getPropertyName();

    if (JFileChooser.DIRECTORY_CHANGED_PROPERTY.equals(prop)) {
      file = null;
    } else if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(prop)) {
      file = (File) evt.getNewValue();
    } else {
      return;
    }

    icon = null;
    if (isShowing()) {
      reload();
      repaint();
    }
  }

  public void reload() {
    if (file == null) {
      icon = null;
      return;
    }

    ImageIcon tmp = new ImageIcon(file.getPath());
    if (tmp != null) {
      tmp = new ImageIcon(tmp.getImage().getScaledInstance(
          PREFERRED_SIZE.width - 2, -1, Image.SCALE_SMOOTH));
    }
    icon = tmp;
  }
}
