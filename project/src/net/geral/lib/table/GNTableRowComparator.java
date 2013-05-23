package net.geral.lib.table;

import java.util.Comparator;
import java.util.List;

import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;

import net.geral.lib.strings.GNStrings;

import org.apache.log4j.Logger;

public class GNTableRowComparator implements Comparator<Object> {
  private static final Logger logger = Logger
                                         .getLogger(GNTableRowComparator.class);
  private final GNTable<?>    table;

  public GNTableRowComparator(final GNTable<?> table) {
    this.table = table;
  }

  @SuppressWarnings("unchecked")
  @Override
  public int compare(final Object a, final Object b) {
    // if any is 'object' only, its a signal that is a new entry
    if ((a.getClass() == GNTableNewEntry.class)
        || (b.getClass() == GNTableNewEntry.class)) {
      if ((a.getClass() == GNTableNewEntry.class)
          && (b.getClass() == GNTableNewEntry.class)) {
        logger.warn("Comparing two new entries? A=B=EditableTableNewEntry!");
        return 0;
      }
      // check the order and ensure it is the last item
      final List<? extends SortKey> keys = table.getRowSorter().getSortKeys();
      SortOrder key = (keys.size() == 0) ? null : keys.get(0).getSortOrder();
      if (key == null) {
        key = SortOrder.ASCENDING;
      }
      final int order = (key == SortOrder.ASCENDING) ? -1 : 1;
      if (a.getClass() == GNTableNewEntry.class) {
        return -1 * order;
      }
      if (b.getClass() == GNTableNewEntry.class) {
        return 1 * order;
      }
    }
    // if not 'new entry', types must be the same
    if (a.getClass() != b.getClass()) {
      throw new IllegalArgumentException("Types are not the same: "
          + a.getClass() + " / " + b.getClass());
    }
    // sort according to data ...
    if (a.getClass() == String.class) {
      return GNStrings.compare((String) a, (String) b);
    }
    // no sorter, use comparable
    if (a instanceof Comparable<?>) {
      return ((Comparable<Object>) a).compareTo(b);
    }
    // invalid type
    throw new IllegalArgumentException("Invalid sort for: " + a.getClass());
  }
}
