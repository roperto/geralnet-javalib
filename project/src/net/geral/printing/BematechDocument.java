package net.geral.printing;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.net.URL;

import javax.swing.ImageIcon;

import org.apache.log4j.Logger;

public abstract class BematechDocument extends PrintDocument {
    private static final Logger logger = Logger
	    .getLogger(BematechDocument.class);
    public static final int PRINTING_WIDTH = 204;
    public static final int PRINTING_HEIGHT = 868;

    protected static final int DEFAULT_SEPARATOR_PADDING = 3;
    protected static final float DEFAULT_SEPARATOR_WIDTH = 1f;
    protected static final Font DEFAULT_FONT = new Font("monospaced",
	    Font.PLAIN, 8);

    private int currentPage = 0;
    private int numberOfPages = -1;

    protected int next_character_x = 0;

    protected boolean autoLineWrapJustOccurred = false;

    public void centralizar(final String string) {
	centralizar(string, g.getFont());
    }

    public void centralizar(final String s, final Font f) {
	final Font previous = g.getFont();
	if (next_character_x != 0) {
	    newline();
	}
	g.setFont(f);
	final int needs = g.getFontMetrics().stringWidth(s);
	next_character_x = (PRINTING_WIDTH - needs) / 2;
	if (next_character_x < 0) {
	    // cannot fit in one line, it will wrap without centralizing
	    next_character_x = 0;
	}
	writeline(s);
	g.setFont(previous);
    }

    public void drawImage(final String img, final int x, final int y,
	    final int dpi) {
	final URL url = getClass()
		.getResource("/res/img/print/" + img + ".png");
	final Image image = new ImageIcon(url).getImage();
	final double dpiFactor = 72.0 / dpi;
	final Graphics2D gs = (Graphics2D) g.create();
	gs.scale(dpiFactor, dpiFactor);
	gs.drawImage(image, x, y, null);
	feed((int) Math.ceil(image.getHeight(null) * dpiFactor));
    }

    public void feed(final int padding) {
	logger.debug("feed: " + padding);

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
	final Paper paper = new Paper();
	paper.setSize(PRINTING_WIDTH, PRINTING_HEIGHT);
	paper.setImageableArea(0, 0, PRINTING_HEIGHT, PRINTING_HEIGHT);
	final PageFormat format = new PageFormat();
	format.setOrientation(PageFormat.PORTRAIT);
	format.setPaper(paper);
	return format;
    }

    public int getHeight() {
	return (int) Math.ceil(g.getTransform().getTranslateY());
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
	final int translate = pageIndex * PRINTING_HEIGHT;
	g.translate(0, -translate);
	g.setFont(DEFAULT_FONT);
	print();
	if (next_character_x != 0) {
	    newline();
	}
	if (numberOfPages == -1) {
	    numberOfPages = (int) Math.ceil(getHeight() / PRINTING_HEIGHT);
	}
	return true;
    }

    public void separator() {
	separator(DEFAULT_SEPARATOR_WIDTH, DEFAULT_SEPARATOR_PADDING);
    }

    public void separator(final float lineWidth) {
	separator(lineWidth, DEFAULT_SEPARATOR_PADDING);
    }

    public void separator(final float lineWidth, final int padding) {
	if (next_character_x != 0) {
	    newline();
	}
	feed(padding);
	g.setStroke(new BasicStroke(lineWidth));
	g.drawLine(0, 0, PRINTING_WIDTH, 0);
	feed(padding);
    }

    public void separator(final int padding) {
	separator(DEFAULT_SEPARATOR_WIDTH, padding);
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
	    write_1_words(l);
	    firstLine = false;
	}
    }

    private void write_1_words(final String s) {
	// if it fits into line, do not bother breaking into words
	if ((g.getFontMetrics().stringWidth(s) + next_character_x) <= PRINTING_WIDTH) {
	    write_commit(s);
	    return;
	}
	final String[] words = s.split(" ");
	if (words.length == 0) {
	    return;
	}
	for (final String w : words) {
	    write_2_word(w);
	}
    }

    private void write_2_word(final String w) {
	final FontMetrics fm = g.getFontMetrics();
	if ((next_character_x + fm.stringWidth(w)) > PRINTING_WIDTH) {
	    // does not fit
	    if (next_character_x == 0) {
		// already in the beggining, draw as many chars as it can
		write_3_characters(w);
	    } else {
		// not in the beggining, start new line and try again
		wrapline();
		write_2_word(w);
	    }
	} else {
	    // fits, just write then (with space, who cares if it goes out of
	    // bounds)
	    write_commit(w + " ");
	}
    }

    private void write_3_characters(final String w) {
	for (int i = 0; i < w.length(); i++) {
	    write_4_character(w.charAt(i));
	}
    }

    private void write_4_character(final char c) {
	final FontMetrics fm = g.getFontMetrics();
	final String sc = String.valueOf(c);
	if ((next_character_x + fm.stringWidth(sc)) > PRINTING_WIDTH) {
	    // does not fit
	    if (next_character_x == 0) {
		// already in the beggining, draw the character anyway and
		// newline
		write_commit(sc);
		wrapline();
	    } else {
		// not in the beggining, start new line and try again
		wrapline();
		write_4_character(c);
	    }
	} else {
	    // fits, just write then
	    write_commit(sc);
	}
    }

    private void write_commit(final String s) {
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

    public void writeline(final String s) {
	write(s);
	newline();
    }
}
