/**
 *
 */
package jp.furplag.util.time;

import static jp.furplag.util.time.DateTimeUtils.*;
import static org.junit.Assert.*;
import jp.furplag.util.Localizer;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.GJChronology;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DateTimeUtilsTest {

  private DateTime dateTime = null;

  private Object expected = null;
  /**
   * @throws java.lang.Exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {}

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception {
    dateTime = null;
  }

  /**
   * @throws java.lang.Exception
   */
  @After
  public void tearDown() throws Exception {}

//  @Test
//  public final void testEnforceDefault() {
//    assertEquals(expected, enforceUTC(dateTime));
//    dateTime = DateTime.now(DateTimeZone.UTC);
//    expected = dateTime.toString(DateTimeFormat.mediumDateTime());
//    assertEquals(expected, enforceDefault(dateTime).toString(DateTimeFormat.mediumDateTime()));
//    expected = enforceDefault(dateTime).plusMillis(DateTimeZone.getDefault().getOffset(dateTime)).minusMillis(dateTime.getZone().getOffset(dateTime));
//    assertTrue(dateTime.isEqual((DateTime) expected));
//
//    dateTime = new DateTime(dateTime, Localizer.newDateTimeZone("PST"));
//    expected = dateTime.toString(ISODateTimeFormat.dateHourMinuteSecondFraction());
//    assertEquals(expected, enforceDefault(dateTime).toString(ISODateTimeFormat.dateHourMinuteSecondFraction()));
//    expected = enforceDefault(dateTime).plusMillis(DateTimeZone.getDefault().getOffset(dateTime)).minusMillis(dateTime.getZone().getOffset(dateTime));
//    assertTrue(dateTime.isEqual((DateTime) expected));
//  }
//
//  @Test
//  public final void testEnforceUTC() {
//    assertEquals(expected, enforceUTC(dateTime));
//    dateTime = DateTime.now();
//    expected = dateTime.toString(DateTimeFormat.mediumDateTime());
//    assertEquals(expected, enforceUTC(dateTime).toString(DateTimeFormat.mediumDateTime()));
//    assertTrue(dateTime.isEqual(enforceUTC(dateTime).minusMillis(dateTime.getZone().getOffset(dateTime))));
//  }

  @Test
  public final void testFromJDDouble() {
    dateTime = new DateTime(0L);
    assertEquals(fromJulianDay(MILLIS_PERIOD), fromJD(MILLIS_PERIOD));
    assertTrue(dateTime.isEqual(fromJD(MILLIS_PERIOD)));
  }

  @Test
  public final void testFromJDDouble1() {
    dateTime = new DateTime(0L);
    assertEquals(0, fromJulianDay(MILLIS_PERIOD) - fromJD(Double.valueOf(MILLIS_PERIOD)));
    assertTrue(dateTime.isEqual(fromJD(Double.valueOf(MILLIS_PERIOD))));
    assertTrue(dateTime.plusHours(12).isEqual(fromJD(Double.valueOf(MILLIS_PERIOD + .5).longValue())));
  }

  /**
   * {@link jp.furplag.util.time.DateTimeUtils#toAJD(java.lang.Object)} のためのテスト・メソッド。
   */
  @Test
  public final void testToAJDObject() {
    assertEquals(toJulianDay(0L), toAJD(0L), 0);
    assertEquals(toJulianDay(0L), toAJD("1970-01-01T0Z"), 0);
    dateTime = DateTime.now();
    assertEquals(toJulianDay(dateTime.getMillis()), toAJD(dateTime), 0);
  }

  /**
   * {@link jp.furplag.util.time.DateTimeUtils#toAJD(java.lang.Object, boolean)} のためのテスト・メソッド。
   */
  @Test
  public final void testToAJDObjectBoolean() {
    assertEquals(toJulianDay(0L), toAJD(0L, true), 0);
    assertEquals(toJulianDay(0L), toAJD("1970-01-01T0Z", true), 0);
    dateTime = DateTime.now();
    assertEquals(toJulianDay(dateTime.getMillis()), toAJD(dateTime, true), 0);
    assertEquals(toJulianDay(dateTime.getMillis()), toAJD(dateTime, true), 0);
    assertEquals(toJulianDay(dateTime.getMillis()), toAJD(dateTime, true), 0);
    assertEquals(toJulianDay(dateTime.getMillis()), toAJD(dateTime, true), 0);
    assertEquals(toJulianDay(dateTime.getMillis()), toAJD(dateTime, true), 0);
    assertEquals(toJulianDay(dateTime.getMillis()), toAJD(dateTime, true), 0);
  }

  /**
   * {@link jp.furplag.util.time.DateTimeUtils#toDT(java.lang.Object)} のためのテスト・メソッド。
   */
  @Test
  public final void testToDTObject() {
    System.out.println(toJDN(DateTime.parse("0593-02-07T00:00Z")));
System.out.println(DateTime.parse("0593-02-07T00:00Z").withChronology(GJChronology.getInstanceUTC()).getDayOfMonth());
    System.out.println(toJDN(new DateTime(GJChronology.getInstanceUTC()).withMillis(DateTime.parse("0593-02-07T00:00Z").getMillis())));
    System.out.println(DateTime.parse("0593-02-07T00:00Z"));
    System.out.println(DateTime.parse("2010-06-30T01:20Z"));
    System.out.println(DateTime.parse("2010-06-30T01:20+02:00"));
    System.out.println(new DateTime("2010-06-30T01:20+02:00"));
//    System.out.println(DateTime.now(GJChronology.getInstanceUTC()).parse("0593-02-07"));
//    System.out.println(new DateTime(GJChronology.getInstanceUTC()).parse("0593-02-09T00:18:59.000+09:18:59"));
//    DateTime d = toDT("0593-02-09T00:18:59.000+09:18:59");
//    System.out.println(toDT(fromJD(toAJD(d))));
//    System.out.println(toAJD(d));
//    System.out.println(toJDN(d));
//
//    DateTime dd = enforceUTC(d.withZone(DateTimeZone.getDefault()));
//    System.out.println(toDT(fromJD(toAJD(dd))));
//    System.out.println(toAJD(dd));
//    System.out.println(toJDN(dd));
//    System.out.println(enforceUTC(new DateTime("0593-02-09T00:18:59.000+09:18:59").withChronology(GJChronology.getInstance())));
//    System.out.println(enforceUTC(new DateTime("0593-02-09T00:18:59.000+09:18:59", GJChronology.getInstance())));
    // fail("まだ実装されていません"); // TODO
  }

  /**
   * {@link jp.furplag.util.time.DateTimeUtils#toDT(java.lang.Object, boolean)} のためのテスト・メソッド。
   */
  @Test
  public final void testToDTObjectBoolean() {
    // fail("まだ実装されていません"); // TODO
  }

  /**
   * {@link jp.furplag.util.time.DateTimeUtils#toDT(java.lang.Object, java.lang.Object)} のためのテスト・メソッド。
   */
  @Test
  public final void testToDTObjectObject() {
    // fail("まだ実装されていません"); // TODO
  }

  /**
   * {@link jp.furplag.util.time.DateTimeUtils#toDT(java.lang.Object, java.lang.Object, boolean)} のためのテスト・メソッド。
   */
  @Test
  public final void testToDTObjectObjectBoolean() {
    // fail("まだ実装されていません"); // TODO
  }

  /**
   * {@link jp.furplag.util.time.DateTimeUtils#toJDN(java.lang.Object)} のためのテスト・メソッド。
   */
  @Test
  public final void testToJDNObject() {
    // fail("まだ実装されていません"); // TODO
  }

  /**
   * {@link jp.furplag.util.time.DateTimeUtils#toJDN(java.lang.Object, boolean)} のためのテスト・メソッド。
   */
  @Test
  public final void testToJDNObjectBoolean() {
    // fail("まだ実装されていません"); // TODO
  }

  /**
   * {@link jp.furplag.util.time.DateTimeUtils#toMJD(java.lang.Object)} のためのテスト・メソッド。
   */
  @Test
  public final void testToMJDObject() {
    // fail("まだ実装されていません"); // TODO
  }

  /**
   * {@link jp.furplag.util.time.DateTimeUtils#toMJD(java.lang.Object, boolean)} のためのテスト・メソッド。
   */
  @Test
  public final void testToMJDObjectBoolean() {
    // fail("まだ実装されていません"); // TODO
  }

}
