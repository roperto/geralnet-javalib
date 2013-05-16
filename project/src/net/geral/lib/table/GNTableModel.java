package net.geral.lib.table;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import net.geral.lib.strings.GNStrings;

import org.apache.log4j.Logger;

public abstract class GNTableModel<I> extends AbstractTableModel {
  private static final Logger logger           = Logger
                                                   .getLogger(GNTableModel.class);
  private static final long   serialVersionUID = 1L;
  private ArrayList<I>        list             = new ArrayList<>();

  private final boolean       allowAdd;
  private final boolean       allowChange;
  private final boolean       allowDelete;
  private int                 numberOfColumns  = -1;
  private boolean             editable         = true;

  public GNTableModel(final boolean add, final boolean change,
      final boolean delete) {
    allowAdd = add;
    allowChange = change;
    allowDelete = delete;
  }

  public synchronized I add(final I obj) {
    list.add(obj);
    fireTableRowsInserted(list.size() - 1, list.size() - 1);
    return obj;
  }

  protected synchronized void automaticAddReplace(final I oldData,
      final I newData) {
    int index = list.indexOf(oldData);
    if (index == -1) {
      // new data, fire new row inserted (for editing),
      // change current editing to new data
      // this holds things in place (do not sort now)
      index = list.size() - 1;
      list.add(newData);
      fireTableRowsUpdated(index, index);
      fireTableRowsInserted(index + 1, index + 1);
    } else {
      list.set(index, newData);
      fireTableRowsUpdated(index, index);
    }
  }

  public boolean canAdd() {
    return editable && allowAdd;
  }

  public boolean canChange() {
    return editable && allowChange;
  }

  public boolean canDelete() {
    return editable && allowDelete;
  }

  public synchronized boolean canRemoveRow(final int row) {
    if (!canDelete()) {
      return false; // cannot delete
    }
    if (!canAdd()) {
      return true; // cannot add, no 'insert row'
    }
    // cannot delete last row (new data row)
    return row < (getRowCount() - 1);
  }

  /**
   * 
   * @param t
   *          old value
   * @param columnIndex
   *          column changed
   * @param aValue
   *          new value to set
   * @return new value or null if not changed
   */
  protected abstract I changeEntry(final I t, final int columnIndex,
      final Object aValue);

  public synchronized void clear() {
    list.clear();
    fireTableDataChanged();
  }

  public abstract I createNewEntry();

  public synchronized I get(final int index) {
    // when trying to get the data for a 'new entry' row
    if (index >= list.size()) {
      return null;
    }
    return list.get(index);
  }

  public synchronized ArrayList<I> getAll() {
    return new ArrayList<I>(list);
  }

  @Override
  public final int getColumnCount() {
    return numberOfColumns;
  }

  @Override
  public String getColumnName(final int column) {
    throw new RuntimeException("get from column model, not from here.");
  }

  public synchronized int getEntriesCount() {
    return list.size();
  }

  public synchronized int getRemoveColumn() {
    final int n = allowDelete ? (getColumnCount() - 1) : -1;
    return n;
  }

  @Override
  public synchronized int getRowCount() {
    return list.size() + (canAdd() ? 1 : 0);
  }

  @Override
  public synchronized Object getValueAt(final int rowIndex,
      final int columnIndex) {
    if (rowIndex >= list.size()) { // 'new entry' line
      return new GNTableNewEntry(); // object to put last
    }
    if (columnIndex == getRemoveColumn()) { // 'delete' column
      return null;
    }

    final I t = list.get(rowIndex);
    final Object v = getValueFor(t, columnIndex);
    if (v == null) {
      logger.warn("Cannot get value of " + t.getClass() + " for R" + rowIndex
          + "C" + columnIndex);
      return "R" + rowIndex + "C" + columnIndex;
    }
    return v;
  }

  protected abstract Object getValueFor(I obj, int columnIndex);

  @Override
  public synchronized boolean isCellEditable(final int rowIndex,
      final int columnIndex) {
    // remove column is -1 if not existing, so its ok to use ==
    if (columnIndex == getRemoveColumn()) {
      return false;
    }
    return canChange();
  }

  public synchronized void remove(final int index) {
    list.remove(index);
    fireTableRowsDeleted(index, index);
  }

  public synchronized void setData(final I[] data) {
    if (data == null) {
      list = new ArrayList<>();
      return;
    }
    list.clear();
    for (final I t : data) {
      list.add(t);
    }
    fireTableDataChanged();
  }

  public void setEditable(final boolean yn) {
    if (editable == yn) {
      return;
    }
    editable = yn;
    fireTableDataChanged();
  }

  public void setNumberOfColumns(final int n) {
    if (numberOfColumns >= 0) {
      throw new IllegalStateException("Number of columns already set ("
          + numberOfColumns + ")");
    }
    numberOfColumns = n;
  }

  @Override
  public synchronized void setValueAt(Object aValue, final int rowIndex,
      final int columnIndex) {
    if (!canChange()) {
      return;
    }

    Object previous = getValueAt(rowIndex, columnIndex);
    if ((previous == null) || (previous.getClass() == GNTableNewEntry.class)) {
      previous = "";
    }
    if (aValue instanceof String) {
      aValue = GNStrings.trim((String) aValue);
    }
    if (aValue.equals(previous)) {
      return;
    }

    final I data = (rowIndex == list.size()) ? createNewEntry() : list
        .get(rowIndex);
    final I newData = changeEntry(data, columnIndex, aValue);
    if (newData != null) {
      automaticAddReplace(data, newData);
    }
  }
}
