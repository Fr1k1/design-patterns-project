package edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.strategy;


// beskorisna klasa jer sam prvo krivo implementiral
public class ContextCustom {
  protected IzracunCijeneKarte izracun;

  public ContextCustom(IzracunCijeneKarte izracun) {
    this.izracun = izracun;
  }

  public boolean izracunajCijenuKarte(double osnovnaCijenaKm, double udaljenostKm,
      boolean jeVikend) {
    try {
      double cijena = izracun.izracunajCijenu(osnovnaCijenaKm, udaljenostKm, jeVikend);
      System.out.println("Izračunata cijena karte: " + cijena + " €");
      return true;
    } catch (Exception e) {
      System.out.println("Greška pri izračunu cijene: " + e.getMessage());
      return false;
    }
  }
}
