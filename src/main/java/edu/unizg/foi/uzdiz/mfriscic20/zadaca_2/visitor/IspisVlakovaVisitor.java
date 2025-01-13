package edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.visitor;

import java.util.ArrayList;
import java.util.List;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.composite.EtapaLeaf;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.composite.PomocniBazniComposite;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.composite.VlakComposite;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.composite.VozniRedComponent;

public class IspisVlakovaVisitor implements VozniRedVisitor {
  private boolean zaglavljeIspisano = false;

  private void ispisiZaglavlje() {
    if (!zaglavljeIspisano) {
      System.out.printf("%-6s %-30s %-30s %-8s %-8s %-5s%n", "Vlak", "Polazna st.", "Odredišna st.",
          "Polazak", "Dolazak", "Km");
      System.out.println("-".repeat(90));
      zaglavljeIspisano = true;
    }
  }

  @Override
  public void posjetiElement(PomocniBazniComposite kompozit) {
    if (kompozit instanceof VlakComposite) {
      VlakComposite vlak = (VlakComposite) kompozit;
      if (!vlak.svaDjeca.isEmpty()) {
        ispisiZaglavlje();

        List<EtapaLeaf> etape = new ArrayList<>();
        for (VozniRedComponent dijete : vlak.svaDjeca) {
          if (dijete instanceof EtapaLeaf) {
            etape.add((EtapaLeaf) dijete);
          }
        }
        etape.sort((e1, e2) -> Integer.compare(e1.getVrijemePolaskaUMinutama(),
            e2.getVrijemePolaskaUMinutama()));

        EtapaLeaf prvaEtapa = etape.get(0);
        EtapaLeaf zadnjaEtapa = etape.get(etape.size() - 1);

        int ukupnaUdaljenost = 0;
        for (EtapaLeaf etapa : etape) {
          ukupnaUdaljenost += etapa.getUdaljenost();
        }

        System.out.printf("%-6s %-30s %-30s %-8s %-8s %-5d%n", vlak.getOznakaVlaka(),
            prvaEtapa.getPolaznaStanicaEtape(), zadnjaEtapa.getOdredisnaStanicaEtape(),
            pretvoriMinuteUVrijeme(prvaEtapa.getVrijemePolaskaUMinutama()),
            pretvoriMinuteUVrijeme(zadnjaEtapa.getVrijemeDolaskaUMinutama()), ukupnaUdaljenost);
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
}
