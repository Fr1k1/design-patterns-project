package edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.strategy;

public class CijenovniContext {
  private IzracunCijeneKarte strategija;
  private double cijenaNormalniKm;
  private double cijenaUbrzaniKm;
  private double cijenaBrziKm;
  private double popustVikend;
  private double popustWebMob;
  private double uvecanjeVlak;

  public void postaviStrategiju(IzracunCijeneKarte strategija) {
    this.strategija = strategija;
    System.out.println("Strategija mi je" + strategija);
  }

  public void postaviCijene(double cijenaNormalniKm, double cijenaUbrzaniKm, double cijenaBrzi,
      double popustVikend, double popustWebMob, double uvecanjeVlak) {
    this.cijenaNormalniKm = cijenaNormalniKm;
    this.cijenaUbrzaniKm = cijenaUbrzaniKm;
    this.cijenaBrziKm = cijenaBrzi;
    this.popustVikend = popustVikend;
    this.popustWebMob = popustWebMob;
    this.uvecanjeVlak = uvecanjeVlak;
  }

  public double izracunajCijenu(double osnovnaCijenaKm, double udaljenostKm, boolean jeVikend)
      throws Exception {
    if (strategija == null) {
      throw new Exception("Strategija nije postavljena!");
    }
    return strategija.izracunajCijenu(osnovnaCijenaKm, udaljenostKm, jeVikend);
  }

  public double getCijenaNormalniKm() {
    System.out.println("Pozivam se za normalne kilometre");
    return cijenaNormalniKm;
  }

  public double getCijenaUbrzaniKm() {
    System.out.println("Pozivam se za ubrzane kilometre");
    return cijenaUbrzaniKm;
  }

  public double getCijenaBrziKm() {
    System.out.println("Pozivam se za brze kilometre");
    return cijenaBrziKm;
  }

  public double getPopustVikend() {
    return popustVikend;
  }

  public double getPopustWebMob() {
    return popustWebMob;
  }

  public double getUvecanjeVlak() {
    return uvecanjeVlak;
  }
}
