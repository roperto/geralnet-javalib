package net.geral.tools;

import java.awt.BorderLayout;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.Arrays;

import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.attribute.Attribute;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import net.geral.lib.printing.PrintPreviewPanel;
import net.geral.lib.printing.TestDocument;

import org.apache.log4j.BasicConfigurator;

public class GNPrinterDebug extends JFrame implements ListSelectionListener {
  private static final long serialVersionUID = 1L;

  public static void main(final String[] args) {
    BasicConfigurator.configure();
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        new GNPrinterDebug();
      }
    });
  }

  private final JList<String>     listPrinters;
  private final JTable            tableProperties;
  private final DefaultTableModel tablePropertiesModel;

  public GNPrinterDebug() {
    setSize(800, 600);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    final JSplitPane split2 = new JSplitPane();
    final JScrollPane scrollPrinters = new JScrollPane();

    listPrinters = new JList<String>(new DefaultListModel<String>());
    listPrinters.getSelectionModel().setSelectionMode(
        ListSelectionModel.SINGLE_SELECTION);
    listPrinters.getSelectionModel().addListSelectionListener(this);
    scrollPrinters.setViewportView(listPrinters);

    final JLabel lblPrinters = new JLabel("Printers:");
    lblPrinters.setHorizontalAlignment(SwingConstants.CENTER);
    scrollPrinters.setColumnHeaderView(lblPrinters);
    split2.setLeftComponent(scrollPrinters);

    final JScrollPane scrollProperties = new JScrollPane();
    split2.setRightComponent(scrollProperties);

    tablePropertiesModel = new DefaultTableModel(new Object[] { "Type",
        "Property", "Value" }, 0);
    tableProperties = new JTable(tablePropertiesModel);
    scrollProperties.setViewportView(tableProperties);

    final JSplitPane split1 = new JSplitPane();
    getContentPane().add(split1, BorderLayout.CENTER);

    split1.setLeftComponent(split2);

    final JScrollPane scrollPreview = new JScrollPane();
    scrollPreview
        .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
    scrollPreview
        .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    split1.setRightComponent(scrollPreview);

    final PrintPreviewPanel previewPanel = new PrintPreviewPanel(
        new TestDocument());
    scrollPreview.setViewportView(previewPanel);

    reload();
    setVisible(true);
  }

  private void addRow(final Object... objs) {
    tablePropertiesModel.addRow(objs);
  }

  private String orientation2string(final int orientation) {
    switch (orientation) {
      case PageFormat.LANDSCAPE:
        return "LANDSCAPE";
      case PageFormat.PORTRAIT:
        return "PORTRAIT";
      case PageFormat.REVERSE_LANDSCAPE:
        return "REVERSE_LANDSCAPE";
      default:
        return "???";
    }
  }

  private void reload() {
    final DefaultListModel<String> m = (DefaultListModel<String>) listPrinters
        .getModel();
    for (final PrintService ps : PrinterJob.lookupPrintServices()) {
      m.addElement(ps.getName());
    }
  }

  private void setPrintService(final PrintService ps) throws PrinterException {
    // clear
    while (tablePropertiesModel.getRowCount() > 0) {
      tablePropertiesModel.removeRow(0);
    }
    if (ps == null) {
      addRow("Nothing Selected");
      return;
    }
    // add
    addRow("PrintService", ps);
    for (final Class<?> c : ps.getSupportedAttributeCategories()) {
      final String p = c.getSimpleName();
      final String v = c.getName();
      addRow("PS.SupportedAttributeCategories", p, v);
    }
    for (final Attribute a : ps.getAttributes().toArray()) {
      final String p = a.getName() + " (" + a.getClass().getSimpleName() + ")";
      final String v = a.toString();
      addRow("PS.Attributes", p, v);
    }
    final DocPrintJob dpj = ps.createPrintJob();
    addRow("PS.DocPrintJob", dpj);
    for (final Attribute a : dpj.getAttributes().toArray()) {
      final String p = a.getName() + " (" + a.getClass().getSimpleName() + ")";
      final String v = a.toString();
      addRow("PS.DPJ.Attributes", p, v);
    }
    addRow("PS.DPJ.PrintService", dpj.getPrintService(), "");
    final PrinterJob pj = PrinterJob.getPrinterJob();
    addRow("PrinterJob (unset)", pj.getPrintService(), pj.toString());
    pj.setPrintService(ps);
    addRow("PrinterJob (set)", pj.getPrintService(), pj.toString());
    final PageFormat dp = pj.defaultPage();
    addRow("PJ.DefaultPage", dp);
    addRow("PJ.DP", "Width", dp.getWidth());
    addRow("PJ.DP", "Height", dp.getHeight());
    addRow("PJ.DP", "ImageableWidth", dp.getImageableWidth());
    addRow("PJ.DP", "ImageableHeight", dp.getImageableHeight());
    addRow("PJ.DP", "ImageableX", dp.getImageableX());
    addRow("PJ.DP", "ImageableY", dp.getImageableY());
    addRow("PJ.DP", "Matrix", Arrays.toString(dp.getMatrix()));
    addRow("PJ.DP", "Orientation", dp.getOrientation());
    addRow("PJ.DP", "Orientation (str)",
        orientation2string(dp.getOrientation()));
    final Paper p = dp.getPaper();
    addRow("PJ.DP.Paper", p);
    addRow("PJ.DP.P", "Width", p.getWidth());
    addRow("PJ.DP.P", "Height", p.getHeight());
    addRow("PJ.DP.P", "ImageableWidth", p.getImageableWidth());
    addRow("PJ.DP.P", "ImageableHeight", p.getImageableHeight());
    addRow("PJ.DP.P", "ImageableX", p.getImageableX());
    addRow("PJ.DP.P", "ImageableY", p.getImageableY());
  }

  @Override
  public void valueChanged(final ListSelectionEvent e) {
    if (e.getValueIsAdjusting()) {
      return;
    }
    final String name = listPrinters.getSelectedValue();
    if (name != null) {
      for (final PrintService ps : PrinterJob.lookupPrintServices()) {
        if (name.equals(ps.getName())) {
          try {
            setPrintService(ps);
          } catch (final PrinterException ex) {
            tablePropertiesModel.insertRow(0,
                new Object[] { "*** EXCEPTION OCCURED ***" });
            tablePropertiesModel.addRow(new Object[] { "***EXCEPTION***",
                ex.getClass().getName(), ex.getMessage() });
          }
          return;
        }
      }
    }
    System.err.println("Not found: " + name);
  }
}
