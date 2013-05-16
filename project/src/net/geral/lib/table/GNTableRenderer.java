package net.geral.lib.table;

import java.awt.Color;
import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;

public class GNTableRenderer extends DefaultTableCellRenderer {
  private static final long  serialVersionUID  = 1L;
  private static Icon        defaultDeleteIcon = null;
  private Border             previousBorder    = null;

  private final Icon         deleteIcon;
  // TODO to configuration ?
  private static final Color FG_NORMAL_ROW     = Color.BLACK;
  private static final Color FG_LAST_ROW       = Color.LIGHT_GRAY;
  private static final int   DEFAULT_ICON_SIZE = 16;

  public static void setDefaultDeleteIcon(final Icon defaultDeleteIcon) {
    GNTableRenderer.defaultDeleteIcon = defaultDeleteIcon;
  }

  public GNTableRenderer() {
    this(defaultDeleteIcon);
  }

  public GNTableRenderer(final Icon deleteIcon) {
    this.deleteIcon = deleteIcon;
  }

  // TODO add option for alternate colors in rows

  public int getRemoveIconWidth() {
    if (deleteIcon == null) {
      return DEFAULT_ICON_SIZE;
    }
    return deleteIcon.getIconWidth();
  }

  @Override
  public Component getTableCellRendererComponent(final JTable table,
      final Object value, final boolean isSelected, final boolean hasFocus,
      final int row, final int column) {

    super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
        row, column);

    if ((value instanceof String) && (((String) value).length() != 0)) {
      setToolTipText((String) value);
    } else {
      setToolTipText(null);
    }

    final GNTable<?> gn = (GNTable<?>) table;
    final GNTableModel<?> model = gn.getModel();
    final boolean newEntryRow = (model.get(gn.convertRowIndexToModel(row)) == null);
    final boolean deleteEntryColumn = (column == gn.getRemoveColumn());

    if (!isSelected) {
      setForeground(newEntryRow ? FG_LAST_ROW : FG_NORMAL_ROW);
    }
    if (newEntryRow && !deleteEntryColumn) {
      setText(gn.getNewEntryText(gn.convertColumnIndexToModel(column)));
    }

    if (deleteEntryColumn) { // delete
      setIcon(deleteIcon);
      setEnabled(model.canRemoveRow(gn.convertRowIndexToModel(row)));
      if (getBorder() != null) {
        previousBorder = getBorder();
      }
      setBorder(null);
    } else {
      setIcon(null);
      setEnabled(true);
      if (getBorder() != null) {
        previousBorder = getBorder();
      }
      setBorder(previousBorder);
    }

    return this;
  }
}
