package edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.visitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.composite.EtapaLeaf;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.composite.PomocniBazniComposite;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.composite.VlakComposite;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.composite.VozniRedComponent;

public class IspisEtapaPremaDanimaVisitor implements VozniRedVisitor {
  private final String trazeniDani;
  private boolean zaglavljeIspisano = false;
  private Map<String, List<EtapaLeaf>> etapePoVlakovima = new HashMap<>();

  public IspisEtapaPremaDanimaVisitor(String trazeniDani) {
    this.trazeniDani = trazeniDani;
  }

  private void ispisiZaglavlje() {
    if (!zaglavljeIspisano) {
      System.out.printf("%-6s %-8s %-30s %-30s %-8s %-8s %-15s%n", "Vlak", "Pruga", "Polazna st.",
          "Odredišna st.", "Polazak", "Dolazak", "Dani");
      System.out.println("-".repeat(110));
      zaglavljeIspisano = true;
    }
  }

  private boolean provjeriDane(String daniVoznje, String trazeniDani) {
    List<String> trazeniDaniList = new ArrayList<>();
    for (int i = 0; i < trazeniDani.length(); i += 2) {
      if (i + 1 < trazeniDani.length()) {
        trazeniDaniList.add(trazeniDani.substring(i, i + 2));
      }
    }

    return trazeniDaniList.stream().allMatch(dan -> daniVoznje.contains(dan));
  }

  @Override
  public void posjetiElement(PomocniBazniComposite kompozit) {
    if (kompozit instanceof VlakComposite) {
      VlakComposite vlak = (VlakComposite) kompozit;
      List<EtapaLeaf> etapeVlaka = new ArrayList<>();

      for (VozniRedComponent dijete : vlak.svaDjeca) {
        if (dijete instanceof EtapaLeaf) {
          etapeVlaka.add((EtapaLeaf) dijete);
        }
      }

      if (!etapeVlaka.isEmpty()) {
        etapePoVlakovima.put(vlak.getOznakaVlaka(), etapeVlaka);
      }
    }
  }

  @Override
  public void posjetiElement(EtapaLeaf etapa) {
    // Ne trebam implementaciju jer obrađujem na razini vlaka
  }

  private String pretvoriMinuteUVrijeme(int minute) {
    return String.format("%02d:%02d", minute / 60, minute % 60);
  }

  // radi poruke s foruma

  private int getNajranijeVrijemePolaska(List<EtapaLeaf> etape) {
    if (etape.isEmpty()) {
      return 0;
    }

    int najranijeVrijeme = etape.get(0).getVrijemePolaskaUMinutama();
    for (EtapaLeaf etapa : etape) {
      if (etapa.getVrijemePolaskaUMinutama() < najranijeVrijeme) {
        najranijeVrijeme = etapa.getVrijemePolaskaUMinutama();
      }
    }
    return najranijeVrijeme;
  }

  public void ispisiEtape() {
    List<Map.Entry<String, List<EtapaLeaf>>> sortedEntries =
        new ArrayList<>(etapePoVlakovima.entrySet());
    sortedEntries.sort((entry1, entry2) -> {
      int vrijemePolaska1 = getNajranijeVrijemePolaska(entry1.getValue());
      int vrijemePolaska2 = getNajranijeVrijemePolaska(entry2.getValue());
      return Integer.compare(vrijemePolaska1, vrijemePolaska2);
    });

    for (Map.Entry<String, List<EtapaLeaf>> entry : sortedEntries) {
      List<EtapaLeaf> etapeVlaka = entry.getValue();
      etapeVlaka.sort((e1, e2) -> Integer.compare(e1.getVrijemePolaskaUMinutama(),
          e2.getVrijemePolaskaUMinutama()));

      boolean sveEtapeVozeUTrazaneDane =
          etapeVlaka.stream().allMatch(etapa -> provjeriDane(etapa.getDaniVoznje(), trazeniDani));

      if (sveEtapeVozeUTrazaneDane) {
        if (!zaglavljeIspisano) {
          ispisiZaglavlje();
        }
        for (EtapaLeaf etapa : etapeVlaka) {
          System.out.printf("%-6s %-8s %-30s %-30s %-8s %-8s %-15s%n", etapa.getOznakaVlaka(),
              etapa.getOznakaPruge(), etapa.getPolaznaStanicaEtape(),
              etapa.getOdredisnaStanicaEtape(),
              pretvoriMinuteUVrijeme(etapa.getVrijemePolaskaUMinutama()),
              pretvoriMinuteUVrijeme(etapa.getVrijemeDolaskaUMinutama()), etapa.getDaniVoznje());
        }
        System.out.println("-".repeat(110));
      }
    }

    if (!zaglavljeIspisano) {
      System.out.println("Nema vlakova koji voze sve etape u dane: " + trazeniDani);
    }
  }

}
