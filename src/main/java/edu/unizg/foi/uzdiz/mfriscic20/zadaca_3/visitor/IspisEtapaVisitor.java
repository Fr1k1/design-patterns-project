package edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.visitor;

import edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.composite.EtapaLeaf;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.composite.PomocniBazniComposite;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.composite.VlakComposite;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.composite.VozniRedComponent;

public class IspisEtapaVisitor implements VozniRedVisitor {
  private String oznakaTrazenogVlaka;
  private boolean vlakPronadjen = false;
  private boolean zaglavljeIspisano = false;


  public IspisEtapaVisitor(String oznakaVlaka) {
    this.oznakaTrazenogVlaka = oznakaVlaka;
  }

  private void ispisiZaglavlje() {
    if (!zaglavljeIspisano) {
      System.out.printf("%-6s %-8s %-30s %-30s %-8s %-8s %-5s %-15s%n", "Vlak", "Pruga",
          "Polazna st.", "Odredišna st.", "Polazak", "Dolazak", "Km", "Dani");
      System.out.println("-".repeat(110));
      zaglavljeIspisano = true;
    }
  }

  @Override
  public void posjetiElement(PomocniBazniComposite kompozit) {
    if (kompozit instanceof VlakComposite) {
      VlakComposite vlak = (VlakComposite) kompozit;
      if (vlak.getOznakaVlaka().equals(oznakaTrazenogVlaka)) {
        vlakPronadjen = true;
        for (VozniRedComponent dijete : vlak.svaDjeca) {
          dijete.prihvati(this);
        }
      }
    }
  }


  @Override
  public void posjetiElement(EtapaLeaf etapa) {
    if (vlakPronadjen) {
      ispisiZaglavlje();
      System.out.printf("%-6s %-8s %-30s %-30s %-8s %-8s %-5d %-15s%n", etapa.getOznakaVlaka(),
          etapa.getOznakaPruge(), etapa.getPolaznaStanicaEtape(), etapa.getOdredisnaStanicaEtape(),
          pretvoriMinuteUVrijeme(etapa.getVrijemePolaskaUMinutama()),
          pretvoriMinuteUVrijeme(etapa.getVrijemeDolaskaUMinutama()), etapa.getUdaljenost(),
          etapa.getDaniVoznje());
    }
  }

  private String pretvoriMinuteUVrijeme(int minute) {
    return String.format("%02d:%02d", minute / 60, minute % 60);
  }
}
