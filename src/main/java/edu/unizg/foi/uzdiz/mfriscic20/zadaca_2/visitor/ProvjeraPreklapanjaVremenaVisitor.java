package edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.visitor;

import java.util.ArrayList;
import java.util.List;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.composite.EtapaLeaf;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.composite.PomocniBazniComposite;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.composite.VlakComposite;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.composite.VozniRedComponent;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.composite.VozniRedComposite;

public class ProvjeraPreklapanjaVremenaVisitor implements VozniRedVisitor {
  private VozniRedComposite vozniRed;
  private List<VlakComposite> vlakoviZaUkloniti = new ArrayList<>();

  public ProvjeraPreklapanjaVremenaVisitor(VozniRedComposite vozniRed) {
    this.vozniRed = vozniRed;
  }

  @Override
  public void posjetiElement(PomocniBazniComposite kompozit) {
    if (kompozit instanceof VlakComposite) {
      VlakComposite vlak = (VlakComposite) kompozit;
      List<VozniRedComponent> etape = new ArrayList<>(vlak.svaDjeca);

      for (int i = 0; i < etape.size() - 1; i++) {
        EtapaLeaf trenutnaEtapa = (EtapaLeaf) etape.get(i);
        EtapaLeaf sljedecaEtapa = (EtapaLeaf) etape.get(i + 1);

        if (trenutnaEtapa.getVrijemeDolaskaUMinutama() > sljedecaEtapa
            .getVrijemePolaskaUMinutama()) {
          // System.out.println("\nDetalji neispravnog vlaka " + vlak.getOznakaVlaka() + ":");
          // System.out.println("Etapa " + (i + 1) + ": " + trenutnaEtapa.getPolaznaStanicaEtape()
          // + " -> " + trenutnaEtapa.getOdredisnaStanicaEtape() + " (Polazak: "
          // + pretvoriMinuteUVrijeme(trenutnaEtapa.getVrijemePolaskaUMinutama()) + ", Dolazak: "
          // + pretvoriMinuteUVrijeme(trenutnaEtapa.getVrijemeDolaskaUMinutama()) + ")");
          // System.out.println("Etapa " + (i + 2) + ": " + sljedecaEtapa.getPolaznaStanicaEtape()
          // + " -> " + sljedecaEtapa.getOdredisnaStanicaEtape() + " (Polazak: "
          // + pretvoriMinuteUVrijeme(sljedecaEtapa.getVrijemePolaskaUMinutama()) + ", Dolazak: "
          // + pretvoriMinuteUVrijeme(sljedecaEtapa.getVrijemeDolaskaUMinutama()) + ")");

          vlakoviZaUkloniti.add(vlak);
          break;
        }
      }
    }
  }

  @Override
  public void posjetiElement(EtapaLeaf etapa) {
    // Nije potrebno implementirati
  }

  private String pretvoriMinuteUVrijeme(int minute) {
    return String.format("%02d:%02d", minute / 60, minute % 60);
  }

  public void ukloniNeispravneVlakove() {
    if (!vlakoviZaUkloniti.isEmpty()) {
      for (VlakComposite vlak : vlakoviZaUkloniti) {
        // System.out.println("Uklonjen vlak: " + vlak.getOznakaVlaka());
        vozniRed.ukloni(vlak);
      }
    }
  }

}
