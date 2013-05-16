package net.geral.printing;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;

public abstract class PrintDocument implements Printable {
	protected Graphics2D	g	= null;

	public abstract PageFormat getFormat();

	@Override
	public int print(final Graphics graphics, final PageFormat pageFormat, final int pageIndex) throws PrinterException {
		g = (Graphics2D)graphics;
		final boolean res = print(pageIndex);
		return res ? Printable.PAGE_EXISTS : Printable.NO_SUCH_PAGE;
	}

	public abstract boolean print(final int pageIndex);
}
