package net.geral.printing;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

//http://docs.oracle.com/javase/tutorial/2d/printing/printable.html
public class PrintSupport {
	public static void print(final PrintDocument document) throws PrinterException {
		final PrinterJob job = PrinterJob.getPrinterJob();
		job.setPrintable(document, document.getFormat());
		job.print();
	}

	private PrintSupport() {}
}
