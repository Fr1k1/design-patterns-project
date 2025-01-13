package edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.csvcitacfactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.SustavPrijevozPutnikaIRobe;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.composite.EtapaLeaf;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.composite.VlakComposite;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.composite.VozniRedComponent;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.composite.VozniRedComposite;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.dto.Pruga;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.dto.Stanica;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.visitor.ProvjeraPreklapanjaVremenaVisitor;

public class CsvCitacVoznogRedaProduct extends CsvCitacProduct {

  private static final String[] REGEXI = {"^[A-Z0-9]+$", // oznaka pruge - slova i brojevi
      "^[NO]$", // smjer
      "^.*$", // polazna stanica
      "^.*$", // odredišna stanica
      "^[A-Z0-9 ]+$", "^[UB]?$", "^([01]?[0-9]|2[0-3]):[0-5][0-9]$", // vrijeme polaska - format
                                                                     // HH:mm (24h)
      "^([0-9]+:[0-5][0-9])?$", // trajanje vožnje - format H:mm ili HH:mm
      "^[0-9]*$" // oznaka dana - može biti prazno ili broj
  };

  private static final String[] PORUKE_GRESKE =
      {"Oznaka pruge mora sadržavati samo slova i brojeve", "Smjer može biti samo N ili O",
          "Polazna stanica nije ispravnog formata", "Odredišna stanica nije ispravnog formata",
          "Oznaka vlaka mora biti broj ili slova", "Vrsta vlaka može biti prazno, U ili B",
          "Vrijeme polaska mora biti u formatu HH:mm",
          "Trajanje vožnje mora biti u formatu H:mm ili HH:mm",
          "Oznaka dana mora biti broj ili prazno"};

  private String validateRetka(List<String> podaci) {
    if (podaci.get(0).trim().isEmpty()) {
      return "Oznaka pruge je obavezna";
    } else if (!Pattern.matches(REGEXI[0], podaci.get(0).trim())) {
      return PORUKE_GRESKE[0] + " (dobiveno: " + podaci.get(0) + ")";
    }

    if (podaci.get(1).trim().isEmpty()) {
      return "Smjer je obavezan";
    } else if (!Pattern.matches(REGEXI[1], podaci.get(1).trim())) {
      return PORUKE_GRESKE[1] + " (dobiveno: " + podaci.get(1) + ")";
    }

    if (podaci.get(4).trim().isEmpty()) {
      return "Oznaka vlaka je obavezna";
    } else if (!Pattern.matches(REGEXI[4], podaci.get(4).trim())) {
      return PORUKE_GRESKE[4] + " (dobiveno: " + podaci.get(4) + ")";
    }

    if (podaci.get(6).trim().isEmpty()) {
      return "Vrijeme polaska je obavezno";
    } else if (!Pattern.matches(REGEXI[6], podaci.get(6).trim())) {
      return PORUKE_GRESKE[6] + " (dobiveno: " + podaci.get(6) + ")";
    }

    // Neobavezna polja
    for (int i = 0; i < podaci.size() && i < REGEXI.length; i++) {
      if (i == 0 || i == 1 || i == 4 || i == 6) {
        continue;
      }

      String value = podaci.get(i).trim();
      if (value.isEmpty()) {
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
        if (prviRedak || redak.trim().isEmpty() || redak.startsWith("#")
            || redak.replaceAll(";", "").trim().isEmpty()) {
          prviRedak = false;
          continue;
        }

        try {
          List<String> podaci = Arrays.asList(redak.split(";"));

          if (podaci.size() < 6) {
            obradiGresku(redak, "Neispravan broj stupaca (barem 6 stupaca je potrebno).",
                ++brojGreskeDatoteka);
            continue;
          }

          String validationError = validateRetka(podaci);
          if (validationError != null) {
            obradiGresku(redak, validationError, ++brojGreskeDatoteka);
            continue;
          }

          String[] obradjeniPodaci = obradiPodatke(podaci);
          Pruga pruga =
              SustavPrijevozPutnikaIRobe.dohvatiInstancu().dohvatiPrugu(obradjeniPodaci[0]);

          if (pruga == null) {
            continue;
          }

          spremiUKompozit(obradjeniPodaci);

        } catch (Exception ex) {
          obradiGresku(redak, ex.getMessage() + " kod voznog reda", ++brojGreskeDatoteka);
        }
      }
    } catch (IOException e) {
      System.out.println("Greška pri čitanju datoteke: " + e.getMessage());
      return;
    }

    VozniRedComposite vozniRed = SustavPrijevozPutnikaIRobe.dohvatiInstancu().dohvatiVozniRed();
    ProvjeraPreklapanjaVremenaVisitor visitor = new ProvjeraPreklapanjaVremenaVisitor(vozniRed);
    vozniRed.prihvati(visitor);
    visitor.ukloniNeispravneVlakove();
  }

  private String dohvatiPolaznoOdredisnaStanicu(Pruga pruga, String smjer, boolean polazna) {

    if (pruga == null) {
      return "Nepoznato";
    }
    List<Stanica> stanice = pruga.getStanice();
    if (stanice == null || stanice.isEmpty()) {
      return "Nepoznato";
    }

    String rezultat;
    if (smjer.equals("N")) {
      rezultat =
          polazna ? stanice.get(0).getStanica() : stanice.get(stanice.size() - 1).getStanica();
    } else {
      rezultat =
          polazna ? stanice.get(stanice.size() - 1).getStanica() : stanice.get(0).getStanica();
    }
    return rezultat;
  }

  private String[] obradiPodatke(List<String> podaci) {
    String[] obradjeniPodaci = new String[9];
    String oznakaPruge = podaci.get(0).trim();
    String smjer = podaci.get(1).trim();

    Pruga pruga = SustavPrijevozPutnikaIRobe.dohvatiInstancu().dohvatiPrugu(oznakaPruge);
    obradjeniPodaci[0] = oznakaPruge;
    obradjeniPodaci[1] = smjer;

    boolean polaznaPrazna = !(podaci.size() > 2 && !podaci.get(2).trim().isEmpty());
    boolean polaznaNepoznato = podaci.size() > 2 && podaci.get(2).trim().equals("Nepoznato");
    if (polaznaPrazna || polaznaNepoznato) {
      obradjeniPodaci[2] = dohvatiPolaznoOdredisnaStanicu(pruga, smjer, true);
    } else {
      obradjeniPodaci[2] = podaci.get(2).trim();
    }

    boolean odredisnaPrazna = !(podaci.size() > 3 && !podaci.get(3).trim().isEmpty());
    boolean odredisnaNepoznato = podaci.size() > 3 && podaci.get(3).trim().equals("Nepoznato");

    if (odredisnaPrazna || odredisnaNepoznato) {
      obradjeniPodaci[3] = dohvatiPolaznoOdredisnaStanicu(pruga, smjer, false);
    } else {
      obradjeniPodaci[3] = podaci.get(3).trim();
    }

    obradjeniPodaci[4] = podaci.get(4).trim();
    obradjeniPodaci[5] =
        podaci.size() > 5 && !podaci.get(5).trim().isEmpty() ? podaci.get(5).trim() : "N";
    obradjeniPodaci[6] = podaci.get(6).trim();
    obradjeniPodaci[7] =
        podaci.size() > 7 && !podaci.get(7).trim().isEmpty() ? podaci.get(7).trim() : "0:00";
    obradjeniPodaci[8] =
        podaci.size() > 8 && !podaci.get(8).trim().isEmpty() ? podaci.get(8).trim() : "0";

    return obradjeniPodaci;
  }

  private void spremiUKompozit(String[] obradjeniPodaci) {
    String oznakaVlaka = obradjeniPodaci[4];
    String vrstaVlaka = obradjeniPodaci[5];

    VozniRedComposite vozniRed = SustavPrijevozPutnikaIRobe.dohvatiInstancu().dohvatiVozniRed();
    VozniRedComponent postojeciVlak = vozniRed.dohvatiDijete(oznakaVlaka);
    VlakComposite vlak;

    if (postojeciVlak == null) {
      vlak = new VlakComposite(oznakaVlaka, vrstaVlaka);
      vozniRed.dodaj(vlak);
    } else {
      vlak = (VlakComposite) postojeciVlak;
    }

    int vrijemePolaskaMin = pretvoriVrijemeUMinute(obradjeniPodaci[6]);
    int trajanjeVoznjeMin = pretvoriVrijemeUMinute(obradjeniPodaci[7]);
    int vrijemeDolaskaMin = vrijemePolaskaMin + trajanjeVoznjeMin;

    EtapaLeaf etapa = new EtapaLeaf(obradjeniPodaci[0], oznakaVlaka, vrstaVlaka, obradjeniPodaci[2],
        obradjeniPodaci[3], trajanjeVoznjeMin, vrijemePolaskaMin, vrijemeDolaskaMin, 0, // udaljenost
                                                                                        // (kasnije
                                                                                        // ju
                                                                                        // racunam)
        obradjeniPodaci[1], obradjeniPodaci[8]);

    vlak.dodaj(etapa);



  }

  private int pretvoriVrijemeUMinute(String vrijeme) {
    String[] dijelovi = vrijeme.split(":");
    Integer konvertiranaVrijednost =
        Integer.parseInt(dijelovi[0]) * 60 + Integer.parseInt(dijelovi[1]);
    return konvertiranaVrijednost;
  }

  private void obradiGresku(String redak, String poruka, int brojGreskeDatoteka) {
    SustavPrijevozPutnikaIRobe.dohvatiInstancu().dodajGresku();
    System.out.printf("VOZNI RED DATOTEKA Redni broj pogreške: %d (ukupno: %d)%n",
        brojGreskeDatoteka,
        SustavPrijevozPutnikaIRobe.dohvatiInstancu().dohvatiUkupniBrojGresaka());
    System.out.println("Sadržaj retka: " + redak);
    System.out.println("Opis pogreške: " + poruka);
    System.out.println();
  }
}
