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
    List<Stanica> staniceNoveRelacije = fixedGetStaniceIzmedu(novaPolazna, novaZavrsna);
    int brojKolosijeka = staniceNoveRelacije.get(0).getBrojKolosjeka();

    for (RelacijaPrugeContext postojecaRelacija : relacije.values()) {
      if (postojecaRelacija.getTrenutnoStanje().getStatus().equals("I")) {
        continue;
      }

      List<Stanica> stanicePostojeceRelacije = fixedGetStaniceIzmedu(
          postojecaRelacija.getPocetnaStanica(), postojecaRelacija.getZavrsnaStanica());

      // Za jedan kolosijek
      if (brojKolosijeka == 1) {
        for (Stanica stanica : staniceNoveRelacije) {
          if (stanicePostojeceRelacije.contains(stanica)) {
            return true;
          }
        }
      }
      // Za dva kolosijeka
      else {
        // Provjeri samo ako je isti smjer
        boolean istiSmjer = novaPolazna.equals(postojecaRelacija.getPocetnaStanica())
            || novaZavrsna.equals(postojecaRelacija.getZavrsnaStanica());

        if (istiSmjer) {
          for (Stanica stanica : staniceNoveRelacije) {
            if (stanicePostojeceRelacije.contains(stanica)) {
              return true;
            }
          }
        }
      }
    }
    return false;
  }

  public boolean mozePutovatiIzmeduStanica(String stanica1, String stanica2) {
    String kljucRelacije = stanica1 + "-" + stanica2;
    RelacijaPrugeContext relacija = relacije.get(kljucRelacije);

    if (relacija != null) {
      return relacija.mozePutovatiVlak();
    }
    return true;
  }

  private boolean mozeMijenjatiStatus(String pocetnaStanica, String zavrsnaStanica,
      String novoStanje) {
    String kljucRelacije = pocetnaStanica + "-" + zavrsnaStanica;
    RelacijaPrugeContext relacija = relacije.get(kljucRelacije);

    // Prvo dohvatimo stanice za ovu relaciju
    List<Stanica> staniceNaRelaciji = fixedGetStaniceIzmedu(pocetnaStanica, zavrsnaStanica);
    int brojKolosijeka = staniceNaRelaciji.get(0).getBrojKolosjeka();

    // Ako relacija već postoji
    if (relacija != null) {
      String trenutnoStanje = relacija.getTrenutnoStanje().getStatus();

      // Pravila promjene stanja ostaju ista
      if (trenutnoStanje.equals("Z")) {
        return novoStanje.equals("T");
      }
      if (trenutnoStanje.equals("T")) {
        return novoStanje.equals("I");
      }
      return true;
    }

    // Za nove relacije s jednim kolosijekom
    if (brojKolosijeka == 1) {
      if (novoStanje.equals("K") || novoStanje.equals("Z")) {
        // Provjeri preklapanje u oba smjera
        String obrnutiKljuc = zavrsnaStanica + "-" + pocetnaStanica;
        return !postojiPreklapanje(pocetnaStanica, zavrsnaStanica)
            && !relacije.containsKey(obrnutiKljuc);
      }
    }
    // Za nove relacije s dva kolosijeka
    else if (brojKolosijeka == 2) {
      if (novoStanje.equals("K") || novoStanje.equals("Z")) {
        // Provjeri preklapanje samo u zadanom smjeru
        String obrnutiKljuc = zavrsnaStanica + "-" + pocetnaStanica;
        RelacijaPrugeContext obrnutaRelacija = relacije.get(obrnutiKljuc);

        // Dozvoli ako nema preklapanja ili ako postoji samo u suprotnom smjeru
        if (!postojiPreklapanje(pocetnaStanica, zavrsnaStanica)) {
          return true;
        }
        // Dozvoli preklapanje ako je u suprotnom smjeru
        if (obrnutaRelacija != null
            && !obrnutaRelacija.getTrenutnoStanje().getStatus().equals("I")) {
          return false;
        }
        return true;
      }
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
