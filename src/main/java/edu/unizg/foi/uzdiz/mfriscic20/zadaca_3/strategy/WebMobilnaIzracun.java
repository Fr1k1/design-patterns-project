package edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.strategy;

public class WebMobilnaIzracun extends IzracunCijeneKarte {
  private final double popustWebMob;

  public WebMobilnaIzracun(double popustVikend, double popustWebMob) {
    super(popustVikend);
    this.popustWebMob = popustWebMob;
  }

  @Override
  public double izracunajCijenu(double osnovnaCijenaKm, double udaljenostKm, boolean jeVikend)
      throws Exception {
    double cijena = osnovnaCijenaKm * udaljenostKm;
    if (jeVikend) {
      cijena *= (1 - popustVikend);
    }
    return cijena * (1 - popustWebMob);
  }
}

