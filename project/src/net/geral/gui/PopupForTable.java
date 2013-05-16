package net.geral.gui;

import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.CellEditor;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;

public class PopupForTable extends JPopupMenu implements MouseListener {
	private static final long	serialVersionUID	= 1L;

	public static JMenuItem createItem(final String label, final String action,
			final ActionListener listener) {
		final JMenuItem m = new JMenuItem(label);
		m.setActionCommand(action);
		m.addActionListener(listener);
		return m;
	}

	private final boolean	ignoreLastRow;

	private final JTable	table;

	public PopupForTable(final JTable table, final boolean ignoreLastRow) {
		this.table = table;
		this.ignoreLastRow = ignoreLastRow;
		table.addMouseListener(this);
	}

	private void checkPopUp(final MouseEvent e) {
		if (e.isPopupTrigger()) {
			if (e.getSource() != table) return;
			final int row = table.rowAtPoint(e.getPoint());
			final int column = table.columnAtPoint(e.getPoint());

			if (ignoreLastRow) {
				if (row + 1 >= table.getRowCount()) return;
			}

			final CellEditor ce = table.getCellEditor();
			if (ce == null) {
				if (!table.isRowSelected(row)) {
					table.changeSelection(row, column, false, false);
				}

				show(e.getComponent(), e.getX(), e.getY());
			}
			else {
				ce.stopCellEditing();
			}

		}
	}

	@Override
	public void mouseClicked(final MouseEvent e) {}

	@Override
	public void mouseEntered(final MouseEvent e) {}

	@Override
	public void mouseExited(final MouseEvent e) {}

	@Override
	public void mousePressed(final MouseEvent e) {
		checkPopUp(e);
	}

	@Override
	public void mouseReleased(final MouseEvent e) {
		checkPopUp(e);
	}
}
