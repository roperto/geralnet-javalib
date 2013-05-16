package net.geral.printing;

import java.awt.Color;

public class TestDocument extends BematechDocument {
    @Override
    protected void print() {
	final int w = PRINTING_WIDTH;
	int h = PRINTING_HEIGHT;
	g.drawLine(0, 0, w, 0);
	g.drawLine(0, h, w, h);
	g.drawLine(0, 0, 0, h);
	g.drawLine(w, 0, w, h);
	g.drawLine(0, 0, w, h);
	g.drawLine(w, 0, 0, h);
	h = 2 * PRINTING_HEIGHT;
	g.setColor(Color.GRAY);
	g.drawLine(0, 0, w, 0);
	g.drawLine(0, h, w, h);
	g.drawLine(0, 0, 0, h);
	g.drawLine(w, 0, w, h);
	g.drawLine(0, 0, w, h);
	g.drawLine(w, 0, 0, h);
	feed(h);
    }
}
