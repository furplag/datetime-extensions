
package jp.furplag.util.time.lunisolar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.google.common.collect.ImmutableList;

import jp.furplag.util.JSONifier;
import jp.furplag.util.ResourceUtils;
import jp.furplag.util.commons.StringUtils;
import jp.furplag.util.time.DateTimeUtils;

public abstract class AbstractEra implements Comparable<AbstractEra> {

  private static final Map<String, String> NAMES = new HashMap<String, String>();

  protected AbstractEra(String key, double fromAJD, Double toAJD, String resourceName) {
    this(key, fromAJD, toAJD, resourceName, null);
  }

  protected AbstractEra(String key, double fromAJD, Double toAJD, String resourceName, List<? extends AbstractEra> eras) {
    if (StringUtils.isSimilarToBlank(key)) throw new IllegalArgumentException("\"key\" must NOT be empty.");
    if (toAJD != null && toAJD <= fromAJD) throw new IllegalArgumentException("\"toAJD\" must be greater than \"fromAJD\".");
    this.key = key;
    this.fromAJD = fromAJD;
    this.toAJD = toAJD;
    this.resourceName = resourceName;
    this.keyPrefix = eras != null && eras.size() > 0 ? "period." : "";
    this.keySuffix = !(eras != null && eras.size() > 0) ? "." + LunisolarDateTimeUtils.toDT(fromAJD).getYear() : "";
    this.eras = initializeEras(eras);
  }

  public final String keyPrefix;

  public final String keySuffix;

  protected final String resourceName;

  private final List<AbstractEra> eras;

  private final double fromAJD;

  private final String key;

  private final Double toAJD;

  @Override
  public int compareTo(AbstractEra other) {
    if (other == null) return 1;
    if (fromAJD != other.fromAJD) return Double.compare(fromAJD, other.fromAJD);

    return toAJD == null ? 1 : Double.compare(toAJD, other.toAJD);
  }

  public boolean contains(Double julianDay) {
    return julianDay != null && fromAJD <= julianDay && julianDay <= (toAJD == null ? julianDay : toAJD);
  }

  public boolean contains(Object instant) {
    return contains(DateTimeUtils.toAJD(instant, true));
  }

  @Override
  public boolean equals(Object other) {
    return other instanceof AbstractEra && compareTo((AbstractEra) other) == 0;
  }

  public List<AbstractEra> getEras() {
    return eras;
  }

  public double getFromAJD() {
    return fromAJD;
  }

  public String getKey() {
    return key;
  }

  public String getKeyPrefix() {
    return keyPrefix;
  }

  public String getKeySuffix() {
    return keySuffix;
  }

  public String getName(Locale locale) {
    if (!NAMES.containsKey(locale.toString())) {
      NAMES.put(locale.toString(), ResourceUtils.get(resourceName, keyPrefix + key + keySuffix, null, StringUtils.capitalize(key), locale));
    }

    return NAMES.get(locale.toString());
  }

  public Double getToAJD() {
    return toAJD;
  }

  @Override
  public String toString() {
    return JSONifier.stringifyLazy(this);
  }

  private List<AbstractEra> initializeEras(List<? extends AbstractEra> eras) {
    List<AbstractEra> erasOfThis = new ArrayList<AbstractEra>();
    if (eras != null) {
      for (AbstractEra e : eras) {
        erasOfThis.add(e);
      }
    }
    Collections.sort(erasOfThis);

    return ImmutableList.copyOf(erasOfThis);
  }
}
