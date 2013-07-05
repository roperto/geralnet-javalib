package net.geral.lib.printing.test;

import java.awt.print.PrinterException;

import net.geral.lib.printing.ContinuousPaperPrintDocument;
import net.geral.lib.printing.PrintSupport;
import net.geral.lib.printing.TestDocument;

import org.apache.log4j.BasicConfigurator;

public class PrintingTest {
  public static void main(final String[] args) {
    BasicConfigurator.configure();
    try {
      // test1();
      test2();
    } catch (final PrinterException e) {
      e.printStackTrace();
    }
  }

  public static void test1() throws PrinterException {
    final TestDocument test = new TestDocument();
    PrintSupport.print(test);
  }

  private static void test2() throws PrinterException {
    PrintSupport.print(new ContinuousPaperPrintDocument() {
      @Override
      protected void print() {
        writeline("Line 1");
        writeline("Line 2");
        writeline("Line 3");
        newline();
        writeline("Line 5");
        writeline("Line 6");
        writeline("Line 7");
      }
    });
  }
}
