/**
 * Copyright (C) 2015+ furplag (https://github.com/furplag/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.furplag.util.time;

import static jp.furplag.util.commons.NumberUtils.add;
import static jp.furplag.util.commons.NumberUtils.isInfiniteOrNaN;
import static jp.furplag.util.commons.NumberUtils.subtract;
import static jp.furplag.util.commons.NumberUtils.valueOf;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ClassUtils;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalTime;
import org.joda.time.chrono.GJChronology;
import org.joda.time.chrono.ISOChronology;

import jp.furplag.util.Localizer;
import jp.furplag.util.commons.NumberUtils;
import jp.furplag.util.commons.ObjectUtils;
import jp.furplag.util.commons.StringUtils;

/**
 * Utilities for date-time.
 *
 * @author furplag
 */
public class DateTimeUtils extends org.joda.time.DateTimeUtils {

  /** internal wrapping of java.text.DateFormat */
  public static enum FormatStyle {
    FULL(DateFormat.FULL), LONG(DateFormat.LONG), MEDIUM(DateFormat.MEDIUM), NONE(-1), SHORT(DateFormat.SHORT);

    static final Pattern PETTERN = Pattern.compile("^[FLMS\\-]{1,2}$", Pattern.CASE_INSENSITIVE);

    private FormatStyle(final int style) {
      this.style = style;
    }

    private final int style;

    public final int getStyle() {
      return style;
    }

    public final boolean is() {
      return this.style != NONE.style;
    }

    public final boolean is(final FormatStyle formatStyle) {
      return this.equals(formatStyle);
    }

    @Override
    public String toString() {
      return this.name().substring(0, 1).replace("N", "-");
    }

    public static final FormatStyle forStyle(final String style) {
      if ("F".equalsIgnoreCase(style)) return FULL;
      if ("L".equalsIgnoreCase(style)) return LONG;
      if ("M".equalsIgnoreCase(style)) return MEDIUM;
      if ("S".equalsIgnoreCase(style)) return SHORT;
      if ("-".equals(style)) return NONE;
      if (StringUtils.isSimilarToBlank(style)) throw new IllegalArgumentException("style must not be empty.");

      throw new IllegalArgumentException("Invalid style character: " + style);
    }

    public static final FormatStyle[] forStyles(final String style) {
      if (!PETTERN.matcher(StringUtils.defaultString(style)).matches()) return new FormatStyle[] { NONE, NONE };
      if (style.length() == 1) return new FormatStyle[] { forStyle(style), forStyle(style) };

      return new FormatStyle[] { forStyle(style.substring(0, 1)), forStyle(style.substring(1)) };
    }
  }

  /** the epoch julian date of 1582-10-15T00:00:00Z. */
  public static final long GREGORIAN_CUTOVER = -12219292800000L;

  /** the millis of one day. */
  public static final double MILLIS_OF_DAY = 86400000d;

  /** the epoch julian date of 1970-01-01T00:00:00Z. */
  public static final double MILLIS_PERIOD = 2440587.5;

  /** the epoch julian date of 1858-11-17T00:00:00Z. */
  public static final double MJD_PERIOD = 2400000.5;

  /**
   * {@code DateTimeUtils} instances should NOT be constructed in standard programming.
   */
  protected DateTimeUtils() {
    super();
  }

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
   * create a localized pattern string describing the datetime format from a two character style pattern.
   * <p>
   * The first character of {@code style} is the date style, and the second character is the time style.
   * </p>
   *
   * @param style specify a character of 'S' for short style, 'M' for medium, 'L' for long, and 'F' for full. A date or time may be omitted by specifying a style character '-'.
   * @param locale
   * @return
   */
  public static String getPattern(final String style, final Object locale) {
    FormatStyle[] styles = FormatStyle.forStyles(style);
    StringBuilder sb = new StringBuilder();
    if (styles[0].is()) sb.append(((SimpleDateFormat) DateFormat.getDateInstance(styles[0].style, Localizer.getAvailableLocale(locale))).toPattern());
    if (styles[0].is() && styles[1].is()) sb.append(" ");
    if (styles[1].is()) sb.append(((SimpleDateFormat) DateFormat.getTimeInstance(styles[1].style, Localizer.getAvailableLocale(locale))).toPattern());

    return sb.toString();
  }

  /**
   * if {@code true}, two datetime objects on the specified timezone are on the same day.
   *
   * @param aDay if {@code null}, always return {@code false} (not means now) .
   * @param anotherDay instant the datetime object, null means current datetime.
   * @param zone TimeZone.
   * @return {@code true} if they represent the same day.
   */
  public static boolean isSameDay(final Object aDay, final Object anotherDay, final DateTimeZone zone) {
    DateTime then = toDT(aDay, zone, true);

    return then != null && then.withTimeAtStartOfDay().isEqual(toDT(anotherDay, zone).withTimeAtStartOfDay());
  }

  public static boolean isToday(final Object then) {
    return isSameDay(then, DateTime.now(), DateTimeZone.getDefault());
  }

  public static boolean isToday(final Object then, final DateTimeZone zone) {
    return isSameDay(then, DateTime.now(), zone);
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
   * {@link org.joda.time.DateTimeUtils#toJulianDay(long)}.
   * <p>
   * Each day starts at midday (not midnight) and time is expressed as a fraction.
   * </p>
   *
   * @param instant the date-time object, null means current date-time.
   * @param strictly if true, returns null when the instant is invalid.
   * @return the astronomical julian day represented by the specified instant.
   */
  public static Double toAJD(final Object instant, final boolean strictly) {
    DateTime then = getDateTime(instant, GJChronology.getInstanceUTC(), strictly);
    if (then == null) return null;

    return (then.getMillis() / MILLIS_OF_DAY) + MILLIS_PERIOD;
  }

  public static double toAJDAtEndOfDay(final Object instant, final DateTimeZone zone) {
    return toAJD(toDT(instant, zone).plusDays(1).withTimeAtStartOfDay().minusMillis(1));
  }

  public static double toAJDAtStartOfDay(final Object instant, final DateTimeZone zone) {
    return toAJD(toDT(instant, zone).withTimeAtStartOfDay());
  }

  /**
   * calculate Chronological Julian Day represented by specified instant.
   *
   * @param instant
   * @param zone
   * @return
   */
  public static double toCJD(final Object instant, final DateTimeZone zone) {
    DateTime then = toDT(instant, zone);

    return toAJD(then.plusMillis(zone.getOffset(then))) + .5d;
  }

  /**
   * shorthand for {@code toDT(instant, DateTimeZone.UTC, false)}.
   *
   * @param instant the datetime object, null means current date-time.
   * @return an instance from an Object that represents a datetime.
   */
  public static DateTime toDT(final Object instant) {
    return toDT(instant, false);
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
    if (instant instanceof DateTime) return new DateTime(((DateTime) instant).getMillis(), ((DateTime) instant).getZone());

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
  public static DateTime toDT(final Object instant, final Object initializer, final boolean strictly) {
    if (strictly && instant == null) return null;
    if (initializer == null) return getDateTime(instant, DateTimeZone.UTC, strictly);
    if (initializer instanceof DateTime) return getDateTime(instant, ((DateTime) initializer), strictly);
    if (initializer instanceof DateTimeZone) return getDateTime(instant, ((DateTimeZone) initializer), strictly);
    if (initializer instanceof Chronology) return getDateTime(instant, ((Chronology) initializer), strictly);
    if (initializer instanceof TimeZone) return getDateTime(instant, Localizer.getDateTimeZone(initializer), strictly);
    if (initializer instanceof String) return getDateTime(instant, Localizer.getDateTimeZone(initializer), strictly);
    if (initializer instanceof LocalTime) return getDateTime(instant, DateTimeZone.forOffsetMillis(((LocalTime) initializer).getMillisOfDay()), strictly);
    if (Number.class.isAssignableFrom(ClassUtils.primitiveToWrapper(initializer.getClass()))) {
      if (NumberUtils.compareTo(valueOf(initializer), MILLIS_OF_DAY) < 0) return getDateTime(instant, DateTimeZone.forOffsetMillis(valueOf(initializer, int.class)), strictly);
    }

    return getDateTime(instant, DateTimeZone.UTC, strictly);
  }

  /**
   * shorthand for {@code toJDN(instant, false)}.
   *
   * @param instant the datetime object, null means current date-time.
   * @return the astronomical julian day represented by the specified instant.
   */
  public static long toJDN(final Object instant) {
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
    Double ajd = toAJD(instant, strictly);

    return ajd == null ? null : add(ajd, .5d, long.class);
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
    Double ajd = toAJD(instant, strictly);

    return ajd == null ? null : subtract(ajd, MJD_PERIOD, double.class);
  }

  /**
   * {@link org.joda.time.DateTime#DateTime(Chronology)}.
   *
   * @param instant the date-time object, null means current date-time.
   * @param chrono the chronology, null means ISOChronology in UTC (NOT default) .
   * @param strictly if true, returns null when the instant is invalid.
   * @return {@link org.joda.time.DateTime#DateTime(Chronology)}.
   */
  private static DateTime getDateTime(final Object instant, final Chronology chrono, final boolean strictly) {
    if (instant instanceof DateTime) return ((DateTime) instant).withChronology(chrono == null ? ISOChronology.getInstanceUTC() : chrono);
    if (strictly && instant == null) return null;
    if (instant == null) return DateTime.now(chrono == null ? ISOChronology.getInstanceUTC() : chrono);
    Long millis = getMillis(instant, strictly);
    if (millis == null) return null;
    try {
      return new DateTime((long) millis, chrono == null ? ISOChronology.getInstanceUTC() : chrono);
    } catch (Exception e) {}
    if (strictly) return null;

    return DateTime.now(chrono == null ? ISOChronology.getInstanceUTC() : chrono);
  }

  /**
   * {@link org.joda.time.DateTime#withMillis(long)}.
   *
   * @param instant the date-time object, null means current date-time.
   * @param dateTime structure for {@code instant}.
   * @param strictly if true, returns null when the instant is invalid.
   * @return {@link org.joda.time.DateTime#withMillis(long)}.
   */
  private static DateTime getDateTime(final Object instant, final DateTime dateTime, final boolean strictly) {
    if (strictly && instant == null) return null;
    if (dateTime == null) return getDateTime(instant, ISOChronology.getInstanceUTC(), strictly);
    if (instant == null) return dateTime.withMillis(DateTime.now().getMillis());
    Long millis = getMillis(instant, strictly);
    if (millis == null) return null;
    try {
      return dateTime.withMillis(millis);
    } catch (Exception e) {}
    if (strictly) return null;

    return dateTime.withMillis(DateTime.now().getMillis());
  }

  /**
   * {@link org.joda.time.DateTime#DateTime(Object, DateTimeZone)}.
   *
   * @param instant the date-time object, null means current date-time.
   * @param zone the time zone, null means UTC (NOT default) .
   * @param strictly if true, returns null when the instant is invalid.
   * @return {@link org.joda.time.DateTime#DateTime(Object, DateTimeZone)}.
   */
  private static DateTime getDateTime(final Object instant, final DateTimeZone zone, final boolean strictly) {
    if (strictly && instant == null) return null;
    if (instant == null) return DateTime.now(zone == null ? DateTimeZone.UTC : zone);
    Long millis = getMillis(instant, strictly);
    if (millis == null) return null;
    try {
      if (millis > GREGORIAN_CUTOVER) return new DateTime((long) millis, zone == null ? DateTimeZone.UTC : zone);

      return new DateTime((long) millis, zone == null ? GJChronology.getInstanceUTC() : GJChronology.getInstance(zone));
    } catch (Exception e) {}
    if (strictly) return null;

    return DateTime.now(zone == null ? DateTimeZone.UTC : zone);
  }

  /**
   * {@link org.joda.time.DateTime#getMillis()}.
   *
   * @param instant the date-time object, null means current date-time.
   * @param strictly if true, returns null when the instant is invalid.
   * @return {@link org.joda.time.DateTime#getMillis()}.
   */
  private static Long getMillis(final Object instant, final boolean strictly) {
    if (instant instanceof DateTime) return ((DateTime) instant).getMillis();
    if (instant != null && ObjectUtils.isAny(ClassUtils.primitiveToWrapper(instant.getClass()), BigDecimal.class, Double.class, Float.class)) {
      if (isInfiniteOrNaN(valueOf(instant, double.class))) return null;
      return fromJD(valueOf(instant, double.class));
    }
    if (instant != null
        && ObjectUtils.isAny(ClassUtils.primitiveToWrapper(instant.getClass()), BigInteger.class, Byte.class, Short.class, Integer.class, Long.class)) { return valueOf(instant, long.class); }
    try {
      return new DateTime(instant, GJChronology.getInstanceUTC()).getMillis();
    } catch (Exception e) {}
    if (strictly) return null;

    return DateTime.now().getMillis();
  }
}
