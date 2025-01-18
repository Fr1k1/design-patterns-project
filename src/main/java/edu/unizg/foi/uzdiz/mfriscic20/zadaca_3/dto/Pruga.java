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

  private boolean postojiPreklapanje(String novaPolazna, String novaZavrsna) {
    // Prvo nađimo sve stanice između za novu relaciju
    List<Stanica> staniceNoveRelacije = fixedGetStaniceIzmedu(novaPolazna, novaZavrsna);

    // Uklonimo granice nove relacije za usporedbu
    List<Stanica> unutarnjeStaniceNove = new ArrayList<>(staniceNoveRelacije);
    unutarnjeStaniceNove.remove(0); // Ukloni prvu stanicu
    if (unutarnjeStaniceNove.size() > 0) {
      unutarnjeStaniceNove.remove(unutarnjeStaniceNove.size() - 1); // Ukloni zadnju stanicu
    }

    for (RelacijaPrugeContext postojecaRelacija : relacije.values()) {
      // Preskočimo relacije koje su ispravne
      if (postojecaRelacija.getTrenutnoStanje().getStatus().equals("I")) {
        continue;
      }
      List<Stanica> stanicePostojeceRelacije = fixedGetStaniceIzmedu(
          postojecaRelacija.getPocetnaStanica(), postojecaRelacija.getZavrsnaStanica());

      // Ako nova relacija sadrži BILO KOJU unutarnju stanicu postojeće relacije,
      // ili ako postojeća relacija sadrži BILO KOJU unutarnju stanicu nove relacije,
      // imamo preklapanje
      for (Stanica stanica : unutarnjeStaniceNove) {
        if (stanicePostojeceRelacije.contains(stanica)) {
          return true;
        }
      }

      // Uklonimo granice postojeće relacije za usporedbu
      List<Stanica> unutarnjeStanicePostojece = new ArrayList<>(stanicePostojeceRelacije);
      unutarnjeStanicePostojece.remove(0);
      if (unutarnjeStanicePostojece.size() > 0) {
        unutarnjeStanicePostojece.remove(unutarnjeStanicePostojece.size() - 1);
      }

      for (Stanica stanica : unutarnjeStanicePostojece) {
        if (staniceNoveRelacije.contains(stanica)) {
          return true;
        }
      }
    }
    return false;
  }

  private boolean mozeMijenjatiStatus(String pocetnaStanica, String zavrsnaStanica,
      String novoStanje) {
    String kljucRelacije = pocetnaStanica + "-" + zavrsnaStanica;
    RelacijaPrugeContext relacija = relacije.get(kljucRelacije);

    // Ako relacija već postoji, provjeravamo samo pravila prijelaza između stanja
    if (relacija != null) {
      String trenutnoStanje = relacija.getTrenutnoStanje().getStatus();

      // Ako je relacija zatvorena, može prijeći samo u testiranje
      if (trenutnoStanje.equals("Z")) {
        return novoStanje.equals("T");
      }

      // Iz testiranja može prijeći samo u ispravno stanje
      if (trenutnoStanje.equals("T")) {
        return novoStanje.equals("I");
      }

      return true; // Za ostale prijelaze nema ograničenja
    }

    // Ako je nova relacija, provjeri preklapanja samo ako je novo stanje K ili Z
    if (novoStanje.equals("K") || novoStanje.equals("Z")) {
      return !postojiPreklapanje(pocetnaStanica, zavrsnaStanica);
    }

    return true;
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
    List<Stanica> staniceNaRelaciji = fixedGetStaniceIzmedu(pocetnaStanica, zavrsnaStanica);
    if (staniceNaRelaciji.isEmpty()) {
      System.out.println("Ne postoji relacija između zadanih stanica");
      return;
    }

    // Provjeri može li se mijenjati status
    if (!mozeMijenjatiStatus(pocetnaStanica, zavrsnaStanica, novoStanje)) {
      System.out.println("Nije moguće promijeniti status relacije zbog postojećih ograničenja");
      return;
    }

    int brojKolosijeka = staniceNaRelaciji.get(0).getBrojKolosjeka();
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
