package edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.strategy;

public class DirektnoUVlakuIzracun extends IzracunCijeneKarte {
  private final double uvecanjeVlak;

  public DirektnoUVlakuIzracun(double popustVikend, double uvecanjeVlak) {
    super(popustVikend);
    this.uvecanjeVlak = uvecanjeVlak;
  }

  @Override
  public double izracunajCijenu(double osnovnaCijenaKm, double udaljenostKm, boolean jeVikend)
      throws Exception {
    double cijena = osnovnaCijenaKm * udaljenostKm;
    if (jeVikend) {
      cijena *= (1 - popustVikend);
    }
    return cijena * (1 + uvecanjeVlak);
  }
}
