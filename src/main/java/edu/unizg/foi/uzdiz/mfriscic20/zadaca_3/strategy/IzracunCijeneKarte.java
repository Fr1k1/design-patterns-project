package edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.strategy;


public abstract class IzracunCijeneKarte {
  protected double cijenaNormalniKm;
  protected double cijenaUbrzaniKm;
  protected double cijenaBrziKm;
  protected double popustVikend;
  protected double popustWebMob;
  protected double uvecanjeVlak;

  public IzracunCijeneKarte(double cijenaNormalni, double cijenaUbrzani, double cijenaBrzi,
      double popustVikend, double popustWebMob, double uvecanjeVlak) {
    this.cijenaNormalniKm = cijenaNormalni;
    this.cijenaUbrzaniKm = cijenaUbrzani;
    this.cijenaBrziKm = cijenaBrzi;
    this.popustVikend = popustVikend;
    this.popustWebMob = popustWebMob;
    this.uvecanjeVlak = uvecanjeVlak;
  }

  public abstract double izracunajCijenu(double osnovnaCijenaKm, double udaljenostKm,
      boolean jeVikend) throws Exception;
}
