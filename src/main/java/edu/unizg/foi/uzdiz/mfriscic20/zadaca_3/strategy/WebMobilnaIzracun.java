package edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.strategy;

public class WebMobilnaIzracun extends IzracunCijeneKarte {
  public WebMobilnaIzracun(double cijenaNormalni, double cijenaUbrzani, double cijenaBrzi,
      double popustVikend, double popustWebMob, double uvecanjeVlak) {
    super(cijenaNormalni, cijenaUbrzani, cijenaBrzi, popustVikend, popustWebMob, uvecanjeVlak);
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
