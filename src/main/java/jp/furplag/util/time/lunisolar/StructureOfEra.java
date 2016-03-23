
package jp.furplag.util.time.lunisolar;

import java.io.Serializable;
import java.util.List;

import org.joda.time.DateTimeZone;

public final class StructureOfEra implements Serializable, Comparable<StructureOfEra> {

  private static final long serialVersionUID = 1L;

  public StructureOfEra() {}

  private List<StructureOfEra> eras;

  private double fromAJD;

  private String key;

  private Double toAJD;

  @Override
  public int compareTo(StructureOfEra other) {
    if (other == null) return 1;
    if (fromAJD != other.fromAJD) return Double.compare(fromAJD, other.fromAJD);

    return toAJD == null ? 1 : Double.compare(toAJD, other.toAJD);
  }

  @Override
  public boolean equals(Object other) {
    return other instanceof StructureOfEra && compareTo((StructureOfEra) other) == 0;
  }

  public List<StructureOfEra> getEras() {
    return eras;
  }

  public double getFromAJD() {
    return fromAJD;
  }

  public String getKey() {
    return key;
  }

  public Double getToAJD() {
    return toAJD;
  }

  public void setEras(List<StructureOfEra> eras) {
    this.eras = eras;
  }

  public void setFromAJD(double fromAJD) {
    this.fromAJD = fromAJD;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public void setToAJD(Double toAJD) {
    this.toAJD = toAJD;
  }
}
