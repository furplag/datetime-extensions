package jp.furplag.util.time;

import static jp.furplag.util.commons.NumberUtils.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.TimeZone;

import jp.furplag.util.Localizer;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalTime;
import org.joda.time.chrono.GJChronology;
import org.joda.time.chrono.ISOChronology;

/**
 * Utilities for date-time.
 *
 * @author furplag.jp
 *
 */
public class DateTimeUtils extends org.joda.time.DateTimeUtils {

  /** the millis of one day */
  protected static final double MILLIS_OF_DAY = 86400000d;

  /** the epoch julian date of 1970-01-01T00:00:00Z */
  protected static final double MILLIS_PERIOD = 2440587.5;

  /** the epoch julian date of 1858-11-17T00:00:00Z */
  protected static final double MJD_PERIOD = 2400000.5;

  protected static final String PATTERN_ISO = "yyyy-MM-dd'T'HH:mm:ss.SSSZZ";
//
// /**
// * shorthand for {@code new DateTime(dateTime.toString("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"), DateTimeZone.UTC)}.
// *
// * @param dateTime the datetime object.
// * @return
// */
// public static DateTime enforceDefault(final DateTime dateTime) {
// return dateTime == null ? null : dateTime.minusMillis(DateTimeZone.getDefault().getOffset(dateTime)).plusMillis(dateTime.getZone().getOffset(dateTime)).withZone(DateTimeZone.getDefault());
// }
//
// /**
// * shorthand for {@code new DateTime(dateTime.toString("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"), DateTimeZone.UTC)}.
// *
// * @param dateTime the datetime object.
// * @return
// */
// public static DateTime enforceUTC(final DateTime dateTime) {
// return dateTime == null ? null : dateTime.plusMillis(dateTime.getZone().getOffset(dateTime)).withZone(DateTimeZone.UTC);
// }

  /**
   * primitive wrapping for override.
   *
   * @param julianDay the julian day.
   * @return the epoch millis from 1970-01-01T0Z.
   */
  public static long fromJD(final double julianDay) {
    return fromJD(Double.valueOf(julianDay));
  }

  /**
   * substitute for {@code org.joda.time.DateTimeUtils.fromJulianDay(double))}.
   *
   * @param julianDay the julian day.
   * @return the epoch millis from 1970-01-01T0Z.
   */
  public static Long fromJD(final Double julianDay) {
    return julianDay == null ? null : Math.round((julianDay - MILLIS_PERIOD) * MILLIS_OF_DAY);
  }

  /**
   * wrapping for {@code new DateTime(instant, chrono)}.
   *
   * @param instant the datetime object. If {@code strictly} is false, null means current date-time.
   * @param chrono the chronology, null means ISOChronology in UTC.
   * @param strictly if true, throw exceptions when the instant is invalid. And if false, returns null when the instant is invalid.
   * @return an instance from an object that represents a datetime.
   * @throws IllegalArgumentException if the instant is invalid.
   */
  private static DateTime getDateTime(final Object instant, final Chronology chrono, final boolean strictly) {
    if (instant instanceof DateTime) return ((DateTime) instant).withChronology(chrono == null ? ISOChronology.getInstanceUTC() : chrono);
    if (strictly && instant == null) return null;
    try {
      DateTime then = DateTime.now(chrono == null ? ISOChronology.getInstanceUTC() : chrono);
      if (instant instanceof Double) return then.withMillis(fromJD((Double) instant));
      if (instant instanceof Float) return then.withMillis(fromJD((Double) instant));
      if (instant instanceof BigDecimal) return then.withMillis(fromJD(((BigDecimal) instant).doubleValue()));
      if (instant instanceof BigInteger) return then.withMillis(((BigInteger) instant).longValue());
      if (instant instanceof Number) return then.withMillis((Long) instant);

      return new DateTime(instant, then.getChronology());
    } catch (IllegalArgumentException e) {
      if (strictly) throw e;
    }

    return DateTime.now(chrono);
  }

  /**
   * shorthand for {@code dateTime.withMills(new DateTime(instant).getMillis())}.
   *
   * @param instant the datetime object. If {@code strictly} is false, null means current date-time.
   * @param dateTime structure for {@code instant}.
   * @param strictly if true, throw exceptions when the instant is invalid. And if false, returns null when the instant is invalid.
   * @return an instance from an Object that represents a datetime.
   * @throws IllegalArgumentException if the instant is invalid.
   */
  private static DateTime getDateTime(final Object instant, final DateTime dateTime, final boolean strictly) {
    if (dateTime == null) return getDateTime(instant, ISOChronology.getInstanceUTC(), strictly);
    if (instant instanceof DateTime) return (DateTime) instant;
    if (strictly && instant == null) return null;
    try {
      if (instant instanceof Double) return dateTime.withMillis(fromJD((Double) instant));
      if (instant instanceof Float) return dateTime.withMillis(fromJD((Double) instant));
      if (instant instanceof BigDecimal) return dateTime.withMillis(fromJD(((BigDecimal) instant).doubleValue()));
      if (instant instanceof BigInteger) return dateTime.withMillis(((BigInteger) instant).longValue());
      if (instant instanceof Number) return dateTime.withMillis((Long) instant);

      return dateTime.withMillis(new DateTime(instant).getMillis());
    } catch (IllegalArgumentException e) {
      if (strictly) throw e;
    }

    return dateTime.withMillis(DateTime.now().getMillis());
  }

  /**
   * wrapping for {@code new DateTime(instant, zone)}.
   *
   * @param instant the datetime object. If {@code strictly} is false, null means current date-time.
   * @param zone the time zone, null means UTC.
   * @param strictly if true, throw exceptions when the instant is invalid. And if false, returns null when the instant is invalid.
   * @return an instance from an object that represents a datetime.
   * @throws IllegalArgumentException if the instant is invalid.
   */
  private static DateTime getDateTime(final Object instant, final DateTimeZone zone, final boolean strictly) {
    if (strictly && instant == null) return null;
    if (instant instanceof DateTime) return ((DateTime) instant).withZone(zone == null ? DateTimeZone.UTC : zone);
    try {
      DateTime then = DateTime.now(zone == null ? DateTimeZone.UTC : zone);
      if (instant instanceof Double) return then.withMillis(fromJD((Double) instant));
      if (instant instanceof Float) return then.withMillis(fromJD((Double) instant));
      if (instant instanceof BigDecimal) return then.withMillis(fromJD(((BigDecimal) instant).doubleValue()));
      if (instant instanceof BigInteger) return then.withMillis(((BigInteger) instant).longValue());
      if (instant instanceof Number) {
        return then.withMillis((Long) instant);
      }

      return new DateTime(instant, then.getZone());
    } catch (IllegalArgumentException e) {
      if (strictly) throw e;
    }

    return DateTime.now(zone == null ? DateTimeZone.UTC : zone);
  }

  /**
   * shorthand for {@code toAJD(instant, false)}.
   * <p>
   * use julian calendar at the date before 1582-10-15T0Z.
   * </p>
   *
   * @param instant the datetime object, null means current date-time.
   * @return the astronomical julian day represented by the specified instant.
   */
  public static Double toAJD(final Object instant) {
    return toAJD(instant, false);
  }

  /**
   * substitute for {@code org.joda.time.DateTimeUtils.toJulianDay(long)}.
   * <p>
   * Each day starts at midday (not midnight) and time is expressed as a fraction.
   * </p>
   *
   * @param instant the datetime object. If {@code strictly} is false, null means current date-time.
   * @param strictly if true, throw exceptions when the instant is invalid. And if false, returns null when the instant is invalid.
   * @return the astronomical julian day represented by the specified instant.
   * @throws IllegalArgumentException if the instant is invalid.
   */
  public static Double toAJD(final Object instant, final boolean strictly) {
    if (strictly && instant == null) return null;
    if (instant instanceof BigDecimal) return ((BigDecimal) instant).doubleValue();
    if (instant instanceof Double) return (Double) instant;
    if (instant instanceof Float) return (Double) instant;
    if (instant instanceof DateTime) return (((DateTime) instant).getMillis() / MILLIS_OF_DAY) + MILLIS_PERIOD;
    try {
      return (getDateTime(instant, GJChronology.getInstanceUTC(), strictly).getMillis() / MILLIS_OF_DAY) + MILLIS_PERIOD;
    } catch (IllegalArgumentException e) {
      if (strictly) throw e;
    }

    return (DateTime.now().getMillis() / MILLIS_OF_DAY) + MILLIS_PERIOD;
  }

  /**
   * shorthand for {@code toDT(instant, DateTimeZone.UTC, false)}.
   *
   * @param instant the datetime object, null means current date-time.
   * @return an instance from an Object that represents a datetime.
   */
  public static DateTime toDT(final Object instant) {
    return getDateTime(instant, DateTimeZone.UTC, false);
  }

  /**
   * shorthand for {@code toDT(instant, DateTimeZone.UTC, strictly)}.
   *
   * @param instant the datetime object. If {@code strictly} is false, null means current date-time.
   * @param strictly if true, throw exceptions when the instant is invalid. And if false, returns null when the instant is invalid.
   * @return an instance from an Object that represents a datetime.
   * @throws IllegalArgumentException if the instant is invalid.
   */
  public static DateTime toDT(final Object instant, final boolean strictly) {
    return getDateTime(instant, DateTimeZone.UTC, strictly);
  }

  /**
   * shorthand for {@code toDT(instant, initializer, false)}.
   *
   * @param instant the datetime object, null means current date-time.
   * @param initializer structure for {@code instant}.
   * @return an instance from an object that represents a datetime.
   */
  public static DateTime toDT(final Object instant, final Object initializer) {
    return toDT(instant, initializer, false);
  }

  /**
   * Constructs an instance from an Object that represents a datetime.
   * <p>
   * {@code initializer} specifiable
   * <ul>
   * <li>{@code DateTime} means {@code initializer.withMillis(new DateTime(instant).getMillis())}.</li>
   * <li>{@code DateTimeZone} and {@code TimeZone} means {@code new DateTime(instant, jp.furplag.util.Localizer.newDateTimeZone(initializer))}.</li>
   * <li>{@code String} means time zone ID or offset time ( {@code new DateTime(instant, jp.furplag.util.Localizer.newDateTimeZone(initializer))} ).</li>
   * <li>{@code Chronology} means {@code new DateTime(instant, initializer)}.</li>
   * <li>{@code long} means the milliseconds of offset to UTC ( {@code new DateTime(instant, DateTimeZone.forOffsetMillis(initializer.getMillisOfDay()))} ).</li>
   * <li>{@code LocalTime} means the time of offset to UTC ( {@code new DateTime(instant, DateTimeZone.forOffsetMillis(initializer.getMillisOfDay()))} ).</li>
   * </ul>
   * </p>
   *
   * @param instant the datetime object. If {@code strictly} is false, null means current date-time.
   * @param initializer structure for {@code instant}.
   * @param strictly if true, throw exceptions when the instant is invalid. And if false, returns null when the instant is invalid.
   * @return an instance from an Object that represents a datetime.
   * @throws IllegalArgumentException if the instant is invalid.
   */
  @SuppressWarnings("unchecked")
  public static DateTime toDT(final Object instant, final Object initializer, final boolean strictly) {
    if (strictly && instant == null) return null;
    if (initializer instanceof DateTime) return getDateTime(instant, ((DateTime) initializer), strictly);
    if (initializer instanceof DateTimeZone) return getDateTime(instant, ((DateTimeZone) initializer), strictly);
    if (initializer instanceof Chronology) return getDateTime(instant, ((Chronology) initializer), strictly);
    if (initializer instanceof TimeZone) return getDateTime(instant, Localizer.newDateTimeZone(initializer), strictly);
    if (initializer instanceof String) return getDateTime(instant, Localizer.newDateTimeZone(initializer), strictly);
    if (initializer instanceof LocalTime) return getDateTime(instant, DateTimeZone.forOffsetMillis(((LocalTime) initializer).getMillisOfDay()), strictly);
    if (initializer instanceof BigInteger) return getDateTime(instant, DateTimeZone.forOffsetMillis(materialize((BigInteger) initializer, Integer.class)), strictly);
    if (initializer instanceof Number && !is((Number) initializer, Float.class, Double.class, BigDecimal.class)) {
      return getDateTime(instant, DateTimeZone.forOffsetMillis(materialize((Long) initializer, Integer.class)), strictly);
    }

    return getDateTime(instant, DateTimeZone.UTC, strictly);
  }

  /**
   * shorthand for {@code toJDN(instant, false)}.
   *
   * @param instant the datetime object, null means current date-time.
   * @return the astronomical julian day represented by the specified instant.
   */
  public static Long toJDN(final Object instant) {
    return toJDN(instant, false);
  }

  /**
   * substitute for {@code org.joda.time.DateTimeUtils.toJulianDayNumber(long)}.
   * <p>
   * Each day starts at midnight and time is expressed as a fraction. JDN 0 is used for the date equivalent to Monday January 1, 4713 BC (Julian).
   * </p>
   *
   * @param instant the datetime object. If {@code strictly} is false, null means current date-time.
   * @param strictly if true, throw exceptions when the instant is invalid. And if false, returns null when the instant is invalid.
   * @return the astronomical julian day represented by the specified instant.
   * @throws IllegalArgumentException if the instant is invalid.
   */
  public static Long toJDN(final Object instant, final boolean strictly) {
    if (strictly && instant == null) return null;
    try {
      return floor(toAJD(instant, strictly) + .5).longValue();
    } catch (IllegalArgumentException e) {
      if (strictly) throw e;
    }

    return null;
  }

  /**
   * shorthand for {@code toMJD(instant, false)}.
   *
   * @param instant the datetime object, null means current date-time.
   * @return the modified julian day represented by the specified instant.
   */
  public static Double toMJD(final Object instant) {
    return toMJD(instant, false);
  }

  /**
   * Calculates the modified Julian Day Number for an instant.
   * <p>
   * Each day starts at midday (not midnight) and time is expressed as a fraction.
   * </p>
   *
   * @param instant the datetime object. If {@code strictly} is false, null means current date-time.
   * @param strictly if true, throw exceptions when the instant is invalid. And if false, returns null when the instant is invalid.
   * @return the modified julian day represented by the specified instant.
   * @throws IllegalArgumentException if the instant is invalid.
   */
  public static Double toMJD(final Object instant, final boolean strictly) {
    if (strictly && instant == null) return null;
    try {
      return toAJD(instant, strictly) - MJD_PERIOD;
    } catch (IllegalArgumentException e) {
      if (strictly) throw e;
    }

    return null;
  }

  /**
   * {@code DateTimeUtils} instances should NOT be constructed in standard programming.
   *
   */
  protected DateTimeUtils() {
    super();
  }
}
