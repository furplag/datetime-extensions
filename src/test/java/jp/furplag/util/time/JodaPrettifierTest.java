/**
 *
 */

package jp.furplag.util.time;

import static jp.furplag.util.time.JodaPrettifier.prettify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.ocpsoft.prettytime.PrettyTime;
import org.ocpsoft.prettytime.units.Decade;

import jp.furplag.util.Localizer;

/**
 * @author furplag
 */
public class JodaPrettifierTest {

  /**
   * @throws java.lang.Exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {}

  /**
   * @throws java.lang.Exception
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception {}

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception {}

  /**
   * @throws java.lang.Exception
   */
  @After
  public void tearDown() throws Exception {}

  /**
   * {@link jp.furplag.util.time.JodaPrettifier#prettify(java.lang.Object)}.
   */
  @Test
  public void testPrettifyObject() {
    assertEquals("null", "", prettify(null));
    assertEquals("empty", "", prettify(""));
    assertEquals("invalid", "", prettify("could not convert date-time."));

    DateTime then = DateTime.now();
    assertEquals(new PrettyTime().format(then.toDate()), prettify(then));
  }

  /**
   * {@link jp.furplag.util.time.JodaPrettifier#prettify(java.lang.Object, java.lang.Object)}.
   */
  @Test
  public void testPrettifyObjectObject() {
    assertEquals("null", "", prettify(null, null));
    assertEquals("empty", "", prettify("", ""));
    assertEquals("invalid", "", prettify("could not convert date-time.", new String[] { "me", "too." }));

    DateTime then = DateTime.now();
    assertEquals(new PrettyTime(then.plusMillis(1).toDate()).format(then.toDate()), prettify(then, null));
    assertEquals(new PrettyTime().format(then.minusMillis(1).toDate()), prettify(then, ""));
    assertEquals(new PrettyTime().format(then.minusMillis(1).toDate()), prettify(then, "not an instant."));
    assertEquals(new PrettyTime().format(then.minusMillis(1).toDate()), prettify(then, then));
    assertEquals(new PrettyTime(then.minusDays(1).toDate()).format(then.toDate()), prettify(then, then.minusDays(1)));
  }

  /**
   * {@link jp.furplag.util.time.JodaPrettifier#prettify(java.lang.Object, java.lang.Object, java.util.Locale, org.joda.time.DateTimeZone)}.
   */
  @Test
  public void testPrettifyObjectObjectLocaleDateTimeZone() {
    DateTime then = DateTime.now();
    for (Locale locale : new Locale[] { Locale.ROOT, Locale.getDefault() }/* Locale.getAvailableLocales() */) {
      for (String zone : new String[] { null, "", "+9", "-7" }/* DateTimeZone.getAvailableIDs() */) {
        assertEquals(new PrettyTime(then.minusDays(1).toDate()).setLocale(locale).format(then.toDate()), prettify(then, then.minusDays(1), locale, zone));
      }
    }
  }

  /**
   * {@link jp.furplag.util.time.JodaPrettifier#prettify(java.lang.Object, java.lang.Object, java.util.Locale, org.joda.time.DateTimeZone, java.lang.Object)}.
   */
  @Test
  public void testPrettifyObjectObjectLocaleDateTimeZoneObject() {
    DateTime then = DateTime.now();
    for (Locale locale : new Locale[] { Locale.ROOT, Locale.getDefault() }/* Locale.getAvailableLocales() */) {
      for (String zone : new String[]{null, "", "+9", "-7"}/*DateTimeZone.getAvailableIDs()*/) {
        assertEquals(prettify(then, then.minusDays(1), locale, zone), prettify(then, then.minusDays(1), locale, Localizer.getDateTimeZone(zone), null));
        assertEquals(prettify(then, then.minusHours(1), locale, zone), prettify(then, then.minusHours(1), locale, Localizer.getDateTimeZone(zone), 12 * 60 * 60 * 1000));
        assertEquals(then.withZone(Localizer.getDateTimeZone(zone)).toString(DateTimeFormat.forStyle("-M").withLocale(locale)), prettify(then, then.minusHours(1), locale, Localizer.getDateTimeZone(zone), 50 * 60 * 1000));
//        assertEquals(then.minusHours(2).withZone(Localizer.getDateTimeZone(zone)).toString(DateTimeFormat.forStyle("-M").withLocale(locale)), prettify(then.minusHours(2), then, locale, Localizer.getDateTimeZone(zone), Hours.ONE));
      }
    }
  }

  /**
   * test for privates.
   */
  @Test
  public void testJodaPrettifier() {
    try {
      Method doPrettify = JodaPrettifier.class.getDeclaredMethod("doPrettify", DateTime.class, DateTime.class, Locale.class);
      doPrettify.setAccessible(true);

      DateTime then = DateTime.now();
      DateTime ref = then.withTimeAtStartOfDay();
      assertEquals("null", new PrettyTime(ref.toDate()).setLocale(Locale.getDefault()).format(then.toDate()), doPrettify.invoke(null, then, ref, Locale.getDefault()));
      for (Locale l : new Locale[] { Locale.ROOT, Locale.getDefault() }/* Locale.getAvailableLocales() */) {
        ref = then.minusYears(100000);
        while (ref.isBefore(then)) {
          ref = ref.plusYears(1000);
          assertEquals(ref.toString(), new PrettyTime(then.toDate()).setLocale(Localizer.getAvailableLocale(l)).format(ref.toDate()), doPrettify.invoke(null, ref, then, Localizer.getAvailableLocale(l)));
        }
        ref = then.minusYears(1000);
        while (ref.isBefore(then)) {
          ref = ref.plusYears(50);
          PrettyTime prettyTime = new PrettyTime(then.toDate()).setLocale(Localizer.getAvailableLocale(l));
          if (Locale.JAPANESE.getLanguage().equals(l.getLanguage())) prettyTime.removeUnit(Decade.class);
          assertEquals(ref.toString(), prettyTime.format(ref.toDate()), doPrettify.invoke(null, ref, then, Localizer.getAvailableLocale(l)));
        }
        ref = then.minusYears(1);
        while (ref.isBefore(then)) {
          ref = ref.plusMonths(1);
          assertEquals(ref.toString(), new PrettyTime(then.toDate()).setLocale(Localizer.getAvailableLocale(l)).format(ref.toDate()), doPrettify.invoke(null, ref, then, Localizer.getAvailableLocale(l)));
        }
        ref = then.minusMonths(1);
        while (ref.isBefore(then)) {
          ref = ref.plusWeeks(1);
          assertEquals(ref.toString(), new PrettyTime(then.toDate()).setLocale(Localizer.getAvailableLocale(l)).format(ref.toDate()), doPrettify.invoke(null, ref, then, Localizer.getAvailableLocale(l)));
        }
        ref = then.minusWeeks(1);
        while (ref.isBefore(then)) {
          ref = ref.plusDays(1);
          assertEquals(ref.toString(), new PrettyTime(then.toDate()).setLocale(Localizer.getAvailableLocale(l)).format(ref.toDate()), doPrettify.invoke(null, ref, then, Localizer.getAvailableLocale(l)));
        }
        ref = then.minusDays(1);
        while (ref.isBefore(then)) {
          ref = ref.plusHours(10);
          assertEquals(ref.toString(), new PrettyTime(then.toDate()).setLocale(Localizer.getAvailableLocale(l)).format(ref.toDate()), doPrettify.invoke(null, ref, then, Localizer.getAvailableLocale(l)));
        }
        ref = then.minusHours(1);
        while (ref.isBefore(then)) {
          ref = ref.plusMinutes(15);
          assertEquals(ref.toString(), new PrettyTime(then.toDate()).setLocale(Localizer.getAvailableLocale(l)).format(ref.toDate()), doPrettify.invoke(null, ref, then, Localizer.getAvailableLocale(l)));
        }
        ref = then.minusMinutes(1);
        while (ref.isBefore(then)) {
          ref = ref.plusSeconds(12);
          assertEquals(ref.toString(), new PrettyTime(then.toDate()).setLocale(Localizer.getAvailableLocale(l)).format(ref.toDate()), doPrettify.invoke(null, ref, then, Localizer.getAvailableLocale(l)));
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      fail(e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
    }
  }

}
