package edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.state.IspravnoStanje;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.state.RelacijaPrugeContext;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.state.StanjePrugeState;

public class Pruga {
  private String oznaka;
  private List<Stanica> stanice;
  private StanjePrugeState trenutnoStanje;
  private Map<String, RelacijaPrugeContext> relacije;

  public Pruga(String oznaka) {
    this.oznaka = oznaka;
    this.stanice = new ArrayList<>();
    this.trenutnoStanje = new IspravnoStanje(); // ovo potencijalno mogu maknuti
    this.relacije = new HashMap<>();
  }

  public Map<String, RelacijaPrugeContext> getRelacije() {
    return new HashMap<>(relacije);
  }


  public List<RelacijaPrugeContext> getRelacijePoStatusu(String status) {
    List<RelacijaPrugeContext> rezultat = new ArrayList<>();
    for (RelacijaPrugeContext relacija : relacije.values()) {
      if (relacija.getTrenutnoStanje().getStatus().equals(status)) {
        rezultat.add(relacija);
      }
    }
    return rezultat;
  }

  public void request(String pocetnaStanica, String zavrsnaStanica, String novoStanje) {
    List<Stanica> staniceNaRelaciji = getStaniceIzmedu(pocetnaStanica, zavrsnaStanica);
    if (staniceNaRelaciji.isEmpty()) {
      return;
    }

    int brojKolosijeka = staniceNaRelaciji.get(0).getBrojKolosjeka();
    // ovo mozda refactor kasnije
    String kljucRelacije = pocetnaStanica + "-" + zavrsnaStanica;
    RelacijaPrugeContext relacija = relacije.get(kljucRelacije);

    if (relacija == null) {
      relacija = new RelacijaPrugeContext(this.oznaka, pocetnaStanica, zavrsnaStanica,
          brojKolosijeka, "N");
      relacije.put(kljucRelacije, relacija);
    }

    relacija.promijeniStanje(novoStanje);
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

  // ovo tu se koristi ak za nist drugo za pregled pruga

  public int getUkupnoKilometara() {
    int ukupno = 0;
    for (Stanica stanica : stanice) {
      ukupno += stanica.getDuzina();
    }
    return ukupno;
  }

  // NE MIJENJAJ OVU METODU
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

  public List<Stanica> fixedGetStaniceIzmedu(String pocetnaStanica, String zavrsnaStanica) {
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
      List<Stanica> obrnutRedoslijed =
          new ArrayList<>(stanice.subList(zavrsniIndex, pocetniIndex + 1));
      Collections.reverse(obrnutRedoslijed);
      return obrnutRedoslijed;
    }

    return stanice.subList(pocetniIndex, zavrsniIndex + 1);
  }

}
