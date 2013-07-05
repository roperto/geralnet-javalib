package net.geral.lib.printing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.net.URL;

import javax.swing.ImageIcon;

import org.apache.log4j.Logger;

public abstract class ContinuousPaperPrintDocument extends PrintDocument {
  private static final Logger logger                   = Logger
                                                           .getLogger(ContinuousPaperPrintDocument.class);
  private static final int    DEFAULT_WIDTH            = 204;
  private static final int    DEFAULT_HEIGHT           = 868;

  protected final int         printWidth;
  protected final int         printHeight;

  protected final int         separadorPadding         = 3;
  protected final float       separadorLineWidth       = 1f;

  protected final Font        defaultFont              = new Font("monospaced",
                                                           Font.PLAIN, 8);

  private int                 currentPage              = 0;
  private int                 numberOfPages            = -1;

  protected int               next_character_x         = 0;
  protected boolean           autoLineWrapJustOccurred = false;

  private final PageFormat    format;
  private final Paper         paper;

  public ContinuousPaperPrintDocument() {
    this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
  }

  public ContinuousPaperPrintDocument(final int width, final int height) {
    printWidth = width;
    printHeight = height;
    paper = new Paper();
    paper.setSize(printWidth, printHeight);
    paper.setImageableArea(0, 0, printHeight, printHeight);
    format = new PageFormat();
    format.setOrientation(PageFormat.PORTRAIT);
    format.setPaper(paper);
  }

  public void drawHorizontalLine() {
    drawHorizontalLine(separadorLineWidth, separadorPadding);
  }

  public void drawHorizontalLine(final float lineWidth) {
    drawHorizontalLine(lineWidth, separadorPadding);
  }

  public void drawHorizontalLine(final float lineWidth, final int padding) {
    if (next_character_x != 0) {
      newline();
    }
    feed(padding);
    g.setStroke(new BasicStroke(lineWidth));
    g.drawLine(0, 0, printWidth, 0);
    feed(padding);
  }

  public void drawHorizontalLine(final int padding) {
    drawHorizontalLine(separadorLineWidth, padding);
  }

  public void drawImage(final String img, final int x, final int y,
      final int dpi) {
    final URL url = getClass().getResource("/res/img/print/" + img + ".png");
    final Image image = new ImageIcon(url).getImage();
    final double dpiFactor = 72.0 / dpi;
    final Graphics2D gs = (Graphics2D) g.create();
    gs.scale(dpiFactor, dpiFactor);
    gs.drawImage(image, x, y, null);
    feed((int) Math.ceil(image.getHeight(null) * dpiFactor));
  }

  public void feed(final int padding) {
    if (padding == 0) {
      return;
    }

    // FIXME bug workaround...
    g.translate(-0.5, 0);
    g.drawLine(0, 0, 0, padding);
    g.translate(+0.5, 0);

    g.translate(0, padding);
  }

  public int getCurrentPage() {
    return currentPage;
  }

  @Override
  public PageFormat getFormat() {
    return format;
  }

  public int getHeight() {
    return (int) Math.ceil(g.getTransform().getTranslateY());
  }

  @Override
  public int getNumberOfPages() {
    return numberOfPages;
  }

  public int getPrintHeight() {
    return printHeight;
  }

  public int getPrintWidth() {
    return printWidth;
  }

  public void newline() {
    // if automatic line wrap just occured, ignore this newline
    // (but allow next one)

    if (autoLineWrapJustOccurred && (next_character_x == 0)) {
      autoLineWrapJustOccurred = false;
    } else {
      autoLineWrapJustOccurred = false;
      feed(g.getFontMetrics().getHeight());
      next_character_x = 0;
    }
  }

  protected abstract void print();

  @Override
  public boolean print(final int pageIndex) {
    logger.debug(getClass() + " print() pageIndex=" + pageIndex);
    if (pageIndex > 0) {
      // when doing first page we get the number of pages
      if (pageIndex >= numberOfPages) {
        return false;
      }
    }
    currentPage = pageIndex + 1;
    final int translate = pageIndex * printHeight;
    g.translate(0, -translate);
    g.setFont(defaultFont);
    g.setColor(Color.BLACK);
    print();
    if (next_character_x != 0) {
      newline();
    }
    if (numberOfPages == -1) {
      final int totalHeight = getHeight();
      numberOfPages = (int) Math.ceil(totalHeight / printHeight);
    }
    return true;
  }

  public void setBold() {
    g.setFont(g.getFont().deriveFont(Font.BOLD));
  }

  public void setPlain() {
    g.setFont(g.getFont().deriveFont(Font.PLAIN));
  }

  protected void wrapline() {
    newline(); // it will ignore double occurances anyway
    autoLineWrapJustOccurred = true;
  }

  public void write(final String s) {
    logger.debug("write: " + s);
    // write, replacing \n for newlines
    final String[] lines = s.split("\n");
    if (lines.length == 0) {
      return;
    }
    boolean firstLine = true;
    for (final String l : lines) {
      if (!firstLine) {
        newline(); // [\n] can be duplicated, do not use wrapline method
      }
      write_step1_words(l);
      firstLine = false;
    }
  }

  private void write_step1_words(final String s) {
    // if it fits into line, do not bother breaking into words
    if ((g.getFontMetrics().stringWidth(s) + next_character_x) <= printWidth) {
      write_step5_commit(s);
      return;
    }
    final String[] words = s.split(" ");
    if (words.length == 0) {
      return;
    }
    for (final String w : words) {
      write_step2_word(w);
    }
  }

  private void write_step2_word(final String w) {
    final FontMetrics fm = g.getFontMetrics();
    if ((next_character_x + fm.stringWidth(w)) > printWidth) {
      // does not fit
      if (next_character_x == 0) {
        // already in the beggining, draw as many chars as it can
        write_step3_characters(w);
      } else {
        // not in the beggining, start new line and try again
        wrapline();
        write_step2_word(w);
      }
    } else {
      // fits, just write then (with space, who cares if it goes out of
      // bounds)
      write_step5_commit(w + " ");
    }
  }

  private void write_step3_characters(final String w) {
    for (int i = 0; i < w.length(); i++) {
      write_step4_character(w.charAt(i));
    }
  }

  private void write_step4_character(final char c) {
    final FontMetrics fm = g.getFontMetrics();
    final String sc = String.valueOf(c);
    if ((next_character_x + fm.stringWidth(sc)) > printWidth) {
      // does not fit
      if (next_character_x == 0) {
        // already in the beggining, draw the character anyway and
        // newline
        write_step5_commit(sc);
        wrapline();
      } else {
        // not in the beggining, start new line and try again
        wrapline();
        write_step4_character(c);
      }
    } else {
      // fits, just write then
      write_step5_commit(sc);
    }
  }

  private void write_step5_commit(final String s) {
    final FontMetrics fm = g.getFontMetrics();
    g.drawString(s, next_character_x, fm.getAscent());
    next_character_x += fm.stringWidth(s);
  }

  public void writeBold(final String s) {
    final Font f = g.getFont();
    setBold();
    write(s);
    g.setFont(f);
  }

  public void writeCentralized(final String string) {
    writeCentralized(string, g.getFont());
  }

  public void writeCentralized(final String s, final Font f) {
    final Font previous = g.getFont();
    if (next_character_x != 0) {
      newline();
    }
    g.setFont(f);
    final int needs = g.getFontMetrics().stringWidth(s);
    next_character_x = (printWidth - needs) / 2;
    if (next_character_x < 0) {
      // cannot fit in one line, it will wrap without centralizing
      next_character_x = 0;
    }
    writeline(s);
    g.setFont(previous);
  }

  public void writeline(final String s) {
    write(s);
    newline();
  }

  public void writePlain(final String s) {
    final Font f = g.getFont();
    setPlain();
    write(s);
    g.setFont(f);
  }
}
