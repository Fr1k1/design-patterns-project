package edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.csvcitacfactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.SustavPrijevozPutnikaIRobe;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.dto.Pruga;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.dto.Stanica;

public class CsvCitacStanicaProduct extends CsvCitacProduct {

  private static final String[] REGEXI = {"^[^;]+$", // naziv stanice
      "^[A-Z]\\d+$", // oznaka pruge
      "^(kol\\.|staj\\.)$", // vrsta stanice
      "^[OZ]$", // status stanice
      "^(DA|NE)$", // putnici ul/iz
      "^(DA|NE)$", // roba ut/ist
      "^[MLR]$", // kategorija pruge
      "^\\d+$", // broj perona
      "^[KE]$", // vrsta pruge
      "^\\d+$", // broj kolosijeka
      "^\\d+([,.]\\d+)?$", // DO po osovini - može biti i točka i zarez
      "^\\d+([,.]\\d+)?$", // DO po duznom m - može biti i točka i zarez
      "^[IKZT]$", // status pruge
      "^\\d*$", // duzina
      "^\\d*$", // vrijeme normalni vlak
      "^\\d*$", // vrijeme ubrzani vlak
      "^\\d*$" // vrijeme brzi vlak
  };

  private static final String[] PORUKE_GRESKE =
      {"Naziv stanice mora biti tekst i ne smije biti prazna",
          "Oznaka pruge mora biti veliko slovo i imati brojeve",
          "Vrsta stanice mora biti kol., staj.", "Status stanice mora biti O ili Z",
          "Putnici ul/iz mora biti DA ili NE", "Roba ut/ist mora biti DA ili NE",
          "Kategorija pruge mora biti M, L ili R", "Broj perona mora biti pozitivan broj",
          "Vrsta pruge mora biti K ili E", "Broj kolosijeka mora biti pozitivan broj",
          "DO po osovini mora biti decimalni broj", "DO po duznom m mora biti decimalni broj",
          "Status pruge mora biti I, K, Z ili T", "Duzina mora biti pozitivan broj",
          "Vrijeme normalnog vlaka mora biti pozitivan broj ili prazno",
          "Vrijeme ubrzanog vlaka mora biti pozitivan broj ili prazno",
          "Vrijeme brzog vlaka mora biti pozitivan broj ili prazno"};

  private String validateRetka(List<String> podaci) {
    for (int i = 0; i < REGEXI.length; i++) {
      String value = podaci.get(i).trim();
      if (i >= 14 && value.isEmpty()) {
        continue;
      }

      if (!Pattern.matches(REGEXI[i], value)) {
        return PORUKE_GRESKE[i] + " (dobiveno: " + value + ")";
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
        if (!prviRedak) {
          if (redak.trim().isEmpty() || redak.startsWith("#")
              || redak.replaceAll(";", "").trim().isEmpty()) {
            continue;
          }
          try {
            String[] polja = redak.split(";", -1);
            List<String> podaci = Arrays.asList(polja);
            if (podaci.size() == 17) {
              String validationError = validateRetka(podaci);
              if (validationError != null) {
                throw new Exception(validationError);
              }
              Integer vrijemeNormalniVlak =
                  podaci.get(14).trim().isEmpty() ? null : parser.tryParseInteger(podaci.get(14));
              Integer vrijemeUbrzaniVlak =
                  podaci.get(15).trim().isEmpty() ? null : parser.tryParseInteger(podaci.get(15));
              Integer vrijemeBrziVlak =
                  podaci.get(16).trim().isEmpty() ? null : parser.tryParseInteger(podaci.get(16));

              Stanica stanica = new Stanica(podaci.get(0), podaci.get(1), podaci.get(2),
                  podaci.get(3), podaci.get(4), podaci.get(5), podaci.get(6),
                  parser.tryParseInteger(podaci.get(7)), podaci.get(8),
                  parser.tryParseInteger(podaci.get(9)),
                  Double.parseDouble(podaci.get(10).replace(',', '.')),
                  Double.parseDouble(podaci.get(11).replace(',', '.')), podaci.get(12),
                  parser.tryParseInteger(podaci.get(13)), vrijemeNormalniVlak, vrijemeUbrzaniVlak,
                  vrijemeBrziVlak);

              String oznakaPruge = podaci.get(1);
              Pruga pruga = SustavPrijevozPutnikaIRobe.dohvatiInstancu().dohvatiPrugu(oznakaPruge);

              if (pruga == null) {
                pruga = new Pruga(oznakaPruge);
                SustavPrijevozPutnikaIRobe.dohvatiInstancu().unesiPrugu(pruga);
              }

              pruga.dodajStanicu(stanica);
              SustavPrijevozPutnikaIRobe.dohvatiInstancu().unesiStanicu(stanica);

            } else {
              obradiGresku(redak, "Neispravan broj stupaca kod stanica", ++brojGreskeDatoteka);
            }
          } catch (Exception ex) {
            obradiGresku(redak, ex.getMessage() + " kod stanica", ++brojGreskeDatoteka);
          }
        }
        prviRedak = false;
      }
    } catch (IOException e) {
      System.out.println("Greška pri čitanju datoteke: " + e.getMessage());
    }
  }

  private void obradiGresku(String redak, String poruka, int brojGreskeDatoteka) {
    SustavPrijevozPutnikaIRobe.dohvatiInstancu().dodajGresku();
    System.out.printf("STANICE DATOTEKA Redni broj pogreške: %d (ukupno: %d)%n", brojGreskeDatoteka,
        SustavPrijevozPutnikaIRobe.dohvatiInstancu().dohvatiUkupniBrojGresaka());
    System.out.println("Sadržaj retka: " + redak);
    System.out.println("Opis pogreške: " + poruka);
    System.out.println();
  }


}
