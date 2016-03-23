
package jp.furplag.util.time.lunisolar;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import jp.furplag.util.time.DateTimeUtils;

public final class LunisolarMonth {

  LunisolarMonth(final DateTime start, final DateTime end) {
    interval = new Interval(start, end);
  }

  private boolean intercalary;

  private final Interval interval;

  private List<Double> midClimates = new ArrayList<Double>();

  private Integer monthOfYear;

  private List<Double> preClimates = new ArrayList<Double>();

  public boolean contains(final Double julianDay) {
    return interval.contains(DateTimeUtils.fromJD(julianDay));
  }

  public boolean contains(final Object then) {
    return contains(DateTimeUtils.toAJD(then));
  }

  public Interval getInterval() {
    return interval;
  }

  public List<Double> getMidClimates() {
    return midClimates;
  }

  public Integer getMonthOfYear() {
    return monthOfYear;
  }

  public List<Double> getPreClimates() {
    return preClimates;
  }

  public int getYear() {
    return interval.getStart().getYear() - (monthOfYear > interval.getStart().getMonthOfYear() ? 1 : 0);
  }

  public boolean hasMidClimate() {
    return midClimates.size() > 0;
  }

  public boolean hasPreClimate() {
    return preClimates.size() > 0;
  }

  public boolean isIntercalary() {
    return intercalary;
  }

  void addMidClimates(final double midClimate) {
    midClimates.add(midClimate);
  }

  void addPreClimates(final double preClimate) {
    preClimates.add(preClimate);
  }

  void setIntercalary(final boolean intercalary) {
    this.intercalary = intercalary;
  }

  void setMonthOfYear(final Integer monthOfYear) {
    this.monthOfYear = monthOfYear;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("{");
    sb.append("year").append(": ").append(getYear()).append(", ");
    sb.append("monthOfYear").append(": ").append(getMonthOfYear()).append(", ");
    sb.append("intercalary").append(": ").append(intercalary).append(", ");
    sb.append("interval").append(": {");
    sb.append("start: ").append(interval.getStart().toString()).append(", ");
    sb.append("end: ").append(interval.getEnd().toString()).append("}, ");
    sb.append("midClimates").append(": [");
    if (hasMidClimate()) {
      for (int i = 0; i < midClimates.size(); i++) {
        if (i > 0) sb.append(", ");
        sb.append(DateTimeUtils.toDT(midClimates.get(i), interval.getStart().getZone()));
      }
    }
    sb.append("], ");
    sb.append("preClimates").append(": [");
    if (hasPreClimate()) {
      for (int i = 0; i < preClimates.size(); i++) {
        if (i > 0) sb.append(", ");
        sb.append(DateTimeUtils.toDT(preClimates.get(i), interval.getStart().getZone()));
      }
    }
    sb.append("]");
    sb.append("}");

    return sb.toString();
  }
}
