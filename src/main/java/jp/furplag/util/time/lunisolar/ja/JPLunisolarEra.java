package jp.furplag.util.time.lunisolar.ja;

import java.util.List;

import jp.furplag.util.time.lunisolar.AbstractEra;

public class JPLunisolarEra extends AbstractEra {

  protected JPLunisolarEra(String key, double fromAJD, Double toAJD) {
    super(key, fromAJD, toAJD, JPLunisolarEra.class.getCanonicalName());
  }

  protected JPLunisolarEra(String key, double fromAJD, Double toAJD, List<JPLunisolarEra> eras) {
    super(key, fromAJD, toAJD, JPLunisolarEra.class.getCanonicalName(), eras);
  }
}
