package net.geral.jodatime;

import java.util.Comparator;

import org.joda.time.LocalDateTime;

public final class JodaTimeComparator implements Comparator<LocalDateTime> {
    JodaTimeComparator() {
    }

    @Override
    public int compare(final LocalDateTime a, final LocalDateTime b) {
	return a.compareTo(b);
    }
}
