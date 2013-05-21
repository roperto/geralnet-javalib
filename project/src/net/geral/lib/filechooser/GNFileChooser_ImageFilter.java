package net.geral.lib.filechooser;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class GNFileChooser_ImageFilter extends FileFilter {
  @Override
  public boolean accept(final File f) {
    if (f.isDirectory()) {
      return true; // allow navigation
    }
    final String[] parts = f.getName().split("\\.");
    if (parts.length < 2) {
      return checkImageExtension(null);
    }
    final String extension = parts[parts.length - 1];
    return checkImageExtension(extension.toLowerCase());
  }

  protected boolean checkImageExtension(final String extension) {
    if ("jpeg".equals(extension)) {
      return true;
    }
    if ("jpg".equals(extension)) {
      return true;
    }
    if ("png".equals(extension)) {
      return true;
    }
    return false;
  }

  @Override
  public String getDescription() {
    return GNFileChooser.getTextOnlyImages();
  }
}