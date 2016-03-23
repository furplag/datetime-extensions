package jp.furplag.util.time.lunisolar;

public interface LunisolarChronology {

  public String name(Object locale);

  public double getDaysOfYear();

  public double getDaysOfMonth();

  public double getLimitOfDayAdvance();

  public boolean isKeplerizeMoon();

  public boolean isKeplerizeSun();
}
