package edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.dto;

import java.util.ArrayList;
import java.util.List;

public class Pruga {
  private String oznaka;
  private List<Stanica> stanice;

  public Pruga(String oznaka) {
    this.oznaka = oznaka;
    this.stanice = new ArrayList<>();
  }

  public void dodajStanicu(Stanica stanica) {
    stanice.add(stanica);
  }

  public String getOznaka() {
    return oznaka;
  }

  public List<Stanica> getStanice() {
    return new ArrayList<>(stanice);
  }

  public Stanica getPocetnaStanica() {
    if (stanice.isEmpty()) {
      return null;
    }

    for (int i = 0; i < stanice.size(); i++) {
      if (i == 0) {
        return stanice.get(i);
      }
    }
    return null;
  }

  public Stanica getZavrsnaStanica() {
    if (stanice.isEmpty()) {
      return null;
    }

    for (int i = stanice.size() - 1; i >= 0; i--) {
      if (i == stanice.size() - 1) {
        return stanice.get(i);
      }
    }
    return null;
  }


  public List<Stanica> getStaniceObrnuto() {
    List<Stanica> obrnuto = new ArrayList<>();
    for (int i = stanice.size() - 1; i >= 0; i--) {
      obrnuto.add(stanice.get(i));
    }
    return obrnuto;
  }

  public int getUkupnoKilometara() {
    int ukupno = 0;
    for (Stanica stanica : stanice) {
      ukupno += stanica.getDuzina();
    }
    return ukupno;
  }

  // NE MIJENJAJ OVU METODU
  // tu se jos mora dodati smjer
  public List<Stanica> getStaniceIzmedu(String pocetnaStanica, String zavrsnaStanica) {
    int pocetniIndex = -1;
    int zavrsniIndex = -1;

    for (int i = 0; i < stanice.size(); i++) {
      if (stanice.get(i).getStanica().equals(pocetnaStanica)) {
        pocetniIndex = i;
      }
      if (stanice.get(i).getStanica().equals(zavrsnaStanica)) {
        zavrsniIndex = i;
      }
    }

    if (pocetniIndex == -1 || zavrsniIndex == -1) {
      return new ArrayList<>();
    }

    if (pocetniIndex > zavrsniIndex) {
      int temp = pocetniIndex;
      pocetniIndex = zavrsniIndex;
      zavrsniIndex = temp;
    }

    return stanice.subList(pocetniIndex, zavrsniIndex + 1);
  }
}
