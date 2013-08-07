package net.geral.lib.table;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Comparator;
import java.util.EventObject;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.TableRowSorter;
import javax.swing.text.JTextComponent;

import org.apache.log4j.Logger;

public abstract class GNTable<M extends GNTableModel<?>> extends JTable {
  private static final Logger          logger           = Logger
                                                            .getLogger(GNTable.class);
  private static final long            serialVersionUID = 1L;

  private final JScrollPane            scroll           = new JScrollPane(
                                                            this,
                                                            ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                                                            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

  protected final GNTableRowComparator defaultSorter    = new GNTableRowComparator(
                                                            this);
  private boolean                      initialized      = false;

  public GNTable(final M model) {
    this(model, new GNTableRenderer());
  }

  public GNTable(final M model, final GNTableRenderer renderer) {
    super(model, new GNTableColumnModel());
    setDefaultRenderer(Object.class, renderer);
    setAutoCreateRowSorter(false);
    // columns
    createColumns();
    if (getModel().canDelete()) {
      // needed to ask for 'allowDelete' instead, but since
      // model was just created, 'editable' is still true.
      createColumn("", new GNTableColumnWidth(renderer.getRemoveIconWidth()));
    }
    initialized = true;
    getModel().setNumberOfColumns(getColumnCount());
    // prepare
    getModel().fireTableStructureChanged(); // will recreate sorters if after
    createRowSorters();
    getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    setAutoResizeMode(AUTO_RESIZE_SUBSEQUENT_COLUMNS);
    putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
    createEventsConfiguration();
  }

  protected void createColumn(final String header,
      final GNTableColumnWidth... widths) {
    if (initialized) {
      // TODO if already initialized, reapply info do model, sorters, etc
      throw new IllegalStateException("Table already initialized.");
    }
    for (final GNTableColumnWidth w : widths) {
      if (w != null) {
        ((GNTableColumnModel) getColumnModel()).add(header, w);
        return;
      }
    }
    throw new IllegalArgumentException("Only null values found for widths.");
  }

  protected void createColumn(final String header, final int allWidths) {
    createColumn(header, new GNTableColumnWidth(allWidths));
  }

  protected void createColumn(final String header, final int min,
      final int pref, final int max) {
    createColumn(header, new GNTableColumnWidth(min, pref, max));
  }

  protected abstract void createColumns();

  private void createEventsConfiguration() {
    addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(final MouseEvent e) {
        if ((getSelectedColumn() == getRemoveColumn())
            && getModel().canDelete()) {
          final int viewRow = getSelectedRow();
          final int modelRow = convertRowIndexToModel(viewRow);
          final boolean canRemove = getModel().canRemoveRow(modelRow);
          logger.debug("delete row clicked [view=" + viewRow + ";model="
              + modelRow + ";canRemove=" + canRemove + "]");
          if (canRemove && (deleteClicked(viewRow, modelRow))) {
            getModel().remove(modelRow);
          }
        }
      }
    });
    // use ENTER to start editing (like F2)
    final Object f2action = getInputMap(
        JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).get(
        KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
    getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(
        KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), f2action);
    // disble F2
    getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(
        KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0), "doNothing");
  }

  protected Comparator<?> createRowSorter(final int columnIndex) {
    // generally use the default, unless method is overriden
    return defaultSorter;
  }

  private void createRowSorters() {
    @SuppressWarnings("unchecked")
    final TableRowSorter<GNTableModel<?>> sorter = (TableRowSorter<GNTableModel<?>>) new TableRowSorter<>(
        getModel());

    setRowSorter(sorter);
    sorter.modelStructureChanged(); // adjust column info to sorter
    for (int i = 0; i < getColumnCount(); i++) {
      if (i == getRemoveColumn()) {
        sorter.setSortable(i, false);
      } else {
        final Comparator<?> comparator = createRowSorter(i);
        sorter.setSortable(i, comparator != null);
        sorter.setComparator(i, comparator);
      }
    }
  }

  /**
   * 
   * @param tableRow
   * @param tableColumn
   * @param canDelete
   * @return if clicked row should be removed from table/model
   */
  protected abstract boolean deleteClicked(final int viewRow,
      final int tableColumn);

  @Override
  public boolean editCellAt(final int row, final int column, final EventObject e) {
    if (!super.editCellAt(row, column, e)) {
      return false;
    }

    final Component c = getEditorComponent();
    if (c instanceof JTextField) {
      final String s = getDefaultEditingString(row, column);
      if (s != null) {
        ((JTextField) c).setText(s);
      }
    }

    editorSelectAll(e);
    return true;
  }

  private void editorFocusAndSelect(final JTextComponent tc,
      final boolean select) {
    tc.requestFocusInWindow();
    if (select) {
      tc.selectAll();
    }
  }

  private void editorSelectAll(final EventObject e) {
    // learned from: http://www.camick.com/java/source/RXTable.java
    final Component editor = getEditorComponent();
    if (!(editor instanceof JTextComponent)) {
      return;
    }
    if (e instanceof MouseEvent) {
      // mouse wait for clicks to be processed (select latter)
      SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
          editorFocusAndSelect((JTextComponent) editor, true);
        }
      });
    } else {
      // other, like keys, do now (to not lose typed key)
      // if action event (ENTER), do not select all -- like google
      // spreadsheets
      editorFocusAndSelect((JTextComponent) editor, !(e instanceof ActionEvent));
    }
  }

  public String getDefaultEditingString(final int row, final int column) {
    return null;
  }

  @SuppressWarnings("unchecked")
  @Override
  public M getModel() {
    return (M) super.getModel();
  }

  public abstract String getNewEntryText(int columnIndex);

  public int getRemoveColumn() {
    return convertColumnIndexToView(getModel().getRemoveColumn());
  }

  public JScrollPane getScroll() {
    return scroll;
  }

  public Object getSelected() {
    final int r = getSelectedRow();
    if (r == -1) {
      return null;
    }
    return getModel().get(convertRowIndexToModel(r));
  }

  public void initialSort(final int columnIndex, final boolean reverse) {
    getRowSorter().toggleSortOrder(columnIndex);
    if (reverse) {
      getRowSorter().toggleSortOrder(columnIndex);
    }
  }

  public void removeSelected() {
    final int i = getSelectedRow();
    if (i != -1) {
      getModel().remove(i);
    }
  }

  public void scrollToSelected() {
    final int n = getSelectedRow();
    int c = getSelectedColumn();
    if (c == -1) {
      c = 0;
    }
    if (n == -1) {
      return;
    }
    scrollRectToVisible(new Rectangle(getCellRect(n, c, true)));
  }

  @Override
  public void setAutoCreateRowSorter(final boolean autoCreateRowSorter) {
    super.setAutoCreateRowSorter(autoCreateRowSorter);
  }

  public void setEditable(final boolean yn) {
    if ((!yn) && isEditing()) {
      getCellEditor().stopCellEditing();
    }
    getModel().setEditable(yn);
  }

  @Override
  public void setValueAt(final Object aValue, final int row, final int column) {
    super.setValueAt(aValue, row, column);
    // FIXME auto select next cell
    // getSelectionModel().setSelectionInterval(row, row);
    // SwingUtilities.invokeLater(new Runnable() {
    // @Override
    // public void run() {
    // // if selection changed row, do not continue
    // if (getSelectedRow() != row) {
    // return;
    // }
    // for (int c = column + 1; c < getColumnCount(); c++) {
    // if (isCellEditable(row, c)) {
    // editCellAt(row, c);
    // return;
    // }
    // }
    // }
    // });
    return;
  }
}
