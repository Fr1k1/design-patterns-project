package edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.composite;

public class VozniRedComposite extends PomocniBazniComposite {

  @Override
  public VozniRedComponent dohvatiDijete(String oznaka) {
    for (VozniRedComponent vlak : svaDjeca) {
      if (vlak instanceof VlakComposite && ((VlakComposite) vlak).getOznakaVlaka().equals(oznaka)) {
        return vlak;
      }
    }
    return null;
  }

  @Override
  public void prikaziDetalje() {
    System.out.println("Detalji voznog reda:");
    for (VozniRedComponent dijete : svaDjeca) {
      dijete.prikaziDetalje();
    }
  }
}
