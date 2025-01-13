package edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.csvcitacfactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.SustavPrijevozPutnikaIRobe;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.vozilobuilder.Vozilo;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.vozilobuilder.VoziloBuilderImpl;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.vozilobuilder.VoziloDirector;

public class CsvCitacVozilaProduct extends CsvCitacProduct {

  private static final String[] REGEXI =
      {"^[^;]+$", "^[^;]+$", "^[^;]+$", "^\\d+$", "^[^;]+$", "^(N|P|TA|TK|TRS|TTS)$", "^[DEN]$",
          "^\\d+$", "^(-1|\\d+(?:,\\d+)?)$", "^\\d+$", "^\\d+$", "^\\d+$", "^\\d+$", "^\\d+$",
          "^\\d+(?:,\\d+)?$", "^\\d+(?:,\\d+)?$", "^\\d+(?:,\\d+)?$", "^[IK]$"};

  private static final String[] PORUKE_GRESKE = {
      "Oznaka vozila mora biti tekst i ne smije biti prazna",
      "Opis vozila mora biti tekst i ne smije biti prazna",
      "Proizvođač mora biti tekst i ne smije biti prazna", "Godina mora biti broj",
      "Namjena mora biti tekst i ne smije biti prazna",
      "Vrsta prijevoza mora biti N, P, TA, TK, TRS ili TTS", "Vrsta pogona mora biti D, E ili N",
      "Maksimalna brzina mora biti pozitivan cijeli broj",
      "Maksimalna snaga mora biti -1 ili pozitivan decimalni broj",
      "Broj sjedećih mjesta mora biti pozitivan cijeli broj",
      "Broj stajaćih mjesta mora biti pozitivan cijeli broj",
      "Broj bicikala mora biti pozitivan cijeli broj",
      "Broj kreveta mora biti pozitivan cijeli broj",
      "Broj automobila mora biti pozitivan cijeli broj",
      "Nosivost mora biti pozitivan decimalni broj", "Površina mora biti pozitivan decimalni broj",
      "Zapremina mora biti pozitivan decimalni broj", "Status mora biti I ili K"};


  private String validateRetka(List<String> podaci) {
    for (int i = 0; i < REGEXI.length; i++) {
      if (!Pattern.matches(REGEXI[i], podaci.get(i))) {
        return PORUKE_GRESKE[i] + " (dobiveno: " + podaci.get(i) + ")";
      }
    }
    return null;
  }


  private void obradiGresku(String redak, String poruka, int brojGreskeDatoteka) {
    SustavPrijevozPutnikaIRobe.dohvatiInstancu().dodajGresku();
    System.out.printf("VOZILA DATOTEKA Redni broj pogreške: %d (ukupno: %d)%n", brojGreskeDatoteka,
        SustavPrijevozPutnikaIRobe.dohvatiInstancu().dohvatiUkupniBrojGresaka());
    System.out.println("Sadržaj retka: " + redak);
    System.out.println("Opis pogreške: " + poruka);
    System.out.println();
  }

  private void odaberiVrstuBuildera(List<String> podaci) throws Exception {
    VoziloDirector director = new VoziloDirector(new VoziloBuilderImpl());
    Vozilo vozilo = null;

    if (podaci.get(5).equals("N")) {
      vozilo = director.constructLokomotiva(podaci);
    } else if (podaci.get(5).equals("P")) {
      vozilo = director.constructPutnickiVagon(podaci);
    } else if (podaci.get(5).equals("TA")) {
      vozilo = director.constructTeretniVagonZaAutomobile(podaci);
    } else if (podaci.get(5).equals("TK")) {
      vozilo = director.constructTeretniVagonZaKontejnere(podaci);
    } else if (podaci.get(5).equals("TRS")) {
      vozilo = director.constructTeretniVagonZaRobuURasutomStanju(podaci);
    } else if (podaci.get(5).equals("TTS")) {
      vozilo = director.constructTeretniVagonZaRobuUTekucemStanju(podaci);
    } else {
      throw new Exception("Nepoznata vrsta vozila");
    }
    if (vozilo != null) {
      SustavPrijevozPutnikaIRobe.dohvatiInstancu().unesiVozilo(vozilo);
    }
  }

  @Override
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
            List<String> podaci = Arrays.asList(redak.split(";"));
            if (podaci.size() != 18) {
              throw new Exception(
                  "Neispravan broj stupaca (očekivano: 18, dobiveno: " + podaci.size() + ")");
            }

            String validationError = validateRetka(podaci);
            if (validationError != null) {
              throw new Exception(validationError);
            }

            odaberiVrstuBuildera(podaci);

          } catch (Exception ex) {
            brojGreskeDatoteka++;
            obradiGresku(redak, ex.getMessage(), brojGreskeDatoteka);
          }
        }
        prviRedak = false;
      }
    } catch (IOException e) {
      System.out.println("Greška pri čitanju datoteke: " + e.getMessage());
    }
  }
}
