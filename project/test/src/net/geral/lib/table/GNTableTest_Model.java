package net.geral.lib.table;

import java.util.Arrays;

public class GNTableTest_Model extends GNTableModel<String[]> {
  private static final long serialVersionUID = 1L;
  private static final int  COLS             = 10;

  public GNTableTest_Model() {
    super(true, true, true);
    for (int i = 0; i < 10; i++) {
      final int r = (int) (Math.random() * 200);
      final int c = 50 - i;
      add(new String[] { String.valueOf(i), String.valueOf(r),
          String.valueOf(c) });
    }
  }

  @Override
  protected String[] changeEntry(final String[] t, final int columnIndex,
      final Object aValue) {
    t[columnIndex] = aValue.toString();
    System.out.println("changed: " + Arrays.toString(t));
    return t;
  }

  @Override
  public String[] createNewEntry() {
    final String[] s = new String[COLS];
    for (int i = 0; i < COLS; i++) {
      s[i] = "";
    }
    return s;
  }

  @Override
  protected Object getValueFor(final String[] obj, final int columnIndex) {
    return obj[columnIndex];
  }
}
