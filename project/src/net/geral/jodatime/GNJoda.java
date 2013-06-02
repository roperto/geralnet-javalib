package net.geral.jodatime;

import org.joda.time.IllegalFieldValueException;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class GNJoda {
  public static final JodaTimeComparator Comparator = new JodaTimeComparator();

  // TODO delete preformated, in Essomerie use translation file
  @Deprecated
  public static final DateTimeFormatter  DMA        = DateTimeFormat
                                                        .forPattern("dd/MM/yyyy");
  @Deprecated
  public static final DateTimeFormatter  DMAHMS     = DateTimeFormat
                                                        .forPattern("dd/MM/yyyy HH:mm:ss");

  @Deprecated
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

  public static LocalDate parseLocalDate(final String value,
      final DateTimeFormatter format) {
    if (value == null) {
      return null;
    }
    try {
      return LocalDate.parse(value, format);
    } catch (final IllegalArgumentException e) {
      return null;
    }
  }

  public static LocalDate parseLocalDate(final String value, final String format) {
    if (value == null) {
      return null;
    }
    return parseLocalDate(value, DateTimeFormat.forPattern(format));
  }

  public static LocalDateTime parseLocalDateTime(final String value,
      final DateTimeFormatter format) {
    if (value == null) {
      return null;
    }
    try {
      return LocalDateTime.parse(value, format);
    } catch (final IllegalArgumentException e) {
      return null;
    }
  }

  public static LocalDateTime parseLocalDateTime(final String value,
      final String format) {
    if (value == null) {
      return null;
    }
    return parseLocalDateTime(value, DateTimeFormat.forPattern(format));
  }

  public static LocalTime parseLocalTime(final String value,
      final DateTimeFormatter format) {
    if (value == null) {
      return null;
    }
    try {
      return LocalTime.parse(value, format);
    } catch (final IllegalArgumentException e) {
      return null;
    }
  }

  public static LocalTime parseLocalTime(final String value, final String format) {
    if (value == null) {
      return null;
    }
    return parseLocalTime(value, DateTimeFormat.forPattern(format));
  }

  public static LocalDateTime sqlLocalDateTime(final String sql) {
    // TODO boolean that select: returns null or 'zero' if invalid
    if (sql == null) {
      return null;
    }
    try {
      // 01-02-2003 04:05:06 --> 01-02-2003T04:05:06
      return LocalDateTime.parse(sql.replace(" ", "T"));
    } catch (final IllegalFieldValueException e) {
      return null;
    }
  }
}
