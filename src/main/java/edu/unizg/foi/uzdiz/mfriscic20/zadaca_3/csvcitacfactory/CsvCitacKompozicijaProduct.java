
package edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.csvcitacfactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.SustavPrijevozPutnikaIRobe;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.dataparser.DataParser;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.dto.Kompozicija;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.vozilobuilder.Vozilo;

public class CsvCitacKompozicijaProduct extends CsvCitacProduct {
  DataParser parser = new DataParser();

  private String validateRetka(List<String> podaci) {
    for (int i = 0; i < REGEXI.length; i++) {
      if (!Pattern.matches(REGEXI[i], podaci.get(i))) {
        return PORUKE_GRESKE[i] + " (dobiveno: " + podaci.get(i) + ")";
      }
    }
    return null;
  }


  private static final String[] REGEXI = {"^\\d+$", "^[^;]+$", "^[PV]$"};

  private static final String[] PORUKE_GRESKE = {"Neispravan format oznake kompozicije",
      "Oznaka prijevoznog sredstva mora biti tekst i ne smije biti prazna",
      "Uloga mora biti P ili V"};

  public void ucitajPodatke(String putanja) {
    boolean prviRedak = true;
    int brojGreskeUDatoteci = 1;
    Map<Integer, String> zadnjiRetciKompozicija = new HashMap<>();
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
            if (podaci.size() != 3) {
              evidentirajGresku(redak, "Neispravan broj stupaca na kompozicijama",
                  brojGreskeUDatoteci++);
              continue;
            }
            if (!validirajPodatke(redak, podaci, brojGreskeUDatoteci)) {
              brojGreskeUDatoteci++;
              continue;
            }
            String pogreskaValidacije = validateRetka(podaci);
            if (pogreskaValidacije != null) {
              evidentirajGresku(redak, pogreskaValidacije, brojGreskeUDatoteci++);
              continue;
            }
            int oznaka = parser.tryParseInteger(podaci.get(0));
            String oznakaPrijevoznogSredstva = podaci.get(1);
            String uloga = podaci.get(2);
            Vozilo vozilo = pronadiVozilo(oznakaPrijevoznogSredstva, redak, brojGreskeUDatoteci);
            if (vozilo == null) {
              continue;
            }
            Kompozicija kompozicija =
                dohvatiIliKreirajKompoziciju(oznaka, vozilo, uloga, redak, zadnjiRetciKompozicija);
          } catch (Exception ex) {
            evidentirajGresku(redak, ex.getMessage(), brojGreskeUDatoteci++);
          }
        }
        prviRedak = false;
      }
      provjeriIUkloniNevalidneKompozicije(zadnjiRetciKompozicija, brojGreskeUDatoteci);
    } catch (IOException e) {
      System.out.println("Greška pri čitanju datoteke: " + e.getMessage());
    }
  }

  private boolean validirajPodatke(String redak, List<String> podaci, int brojGreskeUDatoteci) {
    final Pattern regexBrojevi = Pattern.compile("^\\d+$");
    if (!regexBrojevi.matcher(podaci.get(0).trim()).matches()) {
      evidentirajGresku(redak, "Neispravan format oznake kompozicije", brojGreskeUDatoteci);
      return false;
    }

    final Pattern regexUloga = Pattern.compile("[PV]$");
    if (!regexUloga.matcher(podaci.get(2).trim()).matches()) {
      evidentirajGresku(redak, "Neispravan format uloge", brojGreskeUDatoteci);
      return false;
    }

    return true;
  }

  private void evidentirajGresku(String redak, String poruka, int brojGreskeUDatoteci) {
    SustavPrijevozPutnikaIRobe.dohvatiInstancu().dodajGresku();
    System.out.printf("KOMPOZICIJE DATOTEKA: Redni broj pogreške: %d (ukupno: %d)%n",
        brojGreskeUDatoteci,
        SustavPrijevozPutnikaIRobe.dohvatiInstancu().dohvatiUkupniBrojGresaka());
    System.out.println("Sadržaj retka: " + redak);
    System.out.println("Opis pogreške: " + poruka);
    System.out.println();
  }

  private Vozilo pronadiVozilo(String oznakaPrijevoznogSredstva, String redak,
      int brojGreskeUDatoteci) {
    for (Vozilo v : SustavPrijevozPutnikaIRobe.dohvatiInstancu().dohvatiVozila()) {
      if (v.getOznaka().equals(oznakaPrijevoznogSredstva)) {
        return v;
      }
    }
    evidentirajGresku(redak, "Ne postoji vozilo s oznakom: " + oznakaPrijevoznogSredstva,
        brojGreskeUDatoteci++);
    return null;
  }

  private Kompozicija dohvatiIliKreirajKompoziciju(int oznaka, Vozilo vozilo, String uloga,
      String redak, Map<Integer, String> zadnjiRetciKompozicija) {

    Kompozicija kompozicija = SustavPrijevozPutnikaIRobe.dohvatiInstancu().dohvatiKompozicije()
        .stream().filter(k -> k.getOznaka() == oznaka).findFirst().orElse(null);

    if (kompozicija == null) {
      kompozicija = new Kompozicija(oznaka);
      SustavPrijevozPutnikaIRobe.dohvatiInstancu().unesiKompoziciju(kompozicija);
    }
    kompozicija.dodajVozilo(vozilo, uloga);
    zadnjiRetciKompozicija.put(oznaka, redak);

    return kompozicija;
  }

  private void provjeriIUkloniNevalidneKompozicije(Map<Integer, String> zadnjiRetciKompozicija,
      int brojGreskeUDatoteci) {

    List<Kompozicija> nevalidneKompozicije = new ArrayList<>();

    for (Kompozicija kompozicija : SustavPrijevozPutnikaIRobe.dohvatiInstancu()
        .dohvatiKompozicije()) {
      if (!kompozicija.kompozicijaJeValidna()) {
        String zadnjiRedak = zadnjiRetciKompozicija.get(kompozicija.getOznaka());
        evidentirajGresku(zadnjiRedak,
            "Kompozicija nije ispravna - nedostaje lokomotiva ili vagoni", brojGreskeUDatoteci++);
        nevalidneKompozicije.add(kompozicija);
      }
    }

    for (Kompozicija nevalidna : nevalidneKompozicije) {
      SustavPrijevozPutnikaIRobe.dohvatiInstancu().dohvatiKompozicije().remove(nevalidna);
    }
  }

}


