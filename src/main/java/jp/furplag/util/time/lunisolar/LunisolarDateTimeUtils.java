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
/**
 *
 */

package jp.furplag.util.time.lunisolar;

import static jp.furplag.util.commons.NumberUtils.circulate;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;

import jp.furplag.util.commons.NumberUtils;

/**
 * utilities for East Asian Lunisolar Date Time.
 *
 * @author furplag
 */
public final class LunisolarDateTimeUtils extends jp.furplag.util.time.DateTimeUtils {

  /** the epoch julian date of 2001-01-01T12:00:00Z */
  public static final double J2000 = 2451545d;

  /** the days of julian year. */
  public static final double JULIAN_YEAR = 365.25;

  /** the days of julian century. */
  public static final double JULIAN_CENTURY = JULIAN_YEAR * 100;

  /** perturbation values for ecliptic longitude of Sun and Moon. */
  private static enum Perturbation {
    Moon(new Double[][] { { .0003, 1d, 2322131d, 191d }, { .0003, 1d, 4067d, 70d }, { .0003, 1d, 549197d, 220d }, { .0003, 1d, 1808933d, 58d }, { .0003, 1d, 349472d, 337d }, { .0003, 1d, 381404d, 354d }, { .0003, 1d, 958465d, 340d }, { .0004, 1d, 12006d, 187d }, { .0004, 1d, 39871d, 223d }, { .0005, 1d, 509131d, 242d }, { .0005, 1d, 1745069d, 24d }, { .0005, 1d, 1908795d, 90d }, { .0006, 1d, 2258267d, 156d }, { .0006, 1d, 111869d, 38d }, { .0007, 1d, 27864d, 127d }, { .0007, 1d, 485333d, 186d }, { .0007, 1d, 405201d, 50d }, { .0007, 1d, 790672d, 114d }, { .0008, 1d, 1403732d, 98d }, { .0009, 1d, 858602d, 129d }, { .0011, 1d, 1920802d, 186d }, { .0012, 1d, 1267871d, 249d }, { .0016, 1d, 1856938d, 152d }, { .0018, 1d, 401329d, 274d }, { .0021, 1d, 341337d, 16d }, { .0021, 1d, 71998d, 85d }, { .0021, 1d, 990397d, 357d }, { .0022, 1d, 818536d, 151d }, { .0023, 1d, 922466d, 163d }, { .0024, 1d, 99863d, 122d }, { .0026, 1d, 1379739d, 17d }, { .0027, 1d, 918399d, 182d }, { .0028, 1d, 1934d, 145d }, { .0037, 1d, 541062d, 259d }, { .0038, 1d, 1781068d, 21d }, { .0040, 1d, 133d, 29d }, { .0040, 1d, 1844932d, 56d }, { .0040, 1d, 1331734d, 283d }, { .0050, 1d, 481266d, 205d }, { .0052, 1d, 31932d, 107d }, { .0068, 1d, 926533d, 323d }, { .0079, 1d, 449334d, 188d }, { .0085, 1d, 826671d, 111d }, { .0100, 1d, 1431597d, 315d }, { .0107, 1d, 1303870d, 246d }, { .0110, 1d, 489205d, 142d }, { .0125, 1d, 1443603d, 52d }, { .0154, 1d, 75870d, 41d }, { .0304, 1d, 513197.9, 222.5 }, { .0347, 1d, 445267.1, 27.9 }, { .0409, 1d, 441199.8, 47.4 }, { .0458, 1d, 854535.2, 148.2 }, { .0533, 1d, 1367733.1, 280.7 }, { .0571, 1d, 377336.3, 13.2 }, { .0588, 1d, 63863.5, 124.2 }, { .1144, 1d, 966404d, 276.5 }, { .1851, 1d, 35999d, 87.53 }, { .2136, 1d, 954397.7, 179.93 }, { .6583, 1d, 890534.2, 145.7 }, { 1.2740, 1d, 413335.3, 10.74 }, { 6.2888, 1d, 477198.86, 44.963 } }, //
    new double[] { 481267.8809, 218.3162 }), //
    Sun(new Double[][] { { .0004, 1d, 31557d, 161d }, { .0004, 1d, 29930d, 48d }, { .0005, 1d, 2281d, 221d }, { .0005, 1d, 155d, 118d }, { .0006, 1d, 33718d, 316d }, { .0007, 1d, 9038d, 64d }, { .0007, 1d, 3035d, 110d }, { .0007, 1d, 65929d, 45d }, { .0013, 1d, 22519d, 352d }, { .0015, 1d, 45038d, 254d }, { .0018, 1d, 445267d, 208d }, { .0018, 1d, 19d, 159d }, { .002, 1d, 32964d, 158d }, { .02, 1d, 71998.1, 265.1 }, { -.0048, null, 35999.05, 267.52 }, { 1.9147, 1d, 35999.05, 267.52 } }, //
    new double[] { 36000.7695, 280.4659 });

    private Perturbation(Double[][] values, double[] proportionalValues) {
      this.values = values;
      this.proportionalValues = proportionalValues;
    }

    private final double[] proportionalValues;

    private final Double[][] values;
  }

  /**
   * {@code LunisolarDateTimeUtils} instances should NOT be constructed in standard programming.
   */
  private LunisolarDateTimeUtils() {
    super();
  }

  /**
   * calculate Terrestrial Dynamical Time.
   *
   * @param julianDay
   * @return Terrestrial Dynamical Time.
   */
  public static double toTDT(final double julianDay) {
    return (julianDay - J2000) / JULIAN_CENTURY;
  }

  /**
   * calculate ecliptic longitude of specified julian day.
   * @param julianDay julian day.
   * @param perturbation perturbation values for ecliptic longitude.
   * @return ecliptic longitude represented by angle ( 0&deg;-360&deg; ) .
   */
  private static double getEclipticLongitude(final double julianDay, final Perturbation perturbation) {
    final double tt = toTDT(julianDay);
    double longitude = 0d;
    for (Double[] value : perturbation.values) {
      longitude += value[0] * (value[1] == null ? tt : value[1]) * Math.cos((circulate((tt * value[2]) + value[3])) * Math.PI / 180d);
    }
    longitude += circulate(circulate(perturbation.proportionalValues[0] * tt) + perturbation.proportionalValues[1]);

    return circulate(longitude);
  }

  /**
   * calculate ecliptic longitude of Moon.
   *
   * @param julianDay julian day.
   * @return ecliptic longitude represented by angle ( 0&deg;-360&deg; ) .
   */
  public static double getELOfMoon(final double julianDay) {
    return getEclipticLongitude(julianDay, Perturbation.Moon);
  }

  /**
   * calculate ecliptic longitude of Sun.
   *
   * @param julianDay julian day.
   * @return ecliptic longitude represented by angle ( 0&deg;-360&deg; ) .
   */
  public static double getELOfSun(final double julianDay) {
    return getEclipticLongitude(julianDay, Perturbation.Sun);
  }

  public static int getDayOfMonth(final double firstDayOfMonth, final double julianDay, DateTimeZone zone) {
    DateTime firstDay = toDT(firstDayOfMonth, zone, true);
    if (firstDay == null) throw new IllegalArgumentException("\"firstDayOfMonth\" must NOT be empty.");
    DateTime then = toDT(julianDay, zone).withTimeAtStartOfDay();
    Interval interval = firstDayOfMonth > julianDay ? new Interval(then, firstDay.withTimeAtStartOfDay()) : new Interval(firstDay.withTimeAtStartOfDay(), then);

    return (int) interval.toDuration().getStandardDays() + 1;
  }

  /**
   *
   * @param longitude
   * @return
   */
  public static int getSeasonOfSTL(final double longitude) {
    return 90 * (getAngleOfSolarTerm(longitude) / 90);
  }

  public static int getSolarTerm(final double longitude) {
    return ((int) Math.round(circulate(longitude)) + 2) / 15;
  }

  public static int getAngleOfSolarTerm(final double longitude) {
    return 15 * getSolarTerm(longitude);
  }

  public static int normalizeMonth(final int month) {
    if (1 <= month && month <= 12) return month;
    int modulo = month % 12;
    if (modulo < 0) return 12 + modulo;

    return modulo == 0 ? 12 : modulo;
  }
}
