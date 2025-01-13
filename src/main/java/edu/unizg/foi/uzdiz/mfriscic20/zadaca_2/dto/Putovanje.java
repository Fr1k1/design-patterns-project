package edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Putovanje {
  private List<Stanica> stanice;
  private List<Pruga> pruge;

  public Putovanje() {
    this.stanice = new ArrayList<>();
    this.pruge = new ArrayList<>();
  }

  public void dodajStanicu(Stanica stanica) {
    if (stanica != null) {
      stanice.add(stanica);
    }
  }



  public void dodajPrugu(Pruga pruga) {
    if (pruga == null) {
      return;
    }

    if (pruge.isEmpty()) {
      pruge.add(pruga);
      return;
    }

    Pruga zadnjaPruga = pruge.get(pruge.size() - 1);
    if (!zadnjaPruga.equals(pruga)) {
      pruge.add(pruga);
    }
  }

  public List<Stanica> getStanice() {
    return new ArrayList<>(stanice);
  }

  public List<Pruga> getPruge() {
    return new ArrayList<>(pruge);
  }

  public void setPruge(List<Pruga> pruge) {
    this.pruge = pruge;
  }

  public void setStanice(List<Stanica> stanice) {
    this.stanice = stanice;
  }

  public boolean isEmpty() {
    return stanice.isEmpty();
  }

  public Stanica getPocetnaStanica() {
    return stanice.isEmpty() ? null : stanice.get(0);
  }

  public Stanica getZadnjaStanica() {
    return stanice.isEmpty() ? null : stanice.get(stanice.size() - 1);
  }

  public int getBrojStanica() {
    return stanice.size();
  }

  public int getBrojPruga() {
    return pruge.size();
  }

  public Pruga getPrugaZaStanicu(Stanica stanica) {
    for (Pruga pruga : pruge) {
      if (pruga.getStanice().contains(stanica)) {
        return pruga;
      }
    }
    return null;
  }


  public static Putovanje izPrethodnika(Map<Stanica, Stanica> prethodnici,
      Map<Stanica, Pruga> prugePoPrethodnicima, Stanica pocetna, Stanica zavrsna) {
    Putovanje putovanje = new Putovanje();
    if (pocetna == null || zavrsna == null || prethodnici == null || prugePoPrethodnicima == null) {
      return putovanje;
    }

    Stanica trenutna = zavrsna;
    while (trenutna != null) {
      putovanje.dodajStanicu(0, trenutna);
      Pruga pruga = prugePoPrethodnicima.get(trenutna);
      if (pruga != null) {
        putovanje.dodajPrugu(0, pruga);
      }
      trenutna = prethodnici.get(trenutna);
    }

    return putovanje;
  }

  private void dodajStanicu(int index, Stanica stanica) {
    if (stanica != null && index >= 0 && index <= stanice.size()) {
      stanice.add(index, stanica);
    }
  }

  private void dodajPrugu(int index, Pruga pruga) {
    if (pruga != null && index >= 0 && index <= pruge.size()
        && (pruge.isEmpty() || !pruge.contains(pruga))) {
      pruge.add(index, pruga);
    }
  }

  public int getUkupnoKilometara() {
    if (stanice == null || stanice.size() < 2) {
      return 0;
    }

    int ukupno = 0;
    for (int i = 1; i < stanice.size(); i++) {
      ukupno += stanice.get(i).getDuzina();
    }

    return ukupno;
  }


  public int getUdaljenostDoStanice(Stanica cilj) {
    if (stanice.isEmpty() || !stanice.contains(cilj)) {
      return 0;
    }
    boolean brojim = false;
    int udaljenost = 0;


    for (Stanica stanica : stanice) {
      if (!brojim) {
        if (stanica.equals(stanice.get(0))) {
          brojim = true;
          continue;
        }
      } else {
        udaljenost += stanica.getDuzina();
      }

      if (stanica.equals(cilj)) {
        break;
      }
    }

    return udaljenost;
  }


  public Pruga getPrugaZaStanice(Stanica stanica1, Stanica stanica2) {
    for (Pruga pruga : pruge) {
      boolean prvaStanicaNaPruzi = false;
      boolean drugaStanicaNaPruzi = false;

      for (Stanica stanica : pruga.getStanice()) {
        if (stanica.getStanica().equals(stanica1.getStanica())) {
          prvaStanicaNaPruzi = true;
        }
        if (stanica.getStanica().equals(stanica2.getStanica())) {
          drugaStanicaNaPruzi = true;
        }
      }

      if (prvaStanicaNaPruzi && drugaStanicaNaPruzi) {
        return pruga;
      }
    }
    return null;
  }



}
