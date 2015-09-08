/**
 *
 */
package jp.furplag.util.time.lunisolar;

import static jp.furplag.util.time.DateTimeUtils.*;
import static jp.furplag.util.time.lunisolar.LunisolarDateTimeUtils.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import jp.furplag.util.Jsonifier;
import jp.furplag.util.ResourceUtils;
import jp.furplag.util.commons.NumberUtils;
import jp.furplag.util.commons.StringUtils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.joda.time.chrono.GJChronology;

import com.google.common.collect.ImmutableList;

public abstract class AbstractLunisolar {

  /** Era of time periods for East Asian Lunisolar Date Time. */
  final class Era implements Comparable<Era>, Serializable {

    private static final long serialVersionUID = 1L;

    /** identifier */
    private final String key;

    /** julian day of start of era. */
    private final double start;

    /** julian day of end of era. */
    private final Double end;

    /** timezone of era. */
    private DateTimeZone zone = DateTimeZone.UTC;

    private Era(String key, Object start, Object end, DateTimeZone zone) {
      if (StringUtils.isSimilarToBlank(key)) throw new IllegalArgumentException("\"key\" must not be null.");
      if (zone != null) this.zone = zone;
      this.key = key;
      this.start = toAJD(toDT(start, GJChronology.getInstance(zone)));
      this.end = end == null ? null : toAJD(toDT(end, GJChronology.getInstance(zone)));
    }

    @Override
    public int compareTo(Era other) {
      if (other == null) return 1;
      if (start != other.start) return Double.compare(start, other.start);

      return other == null ? end == null ? 0 : 1 : Double.compare(end, other.end);
    }

    boolean contains(DateTime then) {
      if (then == null) return false;
      if (end == null) return toAJD(then) >= start;

      return contains(toAJD(then));
    }

    boolean contains(double julianDay) {
      return NumberUtils.contains(julianDay, start, end == null ? julianDay + 1d : end);
    }

    boolean contains(long millis) {
      if (end == null) return toAJD(millis) >= start;

      return contains(toAJD(millis));
    }

    @Override
    public boolean equals(Object other) {
      return other instanceof Era && compareTo((Era) other) == 0;
    }

    String getKey() {
      return key;
    }

    Double getEnd() {
      return end;
    }

    double getStart() {
      return start;
    }

    Integer getYearOfEra(DateTime then) {
      if (then == null) return null;
      if (!contains(then)) return null;
      Period period = new Period(getFirstDayOfYear(then.withMillis(fromJD(start)).plusDays(1)), getFirstDayOfYear(then.plusDays(1)));
      int year = period.getYears() + 1;
      int lunisolarMonth = getMonthOfYear(then);
      if (lunisolarMonth > 10 && then.getMonthOfYear() < lunisolarMonth) year -= 1;

      return year;
    }

    @Override
    public String toString() {
      Map<String, Object> map = new LinkedHashMap<String, Object>();
      map.put("key", key);
      map.put("start", toDT(start, GJChronology.getInstanceUTC()).toString());
      map.put("end", end != null ? toDT(end, GJChronology.getInstanceUTC()).toString() : null);
      map.put("locale", locale.toString());
      map.put("zone", zone.getID());

      return Jsonifier.stringifyLazy(map);
    }
  }

  private static final String RESOURCE_PREFIX = "jp.furplag.util.time.lunisolar.";

  private Locale locale = Locale.ROOT;

  private final DateTimeZone zone;

  private List<Era> periods = null;

  private Map<String, List<Era>> eras = null;

  protected AbstractLunisolar(final DateTimeZone zone) {
    this.zone = zone == null ? DateTimeZone.UTC : zone;
  }

  protected Map<String, List<Era>> buildMap(final Object[]... periods) {
    Map<String, List<Era>> map = new HashMap<String, List<Era>>();
    int periodIndex = 0;
    for (Object[] period : periods) {
      periodIndex++;
      String keyOfPeriod = "";
      if (period[0] instanceof String) keyOfPeriod = StringUtils.normalize(period[0].toString());
      if (StringUtils.isSimilarToBlank(keyOfPeriod)) throw new IllegalArgumentException("\"key of Period\" must no be empty.");
      keyOfPeriod = "period." + keyOfPeriod;
      List<Era> eras = new ArrayList<Era>();
      for (int i = 1; i < period.length; i++) {
        String[] eraOfPeriod = Jsonifier.parseLazy(Jsonifier.stringifyLazy(period[i]), String[].class);
        if (!(eraOfPeriod != null && NumberUtils.contains(eraOfPeriod.length, 2, 3))) throw new IllegalArgumentException("the era represent by key of era, and duration.");
        DateTime start = toDT(eraOfPeriod[1], GJChronology.getInstanceUTC(), true).withZone(zone).withTimeAtStartOfDay();
        if (start == null) throw new IllegalArgumentException("\"start of era\" must not be empty.");
        String key = StringUtils.trim(eraOfPeriod[0]);
        if (StringUtils.isSimilarToBlank(key)) throw new IllegalArgumentException("\"key Of Era\" must not be empty.");
        key = key + "." + start.toString("yyyy");
        DateTime end = null;
        if (eraOfPeriod.length > 2 && eraOfPeriod[2] != null) end = toDT(eraOfPeriod[2], GJChronology.getInstanceUTC(), true).withZone(zone).withTimeAtStartOfDay().plusDays(1).minusMillis(1);
        if (end == null && i < period.length - 1) {
          String[] next = Jsonifier.parseLazy(Jsonifier.stringifyLazy(period[i + 1]), String[].class);
          if (next.length > 1) end = toDT(next[1], GJChronology.getInstanceUTC(), true).withZone(zone).withTimeAtStartOfDay().minusMillis(1);
        }
        if (end != null && end.isBefore(start)) throw new IllegalArgumentException("\"duration of era\": '" + new Interval(start, end) + "' is invalid.");
        if (end == null && periodIndex < periods.length) throw new IllegalArgumentException("\"only the last of eras could be set to null end of the period.");
        eras.add(new Era(key, toAJD(start), end == null ? null : toAJD(end), zone));
      }
      map.put(keyOfPeriod, eras);
    }

    return map;
  }

  protected Era getEra(final DateTime then) {
    Era period = getPeriod(then);
    if (period == null) return null;
    for (Era era : eras.get(period.key)) {
      if (era.contains(then)) return era;
    }

    return null;
  }

  protected Locale getLocale() {
    return locale;
  }

  protected String getNameOfEra(final DateTime then) {
    return getNameOfEra(getEra(then));
  }

  protected String getNameOfEra(final Era era) {
    return era == null ? StringUtils.EMPTY : getNameOfEra(era.key);
  }

  protected String getNameOfEra(final String key) {
    return ResourceUtils.get(RESOURCE_PREFIX + getClass().getSimpleName(), key, new String[]{}, StringUtils.truncateAll(key, "period\\.").split("\\.")[0], locale == null ? Locale.getDefault() : locale);
  }

  protected String getNameOfSolarTerm(final String[] terms, final double longitude) {
    int solarTerm = LunisolarDateTimeUtils.getSolarTerm(longitude);
    if (!(terms != null && NumberUtils.contains(solarTerm, 0, terms.length))) return "";

    return ResourceUtils.get(RESOURCE_PREFIX + getClass().getSimpleName(), "solarTerm." + terms[solarTerm], new String[]{}, terms[solarTerm], locale);
  }

  protected String getNameOfDayOfWeek(final String[] dayOfWeeks, final int dayOfWeek) {
    if (!(dayOfWeeks != null && NumberUtils.contains(dayOfWeek, 0, dayOfWeeks.length))) return "";

    return ResourceUtils.get(RESOURCE_PREFIX + getClass().getSimpleName(), "dayOfWeek." + dayOfWeeks[dayOfWeek], new String[]{}, dayOfWeeks[dayOfWeek], locale);
  }

  protected Era getPeriod(final DateTime then) {
    if (periods == null) return null;
    if (then == null) return null;
    if (then.isBefore(fromJD(periods.get(0).start))) return null;
    for (Era period : periods) {
      if (period == null) continue;
      if (period.contains(then)) return period;
    }

    return null;
  }

  protected void initialize(final Map<String, List<Era>> periods) {
    if (this.periods != null) throw new IllegalArgumentException("\"periods\" already initialized.");
    if (this.eras != null) throw new IllegalArgumentException("\"eras\" already initialized.");
    if (!(periods != null && periods.size() > 0)) throw new IllegalArgumentException("\"periods\" must not be empty.");
    this.periods = new ArrayList<Era>();
    eras = new HashMap<String, List<Era>>();
    for (Entry<String, List<Era>> period : periods.entrySet()) {
      String keyOfPeriod = StringUtils.defaultString(period.getKey());
      if (StringUtils.isSimilarToBlank(keyOfPeriod)) throw new IllegalArgumentException("\"key of Period\" must no be empty.");
      if (!keyOfPeriod.startsWith("period.")) keyOfPeriod = "period." + keyOfPeriod;
      eras.put(keyOfPeriod, ImmutableList.copyOf(period.getValue()));
      this.periods.add(new Era(keyOfPeriod, period.getValue().get(0).start, period.getValue().get(period.getValue().size() - 1).end, zone));
    }
    Collections.sort(this.periods);
    this.periods = ImmutableList.copyOf(this.periods);
  }

  protected Map<String, Object> toLunisolar(final DateTime then) {
    return LunisolarDateTimeUtils.toLunisolar(then);
  }

  protected abstract String toStringLunisolar(final Object then, final String formatStyle) throws Exception;

  @SuppressWarnings("unchecked")
  protected <T extends AbstractLunisolar> T withLocale(final Class<T> clazz, final Locale locale) {
    if (locale != null) this.locale = locale;
    return (T) this;
  }
}
