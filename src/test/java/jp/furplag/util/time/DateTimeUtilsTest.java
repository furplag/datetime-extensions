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
package jp.furplag.util.time;

import static jp.furplag.util.time.DateTimeUtils.MILLIS_PERIOD;
import static jp.furplag.util.time.DateTimeUtils.fromJD;
import static jp.furplag.util.time.DateTimeUtils.toAJD;
import static jp.furplag.util.time.DateTimeUtils.toDT;
import static org.joda.time.DateTimeUtils.fromJulianDay;
import static org.joda.time.DateTimeUtils.toJulianDay;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.GJChronology;
import org.joda.time.format.DateTimeFormat;
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
   * {@link jp.furplag.util.time.DateTimeUtils#toAJD(java.lang.Object)}.
   */
  @Test
  public final void testToAJDObject() {
    assertEquals(toJulianDay(0L), toAJD(0L), 0);
    assertEquals(toJulianDay(0L), toAJD("1970-01-01T0Z"), 0);
    dateTime = DateTime.now();
    assertEquals(toJulianDay(dateTime.getMillis()), toAJD(dateTime), 0);
  }

  /**
   * {@link jp.furplag.util.time.DateTimeUtils#toAJD(java.lang.Object, boolean)}.
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
   * {@link jp.furplag.util.time.DateTimeUtils#toDT(java.lang.Object)}.
   */
  // @Test
  public final void testToDTObject() {
    // fail("まだ実装されていません"); // TODO
  }

  /**
   * {@link jp.furplag.util.time.DateTimeUtils#toDT(java.lang.Object, boolean)}.
   */
  @Test
  public final void testToDTObjectBoolean() {
    // fail("まだ実装されていません"); // TODO
  }

  /**
   * {@link jp.furplag.util.time.DateTimeUtils#toDT(java.lang.Object, java.lang.Object)}.
   */
  @Test
  public final void testToDTObjectObject() {
    // fail("まだ実装されていません"); // TODO
  }

  /**
   * {@link jp.furplag.util.time.DateTimeUtils#toDT(java.lang.Object, java.lang.Object, boolean)}.
   */
  @Test
  public final void testToDTObjectObjectBoolean() {
    assertEquals("null", null, toDT(null, null, true));

    assertEquals(DateTime.now(DateTimeZone.UTC).toString(DateTimeFormat.forStyle("MS")), toDT(null, null, false).toString(DateTimeFormat.forStyle("MS")));
    assertEquals(new DateTime("1996-01-23T0Z", DateTimeZone.UTC), toDT(822355200000L, null, true));
    assertEquals(new DateTime("1996-01-23T0Z", DateTimeZone.UTC), toDT(2450105.5f, null, true));
    assertEquals(null, toDT(Float.NaN, null, true));
    assertEquals(null, toDT(Double.NEGATIVE_INFINITY, null, true));
    assertEquals(new DateTime("1996-01-23T0Z", DateTimeZone.UTC), toDT("1996-01-23T0Z", null, true));
    assertEquals(new DateTime("1996-01-23T0Z", DateTimeZone.UTC), toDT("1996-01-23T9+0900", null, true));
    assertEquals(new DateTime("1996-01-23T0Z", DateTimeZone.getDefault()), toDT("1996-01-23T0Z", DateTimeZone.getDefault(), true));

    assertEquals(new DateTime("1996-01-23T0Z", DateTimeZone.getDefault()), toDT("1996-01-23T0Z", new DateTime("1996-01-23"), true));

    assertEquals(new DateTime("0800-12-25T0Z", GJChronology.getInstanceUTC()), toDT("0800-12-25T0Z", null, true));
    assertEquals(new DateTime("2112-09-03T0Z", GJChronology.getInstanceUTC()).toString(), toDT("2112-09-03T0Z", null, true).toString());

    assertEquals(new DateTime("1996-01-23T0Z", GJChronology.getInstance()).toString(), toDT("1996-01-23T0Z", DateTimeZone.getDefault(), true).toString());
  }

  /**
   * {@link jp.furplag.util.time.DateTimeUtils#toJDN(java.lang.Object)}.
   */
  @Test
  public final void testToJDNObject() {
    // fail("まだ実装されていません"); // TODO
  }

  /**
   * {@link jp.furplag.util.time.DateTimeUtils#toJDN(java.lang.Object, boolean)}.
   */
  @Test
  public final void testToJDNObjectBoolean() {
    // fail("まだ実装されていません"); // TODO
  }

  /**
   * {@link jp.furplag.util.time.DateTimeUtils#toMJD(java.lang.Object)}.
   */
  @Test
  public final void testToMJDObject() {
    // fail("まだ実装されていません"); // TODO
  }

  /**
   * {@link jp.furplag.util.time.DateTimeUtils#toMJD(java.lang.Object, boolean)}.
   */
  @Test
  public final void testToMJDObjectBoolean() {
    // fail("まだ実装されていません"); // TODO
  }

}
