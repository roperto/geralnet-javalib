package net.geral.lib.table;

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;

public class GNTableColumnModel extends DefaultTableColumnModel {
  private static final long serialVersionUID = 1L;

  public void add(final String header, final GNTableColumnWidth w) {
    final int n = getColumnCount();
    final TableColumn c = new TableColumn(n);
    c.setHeaderValue(header);
    w.apply(c);
    addColumn(c);
  }
}
