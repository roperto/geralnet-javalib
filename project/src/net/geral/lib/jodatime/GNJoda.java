package net.geral.lib.jodatime;

import org.joda.time.IllegalFieldValueException;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class GNJoda {
  public static final String            SQL_DATE_STRING     = "yyyy-MM-dd";
  public static final String            SQL_TIME_STRING     = "HH:mm:ss";
  public static final String            SQL_DATETIME_STRING = "yyyy-MM-dd HH:mm:ss.SSS";
  public static final DateTimeFormatter SQL_DATE_FORMAT     = DateTimeFormat
                                                                .forPattern(SQL_DATE_STRING);
  public static final DateTimeFormatter SQL_TIME_FORMAT     = DateTimeFormat
                                                                .forPattern(SQL_TIME_STRING);
  public static final DateTimeFormatter SQL_DATETIME_FORMAT = DateTimeFormat
                                                                .forPattern(SQL_DATETIME_STRING);

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

  public static LocalDate sqlLocalDate(final String sql,
      final boolean nullForException) {
    if (sql == null) {
      return null;
    }
    try {
      return LocalDate.parse(sql, SQL_DATE_FORMAT);
    } catch (final IllegalFieldValueException e) {
      if (nullForException) {
        return null;
      } else {
        throw e;
      }
    }
  }

  public static LocalDateTime sqlLocalDateTime(final String sql,
      final boolean nullForException) {
    if (sql == null) {
      return null;
    }
    try {
      return LocalDateTime.parse(sql, SQL_DATETIME_FORMAT);
    } catch (final IllegalFieldValueException e) {
      if (nullForException) {
        return null;
      } else {
        throw e;
      }
    }
  }

  public static LocalTime sqlLocalTime(final String sql,
      final boolean nullForException) {
    if (sql == null) {
      return null;
    }
    try {
      return LocalTime.parse(sql, SQL_TIME_FORMAT);
    } catch (final IllegalFieldValueException e) {
      if (nullForException) {
        return null;
      } else {
        throw e;
      }
    }
  }
}
