
package jp.furplag.util.time.lunisolar;

import static jp.furplag.util.commons.NumberUtils.circulate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.chrono.GregorianChronology;

import com.google.common.collect.Iterables;

import jp.furplag.util.Localizer;
import jp.furplag.util.ResourceUtils;
import jp.furplag.util.commons.StringUtils;
import jp.furplag.util.time.DateTimeUtils;

public abstract class AbstractLunisolar {

  /** the epoch julian date of 2001-01-01T12:00:00Z */
  public static final double J2000 = 2451545d;

  /** delta of the days of month in the moon. */
  public static final double SYNODIC_MONTH_INCREMENTAL = .000000002162;

  protected static final Pattern IGNORE_PATTERN = Pattern.compile("'[^']*'");

  private static final Map<String, String> I18N = new HashMap<String, String>();

  protected AbstractLunisolar(LunisolarChronology chronology) {
    if (chronology == null) throw new IllegalArgumentException("chronology must NOT be empty.");
    daysOfYear = chronology.getDaysOfYear();
    daysOfMonth = chronology.getDaysOfMonth();
    limitOfDayAdvance = chronology.getLimitOfDayAdvance();
    enforceDayAdvance = limitOfDayAdvance < 0 && limitOfDayAdvance < 1;
    keplerizeMoon = chronology.isKeplerizeMoon();
    keplerizeSun = chronology.isKeplerizeSun();
    resourceName = getClass().getCanonicalName();
  }

  protected final String resourceName;

  /** astronomical julian day of Lunar month. */
  private final double daysOfMonth;

  /** astronomical julian day of Solar year. */
  private final double daysOfYear;

  /** if the moment of new moon was after 18:00 of the day, the month start to next day of actual first day of the month. */
  private final boolean enforceDayAdvance;

  private final double limitOfDayAdvance;

  /** if false, calculate Lunar month constantly. */
  private final boolean keplerizeMoon;

  /** if false, calculate Solar term constantly. */
  private final boolean keplerizeSun;

  public double addMonth(final double julianDay, final int months) {
    return julianDay + (optimizeSynodicMonth(julianDay) * (double) months);
  }

  public abstract String getString(String formatStyle, Object locale);

  private List<LunisolarMonth> getCalendar(List<Double> firstDays, List<Double> solarTerms) {
    List<LunisolarMonth> calendar = new ArrayList<LunisolarMonth>();
    for (int i = 1; i < firstDays.size(); i++) {
      LunisolarMonth month = new LunisolarMonth(toDT(firstDays.get(i - 1)), toDT(firstDays.get(i)));
      for (double solarTerm : solarTerms) {
        if (!month.contains(solarTerm)) continue;
        if (keplerizeSun) {
          int termIndex = LunisolarDateTimeUtils.getSolarTerm(getELOfSun(solarTerm));
          if (termIndex % 2 == 0) {
            month.addMidClimates(solarTerm);
          } else {
            month.addPreClimates(solarTerm);
          }
        } else {
          int termIndex = ((int) Math.round(getELOfSun(solarTerm))) / 15 * 15;
          System.out.print(DateTimeUtils.toDT(solarTerm, GregorianChronology.getInstance(Localizer.getDateTimeZone("+9"))));
          System.out.print("("+getELOfSun(solarTerm)+")");
          System.out.println("("+termIndex+")");
          if (termIndex % 2 == 0) {
            month.addMidClimates(solarTerm);
          } else {
            month.addPreClimates(solarTerm);
          }
        }
      }
      calendar.add(month);
    }

    return calendar;
  }

  protected List<Double> getFirstDayOfMonths(final double fromJD, final double toJD) {
    return keplerizeMoon ? getDynamicalFDMs(fromJD, toJD) : getStaticalFDMs(fromJD, toJD);
  }

  private List<Double> getDynamicalFDMs(final double fromJD, final double toJD) {
    List<Double> firstDays = new ArrayList<Double>();
    firstDays.add(toAJDAtStartOfDay(dayAdvance(toJD)));
    do {
      firstDays.add(toAJDAtStartOfDay(dayAdvance(getFirstDayOfMonth(Iterables.getLast(firstDays) - 2))));
    } while (Iterables.getLast(firstDays) > fromJD);
    Collections.reverse(firstDays);

    return firstDays;
  }

  private List<Double> getStaticalFDMs(final double fromJD, final double toJD) {
    List<Double> firstDays = new ArrayList<Double>();
    firstDays.add(toAJDAtStartOfDay(dayAdvance(fromJD)));
    do {
      firstDays.add(dayAdvance(Iterables.getLast(firstDays) + daysOfMonth));
    } while (Iterables.getLast(firstDays) < toJD);
    if (Iterables.getLast(firstDays) + 2 > toJD) firstDays.remove(firstDays.size() - 1);

    return firstDays;
  }

  protected List<Double> getSolarTerms(final double fromJD, final double toJD) {
    return keplerizeSun ? getDynamicalSTMs(fromJD, toJD) : getStaticalSTMs(fromJD, toJD, toJD - fromJD);
  }

  private List<Double> getDynamicalSTMs(final double fromJD, final double toJD) {
    int solarTerm = LunisolarDateTimeUtils.getSolarTerm(getELOfSun(toAJDAtEndOfDay(toJD)));
    List<Double> solarTerms = new ArrayList<Double>();
    solarTerms.add(getLatestTerm(toAJDAtEndOfDay(toJD), 15 * solarTerm));
    do {
      solarTerm--;
      solarTerms.add(getLatestTerm(Iterables.getLast(solarTerms), circulate(15 * solarTerm)));
    } while (Iterables.getLast(solarTerms) > fromJD);
    if (Iterables.getLast(solarTerms) < fromJD) solarTerms.remove(solarTerms.size() - 1);
    Collections.reverse(solarTerms);

    return solarTerms;
  }

  private List<Double> getStaticalSTMs(final double fromJD, final double toJD, double daysOfYear) {
    List<Double> solarTerms = new ArrayList<Double>();
    solarTerms.add(fromJD);
    while (Iterables.getLast(solarTerms) < toAJDAtStartOfDay(toJD) + 1) {
      solarTerms.add(Iterables.getLast(solarTerms) + (daysOfYear / 24) - .3d);
    }
    for (Double solarTerm : solarTerms) {
      System.out.print(LunisolarDateTimeUtils.toDT(solarTerm, GregorianChronology.getInstance(Localizer.getDateTimeZone("+9"))));
      System.out.println("("+ getELOfSun(solarTerm) +")");
    }
    return solarTerms;
  }

  private double dayAdvance(final double julianDay) {
    if (!enforceDayAdvance) return julianDay;

    return julianDay + (limitOfDayAdvance <= (julianDay - toAJDAtStartOfDay(julianDay)) ? 1 : 0);
  }

  protected List<LunisolarMonth> getCalendarOfSolsticed(double julianDay) {
    double firstDayOfYear = getFirstDayOfYear(toDT(julianDay));
    double firstDayOfNextYear = getFirstDayOfYear(toDT(addMonth(firstDayOfYear, 14)));
    double wsOfLastYear = getLatestTerm(firstDayOfYear, 270);
    double winterSolstice = getLatestTerm(firstDayOfNextYear, 270);
    List<Double> firstDays = getFirstDayOfMonths(getFirstDayOfMonth(wsOfLastYear), getFirstDayOfMonth(winterSolstice));
    List<Double> solarTerms = getSolarTerms(getFirstDayOfMonth(wsOfLastYear), getFirstDayOfMonth(winterSolstice));
    List<LunisolarMonth> calendar = getCalendar(firstDays, solarTerms);
    int monthIndex = 10;
    boolean leaped = false;
    for (LunisolarMonth month : calendar) {
      if (!leaped && !month.hasMidClimate()) {
        month.setIntercalary(true);
        leaped = true;
      } else {
        monthIndex = LunisolarDateTimeUtils.normalizeMonth(monthIndex + 1);
      }
      month.setMonthOfYear(monthIndex);
    }

    return calendar;
  }

  protected List<LunisolarMonth> getCalendarOfYear(double julianDay) {
    List<LunisolarMonth> calendar = getCalendarOfSolsticed(julianDay);
    calendar.addAll(getCalendarOfSolsticed(getWinterSolstice(toDT(julianDay).plusYears(1))));
    calendar.remove(0);
    calendar.remove(0);
//    Collections.reverse(calendar);
    do {
      calendar.remove(calendar.size() - 1);
    } while (Iterables.getLast(calendar).getMonthOfYear() < 12);
//    Collections.reverse(calendar);

    for (LunisolarMonth lunisolarMonth : calendar) {
      System.out.println(lunisolarMonth);
    }

    return calendar;
  }

  protected String[] getEarthlyBranches(Locale locale) {
    return getResource(resourceName, "earthlyBranches", null, "子,丑,寅,卯,辰,巳,午,未,申,酉,戌,亥", locale).split(",");
  }

  protected int getEarthlyBranchOfYear(int year) {
    int earthlyBranch = (year % 12);
    if (earthlyBranch < 0) earthlyBranch += 12;

    return earthlyBranch;
  }

  protected String getEarthlyBranchOfYear(int year, Locale locale) {
    int earthlyBranch = getEarthlyBranchOfYear(year) - 4;
    if (earthlyBranch < 0) earthlyBranch += 12;

    return getEarthlyBranches(locale)[earthlyBranch];
  }

  protected double getELOfMoon(double julianDay) {
    return LunisolarDateTimeUtils.getELOfMoon(julianDay);
  }

  protected double getELOfSun(double julianDay) {
    return LunisolarDateTimeUtils.getELOfSun(julianDay);
  }

  protected double getFirstDayOfMonth(final double julianDay) {
    double temporaryN = (long) julianDay;
    double temporaryF = julianDay - temporaryN;
    double delta = 0d;
    double diffN = 0d;
    double diffF = 0d;
    int counter = 0;
    do {
      counter++;
      double elOfSun = getELOfSun(temporaryN + temporaryF);
      double elOfMoon = getELOfMoon(temporaryN + temporaryF);
      delta = elOfMoon - elOfSun;
      if (counter == 1) {
        delta = circulate(delta);
      } else if (delta > 345) {
        delta -= 360d;
      } else if (delta < -15d) {
        delta = circulate(delta);
      }

      diffF = delta * daysOfMonth / 360d;
      diffN = (long) diffF;
      diffF -= diffN;
      temporaryN -= diffN;
      temporaryF -= diffF;
      if (Math.abs(diffN + diffF) <= 1E-8 && (temporaryN + temporaryF) - julianDay > 0) {
        temporaryN -= 20d;
        diffN = 1d;
      }
    } while (Math.abs(diffN + diffF) > 1E-8 && counter < 100);

    return temporaryN + temporaryF;
  }

  protected List<Double> getFirstDayOfMonths(final double winterSolstice) {
    List<Double> firstDays = new ArrayList<Double>();
    double firstDayOfNovember = toAJDAtStartOfDay(getFirstDayOfMonth(toAJDAtEndOfDay(winterSolstice)));
    firstDays.add(toAJDAtStartOfDay(getFirstDayOfMonth(toAJDAtEndOfDay(getLatestTerm(addMonth(winterSolstice, 14), 270)))));
    do {
      double firstDay = getFirstDayOfMonth(Iterables.getLast(firstDays) - 2);
      System.out.print(LunisolarDateTimeUtils.toDT(firstDay, GregorianChronology.getInstance(Localizer.getDateTimeZone("+9"))));
      if (enforceDayAdvance) {
        if (toDT(firstDay).getHourOfDay() > 17) firstDay += 1;
      }
      System.out.print(" to ");
      System.out.println(LunisolarDateTimeUtils.toDT(firstDay, GregorianChronology.getInstance(Localizer.getDateTimeZone("+9"))));
      firstDays.add(toAJDAtStartOfDay(firstDay));
    } while (Iterables.getLast(firstDays) > firstDayOfNovember);
    Collections.reverse(firstDays);

    return firstDays;
  }

  protected int getHeavenlyStemOfYear(int year) {
    int heavenlyStem = (year % 10);
    if (heavenlyStem < 0) heavenlyStem += 10;

    return heavenlyStem;
  }

  protected String getHeavenlyStemOfYear(int year, Locale locale) {
    int heavenlyStem = getHeavenlyStemOfYear(year) - 4;
    if (heavenlyStem < 0) heavenlyStem += 10;

    return getHeavenlyStems(locale)[heavenlyStem];
  }

  protected String[] getHeavenlyStems(Locale locale) {
    return getResource(resourceName, "heavenlyStems", null, "甲,乙,丙,丁,戊,己,庚,辛,壬,癸", locale).split(",");
  }

  protected String[] getSexagenaryCycle(Locale locale) {
    String[] sexagenaryCycle = new String[60];
    String[] heavenlyStems = getHeavenlyStems(locale);
    String[] earthlyBranches = getEarthlyBranches(locale);
    for (int i = 0; i < 60; i++) {
      sexagenaryCycle[i] = heavenlyStems[i % 10] + earthlyBranches[i % 12];
    }

    return sexagenaryCycle;
  }

  protected double getLatestTerm(final double julianDay, final double angle) {
    final double expected = circulate(angle);
    double temporaryN = (long) julianDay;
    double temporaryF = julianDay - temporaryN;
    double delta = 0d;
    double diff = 0d;
    int counter = 0;
    do {
      delta = getELOfSun(temporaryN + temporaryF) - expected;
      if (counter == 0 && delta < 0d) {
        delta = circulate(delta);
      } else if (delta > 180d) {
        delta -= 360d;
      } else if (delta < -180d) {
        delta += 360d;
      }
      diff = delta * daysOfYear / 360d;
      temporaryN -= (long) diff;
      temporaryF -= diff - ((long) diff);
      if (temporaryF < 0) {
        temporaryF++;
        temporaryN--;
      } else if (temporaryF > 1) {
        temporaryF--;
        temporaryN++;
      }
      counter++;
    } while (Math.abs(diff) > 1E-8 && counter < 100);

    return temporaryN + temporaryF;
  }

  protected int getSexagenaryOfDay(double julianDay) {
    return (((int) (DateTimeUtils.toJDN(julianDay) + 49L) % 60));
  }

  protected String getSexagenaryOfDay(double julianDay, Locale locale) {
    return getSexagenaryCycle(locale)[getSexagenaryOfDay(julianDay)] + getSexagenaryOfDay(julianDay);
  }

  protected int[] getSexagenaryOfYear(int year) {
    return new int[getHeavenlyStemOfYear(year) + getEarthlyBranchOfYear(year)];
  }

  protected List<Double> getSolarTerms(final double winterSolstice) {
    return keplerizeSun ? getDynamicalSolarTerms(winterSolstice) : getStaticalSolarTerms(winterSolstice);
  }

  protected String[] getSolarTerms(final Locale locale) {
    return getResource(resourceName, "solarTerms", null, "春分,清明,穀雨,立夏,小満,芒種,夏至,小暑,大暑,立秋,処暑,白露,秋分,寒露,霜降,立冬,小雪,大雪,冬至,小寒,大寒,立春,雨水,啓蟄", locale).split(",");
  }

  protected String[] getSixdaysOfWeek(final Locale locale) {
    return getResource(resourceName, "daysOfWeek", null, "大安,赤口,先勝,友引,先負,仏滅", locale).split(",");
  }

  protected String getSolarTerm(final int solarTerm, Locale locale) {
    return getSolarTerms(locale)[solarTerm];
  }

  protected String getString() {
    return getString("F-");
  }

  protected String getString(String formatStyle) {
    return getString(formatStyle, Localizer.getAvailableLocale("ja_JP_JP"));
  }

  protected abstract double toAJDAtEndOfDay(Object instant);

  protected abstract double toAJDAtStartOfDay(Object instant);

  protected abstract DateTime toDT(Object instant);

  protected double toTDT(double julianDay) {
    return LunisolarDateTimeUtils.toTDT(julianDay);
  }

  private List<Double> getDynamicalSolarTerms(final double winterSolstice) {
    List<Double> terms = new ArrayList<Double>();
    terms.add(getLatestTerm(addMonth(winterSolstice, 14), 255));
    do {
      terms.add(getLatestTerm(Iterables.getLast(terms), 255 - (15 * terms.size())));
    } while (Iterables.getLast(terms) > toAJDAtEndOfDay(winterSolstice));
    terms.add(getLatestTerm(winterSolstice, 255));
    Collections.reverse(terms);

    return terms;
  }

  private String getResource(String resourceName, String key, String[] arguments, String defaultString, Locale locale) {
    if (!(!StringUtils.isSimilarToBlank(key) && locale != null)) return "";
    String resourceKey = locale.toString() + "_" + key;
    if (!I18N.containsKey(resourceKey)) I18N.put(resourceKey, ResourceUtils.get(resourceName, key, arguments, defaultString, locale));

    return I18N.get(resourceKey);
  }

  private List<Double> getStaticalSolarTerms(final double winterSolstice) {
    List<Double> terms = new ArrayList<Double>();
    double wsNext = getLatestTerm(addMonth(winterSolstice, 14), 270);
    double year = wsNext - winterSolstice;
    terms.add(getLatestTerm(winterSolstice, 255));
    terms.add(winterSolstice);
    do {
      terms.add(Iterables.getLast(terms) + (year / 24));
    } while (Iterables.getLast(terms) < wsNext);
    for (Double term : terms) {
      if (term.compareTo(winterSolstice) != 0) term = term - .3d;
    }

    return terms;
  }

  private double optimizeSynodicMonth(final double julianDay) {
    return ((julianDay - J2000) * SYNODIC_MONTH_INCREMENTAL) + daysOfMonth;
  }

  @SuppressWarnings("unchecked")
  protected static <T extends AbstractEra> T getEra(List<T> periods, double julianDay) {
    AbstractEra period = getPeriod(periods, julianDay);
    if (period == null) return null;
    for (AbstractEra era : period.getEras()) {
      if (era.contains(julianDay)) return (T) era;
    }

    return null;
  }

  @SuppressWarnings("unchecked")
  protected static <T extends AbstractEra> T getPeriod(List<T> periods, double julianDay) {
    if (periods == null) return null;
    for (AbstractEra period : periods) {
      if (period.contains(julianDay)) return (T) period;
    }

    return null;
  }

  protected final double getFirstDayOfYear(DateTime then) {
    double springEquinox = getLatestTerm(toAJDAtEndOfDay(then.withMonthOfYear(4).withDayOfMonth(1)), 0);

    return getFirstDayOfMonth(addMonth(springEquinox, -1));
  }

  protected final double getWinterSolstice(DateTime then) {
    return getLatestTerm(getFirstDayOfYear(then.plusYears(1)), 270);
  }
}
