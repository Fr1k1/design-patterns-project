package edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.command;

import java.util.List;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.memento.KartaCaretaker;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.memento.KartaMemento;

public class StatistikaReceiver {

  private final KartaCaretaker kartaCaretaker;

  public StatistikaReceiver(KartaCaretaker kartaCaretaker) {
    this.kartaCaretaker = kartaCaretaker;

  }

  // zna kako izvrsiti operaciju koja je pridruzena s provedbom zahtjeva i bilo koja klasa moze biti
  // receiver

  public void prikaziStatistiku(String datum) {
    List<KartaMemento> karte = kartaCaretaker.dohvatiKarteZaDatum(datum);
    int ukupnoKarata = karte.size();

    double ukupanPrihod = 0;
    for (KartaMemento karta : karte) {
      ukupanPrihod += karta.getKonacnaCijena();
    }

    System.out.println("STATISTIKA ZA " + datum);
    System.out.println("Ukupno karata: " + ukupnoKarata);
    System.out.println("Ukupan prihod: " + String.format("%.2f €", ukupanPrihod));
  }
}

