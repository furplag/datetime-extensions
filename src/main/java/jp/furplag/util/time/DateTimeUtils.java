package jp.furplag.util.time;

import static jp.furplag.util.commons.NumberUtils.floor;
import static jp.furplag.util.commons.NumberUtils.materialize;

import org.joda.time.DateTime;

public class DateTimeUtils extends org.joda.time.DateTimeUtils {

  /** the epoch julian date of 1858-11-17T00:00:00Z */
  protected static final double MJD_PERIOD = 2400000.5;

  protected DateTimeUtils() {
    super();
  }

  protected static DateTime fromJDtoDT(final double julianDay) {
    return new DateTime(fromJulianDay(julianDay));
  }

  protected static DateTime fromJDtoDT(final double julianDay, final DateTime dateTime) {
    if (dateTime == null) new DateTime(fromJulianDay(julianDay));

    return dateTime.withMillis(fromJulianDay(julianDay));
  }

  /**
   * <p>
   * shorthand for {@code org.joda.time.DateTimeUtils.toJulianDay(dateTime.getMillis())}.
   * </p>
   * <p>
   * Each day starts at midday (not midnight) and time is expressed as a fraction.
   * </p>
   *
   * @param dateTime
   * @return the astronomical Julian Day represented by the specified instant
   * @see org.joda.time.DateTimeUtils.toJulianDay
   */
  protected static Double toAJD(final DateTime dateTime, boolean toFixTimeDiff) {
    if (dateTime == null) return null;
    return toJulianDay(dateTime.getMillis());
  }

  protected static Double toAJD(final DateTime dateTime) {
    return toAJD(dateTime, false);
  }

  /**
   * <p>
   * Calculates the modified Julian Day Number for an instant.
   * </p>
   * <p>
   * Each day starts at midday (not midnight) and time is expressed as a fraction.
   * </p>
   *
   * @param dateTime
   * @return the Modified Julian Day represented by the specified instant
   * @see org.joda.time.DateTimeUtils.toJulianDay
   */
  protected static Double toMJD(final DateTime dateTime) {
    return dateTime != null ? toJulianDay(dateTime.getMillis()) - MJD_PERIOD : null;
  }

  /**
   * <p>
   * shorthand for {@code org.joda.time.DateTimeUtils.toJulianDayNumber(dateTime.getMillis())}.
   * </p>
   * <p>
   * Each day starts at midnight and time is expressed as a fraction. JDN 0 is used for the date equivalent to Monday January 1, 4713 BC (Julian).
   * </p>
   *
   * @param dateTime
   * @return the astronomical Julian Day Number represented by the specified instant
   * @see org.joda.time.DateTimeUtils.toJulianDayNumber
   */
  protected static Long toJDN(final DateTime dateTime) {
    return dateTime == null ? null : materialize(floor(toJulianDay(dateTime.getMillis()) + .5d), Long.class);
  }
}
