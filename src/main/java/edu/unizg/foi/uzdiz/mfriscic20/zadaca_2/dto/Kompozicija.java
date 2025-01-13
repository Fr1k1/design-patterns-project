package edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.dto;

import java.util.ArrayList;
import java.util.List;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.vozilobuilder.Vozilo;

public class Kompozicija {
  private Integer oznaka;
  private List<Vozilo> vozila = new ArrayList<>();
  private List<String> uloge = new ArrayList<>();

  public Kompozicija() {}

  public Kompozicija(Integer oznaka) {
    this.oznaka = oznaka;
  }

  public void dodajVozilo(Vozilo vozilo, String uloga) {
    if (vozilo != null) {
      this.vozila.add(vozilo);
      this.uloge.add(uloga);
    }
  }

  public boolean kompozicijaJeValidna() {
    boolean imaVucu = uloge.contains("P");
    boolean imaVagon = uloge.contains("V");
    boolean sveSuVucna = true;

    for (String uloga : uloge) {
      if (!uloga.equals("P")) {
        sveSuVucna = false;
        break;
      }
    }

    return imaVucu && (imaVagon || sveSuVucna);
  }

  public Integer getOznaka() {
    return oznaka;
  }

  public List<Vozilo> getVozila() {
    return vozila;
  }

  public List<String> getUloge() {
    return uloge;
  }

  public int getBrojVozila() {
    return vozila.size();
  }

  public Vozilo getVozilo(int index) {
    if (index >= 0 && index < vozila.size()) {
      return vozila.get(index);
    }
    return null;
  }

  public String getUloga(int index) {
    if (index >= 0 && index < uloge.size()) {
      return uloge.get(index);
    }
    return null;
  }
}
