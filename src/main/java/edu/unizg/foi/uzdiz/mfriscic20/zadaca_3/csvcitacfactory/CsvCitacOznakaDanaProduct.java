package edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.csvcitacfactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.SustavPrijevozPutnikaIRobe;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.dto.OznakaDana;

public class CsvCitacOznakaDanaProduct extends CsvCitacProduct {

  private static final String[] REGEXI = {"^\\d+$", "^[^;]+$"};

  private static final String[] PORUKE_GRESKE = {"Oznaka dana mora biti pozitivan broj",
      "Dani voznje mora biti tekst i ne smije biti prazan"};



  private String validateRetka(List<String> podaci) {
    for (int i = 0; i < REGEXI.length; i++) {
      if (!Pattern.matches(REGEXI[i], podaci.get(i))) {
        return PORUKE_GRESKE[i] + " (dobiveno: " + podaci.get(i) + ")";
      }
    }
    return null;
  }

  public void ucitajPodatke(String putanja) {
    boolean prviRedak = true;
    int brojGreskeDatoteka = 0;
    try (BufferedReader citac = new BufferedReader(new FileReader(putanja))) {
      String redak;
      while ((redak = citac.readLine()) != null) {
        if (prviRedak) {
          prviRedak = false;
          continue;
        }
        if (redak.trim().isEmpty() || redak.startsWith("#")
            || redak.replaceAll(";", "").trim().isEmpty()) {
          continue;
        }
        try {
          List<String> podaci = Arrays.asList(redak.split(";", -1)); // -1 zadržava prazne elemente
          if (podaci.size() < 2) {
            obradiGresku(redak, "Neispravan broj stupaca u oznakama dana", ++brojGreskeDatoteka);
            continue;
          }
          if (podaci.get(1).trim().isEmpty()) {
            podaci.set(1, "PoUSrČPeSuN");
          }

          String validationError = validateRetka(podaci);
          if (validationError != null) {
            throw new Exception(validationError);
          }

          Integer oznaka = parser.tryParseInteger(podaci.get(0));
          String dani = podaci.get(1);
          OznakaDana oznakaDana = new OznakaDana(oznaka, dani);
          SustavPrijevozPutnikaIRobe.dohvatiInstancu().unesiOznakuDana(oznakaDana);
        } catch (Exception ex) {
          obradiGresku(redak, ex.getMessage() + " kod oznaka dana", ++brojGreskeDatoteka);
        }
      }
    } catch (IOException e) {
      System.out.println("Greška pri čitanju datoteke: " + e.getMessage());
    }
  }

  private void obradiGresku(String redak, String poruka, int brojGreskeDatoteka) {
    SustavPrijevozPutnikaIRobe.dohvatiInstancu().dodajGresku();
    System.out.printf("OZNAKE DANA DATOTEKA: Redni broj pogreške: %d (ukupno: %d)%n",
        brojGreskeDatoteka,
        SustavPrijevozPutnikaIRobe.dohvatiInstancu().dohvatiUkupniBrojGresaka());
    System.out.println("Sadržaj retka: " + redak);
    System.out.println("Opis pogreške: " + poruka);
    System.out.println();
  }
}
