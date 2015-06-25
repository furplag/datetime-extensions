/**
 *
 */
package jp.furplag.util.time.lunisolar;

import static jp.furplag.util.commons.NumberUtils.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import jp.furplag.util.Localizer;
import jp.furplag.util.commons.StringUtils;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.PeriodType;

import com.google.common.collect.ImmutableMap;

/**
 * utilities for East Asian Lunisolar Date Time.
 *
 * @author furplag
 *
 */
public class LunisolarDateTimeUtils extends jp.furplag.util.time.DateTimeUtils {

  public static final Pattern PATTERN_FORMAT_STYLE = Pattern.compile("^[FLMS\\-]{1,2}$", Pattern.CASE_INSENSITIVE);

  /** internal wrapping of java.text.DateFormat */
  public static enum FormatStyle {
    FULL(DateFormat.FULL), LONG(DateFormat.LONG), MEDIUM(DateFormat.MEDIUM), SHORT(DateFormat.SHORT), NONE(-1);

    private static final FormatStyle forStyle(final String style) {
      if ("F".equals(style)) return FULL;
      if ("L".equals(style)) return LONG;
      if ("M".equals(style)) return MEDIUM;
      if ("S".equals(style)) return SHORT;
      if ("-".equals(style)) return NONE;
      if (StringUtils.isSimilarToBlank(style)) throw new IllegalArgumentException("style must not be empty.");

      throw new IllegalArgumentException("Invalid style character: " + style);
    }

    private final int style;

    private FormatStyle(final int style) {
      this.style = style;
    }

    public final boolean is() {
      return this.style != NONE.style;
    }

    public final boolean is(final FormatStyle formatStyle) {
      return this.equals(formatStyle);
    }
  }

  /** the days of julian year. */
  public static final double JULIAN_YEAR = 365.25;

  /** the epoch julian date of 2001-01-01T12:00:00Z */
  public static final double J2000 = 2451545d;

  /** the days of month in the moon. */
  public static final double SYNODIC_MONTH = 29.530588853;

  /** delta of the days of month in the moon. */
  public static final double SYNODIC_MONTH_INCREMENTAL = .000000002162;

  /** the perturbations of ecliptic longitude of the sun and the moon. */
  private static final Map<String, Double[][]> PERTURBATIONS;

  /** proportional parameters of the perturbations of ecliptic longitude of the sun and the moon. */
  private static final Map<String, Double[][]> PROPORTIONALS;

  static {
    Map<String, Double[][]> map = new HashMap<String, Double[][]>();
    map.put("sun", new Double[][]{{.0004, 1d, 31557d, 161d}, {.0004, 1d, 29930d, 48d}, {.0005, 1d, 2281d, 221d}, {.0005, 1d, 155d, 118d}, {.0006, 1d, 33718d, 316d}, {.0007, 1d, 9038d, 64d}, {.0007, 1d, 3035d, 110d}, {.0007, 1d, 65929d, 45d}, {.0013, 1d, 22519d, 352d}, {.0015, 1d, 45038d, 254d}, {.0018, 1d, 445267d, 208d}, {.0018, 1d, 19d, 159d}, {.002, 1d, 32964d, 158d}, {.02, 1d, 71998.1, 265.1}, {-.0048, null, 35999.05, 267.52}, {1.9147, 1d, 35999.05, 267.52}});
    map.put("moon", new Double[][]{{.0003, 1d, 2322131d, 191d}, {.0003, 1d, 4067d, 70d}, {.0003, 1d, 549197d, 220d}, {.0003, 1d, 1808933d, 58d}, {.0003, 1d, 349472d, 337d}, {.0003, 1d, 381404d, 354d}, {.0003, 1d, 958465d, 340d}, {.0004, 1d, 12006d, 187d}, {.0004, 1d, 39871d, 223d}, {.0005, 1d, 509131d, 242d}, {.0005, 1d, 1745069d, 24d}, {.0005, 1d, 1908795d, 90d}, {.0006, 1d, 2258267d, 156d}, {.0006, 1d, 111869d, 38d}, {.0007, 1d, 27864d, 127d}, {.0007, 1d, 485333d, 186d}, {.0007, 1d, 405201d, 50d}, {.0007, 1d, 790672d, 114d}, {.0008, 1d, 1403732d, 98d}, {.0009, 1d, 858602d, 129d}, {.0011, 1d, 1920802d, 186d}, {.0012, 1d, 1267871d, 249d}, {.0016, 1d, 1856938d, 152d}, {.0018, 1d, 401329d, 274d}, {.0021, 1d, 341337d, 16d}, {.0021, 1d, 71998d, 85d}, {.0021, 1d, 990397d, 357d}, {.0022, 1d, 818536d, 151d}, {.0023, 1d, 922466d, 163d}, {.0024, 1d, 99863d, 122d}, {.0026, 1d, 1379739d, 17d}, {.0027, 1d, 918399d, 182d}, {.0028, 1d, 1934d, 145d}, {.0037, 1d, 541062d, 259d}, {.0038, 1d, 1781068d, 21d}, {.0040, 1d, 133d, 29d}, {.0040, 1d, 1844932d, 56d}, {.0040, 1d, 1331734d, 283d}, {.0050, 1d, 481266d, 205d}, {.0052, 1d, 31932d, 107d}, {.0068, 1d, 926533d, 323d}, {.0079, 1d, 449334d, 188d}, {.0085, 1d, 826671d, 111d}, {.0100, 1d, 1431597d, 315d}, {.0107, 1d, 1303870d, 246d}, {.0110, 1d, 489205d, 142d}, {.0125, 1d, 1443603d, 52d}, {.0154, 1d, 75870d, 41d}, {.0304, 1d, 513197.9, 222.5}, {.0347, 1d, 445267.1, 27.9}, {.0409, 1d, 441199.8, 47.4}, {.0458, 1d, 854535.2, 148.2}, {.0533, 1d, 1367733.1, 280.7}, {.0571, 1d, 377336.3, 13.2}, {.0588, 1d, 63863.5, 124.2}, {.1144, 1d, 966404d, 276.5}, {.1851, 1d, 35999d, 87.53}, {.2136, 1d, 954397.7, 179.93}, {.6583, 1d, 890534.2, 145.7}, {1.2740, 1d, 413335.3, 10.74}, {6.2888, 1d, 477198.86, 44.963}});
    PERTURBATIONS = ImmutableMap.copyOf(map);

    map.clear();

    map.put("sun", new Double[][]{{36000.7695, null}, {280.4659, 1d}});
    map.put("moon", new Double[][]{{481267.8809, null}, {218.3162, 1d}});
    PROPORTIONALS = ImmutableMap.copyOf(map);
  }

  /** interval of months in ancient lunisolar calendar. */
  protected static final double[] ARCHAIC_LUNAR_MONTHS = {15.28995536, 15.57566964, 15.69471726, 15.81376488, 15.9328125, 15.9328125, 15.81376488, 15.69471726, 15.57566964, 15.4328125, 15.28995536, 15.14709821, 15.00424107, 14.86138393, 14.74233631, 14.62328869, 14.50424107, 14.50424107, 14.62328869, 14.74233631, 14.86138393, 15.00424107, 15.14709821};

  /**
   * calculate the day of month in Lunisolar calendars.
   *
   * @param dateTime
   * @return
   */
  public static int getDayOfMonth(final DateTime dateTime) {
    return getDayOfMonth(getFirstDayOfMonth(dateTime), dateTime);
  }

  /**
   * calculate the day of month in Lunisolar calendars.
   *
   * @param firstDayOfMonth
   * @param dateTime
   * @return
   */
  public static int getDayOfMonth(final DateTime firstDayOfMonth, final DateTime dateTime) {
    if (!(firstDayOfMonth != null && dateTime != null)) return -1;
    DateTime firstDay = firstDayOfMonth.withZone(dateTime.getZone()).withTimeAtStartOfDay();
    DateTime then = dateTime.withZone(dateTime.getZone()).withTimeAtStartOfDay();
    if (firstDay.isAfter(then)) return -1;

    return new Interval(firstDay, then).toPeriod(PeriodType.days()).getDays();
  }

  /**
   * calculate the ecliptic longitude of the sun and the moon.
   *
   * @param julianDay
   * @param perturbations
   * @param proportionals
   * @return the longitude (between 0 &deg; and 360 &deg;).
   */
  private static double getEclipticLongitude(final double julianDay, final Double[][] perturbations, final Double[][] proportionals) {
    if (perturbations == null) throw new IllegalArgumentException("perturbations must not be null.");
    if (proportionals == null) throw new IllegalArgumentException("proportionals must not be null.");
    final BigDecimal tt = divide(subtract(new BigDecimal(julianDay), J2000), JULIAN_YEAR * 100d);
    BigDecimal longitude = new BigDecimal(0d);
    for (Double[] perturbation : perturbations) {
      BigDecimal temporary = cos(toRadian(circulate(add(multiply(round(tt, 17), perturbation[2]), perturbation[3]))));
      temporary = multiply(temporary, multiply(new BigDecimal(perturbation[0]), perturbation[1] == null ? round(tt, 17) : perturbation[1]));
      longitude = add(longitude, temporary);
    }
    for (Double[] proportional : proportionals) {
      BigDecimal temporary = multiply(new BigDecimal(proportional[0]), (proportional[1] == null ? round(tt, 17) : proportional[1]));
      longitude = add(longitude, temporary);
    }

    return materialize(circulate(longitude), Double.class);
  }

  /**
   * calculate the ecliptic longitude of the moon.
   *
   * @param then
   * @return
   */
  public static double getELoMoon(final DateTime then) {
    return getEclipticLongitude(toAJD(then), PERTURBATIONS.get("moon"), PROPORTIONALS.get("moon"));
  }

  /**
   * calculate the ecliptic longitude of the moon.
   *
   * @param julianDay
   * @return
   */
  public static double getELoMoon(final double julianDay) {
    return getEclipticLongitude(julianDay, PERTURBATIONS.get("moon"), PROPORTIONALS.get("moon"));
  }

  /**
   * calculate the ecliptic longitude of the sun.
   *
   * @param then
   * @return
   */
  public static double getELoSun(final DateTime then) {
    return getEclipticLongitude(toAJD(then), PERTURBATIONS.get("sun"), PROPORTIONALS.get("sun"));
  }

  /**
   * calculate the ecliptic longitude of the sun.
   *
   * @param julianDay
   * @return
   */
  public static double getELoSun(final double julianDay) {
    return getEclipticLongitude(julianDay, PERTURBATIONS.get("sun"), PROPORTIONALS.get("sun"));
  }

  /**
   * calculate the first day of lunisolar month.
   *
   * @param dateTIme
   * @return
   */
  public static DateTime getFirstDayOfMonth(final DateTime dateTime) {
    return toDT(getFirstDayOfMonth(toAJD(dateTime.withTimeAtStartOfDay().plusDays(1).minusMillis(1))), dateTime);
  }

  /**
   * calculate the first day of lunisolar month.
   *
   * @param julianDay
   * @return
   */
  public static double getFirstDayOfMonth(final double julianDay) {
    final BigDecimal margin = new BigDecimal(1E-6);
    BigDecimal temporary = new BigDecimal(julianDay);
    BigDecimal elOfSun;
    BigDecimal elOfMoon;
    BigDecimal delta;
    BigDecimal diff;
    int counter = 0;
    do {
      counter++;
      elOfSun = materialize(getELoSun(temporary.doubleValue()), BigDecimal.class);
      elOfMoon = materialize(getELoMoon(temporary.doubleValue()), BigDecimal.class);
      delta = subtract(elOfMoon, elOfSun);
      if (counter == 1) delta = circulate(delta);
      if (compareTo(delta, 280) > -1) delta = add(delta.negate(), -360);
      if (!contains(delta, -40, 40)) delta = circulate(delta);
      diff = divide(multiply(delta, optimizeSynodicMonth(temporary.doubleValue())), 360);
      temporary = subtract(temporary, diff);
      if (margin.compareTo(delta.abs()) > -1 && compareTo(temporary, julianDay) > 0) {
        temporary = subtract(temporary, divide(optimizeSynodicMonth(temporary.doubleValue()), 2, 2));
        delta = BigDecimal.TEN;
      }
    } while (delta.abs().compareTo(margin) > -1 && counter < 1E5);

    return temporary.doubleValue();
  }

  /**
   * calculate the first day of year at Lunisolar calendar.
   *
   * @param dateTime
   * @return
   */
  public static DateTime getFirstDayOfYear(final DateTime dateTime) {
    return dateTime == null ? null : getFirstDayOfMonth(getLatestSeasonTerm(dateTime.plusMonths(normalize((getSolarTerm(getLatestSeasonPeak(dateTime)) / 2) + 2, 1, 12) < 4 ? 2 : 0), 0d).minusDays(30));
  }

  /**
   * calculate the first day of year at Lunisolar calendar.
   *
   * @param dateTime
   * @param seasonCurrentTerm
   * @return
   */
  public static DateTime getFirstDayOfYear(final DateTime dateTime, final int seasonCurrentTerm) {
    return dateTime == null ? null : getFirstDayOfMonth(getLatestSeasonTerm(dateTime.plusMonths(normalize((seasonCurrentTerm / 2) + 2, 1, 12) < 4 ? 2 : 0), 0d).minusDays(30));
  }

  /**
   * calculate the latest (Autumn) equinox or (Winter) solstice.
   *
   * <p>
   * <ul>
   * <li>
   * Spring equinox (0 &deg; and 360 &deg;).</li>
   * <li>
   * Summer solstice (90 &deg;).</li>
   * <li>
   * Autumn equinox (180 &deg;).</li>
   * <li>
   * Winter solstice (270 &deg;).</li>
   * </ul>
   * </p>
   *
   * @param dateTime
   * @return
   */
  public static DateTime getLatestSeasonPeak(final DateTime dateTime) {
    return toDT(getLatestSeasonPeak(toAJD(dateTime)), dateTime);
  }

  /**
   * calculate the latest (Autumn) equinox or (Winter) solstice.
   *
   * <p>
   * <ul>
   * <li>
   * Spring equinox (0 &deg; and 360 &deg;).</li>
   * <li>
   * Summer solstice (90 &deg;).</li>
   * <li>
   * Autumn equinox (180 &deg;).</li>
   * <li>
   * Winter solstice (270 &deg;).</li>
   * </ul>
   * </p>
   *
   * @param julianDay
   * @return
   */
  public static double getLatestSeasonPeak(final double julianDay) {
    double expected = getELoSun(julianDay);

    return getLatestSeasonTerm(julianDay, expected <= 90d ? 0d : expected > 270d ? 270d : (90d < expected && expected <= 180d) ? 90d : 180d);
  }

  /**
   * calculate the latest (Autumn) equinox or (Winter) solstice.
   *
   * <p>
   * <ul>
   * <li>
   * Spring equinox (0 &deg; and 360 &deg;).</li>
   * <li>
   * Summer solstice (90 &deg;).</li>
   * <li>
   * Autumn equinox (180 &deg;).</li>
   * <li>
   * Winter solstice (270 &deg;).</li>
   * </ul>
   * </p>
   *
   * @param dateTime
   * @param angle
   * @return
   */
  protected static DateTime getLatestSeasonTerm(final DateTime dateTime, final double angle) {
    return toDT(getLatestSeasonTerm(toAJD(dateTime), angle), dateTime);
  }

  /**
   * calculate the latest (Autumn) equinox or (Winter) solstice.
   *
   * <p>
   * <ul>
   * <li>
   * Spring equinox (0 &deg; and 360 &deg;).</li>
   * <li>
   * Summer solstice (90 &deg;).</li>
   * <li>
   * Autumn equinox (180 &deg;).</li>
   * <li>
   * Winter solstice (270 &deg;).</li>
   * </ul>
   * </p>
   *
   * @param julianDay
   * @param angle
   * @return
   */
  protected static double getLatestSeasonTerm(final double julianDay, final double angle) {
    final BigDecimal expected = new BigDecimal(round(circulate(angle)));
    final BigDecimal margin = new BigDecimal(1E-6);
    BigDecimal temporary = new BigDecimal(julianDay);
    BigDecimal longitude = new BigDecimal(0d);
    BigDecimal delta;
    BigDecimal diff;
    int counter = 0;
    do {
      counter++;
      longitude = materialize(getELoSun(temporary.doubleValue()), longitude);
      if (expected.signum() == 0 && compareTo(longitude, 359) > 0) longitude = add(longitude, -360);
      delta = subtract(longitude, expected);
      diff = divide(multiply(delta, JULIAN_YEAR), 360d);
      temporary = temporary.subtract(diff);
    } while (delta.abs().compareTo(margin) > -1 && counter < 1E5);

    return temporary.doubleValue();
  }

  /**
   * calculate the month of year in Lunisolar calendar.
   *
   * @param dateTime
   * @return
   */
  public static int getMonthOfYear(final DateTime dateTime) {
    return getMonthOfYear(getELoSun(dateTime));
  }

  /**
   * calculate the month of year in Lunisolar calendar.
   *
   * @param longitude
   * @return
   */
  public static int getMonthOfYear(final double longitude) {
    double index = (getSolarTerm(longitude) / 2d) + 2d;
    if (index != (int) index) index += 1d;

    return (int) index == 12 ? (int) index : ((int) index % 12);
  }

  /**
   * calculate the closest (Autumn) equinox or (Winter) solstice.
   *
   * <p>
   * <ul>
   * <li>
   * Spring equinox (0 &deg; and 360 &deg;).</li>
   * <li>
   * Summer solstice (90 &deg;).</li>
   * <li>
   * Autumn equinox (180 &deg;).</li>
   * <li>
   * Winter solstice (270 &deg;).</li>
   * </ul>
   * </p>
   *
   * @param dateTime
   * @param angle
   * @return
   */
  protected static DateTime getNearbySeasonTerm(final DateTime dateTime, final double angle) {
    return toDT(getNearbySeasonTerm(toAJD(dateTime), angle), dateTime);
  }

  /**
   * calculate the closest (Autumn) equinox or (Winter) solstice.
   *
   * <p>
   * <ul>
   * <li>
   * Spring equinox (0 &deg; and 360 &deg;).</li>
   * <li>
   * Summer solstice (90 &deg;).</li>
   * <li>
   * Autumn equinox (180 &deg;).</li>
   * <li>
   * Winter solstice (270 &deg;).</li>
   * </ul>
   * </p>
   *
   * @param julianDay
   * @param angle
   * @return
   */
  protected static double getNearbySeasonTerm(final double julianDay, final double angle) {
    double prev = getLatestSeasonTerm(julianDay, angle);
    double next = getLatestSeasonTerm(julianDay + JULIAN_YEAR, angle);
    if (prev == next) next = getLatestSeasonTerm(add(next, optimizeSynodicMonth(julianDay)), angle);

    return julianDay - prev > next - julianDay ? next : prev;
  }

  /**
   * calculate the closest position of the 24 points of Lunisolar calendars.
   *
   * @param dateTime
   * @param termLength
   * @return
   */
  public static int getSolarTerm(final DateTime dateTime) {
    return getSolarTerm(getELoSun(dateTime));
  }

  /**
   * calculate the closest position of the 24 points of Lunisolar calendars.
   *
   * @param longitude
   * @return
   */
  protected static int getSolarTerm(final double longitude) {
    return materialize(remainder(ceil(divide((circulate(longitude) * 24), 360, 8, RoundingMode.FLOOR)), 24), Integer.class);
  }

  /**
   * optimized synodic month.
   *
   * @param julianDay
   * @return
   */
  protected static BigDecimal optimizeSynodicMonth(final double julianDay) {
    //return new BigDecimal(SYNODIC_MONTH);
        return add(multiply(subtract(new BigDecimal(julianDay), J2000), SYNODIC_MONTH_INCREMENTAL), SYNODIC_MONTH);
  }

  /**
   * calculate lunisolar date.
   *
   * <p>
   * returns :
   * </p>
   * <p>
   * <ul>
   * <li>
   * ajd: the astronomical Julian Day represented by the specified instant.</li>
   * <li>
   * jdn: the astronomical Julian Day Number represented by the specified instant.</li>
   * <li>
   * longitude: the ecliptic longitude of the sun in specified instant.</li>
   * <li>
   * solarTerm: the closest position of the 24 points of Lunisolar calendars.</li>
   * <li>
   * seasonCurrent: the latest (Autumn) equinox or (Winter) solstice.</li>
   * <li>
   * seasonCurrentLongitude: the ecliptic longitude of the sun in peak of current season.</li>
   * <li>
   * seasonCurrentTerm: the solar term in peak of current season.</li>
   * <li>
   * firstDayOfYear: the first day of the year in current Lunisolar calendar.</li>
   * <li>
   * monthOfYear: the first day of Lunisolar month.</li>
   * <li>
   * dayOfMonth: the day of Lunisolar month represented by specified instant.</li>
   * <li>
   * dayOfWeek: the day of week represented by "六曜".</li>
   * <li>
   * isIntercalary: if {@code true}, the month is an intercalary month in Lunisolar calendar.</li>
   * <li>
   * isPeakOfSeason: if {@code true}, the day is (Autumn) equinox or (Winter) solstice.</li>
   * </ul>
   * </p>
   *
   * @param dateTime
   * @return
   */

  public static Map<String, Object> toLunisolar(final DateTime dateTime) {
    return toLunisolar(dateTime, false);
  }

  /**
   * calculate lunisolar date.
   *
   * <p>
   * returns :
   * </p>
   * <p>
   * <ul>
   * <li>
   * ajd: the astronomical Julian Day represented by the specified instant.</li>
   * <li>
   * jdn: the astronomical Julian Day Number represented by the specified instant.</li>
   * <li>
   * longitude: the ecliptic longitude of the sun in specified instant.</li>
   * <li>
   * solarTerm: the closest position of the 24 points of Lunisolar calendars.</li>
   * <li>
   * seasonCurrent: the latest (Autumn) equinox or (Winter) solstice.</li>
   * <li>
   * seasonCurrentLongitude: the ecliptic longitude of the sun in peak of current season.</li>
   * <li>
   * seasonCurrentTerm: the solar term in peak of current season.</li>
   * <li>
   * firstDayOfYear: the first day of the year in current Lunisolar calendar.</li>
   * <li>
   * monthOfYear: the first day of Lunisolar month.</li>
   * <li>
   * dayOfMonth: the day of Lunisolar month represented by specified instant.</li>
   * <li>
   * dayOfWeek: the day of week represented by "六曜".</li>
   * <li>
   * isIntercalary: if {@code true}, the month is an intercalary month in Lunisolar calendar.</li>
   * <li>
   * isPeakOfSeason: if {@code true}, the day is (Autumn) equinox or (Winter) solstice.</li>
   * </ul>
   * </p>
   *
   * @param dateTime
   * @return
   */
  private static Map<String, Object> toLunisolar(final DateTime dateTime, boolean printStackTrace) {
    Map<String, Object> lunisolar = new LinkedHashMap<String, Object>();
    if (dateTime == null) return lunisolar;
    DateTime utc = toDT(dateTime);
    double ajd = toAJD(dateTime);
    long jdn = toJDN(ajd);
    double longitude = getELoSun(dateTime);
    int solarTerm = getSolarTerm(longitude);
    int dayOfMonth = -1;
    boolean isIntercalary = false;

    lunisolar.put("then", dateTime);
    lunisolar.put("utc", utc);
    lunisolar.put("julianDay", ajd);
    lunisolar.put("julianDayNumber", jdn);
    lunisolar.put("longitude", longitude);
    lunisolar.put("solarTerm", solarTerm);

    DateTime seasonCurrent = getLatestSeasonPeak(dateTime);
    double seasonCurrentLongitude = round(getELoSun(seasonCurrent));
    int seasonCurrentTerm = getSolarTerm(seasonCurrentLongitude);
    lunisolar.put("seasonCurrent", seasonCurrent);
    lunisolar.put("seasonCurrentLongitude", seasonCurrentLongitude);
    lunisolar.put("seasonCurrentTerm", seasonCurrentTerm);

    int monthOfYear = normalize(ceil(seasonCurrentTerm / 2) + 2, 1, 12);
    DateTime temporary = seasonCurrent;

    if (dateTime.withTimeAtStartOfDay().isEqual(seasonCurrent.withZone(dateTime.getZone()).withTimeAtStartOfDay())) {
      lunisolar.put("solarTerm", seasonCurrentTerm);
      temporary = solarTerm == 0 ? seasonCurrent : getLatestSeasonTerm(dateTime, 0d);
      if (printStackTrace) lunisolar.put("springEquinox", temporary);
      lunisolar.put("firstDayOfYear", getFirstDayOfMonth(temporary.minusDays(30)));
      lunisolar.put("monthOfYear", monthOfYear);
      temporary = getFirstDayOfMonth(dateTime.plusDays(1));
      lunisolar.put("firstDayOfMonth", temporary);
      dayOfMonth = getDayOfMonth(temporary, dateTime);
      lunisolar.put("dayOfMonth", dayOfMonth);
      lunisolar.put("dayOfWeek", (monthOfYear + dayOfMonth) % 6);
      lunisolar.put("isIntercalary", false);
      lunisolar.put("isPeakOfSeason", true);

      return lunisolar;
    }

    double seasonNextLongitude = circulate(seasonCurrentLongitude + 90d);
    DateTime seasonNext = getNearbySeasonTerm(dateTime, seasonNextLongitude);
    int seasonNextTerm = getSolarTerm(seasonNextLongitude);
    lunisolar.put("seasonNext", seasonNextLongitude);
    lunisolar.put("seasonNextLongitude", seasonNext);
    lunisolar.put("seasonNextTerm", seasonNextTerm);

    if (dateTime.withTimeAtStartOfDay().isEqual(seasonNext.withZone(dateTime.getZone()).withTimeAtStartOfDay())) {
      lunisolar.put("seasonCurrent", seasonNext);
      lunisolar.put("seasonCurrentLongitude", seasonNextLongitude);
      lunisolar.put("seasonCurrentTerm", seasonNextTerm);
      monthOfYear = (seasonNextTerm / 2) + 2;
      lunisolar.put("solarTerm", seasonNextTerm);
      temporary = solarTerm == 0 ? seasonNext : getLatestSeasonTerm(dateTime, 0d);
      if (printStackTrace) lunisolar.put("springEquinox", temporary);
      lunisolar.put("firstDayOfYear", getFirstDayOfMonth(temporary.minusDays(30)));
      lunisolar.put("monthOfYear", monthOfYear);
      temporary = getFirstDayOfMonth(dateTime.plusDays(1));
      lunisolar.put("firstDayOfMonth", temporary);
      dayOfMonth = getDayOfMonth(temporary, dateTime);
      lunisolar.put("dayOfMonth", dayOfMonth);
      lunisolar.put("dayOfWeek", (monthOfYear + dayOfMonth) % 6);
      lunisolar.put("isIntercalary", false);
      lunisolar.put("isPeakOfSeason", true);

      return lunisolar;
    }

    List<Long> terms = new ArrayList<Long>();
    for (double angle = 30; angle <= 90d; angle += 30) {
      temporary = getNearbySeasonTerm(dateTime, seasonCurrentLongitude + angle);
      terms.add(temporary.getMillis());
      lunisolar.put("terms[" + (terms.size() - 1) + "]", temporary);
    }

    List<Long> firstDayOfMonths = new ArrayList<Long>();
    temporary = seasonCurrent.plusDays(2).withTimeAtStartOfDay().minusMillis(1);
    while (firstDayOfMonths.size() < 5) {
      if (firstDayOfMonths.size() < 1) {
        firstDayOfMonths.add(getFirstDayOfMonth(seasonCurrent.plusDays(2).withTimeAtStartOfDay().minusMillis(1)).getMillis());
        continue;
      }
      temporary = getFirstDayOfMonth(dateTime.withMillis(firstDayOfMonths.get(firstDayOfMonths.size() - 1)).plusDays(31).withTimeAtStartOfDay().minusMillis(1));
      if (firstDayOfMonths.get(firstDayOfMonths.size() - 1) < temporary.getMillis()) firstDayOfMonths.add(temporary.getMillis());
      if (firstDayOfMonths.size() == 5) isIntercalary = temporary.getMillis() <= terms.get(terms.size() - 1);
    }

    for (int i = 1; i < firstDayOfMonths.size(); i++) {
      long start = firstDayOfMonths.get(i - 1);
      long end = firstDayOfMonths.get(i);
      Interval month = new Interval(dateTime.withMillis(start).withTimeAtStartOfDay(), dateTime.withMillis(end).withTimeAtStartOfDay());
      lunisolar.put("months[" + (i - 1) + "]", month);
      if (!month.contains(dateTime)) {
        continue;
      }
      lunisolar.put("firstDayOfMonth", month.getStart());
      lunisolar.put("daysOfMonth", month.withEnd(dateTime.withMillis(end).withTimeAtStartOfDay()).toPeriod(PeriodType.days()).getDays());
      dayOfMonth = (int) (toJDN(dateTime) - toJDN(month.getStart()) + 1L);
      monthOfYear = normalize(monthOfYear + (i - 1), 1, 12);
      if (isIntercalary) {
        boolean isIntercalaryInternal = true;
        for (long term : terms) {
          if (month.contains(term)) isIntercalaryInternal = false;
        }
        isIntercalary = isIntercalaryInternal && !month.contains(seasonCurrent);
      }

      break;
    }
    if (dayOfMonth < 0) {
      temporary = dateTime.withMillis(firstDayOfMonths.get(firstDayOfMonths.size() - 1)).withTimeAtStartOfDay();
      lunisolar.put("firstDayOfMonth", temporary);
      dayOfMonth = (int) (toJDN(dateTime) - toJDN(temporary) + 1L);
      isIntercalary = false;
    }

    if (isIntercalary) monthOfYear = normalize(monthOfYear - 1, 1, 12);
    temporary = getLatestSeasonTerm(dateTime.plusMonths(monthOfYear < 4 ? 2 : 0), 0d);
    lunisolar.put("firstDayOfYear", getFirstDayOfMonth(temporary.minusDays(30)));
    lunisolar.put("monthOfYear", monthOfYear);
    lunisolar.put("dayOfMonth", dayOfMonth);
    lunisolar.put("dayOfWeek", (monthOfYear + dayOfMonth) % 6);
    lunisolar.put("isIntercalary", isIntercalary && (normalize(monthOfYear - 2, 1, 12) % 6 != 0));
    lunisolar.put("isPeakOfSeason", false);

    if (!printStackTrace) return lunisolar;
    for (Entry<String, Object> e : lunisolar.entrySet()) {
      System.out.println(e.getKey() + ": " + e.getValue());
    }

    return lunisolar;
  }
  /**
   * {@code LunisolarDateTimeUtils} instances should NOT be constructed in standard programming.
   *
   */
  protected LunisolarDateTimeUtils() {
    super();
  }

  public static String getLocalizedPattern(final String style, final Object locale) {
    String[] styles = new String[]{"-", "-"};
    if (PATTERN_FORMAT_STYLE.matcher(StringUtils.defaultString(style)).matches()) {
      if (style.length() == 1) {
        styles[0] = style.toUpperCase();
        styles[1] = style.toUpperCase();
      } else {
        styles[0] = style.toUpperCase().substring(0, 1);
        styles[1] = style.toUpperCase().substring(1);
      }
    }
    FormatStyle dateStyle = FormatStyle.forStyle(styles[0]);
    FormatStyle timeStyle = FormatStyle.forStyle(styles[1]);
    StringBuilder sb = new StringBuilder();
    if (dateStyle.is()) sb.append(((SimpleDateFormat) DateFormat.getDateInstance(dateStyle.style, Localizer.newLocale(locale))).toLocalizedPattern()).append(" ");
    if (timeStyle.is()) sb.append(((SimpleDateFormat) DateFormat.getTimeInstance(timeStyle.style, Localizer.newLocale(locale))).toLocalizedPattern());

    return sb.toString();
  }
}
