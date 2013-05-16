package net.geral.printing;

import javax.swing.JPanel;

public class ___PrintPreviewPanel extends JPanel {
	private static final long			serialVersionUID	= 1L;
	/*
	private final ___Printer		document;

	private final PrintDocument	paintPrinter		= new PrintDocument();

	public ___PrintPreviewPanel() {
		this(null);
	}

	public ___PrintPreviewPanel(final ___Printer doc) {
		setBackground(Color.WHITE);
		document = doc;
	}

	@Override
	public void paint(final Graphics g) {
		super.paint(g);
		if ((paintPrinter == null) || (document == null)) {
			g.setColor(Color.RED);
			g.drawString("Invalid painter.", 20, 20);
		}
		else {
			paintPrinter.setGraphics((Graphics2D)g);
			final int h = document.printAll(paintPrinter);
			setHeight(h);
		}
	}

	private void setHeight(final int h) {
		Dimension d = getSize();
		d = new Dimension(d.width, h + 30);
		setMinimumSize(d);
		setSize(d);
		setPreferredSize(d);
	}
	*/
}
