package net.geral.lib.table;


public class GNTableTest_Table extends GNTable<GNTableModel<String[]>> {
  private static final long serialVersionUID = 1L;

  public GNTableTest_Table() {
    super(new GNTableTest_Model());
  }

  @Override
  protected void createColumns() {
    createColumn("Col 1", 100, 150, 200);
    createColumn("Col 2", 200, 250, 300);
    createColumn("Col 3", 300, 350, 400);
  }

  @Override
  protected boolean deleteClicked(final int tableRow, final int tableColumn) {
    return true;
  }

  @Override
  public String getNewEntryText(final int columnIndex) {
    return columnIndex == 3 ? "new 3" : "-";
  }
}
