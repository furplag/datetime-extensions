/**
 *
 */
package jp.furplag.util.time;

import static jp.furplag.util.commons.NumberUtils.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import jp.furplag.util.commons.StringUtils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.chrono.GJChronology;

import com.google.common.collect.ImmutableMap;

public class LunisolarDateUtils extends jp.furplag.util.time.DateTimeUtils {

  private static final double JULIAN_YEAR = 365.25;

  private static final double JULIAN_CENTURY = JULIAN_YEAR * 100d;

  /** the epoch julian date of 2001-01-01T12:00:00Z */
  private static final double J2000 = 2451545d;

  private static final double SYNODIC_MONTH = 29.530588853;

  private static final double SYNODIC_MONTH_INCREMENTAL = .000000002162;

  private static final Map<String, Double[][]> PERTURBATIONS;
  static {
    Map<String, Double[][]> perturbations = new HashMap<String, Double[][]>();
    perturbations.put("sun", new Double[][]{{.0004, 1d, 31557d, 161d}, {.0004, 1d, 29930d, 48d}, {.0005, 1d, 2281d, 221d}, {.0005, 1d, 155d, 118d}, {.0006, 1d, 33718d, 316d}, {.0007, 1d, 9038d, 64d}, {.0007, 1d, 3035d, 110d}, {.0007, 1d, 65929d, 45d}, {.0013, 1d, 22519d, 352d}, {.0015, 1d, 45038d, 254d}, {.0018, 1d, 445267d, 208d}, {.0018, 1d, 19d, 159d}, {.002, 1d, 32964d, 158d}, {.02, 1d, 71998.1, 265.1}, {-.0048, null, 35999.05, 267.52}, {1.9147, 1d, 35999.05, 267.52}});
    perturbations.put("moon", new Double[][]{{.0003, 1d, 2322131d, 191d}, {.0003, 1d, 4067d, 70d}, {.0003, 1d, 549197d, 220d}, {.0003, 1d, 1808933d, 58d}, {.0003, 1d, 349472d, 337d}, {.0003, 1d, 381404d, 354d}, {.0003, 1d, 958465d, 340d}, {.0004, 1d, 12006d, 187d}, {.0004, 1d, 39871d, 223d}, {.0005, 1d, 509131d, 242d}, {.0005, 1d, 1745069d, 24d}, {.0005, 1d, 1908795d, 90d}, {.0006, 1d, 2258267d, 156d}, {.0006, 1d, 111869d, 38d}, {.0007, 1d, 27864d, 127d}, {.0007, 1d, 485333d, 186d}, {.0007, 1d, 405201d, 50d}, {.0007, 1d, 790672d, 114d}, {.0008, 1d, 1403732d, 98d}, {.0009, 1d, 858602d, 129d}, {.0011, 1d, 1920802d, 186d}, {.0012, 1d, 1267871d, 249d}, {.0016, 1d, 1856938d, 152d}, {.0018, 1d, 401329d, 274d}, {.0021, 1d, 341337d, 16d}, {.0021, 1d, 71998d, 85d}, {.0021, 1d, 990397d, 357d}, {.0022, 1d, 818536d, 151d}, {.0023, 1d, 922466d, 163d}, {.0024, 1d, 99863d, 122d}, {.0026, 1d, 1379739d, 17d}, {.0027, 1d, 918399d, 182d}, {.0028, 1d, 1934d, 145d}, {.0037, 1d, 541062d, 259d}, {.0038, 1d, 1781068d, 21d}, {.0040, 1d, 133d, 29d}, {.0040, 1d, 1844932d, 56d}, {.0040, 1d, 1331734d, 283d}, {.0050, 1d, 481266d, 205d}, {.0052, 1d, 31932d, 107d}, {.0068, 1d, 926533d, 323d}, {.0079, 1d, 449334d, 188d}, {.0085, 1d, 826671d, 111d}, {.0100, 1d, 1431597d, 315d}, {.0107, 1d, 1303870d, 246d}, {.0110, 1d, 489205d, 142d}, {.0125, 1d, 1443603d, 52d}, {.0154, 1d, 75870d, 41d}, {.0304, 1d, 513197.9, 222.5}, {.0347, 1d, 445267.1, 27.9}, {.0409, 1d, 441199.8, 47.4}, {.0458, 1d, 854535.2, 148.2}, {.0533, 1d, 1367733.1, 280.7}, {.0571, 1d, 377336.3, 13.2}, {.0588, 1d, 63863.5, 124.2}, {.1144, 1d, 966404d, 276.5}, {.1851, 1d, 35999d, 87.53}, {.2136, 1d, 954397.7, 179.93}, {.6583, 1d, 890534.2, 145.7}, {1.2740, 1d, 413335.3, 10.74}, {6.2888, 1d, 477198.86, 44.963}});

    PERTURBATIONS = ImmutableMap.copyOf(perturbations);
  }

  private static final Map<String, Double[][]> PROPORTIONALS;
  static {
    Map<String, Double[][]> proportionals = new HashMap<String, Double[][]>();
    proportionals.put("sun", new Double[][]{{36000.7695, null}, {280.4659, 1d}});
    proportionals.put("moon", new Double[][]{{481267.8809, null}, {218.3162, 1d}});

    PROPORTIONALS = ImmutableMap.copyOf(proportionals);
  }

  private static double[] ARCHAIC_LUNAR_MONTHS = {
    15.28995536, // 0:春分
    15.4328125, // 15:清明
    15.57566964, // 30:穀雨
    15.69471726, // 45:立夏
    15.81376488, // 60:小満
    15.9328125, // 75:芒種
    15.9328125, // 90:夏至
    15.81376488, // 105:小暑
    15.69471726, // 120:大暑
    15.57566964, // 135:立秋
    15.4328125, // 150:処暑
    15.28995536, // 175:白露
    15.14709821, // 180:秋分
    15.00424107, // 195:寒露
    14.86138393, // 210:霜降
    14.74233631, // 225:立冬
    14.62328869, // 240:小雪
    14.50424107, // 255:大雪
    14.50424107, // 270:冬至
    14.62328869, // 285:小寒
    14.74233631, // 300:大寒
    14.86138393, // 315:立春
    15.00424107, // 330:雨水
    15.14709821 // 345:啓蟄
  };

  private static final String[] SOLAR_TERMS = "春分,清明,穀雨,立夏,小満,芒種,夏至,小暑,大暑,立秋,処暑,白露,秋分,寒露,霜降,立冬,小雪,大雪,冬至,小寒,大寒,立春,雨水,啓蟄".split(",");

  private static final String[] DAYS_OF_WEEKS = "先勝,友引,先負,仏滅,大安,赤口".split(",");

  private static enum FormatStyle {
    FULL(DateFormat.FULL), LONG(DateFormat.LONG), MEDIUM(DateFormat.MEDIUM), SHORT(DateFormat.SHORT), NONE(-1);

    private final int style;

    private FormatStyle(final int style) {
      this.style = style;
    }

    static FormatStyle forStyle(final String style) {
      if ("F".equals(style)) return FULL;
      if ("L".equals(style)) return LONG;
      if ("M".equals(style)) return MEDIUM;
      if ("S".equals(style)) return SHORT;
      if ("-".equals(style)) return NONE;
      if (StringUtils.isSimilarToBlank(style)) throw new IllegalArgumentException("style must not be empty.");

      throw new IllegalArgumentException("Invalid style character: " + style);
    }

    boolean is() {
      return this.style != NONE.style;
    }

    boolean is(final FormatStyle formatStyle) {
      return this.equals(formatStyle);
    }
  }

  protected LunisolarDateUtils() {
    super();
  }

  protected static DateTime getLatestDayAsStartOfMonth(final DateTime dateTIme) {
    return fromJDtoDT(getLatestDayAsStartOfMonth(toAJD(dateTIme)), dateTIme);
  }

  /**
   * Calculate latest day as start of month in lunisolar calendar.
   *
   * @param julianDay
   * @param noFuture
   * @return
   */
  protected static double getLatestDayAsStartOfMonth(final double julianDay) {
    final BigDecimal margin = new BigDecimal(1E-6);
    BigDecimal temporary = new BigDecimal(julianDay);
    BigDecimal delta;
    BigDecimal diff;
    BigDecimal elOfSun;
    BigDecimal elOfMoon;
    int counter = 0;
    do {
      counter++;
      elOfSun = materialize(getELoSun(temporary.doubleValue()), BigDecimal.class);
      elOfMoon = materialize(getELoMoon(temporary.doubleValue()), BigDecimal.class);
      delta = subtract(elOfMoon, elOfSun);
      if (counter == 1) delta = normalize(delta, 0, 360);
      diff = divide(multiply(delta, SYNODIC_MONTH/* getLunisolarMonth(temporary.doubleValue()) */), 360);
      temporary = subtract(temporary, diff);
      if (margin.compareTo(delta.abs()) > -1 && compareTo(temporary, julianDay) > 0) {
        temporary = subtract(temporary, divide(getLunisolarMonth(temporary.doubleValue()), 2, 2));
        delta = BigDecimal.TEN;
      }
    } while (delta.abs().compareTo(margin) > -1 && counter < 1E5);

    return temporary.doubleValue();
  }

  protected static int getDayOfMonth(final DateTime dateTime) {
    DateTime dayAsStartOfMonth = getLatestDayAsStartOfMonth(dateTime);
    int dayOfMonth = 1;
    dayOfMonth += new Period(dayAsStartOfMonth.withZone(DateTimeZone.forID("Asia/Tokyo")).withTimeAtStartOfDay(), dateTime.withZone(DateTimeZone.forID("Asia/Tokyo")).withTimeAtStartOfDay(), PeriodType.days()).getDays();
    if (dateTime.withZone(DateTimeZone.UTC).getDayOfMonth() != dateTime.withZone(DateTimeZone.forID("Asia/Tokyo")).getDayOfMonth()) dayOfMonth += 1;

    return dayOfMonth;
  }

  private static double getEclipticLongitude(double julianDay, Double[][] perturbations, Double[][] proportionals) {
    if (perturbations == null) throw new IllegalArgumentException("perturbations must not be null.");
    if (proportionals == null) throw new IllegalArgumentException("proportionals must not be null.");
    final BigDecimal tt = divide(subtract(new BigDecimal(julianDay), J2000), JULIAN_CENTURY);
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

  protected static double getELoMoon(DateTime then) {
    return getEclipticLongitude(toAJD(then), PERTURBATIONS.get("moon"), PROPORTIONALS.get("moon"));
  }

  protected static double getELoMoon(double julianDay) {
    return getEclipticLongitude(julianDay, PERTURBATIONS.get("moon"), PROPORTIONALS.get("moon"));
  }

  protected static double getELoSun(DateTime then) {
    return getEclipticLongitude(toAJD(then), PERTURBATIONS.get("sun"), PROPORTIONALS.get("sun"));
  }

  protected static double getELoSun(double julianDay) {
    return getEclipticLongitude(julianDay, PERTURBATIONS.get("sun"), PROPORTIONALS.get("sun"));
  }

  /**
   * <p>
   * Calculate a Day of latest (Autumn)equinox or (Winter) solstice from DateTime instant.
   * <ul>
   * <li>
   * 0 deg is Spring equinox.</li>
   * <li>
   * 90 deg is Summer solstice.</li>
   * <li>
   * 180 deg is Autumn equinox.</li>
   * <li>
   * 270 deg is Winter solstice.</li>
   * </ul>
   * </p>
   *
   * @param DateTime
   * @return an instant of latest (Autumn)equinox or (Winter) solstice.
   */
  protected static DateTime getLatestSeasonPeak(final DateTime dateTime) {
    double expected = getELoSun(dateTime);

    return fromJDtoDT(getLatestSeasonTerm(toAJD(dateTime), expected <= 90d ? 0d : expected > 270d ? 270d : (90d < expected && expected <= 180d) ? 90d : 180d), new DateTime(DateTimeZone.UTC));
  }

  protected static DateTime getLatestSeasonTerm(final DateTime dateTime, final double angle) {
    return fromJDtoDT(getLatestSeasonTerm(toAJD(dateTime), angle), new DateTime(DateTimeZone.UTC));
  }

  /**
   * <p>
   * Calculate a Julian Day of latest (Autumn)equinox or (Winter) solstice.
   * </p>
   *
   * @param julianDay a Julian Day, may be modified Julian Day
   * @param angle longitude of sun of season peak
   * @return an astronomical Julian Day of latest (Autumn)equinox or (Winter) solstice.
   */
  private static double getLatestSeasonTerm(final double julianDay, final double angle) {
    final BigDecimal expected = new BigDecimal(round(normalize(angle, 0, 360)));
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

  private static BigDecimal getLunisolarMonth(double julianDay) {
    return add(multiply(subtract(new BigDecimal(julianDay), J2000), SYNODIC_MONTH_INCREMENTAL), SYNODIC_MONTH);
  }

  protected static int getLunisolarMoY(DateTime dateTime) {
    return getLunisolarMoY(getELoSun(dateTime));
  }

  private static int getLunisolarMoY(double longitude) {
    Double index = divide(normalize(longitude, 0, 360) * 12d, 360, 1) + 2d;
    if (index != index.intValue()) index += 1d;

    return index.intValue() == 12 ? index.intValue() : remainder(index.intValue(), 12);
  }

  protected static String getNameOfSolarTerm(DateTime dateTime) {
    return SOLAR_TERMS[getSolarTerm(dateTime) % SOLAR_TERMS.length];
  }

  protected static String getNameOfSolarTerm(double longitude) {
    return SOLAR_TERMS[getSolarTerm(longitude) % SOLAR_TERMS.length];
  }

  private static DateTime getNearbySeasonTerm(final DateTime dateTime, final double angle) {
    return fromJDtoDT(getNearbySeasonTerm(toAJD(dateTime), angle), new DateTime(DateTimeZone.UTC));
  }

  private static double getNearbySeasonTerm(final double julianDay, final double angle) {
    double prev = getLatestSeasonTerm(julianDay, angle);
    double next = getLatestSeasonTerm(add(julianDay, getLunisolarMonth(julianDay)), angle);

    return julianDay - prev > next - julianDay ? next : prev;
  }

  protected static int getSolarTerm(DateTime dateTime) {
    return getSolarTerm(getELoSun(dateTime));
  }

  private static int getSolarTerm(double longitude) {
    return materialize(remainder(ceil(divide((normalize(longitude, 0, 360) * 24), 360, 8, RoundingMode.FLOOR)), SOLAR_TERMS.length), Integer.class);
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
  protected static Double toAJD(final DateTime dateTime) {
    if (dateTime == null) return null;
    return toJulianDay(dateTime.getMillis());
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

  public static String toStringLunisolar(final DateTime dateTime) {
    return toStringLunisolar(dateTime, FormatStyle.FULL, FormatStyle.NONE);
  }

  public static String toStringLunisolar(final DateTime dateTime, final String formatStyle) {
    if (formatStyle == null) return toStringLunisolar(dateTime);
    if ("--".equals(formatStyle)) return "";
    if (!formatStyle.matches("^[FLMS\\-]{2,2}$")) throw new IllegalArgumentException("Invalid style character: " + formatStyle);

    return toStringLunisolar(dateTime, FormatStyle.forStyle(StringUtils.truncateLast(formatStyle, ".")), FormatStyle.forStyle(StringUtils.truncateFirst(formatStyle, ".")));
  }

  private static String toStringLunisolar(final DateTime dateTime, final FormatStyle dateStyle, final FormatStyle timeStyle) {
    Locale locale = new Locale("ja", "JP", "JP");
    String pattern = !dateStyle.is() ? "" : ((SimpleDateFormat) DateFormat.getDateInstance(dateStyle.style, locale)).toLocalizedPattern();
    if (dateStyle.is(FormatStyle.FULL)) pattern += " (EEEE)";
    if (timeStyle.is()) {
      pattern += " ";
      pattern += ((SimpleDateFormat) DateFormat.getTimeInstance(timeStyle.style, locale)).toLocalizedPattern();
      pattern = StringUtils.truncateLast(pattern, "(?i)\\s*z").trim();
    }
    pattern = pattern.replaceAll("G+", JapaneseEra.getEraAsText(dateTime, locale, false));
    if (dateTime.compareTo(new DateTime("1872-12-31T23:59:59.999+09:00:00")) > 0) {
      pattern = pattern.replaceAll("y+", StringUtils.replaceAll(Integer.toString(dateTime.getYear() - JapaneseEra.getEra(dateTime).start.getYear() + 1), "^1$", "元"));
      return dateTime.withZone(DateTimeZone.forID("Asia/Tokyo")).toString(pattern);
    }
    Map<String, Object> map = getLunisolar(dateTime);
    pattern = pattern.replaceAll("y+", StringUtils.replaceAll(JapaneseEra.getYearOfEra(dateTime, locale, false), dateStyle.is(FormatStyle.FULL) ? "^1$" : null, "元"));
    pattern = pattern.replaceAll("M+", ((Boolean) map.get("isIntercalary") ? "閏" : "") + map.get("monthOfYear").toString());
    pattern = pattern.replaceAll("d+", map.get("dayOfMonth").toString());
    pattern = pattern.replaceAll("E+", DAYS_OF_WEEKS[optimize(map.get("dayOfWeek").toString(), Integer.class)]);

    return dateTime.withZone(DateTimeZone.forID("Asia/Tokyo")).toString(pattern);
  }

  public static Map<String, Object> getLunisolar(DateTime dateTime) {
    if (dateTime == null) return null;
    Map<String, Object> ret = new HashMap<String, Object>();
    DateTime seasonCurrent = getLatestSeasonPeak(dateTime);
    double seasonCurrentLongitude = normalize(round(getELoSun(seasonCurrent)), 0, 360);

    List<DateTime> terms = new ArrayList<DateTime>();
    DateTime temporary = dateTime;
    double longitude = seasonCurrentLongitude;
    do {
      longitude += 30d;
      terms.add(getNearbySeasonTerm(temporary, longitude));
    } while (round(longitude) < (circulate(seasonCurrentLongitude + 90d) == 0 ? 360 : circulate(seasonCurrentLongitude + 90d)));

    List<DateTime> dayAsStartOfMonths = new ArrayList<DateTime>();
    boolean isIntercalary = false;
    temporary = seasonCurrent;
    do {
      temporary = getLatestDayAsStartOfMonth(temporary.withTimeAtStartOfDay().plusDays(dayAsStartOfMonths.size() < 1 ? 1 : 31));
      dayAsStartOfMonths.add(temporary);
      if (dayAsStartOfMonths.size() == 5) isIntercalary = !temporary.isAfter(terms.get(terms.size() - 1));
    } while (dayAsStartOfMonths.size() < 5);

    boolean isIntercalaryInternal = true;
    int monthOfYear = getLunisolarMoY(seasonCurrent);
    int dayOfMonth = -1;
    int dayOfWeek = -1;
    for (int i = 1; i < dayAsStartOfMonths.size(); i++) {
      DateTime start = dayAsStartOfMonths.get(i - 1);
      DateTime end = dayAsStartOfMonths.get(i).minusMillis(1);
      Interval month = new Interval(start, end).withChronology(GJChronology.getInstanceUTC());
      if (month.contains(dateTime)) {
        monthOfYear = getLunisolarMoY(start);
        if (i == 1) isIntercalaryInternal = month.contains(seasonCurrent);
        for (DateTime term : terms) {
          if (new Interval(start, end).contains(term)) isIntercalaryInternal = false;
        }
        if (isIntercalary && isIntercalaryInternal) monthOfYear -= 1;
        if (monthOfYear < 1) monthOfYear += 12;
        if (monthOfYear > 12) monthOfYear %= 12;
        dayOfMonth = Long.valueOf(toJDN(dateTime) - toJDN(start)).intValue();
        if ((dateTime.getMillis() - start.getMillis()) % 86400000 / 3600000 > 9) dayOfMonth += 1;
        dayOfWeek = (monthOfYear + dayOfMonth - 2) % 6;
        isIntercalary = isIntercalary && isIntercalaryInternal;
      }
    }
    ret.put("monthOfYear", monthOfYear);
    ret.put("dayOfMonth", dayOfMonth);
    ret.put("dayOfWeek", dayOfWeek);
    ret.put("isIntercalary", isIntercalary);

    return ret;
  }
}
