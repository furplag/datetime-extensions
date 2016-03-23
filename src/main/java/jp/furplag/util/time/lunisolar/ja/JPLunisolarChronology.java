
package jp.furplag.util.time.lunisolar.ja;

import java.util.Locale;

import jp.furplag.util.Localizer;
import jp.furplag.util.ResourceUtils;
import jp.furplag.util.commons.StringUtils;
import jp.furplag.util.time.lunisolar.LunisolarChronology;

/**
 * Historical method of Chronology for Japanese Lunisolar Calendar.
 *
 * @author furplag
 */
public enum JPLunisolarChronology implements LunisolarChronology {

  // 年, 平均朔望月, 平朔, 平気(, 進朔限)
  Origin(365.244776, 29.530597, false, false),          // 平朔儀鳳暦
  Genka(365.2467, 29.530585, true, false),              // 元嘉暦:    0454-02-14(1886925.125)
  Giho(365.244776, 29.530597, true, false),             // 儀鳳暦:    0697-01-28(1975664.125)
  Taien(365.244408, 29.530592, true, false),            // 大衍暦:    0764-02-07(2000145.125)
  Taien766(365.244408, 29.530592, true, false, 2660 / 3040d), // 大衍暦:    0766-02-14(2000883.125)
  Taien768(365.244408, 29.530592, true, false, 2534 / 3040d), // 大衍暦:    0768-01-24(2001592.125)
  Taien793(365.244408, 29.530592, true, false, 2787 / 3040d), // 大衍暦:    0793-02-15(2010746.125)
  Taien837(365.244408, 29.530592, true, false, 2660 / 3040d), // 大衍暦:    0837-02-09(2026811.125)
  Goki(365.244776, 29.530597, true, false),             // 五紀暦:    0858-01-19(2034460.125)
  Senmyo(365.244643, 29.530595, true, false, 6300 / 8400d), // 宣明暦:  0862-02-03(2035936.125)
  Jokyo(365.241696, 29.530590, true, false), // 貞享乙丑暦:  1685-02-04(2336528.125)
  //貞享暦:  1687-02-12(2337266.125)
  // 宝暦癸酉暦: 1753-02-03(2361363.125)
  Horyaku(365.241542, 29.530590, true, false),
  // 宝暦甲戌暦: 1754-01-23(236717.125)
  // 宝暦暦: 1755-02-11(2362101.125)
  // 修正宝暦暦: 1771-02-15(2362101.125)
  Kansei(365.242347, 29.530584, true, false), //寛政暦: 1798-02-16(2377812.125)
  //寛政丁亥暦: 1827-01-27(2388383.125)
  Tenpo(365.242234, 29.530588, true, true), //天保暦: 1844-02-18(2394614.125)
;
  private static final String RESOURCE_PATH = JPLunisolar.class.getCanonicalName();

  public static JPLunisolarChronology getChronology(double julianDay) {
    if (julianDay >= 2394614.125) return Tenpo;
    if (julianDay >= 2377812.125) return Kansei;
    if (julianDay >= 2362101.125) return Horyaku;
    if (julianDay >= 2336528.125) return Jokyo;
    if (julianDay >= 2035936.125) return Senmyo;
    if (julianDay >= 2034460.125) return Goki;
    if (julianDay >= 2000145.125) return Taien;
    if (julianDay >= 1975664.125) return Giho;
    if (julianDay >= 1886925.125) return Genka;

    return Origin;
  }

  /** astronomical julian day of Solar year. */
  private final double daysOfYear;

  /** astronomical julian day of Lunar month. */
  private final double daysOfMonth;

  /** if the moment of new moon was after {@code dayAdvance} of the day, the month start to next day of actual first day of the month. */
  private final double limitOfDayAdvance;

  /** if false, calculate Lunar month constantly. */
  private final boolean keplerizeMoon;

  /** if false, calculate Solar term constantly. */
  private final boolean keplerizeSun;

  private JPLunisolarChronology(double daysOfYear, double daysOfMonth, boolean keplerizeMoon, boolean keplerizeSun) {
    this(daysOfYear, daysOfMonth, keplerizeMoon, keplerizeSun, 1d);
  }

  private JPLunisolarChronology(double daysOfYear, double daysOfMonth, boolean keplerizeMoon, boolean keplerizeSun, double limitOfDayAdvance) {
    this.daysOfYear = daysOfYear;
    this.daysOfMonth = daysOfMonth;
    this.limitOfDayAdvance = limitOfDayAdvance <= 0 ? 1d : limitOfDayAdvance;
    this.keplerizeMoon = keplerizeMoon;
    this.keplerizeSun = keplerizeSun;
  }

  @Override
  public String name(Object locale) {
    return ResourceUtils.get(RESOURCE_PATH, "chronology." + name(), new String[] {}, StringUtils.capitalize(name()), locale == null ? Locale.ROOT : Localizer.getAvailableLocale(locale));
  }

  @Override
  public double getDaysOfYear() {
    return daysOfYear;
  }

  @Override
  public double getDaysOfMonth() {
    return daysOfMonth;
  }

  @Override
  public double getLimitOfDayAdvance() {
    return limitOfDayAdvance;
  }

  @Override
  public boolean isKeplerizeMoon() {
    return keplerizeMoon;
  }

  @Override
  public boolean isKeplerizeSun() {
    return keplerizeSun;
  }
}
