package net.geral.lib.printing;

import java.awt.Color;
import java.awt.print.PageFormat;
import java.awt.print.Paper;

public class TestDocument extends ContinuousPaperPrintDocument {
  @Override
  public PageFormat getFormat() {
    final Paper paper = new Paper();
    paper.setSize(printWidth, printHeight);
    paper.setImageableArea(0, 0, printHeight, printHeight);
    final PageFormat format = new PageFormat();
    format.setOrientation(PageFormat.PORTRAIT);
    format.setPaper(paper);
    return format;
  }

  @Override
  protected void print() {
    final int w = printWidth;
    int h = 2 * printHeight;
    g.setColor(Color.GRAY);
    g.drawLine(0, 0, w, 0);
    g.drawLine(0, h, w, h);
    g.drawLine(0, 0, 0, h);
    g.drawLine(w, 0, w, h);
    g.drawLine(0, 0, w, h);
    g.drawLine(w, 0, 0, h);
    g.setColor(Color.BLACK);
    h = printHeight;
    g.drawLine(0, 0, w, 0);
    g.drawLine(0, h, w, h);
    g.drawLine(0, 0, 0, h);
    g.drawLine(w, 0, w, h);
    g.drawLine(0, 0, w, h);
    g.drawLine(w, 0, 0, h);

    feed(2 * printHeight);
  }
}
