package edu.unizg.foi.uzdiz.mfriscic20.zadaca_2;

import java.util.ArrayList;
import java.util.List;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.composite.VlakComposite;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.dto.Putovanje;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.dto.Stanica;

public class IspisnikIVI2S {

  private static final char[] DOZVOLJENA_SLOVA = {'S', 'P', 'K', 'V'};


  public void ispisiTablicuVoznogReda(Putovanje put, List<VlakComposite> vlakovi,
      String formatPrikaza) {
    List<Character> stupci = parsirajFormatPrikaza(formatPrikaza);
    ispisiZaglavlje(stupci, vlakovi);
    ispisiTablicuSadrzaja(stupci, put, vlakovi);
  }

  private List<Character> parsirajFormatPrikaza(String format) {
    List<Character> stupci = new ArrayList<>();
    for (char c : format.toCharArray()) {
      for (char dozvoljeni : DOZVOLJENA_SLOVA) {
        if (c == dozvoljeni) {
          stupci.add(c);
          break;
        }
      }
    }
    return stupci;
  }

  private void ispisiZaglavlje(List<Character> stupci, List<VlakComposite> vlakovi) {
    StringBuilder zaglavlje = new StringBuilder();
    int ukupnaSirina = izracunajUkupnuSirinu(stupci, vlakovi);

    for (char stupac : stupci) {
      switch (stupac) {
        case 'S':
          zaglavlje.append(String.format("%-20s", "Stanica"));
          break;
        case 'P':
          zaglavlje.append(String.format("%-10s", "Pruga"));
          break;
        case 'K':
          zaglavlje.append(String.format("%-8s", "Km"));
          break;
        case 'V':
          for (VlakComposite vlak : vlakovi) {
            zaglavlje.append(String.format(" %-8s", vlak.getOznakaVlaka()));
          }
          break;
      }
    }
    System.out.println(zaglavlje);
    System.out.println("-".repeat(ukupnaSirina));
  }

  private int izracunajUkupnuSirinu(List<Character> stupci, List<VlakComposite> vlakovi) {
    int ukupnaSirina = 0;
    for (char stupac : stupci) {
      switch (stupac) {
        case 'S':
          ukupnaSirina += 20;
          break;
        case 'P':
          ukupnaSirina += 10;
          break;
        case 'K':
          ukupnaSirina += 8;
          break;
        case 'V':
          ukupnaSirina += vlakovi.size() * 9;
          break;
      }
    }
    return ukupnaSirina;
  }

  private void ispisiTablicuSadrzaja(List<Character> stupci, Putovanje put,
      List<VlakComposite> vlakovi) {
    int ukupnoKm = 0;
    List<Stanica> stanice = put.getStanice();

    for (int i = 0; i < stanice.size(); i++) {
      Stanica stanica = stanice.get(i);
      if (i > 0) {
        ukupnoKm += stanice.get(i - 1).getDuzina();
      }

      StringBuilder redak = new StringBuilder();
      for (char stupac : stupci) {
        switch (stupac) {
          case 'S':
            redak.append(String.format("%-20s", stanica.getStanica()));
            break;
          case 'P':
            redak.append(String.format("%-10s", stanica.getOznakaPruge()));
            break;
          case 'K':
            redak.append(String.format("%-8d", ukupnoKm));
            break;
          case 'V':
            for (VlakComposite vlak : vlakovi) {
              String vrijeme = vlak.dohvatiVrijemePolaska(stanica);
              redak.append(String.format(" %-8s", vrijeme.isEmpty() ? "-" : vrijeme));
            }
            break;
        }
      }
      System.out.println(redak);
    }
  }

}
