
package jp.furplag.util.time.lunisolar.ja;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableList;

import jp.furplag.util.JSONifier;
import jp.furplag.util.Localizer;
import jp.furplag.util.ResourceUtils;
import jp.furplag.util.time.DateTimeUtils;
import jp.furplag.util.time.DateTimeUtils.FormatStyle;
import jp.furplag.util.time.lunisolar.AbstractLunisolar;
import jp.furplag.util.time.lunisolar.LunisolarMonth;
import jp.furplag.util.time.lunisolar.StructureOfEra;

public class JPLunisolar extends AbstractLunisolar {

  private static final List<JPLunisolarEra> ERAS = initialize();

  private static final DateTimeZone ZONE = Localizer.getDateTimeZone("Etc/GMT-9");

  public JPLunisolar(JPLunisolarChronology chronology) {
    super(chronology);
    this.chronology = chronology;
  }

  public JPLunisolar(Object instant, JPLunisolarChronology chronology) {
    super(chronology);
    this.chronology = chronology;
    dateTime = toDT(instant);
    julianDay = DateTimeUtils.toAJD(dateTime);
  }

  public JPLunisolar(Object instant) {
    this(JPLunisolarChronology.getChronology(DateTimeUtils.toAJD(DateTimeUtils.toDT(instant, ZONE, false))));
    dateTime = toDT(instant);
    julianDay = DateTimeUtils.toAJD(dateTime);
  }

  private List<LunisolarMonth> calendarOfYear;

  private final JPLunisolarChronology chronology;

  private DateTime dateTime;

  private Integer dayOfMonth;

  private int elappse = -1;

  private JPLunisolarEra era;

  private boolean intercalary = false;

  private Double julianDay;

  private Integer monthOfYear;

  private final Map<String, Map<String, String>> strings = new HashMap<String, Map<String, String>>();

  private Integer year;

  private Integer yearOfEra;

  public JPLunisolar clear() {
    reset();

    return this;
  }

  public List<LunisolarMonth> getCalendarOfYear() {
    return calendarOfYear;
  }

  public JPLunisolarChronology getChronology() {
    return chronology;
  }

  public DateTime getDateTime() {
    return dateTime;
  }

  public Integer getDayOfMonth() {
    return dayOfMonth;
  }

  public String getEarthlyBranchOfYear(Locale locale) {
    if (!isMaterialized()) return "";

    return getEarthlyBranchOfYear(year, locale);
  }

  public String getHeavenlyStemOfYear(Locale locale) {
    if (!isMaterialized()) return "";

    return getHeavenlyStemOfYear(year, locale);
  }

  public Double getJulianDay() {
    return julianDay;
  }

  public Integer getMonthOfYear() {
    return monthOfYear;
  }

  public String getSexagenaryOfDay(Locale locale) {
    if (!isMaterialized()) return "";

    return getSexagenaryOfDay(julianDay, locale);
  }

  public String getSexagenaryOfYear(Locale locale) {
    return getHeavenlyStemOfYear(locale) + getEarthlyBranchOfYear(locale);
  }

  @Override
  public String getString(String formatStyle, Object locale) {
    if (!isMaterialized()) return "";
    FormatStyle[] formatStyles = FormatStyle.forStyles(formatStyle);
    FormatStyle dateFormat = formatStyles[0];
    FormatStyle timeFormat = formatStyles[1];
    Locale aLocale = Localizer.getAvailableLocale(locale);
    String key = dateFormat.toString() + timeFormat.toString();
    if (strings.containsKey(aLocale) && strings.get(aLocale).containsKey(key)) return strings.get(aLocale).get(key);
    String format = "";
    List<Object> temporaries = new ArrayList<Object>();
    if (dateFormat.is()) {
      format += DateTimeUtils.getPattern(dateFormat + "-", aLocale);
      if (timeFormat.is()) format += "T";
    }
    if (timeFormat.is()) format += DateTimeUtils.getPattern("-" + timeFormat, aLocale);
    Matcher m = IGNORE_PATTERN.matcher(format);
    while (m.find()) {
      String temporary = m.group();
      format = format.replace(temporary, "{" + (temporaries.size()) + "}");
      temporaries.add(temporary.replaceAll("(^')|('$)", ""));
    }
    if (dateFormat.is()) {
      if (!format.contains("E")) {
        if (format.matches("d.*M")) {
          format = "E, " + format;
        } else if (format.contains("T")) {
          format = format.replace("T", " ET");
        } else {
          format += " E";
        }
      }
      format = format.replaceFirst("E+", "{" + (temporaries.size()) + "}");
      temporaries.add(getSixdaysOfWeek(aLocale)[(monthOfYear + dayOfMonth) % 6]);
      if (format.contains("dd")) {
        format = format.replaceFirst("d+", "{" + (temporaries.size()) + "}");
        temporaries.add((format.contains("dd") && dayOfMonth < 10 ? "0" : "") + dayOfMonth);
        if (intercalary) {
          format = format.replaceFirst("M+", "$0{" + (temporaries.size()) + "}");
          temporaries.add(ResourceUtils.get(resourceName, "intercalary", null, dateFormat.is(FormatStyle.FULL) ? "(LeapMonth)" : "L", aLocale));
        }
      }
      JPLunisolarEra era = getEra(ERAS, julianDay);
      if (era != null) {
        if (!format.contains("G")) format = format.replaceFirst("y+", dateFormat.is(FormatStyle.FULL) ? ("GGGG$0") : "G$0");
        format = format.replaceFirst("G+", "{" + (temporaries.size()) + "}");
        temporaries.add(era.getName(aLocale));
        format = format.replaceFirst("y+", "{" + (temporaries.size()) + "}");
        temporaries.add(yearOfEra);
      }
    }
    String formatted = MessageFormat.format(dateTime.withYear(year).withMonthOfYear(monthOfYear).withDayOfMonth(dayOfMonth).toString(format.replace("T", " "), aLocale), temporaries.toArray(new Object[] {}));
    if (!strings.containsKey(aLocale)) strings.put(aLocale.toString(), new HashMap<String, String>());
    strings.get(aLocale.toString()).put(key, formatted);

    return formatted;
  }

  public Integer getYear() {
    return year;
  }

  public boolean isIntercalary() {
    return intercalary;
  }

  public JPLunisolar materialize() {
    if (isMaterialized()) return this;
    if (dateTime == null) return this;
    long elappse = System.currentTimeMillis();
    double firstDayOfYear = getFirstDayOfYear(dateTime);
    calendarOfYear = getCalendarOfYear(julianDay);
    for (LunisolarMonth month : calendarOfYear) {
//      System.out.println(month);
      if (month.contains(julianDay)) {
        year = month.getYear();
        monthOfYear = month.getMonthOfYear();
        intercalary = month.isIntercalary();
        dayOfMonth = (int) new Interval(month.getInterval().getStart(), dateTime.withTimeAtStartOfDay()).toDuration().getStandardDays() + 1;
      }
    }
    era = getEra(ERAS, julianDay);
    if (era != null) {
      double firstDayOfStartyearOfEra = getFirstDayOfYear(toDT(era.getFromAJD()));
      yearOfEra = dateTime.getYear() - toDT(firstDayOfStartyearOfEra).getYear();
      if (year == dateTime.getYear()) yearOfEra++;
    } else {
      yearOfEra = year;
    }
    this.elappse = (int) (System.currentTimeMillis() - elappse);

    return this;
  }

  public JPLunisolar materialize(Object instant) {
    return set(instant).materialize();
  }

  public JPLunisolar set(Object instant) {
    reset();
    dateTime = toDT(instant);
    julianDay = DateTimeUtils.toAJD(dateTime);

    return this;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("chronology\t:\t").append(chronology.name(Localizer.getAvailableLocale("ja_JP"))).append("\n");
    if (!isMaterialized()) return sb.toString();
    sb.append("then\t:\t").append(dateTime).append("(").append(julianDay).append(")\n");
    sb.append("lunisolarized\t:\t").append(year).append("-").append(monthOfYear).append(intercalary ? "=" : "").append("-").append(dayOfMonth).append("\n");
    sb.append("elappse\t:\t").append(elappse).append("ms\n");

    return sb.toString();
  }

  @Override
  protected double toAJDAtEndOfDay(Object instant) {
    return DateTimeUtils.toAJDAtEndOfDay(instant, ZONE);
  }

  @Override
  protected double toAJDAtStartOfDay(Object instant) {
    return DateTimeUtils.toAJDAtStartOfDay(instant, ZONE);
  }

  @Override
  protected DateTime toDT(Object instant) {
    return DateTimeUtils.toDT(instant, ZONE);
  }

  private boolean isMaterialized() {
    return elappse > 0;
  }

  private void reset() {
    calendarOfYear = null;
    dateTime = null;
    dayOfMonth = null;
    elappse = -1;
    era = null;
    intercalary = false;
    julianDay = null;
    monthOfYear = null;
    strings.clear();
    year = null;
  }

  public static DateTimeZone getZone() {
    return ZONE;
  }

  public static void main(String[] args) {

    JPLunisolar luni = new JPLunisolar("0859-10-17T0+09").materialize("0859-10-17T0+09");

//    DateTime d = DateTime.now().withTimeAtStartOfDay().withMonthOfYear(1).withDayOfMonth(1);
//    System.out.println(luni.toDT(luni.getFirstDayOfYear(d.plusMillis(-1))));
//    int i = 0;
//    while (i < 20) {
//      System.out.print(d.plusMonths(i));
//      System.out.print("\t");
//      System.out.println(luni.toDT(luni.getFirstDayOfYear(d.plusMonths(i))));
//      i++;
//    }
    System.out.println(luni);
//
//    System.out.println(luni.getString("FF", Locale.getDefault()));
//    System.out.println();
//    luni.getCalendarOfYear(2361005.125);
//    System.out.println(luni.toDT(2000145.125));
//    System.out.println(DateTimeUtils.toJDN(2000145.625));
  }

  private static final List<JPLunisolarEra> initialize() {
    List<JPLunisolarEra> eras = new ArrayList<JPLunisolarEra>();
    List<JPLunisolarEra> erasOfPeriod = new ArrayList<JPLunisolarEra>();
    for (StructureOfEra period : JSONifier.parseLazy(ResourceUtils.get(JPLunisolarEra.class.getCanonicalName(), "eras", null, null, Locale.ROOT), new TypeReference<List<StructureOfEra>>() {})) {
      erasOfPeriod.clear();
      for (StructureOfEra e : period.getEras()) {
        erasOfPeriod.add(new JPLunisolarEra(e.getKey(), e.getFromAJD(), e.getToAJD()));
      }
      eras.add(new JPLunisolarEra(period.getKey(), period.getFromAJD(), period.getToAJD(), erasOfPeriod));
    }

    return ImmutableList.copyOf(eras);
  }
}
