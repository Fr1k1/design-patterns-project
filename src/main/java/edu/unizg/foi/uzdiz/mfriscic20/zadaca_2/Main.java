package edu.unizg.foi.uzdiz.mfriscic20.zadaca_2;

import java.util.HashMap;
import java.util.Map;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.csvcitacfactory.CsvCitacCreator;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.csvcitacfactory.KompozicijeCsvCitacCreator;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.csvcitacfactory.OznakaDanaCsvCitacCreator;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.csvcitacfactory.StaniceCsvCitacCreator;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.csvcitacfactory.VozilaCsvCitacCreator;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.csvcitacfactory.VozniRedCsvCitacCreator;

public class Main {

  private static final String[] REDOSLIJED_ZASTAVICA = {"--zs", "--zps", "--zk", "--zvr", "--zod"};

  public static void main(String[] args) {
    Map<String, String> mapaArgumenata = new HashMap<>();

    if (args.length != 10) {
      System.out.println("Broj argumenata nije ispravan.");
      return;
    }

    for (int i = 0; i < args.length; i += 2) {
      String zastavica = args[i];
      String vrijednost = args[i + 1];

      if (dohvatiTvornicu(zastavica) == null) {
        return;
      }

      mapaArgumenata.put(zastavica, vrijednost);
    }

    for (String zastavica : REDOSLIJED_ZASTAVICA) {
      if (!mapaArgumenata.containsKey(zastavica)) {
        System.out.println("Nedostaje zastavica: " + zastavica);
        return;
      }
    }

    mapirajObjekteRedom(mapaArgumenata);
    SustavPrijevozPutnikaIRobe.dohvatiInstancu().zapocniRad();
  }


  private static void mapirajObjekteRedom(Map<String, String> mapaArgumenata) {
    for (String zastavica : REDOSLIJED_ZASTAVICA) {
      CsvCitacCreator factory = dohvatiTvornicu(zastavica);
      if (factory != null) {
        try {
          factory.procesirajDatoteku(mapaArgumenata.get(zastavica));
        } catch (Exception e) {
          System.err.println("Greška pri čitanju datoteke " + mapaArgumenata.get(zastavica) + ": "
              + e.getMessage());
        }
      }
    }
  }

  private static CsvCitacCreator dohvatiTvornicu(String zastavica) {
    CsvCitacCreator factory = null;

    switch (zastavica) {
      case "--zs":
        factory = new StaniceCsvCitacCreator();
        break;
      case "--zps":
        factory = new VozilaCsvCitacCreator();
        break;
      case "--zk":
        factory = new KompozicijeCsvCitacCreator();
        break;
      case "--zvr":
        factory = new VozniRedCsvCitacCreator();
        break;
      case "--zod":
        factory = new OznakaDanaCsvCitacCreator();
        break;
      default:
        System.err.println("Nepoznata zastavica: " + zastavica);
        break;
    }

    return factory;
  }

}
