package edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.strategy;


public abstract class IzracunCijeneKarte {
  protected final double popustVikend;

  public IzracunCijeneKarte(double popustVikend) {
    this.popustVikend = popustVikend;
  }

  public abstract double izracunajCijenu(double osnovnaCijenaKm, double udaljenostKm,
      boolean jeVikend) throws Exception;
}
