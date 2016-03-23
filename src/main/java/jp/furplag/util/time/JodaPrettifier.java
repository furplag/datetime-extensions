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

import static jp.furplag.util.time.DateTimeUtils.isToday;
import static jp.furplag.util.time.DateTimeUtils.toAJD;
import static jp.furplag.util.time.DateTimeUtils.toDT;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.joda.time.base.BaseSingleFieldPeriod;
import org.joda.time.chrono.GJChronology;
import org.joda.time.format.DateTimeFormat;
import org.ocpsoft.prettytime.PrettyTime;
import org.ocpsoft.prettytime.units.Decade;

import jp.furplag.util.Localizer;
import jp.furplag.util.commons.ClassUtils;
import jp.furplag.util.commons.NumberUtils;
import jp.furplag.util.commons.ObjectUtils;
import jp.furplag.util.commons.StringUtils;

/**
 * Social Style Date and Time Formatting for {@link org.joda.time.DateTime}.
 *
 * @author furplag
 */
public class JodaPrettifier {

  /**
   * {@code JodaPrettifier} instances should NOT be constructed in standard programming.
   */
  protected JodaPrettifier() {}

  /**
   * Shorthand for {@link #prettify(Object, Object, Locale, DateTimeZone, Object)}.
   *
   * @param then the datetime object, null means current date-time.
   * @return {@code prettify(then, null, Locale.getDefault(), DateTimeZone.getDefault(), null)}.
   */
  public static String prettify(final Object then) {
    return prettify(then, null);
  }

  /**
   * Shorthand for {@link #prettify(Object, Object, Locale, DateTimeZone, Object)}.
   *
   * @param then the datetime object, null means current date-time.
   * @param reference the moment of a starting point ( {@link org.joda.time.ReadableInstant} and {@link Long} specifiable ). Use {@code DateTime.now()} as a start point if {@code reference} is null.
   * @return {@code prettify(then, null, Locale.getDefault(), DateTimeZone.getDefault(), null)}.
   */
  public static String prettify(final Object then, final Object reference) {
    return prettify(then, reference, Locale.getDefault(), DateTimeZone.getDefault(), null);
  }

  /**
   * Return the prettified String if the period includes specified moment.
   *
   * <pre>
   * prettify(DateTime.now().minusHours(1), null, null, null, null) = "one hour ago." prettify(DateTime.now(), DateTime.now().plusYears(1), null, null, null) = "one year ago." prettify(DateTime.now().minusHours(1), null, null, null, new Period().withDays(1)) = "one hour ago." prettify(DateTime.now().minusHours(1), null, null, null, new Period().withMinites(10)) =
   * DateTime.now().withZone(DateTimeZone.UTC).minusHours(1).toString(DateTimeFormat.forStyle("-M"))
   *
   * <pre>
   *
   * @param then the datetime object, null means current date-time.
   * @param reference the moment of a starting point ( {@link org.joda.time.ReadableInstant} and {@link Long} specifiable ). Use {@code DateTime.now()} as a start point if {@code reference} is null.
   * @param locale the language for Localization ( {@code String} and {@code Locale} specifiable ). Use ROOT if {@code locale} is null.
   * @param limit if the moment is in the specified period, return prettified String ( {@code Period} and {@code Interval} specifiable ). Prettify all, if null.
   * @return the prettified String if the period includes specified moment. In other situation, return stringified date-time.
   */
  public static String prettify(final Object then, final Object reference, final Locale locale, final DateTimeZone zone, final Object limit) {
    DateTime temporary = DateTimeUtils.toDT(then, zone, true);
    if (temporary == null) return StringUtils.EMPTY;
    DateTime ref = DateTimeUtils.toDT(reference, temporary.getZone(), true);
    if (ref == null) return doPrettify(temporary, null, locale);
    if (ref.isEqual(temporary)) ref = ref.plusMillis(1);
    if (limit == null) return doPrettify(temporary, ref, locale);
    Interval limitter = null;
    if (Interval.class.equals(limit)) limitter = (Interval) limit;
    if (limit instanceof Period) {
      limitter = new Interval(ref.minus((Period) limit), ref.plusMillis(1).plus((Period) limit));
    }
    if (limit instanceof BaseSingleFieldPeriod) {
      limitter = new Interval(ref.minus(new Period(limit)), ref.plusMillis(1).plus(new Period(limit)));
    }
    if (ObjectUtils.isAny(ClassUtils.primitiveToWrapper(limit.getClass()), Double.class, Float.class)) {
      limitter = new Interval(toDT(toAJD(ref) - NumberUtils.valueOf(limit, double.class), ref), toDT(toAJD(ref) + NumberUtils.valueOf(limit, double.class), ref));
    } else if (BigDecimal.class.equals(limit.getClass())) {
      if (NumberUtils.compareTo((BigDecimal) limit, NumberUtils.down(limit)) == 0) {
        limitter = new Interval(ref.minusMillis(NumberUtils.valueOf(limit, int.class)), ref.plusMillis(NumberUtils.valueOf(limit, int.class) + 1));
      } else {
        limitter = new Interval(toDT(toAJD(ref) - NumberUtils.valueOf(limit, double.class), ref), toDT(toAJD(ref) + NumberUtils.valueOf(limit, double.class), ref));
      }
    } else if (Number.class.isAssignableFrom(ClassUtils.primitiveToWrapper(limit.getClass()))) {
      limitter = new Interval(ref.minusMillis(NumberUtils.valueOf(limit, int.class)), ref.plusMillis(NumberUtils.valueOf(limit, int.class) + 1));
    }
    if (DateTime.class.equals(limit.getClass())) {
      limitter = new Interval(ref.minus(((DateTime) limit).getMillis()), ref.plus(((DateTime) limit).getMillis() + 1L));
    }
    if (Boolean.class.equals(limit.getClass())) {
      limitter = new Interval(temporary.minusMillis(1), ((Boolean) limit) ? temporary.plusMillis(1) : temporary.minusMillis(1));
    }
    if (limitter == null) return doPrettify(temporary, ref, locale);
    if (limitter.contains(temporary)) return doPrettify(temporary, ref, locale);

    return toDT(temporary, GJChronology.getInstance(temporary.getZone())).toString(DateTimeFormat.forStyle(isToday(temporary, temporary.getZone()) ? "-M" : "MS").withLocale(locale == null ? Locale.ROOT : locale));
  }
  public static void main(String[] args) {
    System.out.println(isToday(DateTime.now(), DateTimeZone.getDefault()));
    System.out.println(prettify(DateTime.now().minusMinutes(10), DateTime.now(), Locale.JAPAN, DateTimeZone.getDefault(), false));
  }

  /**
   * Shorthand for {@link #prettify(Object, Object, Locale, DateTimeZone, Object)}.
   *
   * @param then the datetime object, null means current date-time.
   * @param reference the moment of a starting point ( {@link org.joda.time.ReadableInstant} and {@link Long} specifiable ). Use {@code DateTime.now()} as a start point if {@code reference} is null.
   * @return {@code prettify(then, null, Locale.getDefault(), DateTimeZone.getDefault(), null)}.
   */
  public static String prettify(final Object then, final Object reference, final Object locale, final Object zone) {
    return prettify(then, reference, Localizer.getAvailableLocale(locale), Localizer.getDateTimeZone(zone), null);
  }

  /**
   * substitute for {@link org.ocpsoft.prettytime.PrettyTime#format(Date)} (Does not format Decade, in Japanese situation.) .
   *
   * @param then the moment, must not be null.
   * @param reference the moment of starting point.
   * @param locale the language for localization.
   * @return the prettified string.
   */
  private static String doPrettify(final DateTime then, final DateTime reference, final Locale locale) {
    if (then == null) throw new IllegalArgumentException("arguments[0]: then is null.");
    DateTime ref = reference == null ? then.plusMillis(1) : reference;
    PrettyTime prettyTime = new PrettyTime(ref.toDate());
    if (locale != null && Locale.JAPANESE.getLanguage().equals(locale.getLanguage())) prettyTime.removeUnit(Decade.class);

    return prettyTime.setLocale(locale == null ? Locale.ROOT : locale).format(then.toDate());
  }
}
