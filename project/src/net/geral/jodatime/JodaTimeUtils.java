package net.geral.jodatime;

import org.joda.time.IllegalFieldValueException;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class JodaTimeUtils {
    public static final JodaTimeComparator Comparator = new JodaTimeComparator();

    // TODO delete preformated, in Essomerie use translation file
    @Deprecated
    public static final DateTimeFormatter DMA = DateTimeFormat
	    .forPattern("dd/MM/yyyy");
    @Deprecated
    public static final DateTimeFormatter DMAHMS = DateTimeFormat
	    .forPattern("dd/MM/yyyy HH:mm:ss");

    public static LocalDate parseLocalDate(final String string) {
	if (string == null) {
	    return null;
	}
	try {
	    return LocalDate.parse(string);
	} catch (final IllegalFieldValueException e) {
	    return null;
	}
    }

    public static LocalDateTime parseLocalDateTime(final String string) {
	// TODO boolean that select: returns null or 'zero' if invalid
	if (string == null) {
	    return null;
	}
	try {
	    return LocalDateTime.parse(string.replace(" ", "T")); // reformat
								  // string
	} catch (final IllegalFieldValueException e) {
	    return null;
	}
    }
}
