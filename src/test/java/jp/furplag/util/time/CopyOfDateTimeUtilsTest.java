/**
 *
 */
package jp.furplag.util.time;

import static org.junit.Assert.assertEquals;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.GJChronology;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class CopyOfDateTimeUtilsTest {

  /**
   * @throws java.lang.Exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {}

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

  @Test
  public final void testFromJDDouble() {
    Double julianDay = null;
    DateTime then = null;
    Object expected = null;

    assertEquals(expected, DateTimeUtils.fromJD(julianDay));

    then = new DateTime("2001-01-01T12:00:00.000Z");
    julianDay = DateTimeUtils.toJulianDay(then.getMillis());
    expected = then.getMillis();
    long julianDayNumber = Double.valueOf(julianDay).longValue();
    assertEquals(expected, DateTimeUtils.fromJD(julianDayNumber));

    then = new DateTime();
    julianDay = DateTimeUtils.toJulianDay(then.getMillis());
    expected = then.getMillis();
    assertEquals(expected, DateTimeUtils.fromJD(julianDay));
  }

  @Test
  public final void testToAJDDateTime() {
    DateTime then = null;
    Object expected = null;

    assertEquals(expected, DateTimeUtils.toAJD(then));
    then = new DateTime("2001-01-01T12:00:00.000Z");

    expected = DateTimeUtils.toJulianDay(then.getMillis());
    assertEquals(expected, DateTimeUtils.toAJD(then));
  }

  @Test
  public final void testToAJDLong() {
    Long millis = null;
    Object expected = null;
    assertEquals(expected, DateTimeUtils.toAJD(millis));
    millis = new DateTime("2001-01-01T12:00:00.000Z").getMillis();

    expected = DateTimeUtils.toJulianDay(millis);
    assertEquals(expected, DateTimeUtils.toAJD(millis));
  }

  @Test
  public final void testToDTDouble() {
    Double julianDay = null;
    DateTime then = null;
    Object expected = null;

    assertEquals(expected, DateTimeUtils.toDT(julianDay));

    then = new DateTime("2001-01-01T12:00:00.000Z");
    julianDay = DateTimeUtils.toJulianDay(then.getMillis());
    expected = then;
    assertEquals(then, DateTimeUtils.toDT(julianDay));
  }

  @Test
  public final void testToDTDoubleDateTime() {
    Double julianDay = null;
    DateTime then = null;
    Object expected = null;

    assertEquals(expected, DateTimeUtils.toDT(julianDay));

    then = new DateTime("2001-01-01T12:00:00.000Z");
    julianDay = DateTimeUtils.toJulianDay(then.getMillis());
    then = DateTime.now(GJChronology.getInstance(DateTimeZone.forID("Etc/GMT+4")));
    expected = then.withMillis(DateTimeUtils.fromJulianDay(julianDay));
    assertEquals(expected, DateTimeUtils.toDT(julianDay, then));
  }

  @Test
  public final void testToJDNDateTime() {
    DateTime then = null;
    Object expected = null;

    assertEquals(expected, DateTimeUtils.toJDN(then));
    then = new DateTime("2001-01-01T12:00:00.000Z");

    expected = DateTimeUtils.toJulianDayNumber(then.getMillis());
    assertEquals(expected, DateTimeUtils.toJDN(then));
  }

  @Test
  public final void testToJDNLong() {
    Long millis = null;
    Object expected = null;
    assertEquals(expected, DateTimeUtils.toJDN(millis));
    millis = new DateTime("2001-01-01T12:00:00.000Z").getMillis();

    expected = DateTimeUtils.toJulianDayNumber(millis);
    assertEquals(expected, DateTimeUtils.toJDN(millis));
  }

  @Test
  public final void testToMJD() {
///1858-11-17T00:00:00Z
    DateTime then = null;
    Object expected = null;

    assertEquals(expected, DateTimeUtils.toMJD(then));

    then = DateTime.now();
    expected = DateTimeUtils.toJulianDay(then.getMillis()) - DateTimeUtils.toJulianDay(new DateTime("1858-11-17T00:00:00Z").getMillis());
    assertEquals(expected, DateTimeUtils.toMJD(then));
  }

}
