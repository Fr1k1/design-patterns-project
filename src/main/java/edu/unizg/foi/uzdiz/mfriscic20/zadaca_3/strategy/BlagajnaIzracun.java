package edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.strategy;

public class BlagajnaIzracun extends IzracunCijeneKarte {
  public BlagajnaIzracun(double popustVikend) {
    super(popustVikend);
  }

  @Override
  public double izracunajCijenu(double osnovnaCijenaKm, double udaljenostKm, boolean jeVikend)
      throws Exception {
    double cijena = osnovnaCijenaKm * udaljenostKm;
    if (jeVikend) {
      cijena *= (1 - popustVikend);
    }
    return cijena;
  }
}
