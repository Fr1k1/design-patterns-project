package edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.memento;

import java.time.LocalDateTime;

// ova klasa se cini ok
public class KartaMemento {
  private final String oznakaVlaka;
  private final String polaznaStanica;
  private final String odredisnaStanica;
  private final String datum;
  private final String nacinKupnje;
  private final double osnovnaCijena;
  private final double konacnaCijena;
  private final double udaljenostKm;
  private final boolean jeVikend;
  private final LocalDateTime vrijemeKupnje;

  public KartaMemento(String oznakaVlaka, String polaznaStanica, String odredisnaStanica,
      String datum, String nacinKupnje, double osnovnaCijena, double konacnaCijena,
      double udaljenostKm, boolean jeVikend, LocalDateTime vrijemeKupnje) {
    this.oznakaVlaka = oznakaVlaka;
    this.polaznaStanica = polaznaStanica;
    this.odredisnaStanica = odredisnaStanica;
    this.datum = datum;
    this.nacinKupnje = nacinKupnje;
    this.osnovnaCijena = osnovnaCijena;
    this.konacnaCijena = konacnaCijena;
    this.udaljenostKm = udaljenostKm;
    this.jeVikend = jeVikend;
    this.vrijemeKupnje = vrijemeKupnje;
    // this.fullState on tu ima stavljeno da je false...e sad ne znam kaj ce mu to
  }

  public String getOznakaVlaka() {
    return oznakaVlaka;
  }

  public String getPolaznaStanica() {
    return polaznaStanica;
  }

  public String getOdredisnaStanica() {
    return odredisnaStanica;
  }

  public String getDatum() {
    return datum;
  }

  public String getNacinKupnje() {
    return nacinKupnje;
  }

  public double getOsnovnaCijena() {
    return osnovnaCijena;
  }

  public double getKonacnaCijena() {
    return konacnaCijena;
  }

  public double getUdaljenostKm() {
    return udaljenostKm;
  }

  public boolean isJeVikend() {
    return jeVikend;
  }

  public LocalDateTime getVrijemeKupnje() {
    return vrijemeKupnje;
  }
}
