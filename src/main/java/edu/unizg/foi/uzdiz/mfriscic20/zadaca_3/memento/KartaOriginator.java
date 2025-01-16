package edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.memento;

import java.time.LocalDateTime;

public class KartaOriginator {
  private String oznakaVlaka;
  private String polaznaStanica;
  private String odredisnaStanica;
  private String datum;
  private String nacinKupnje;
  private double osnovnaCijena;
  private double konacnaCijena;
  private double udaljenostKm;
  private boolean jeVikend;
  private LocalDateTime vrijemeKupnje;

  public void setStanje(String oznakaVlaka, String polaznaStanica, String odredisnaStanica,
      String datum, String nacinKupnje, double osnovnaCijena, double konacnaCijena,
      double udaljenostKm, boolean jeVikend) {
    this.oznakaVlaka = oznakaVlaka;
    this.polaznaStanica = polaznaStanica;
    this.odredisnaStanica = odredisnaStanica;
    this.datum = datum;
    this.nacinKupnje = nacinKupnje;
    this.osnovnaCijena = osnovnaCijena;
    this.konacnaCijena = konacnaCijena;
    this.udaljenostKm = udaljenostKm;
    this.jeVikend = jeVikend;
    // kaj s ovim
    this.vrijemeKupnje = LocalDateTime.now();
  }

  // ok to nije ni slicno kermeku to sad moram promijeniti

  public KartaMemento createMemento() {
    return new KartaMemento(oznakaVlaka, polaznaStanica, odredisnaStanica, datum, nacinKupnje,
        osnovnaCijena, konacnaCijena, udaljenostKm, jeVikend, vrijemeKupnje);
  }

  public void setMemento(KartaMemento memento) {
    this.oznakaVlaka = memento.getOznakaVlaka();
    this.polaznaStanica = memento.getPolaznaStanica();
    this.odredisnaStanica = memento.getOdredisnaStanica();
    this.datum = memento.getDatum();
    this.nacinKupnje = memento.getNacinKupnje();
    this.osnovnaCijena = memento.getOsnovnaCijena();
    this.konacnaCijena = memento.getKonacnaCijena();
    this.udaljenostKm = memento.getUdaljenostKm();
    this.jeVikend = memento.isJeVikend();
    this.vrijemeKupnje = memento.getVrijemeKupnje();
  }
}
