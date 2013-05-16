package net.geral.lib.table;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

@Deprecated
// FIXME delme use table.getScroll()
public class GNTableScroll<T extends GNTable<?>> extends JScrollPane {
  private static final long serialVersionUID = 1L;

  public GNTableScroll(final T t) {
    super(t, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
  }

  @SuppressWarnings("unchecked")
  public T getTable() {
    return (T) getViewport().getView();
  }
}
