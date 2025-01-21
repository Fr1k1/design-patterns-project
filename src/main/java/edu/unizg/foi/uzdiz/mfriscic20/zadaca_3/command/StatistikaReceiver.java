package edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.command;

import java.time.format.DateTimeFormatter;
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

  public void prikaziStatistikuPutovanja(String datum) {
    List<KartaMemento> karte = kartaCaretaker.dohvatiKarteZaDatum(datum);
    int ukupnoKarata = karte.size();
    double ukupanPrihod = 0;
    for (KartaMemento karta : karte) {
      ukupanPrihod += karta.getKonacnaCijena();
    }
    System.out.println("STATISTIKA PUTOVANJA (datum putovanja na karti) ZA " + datum);
    System.out.println("Ukupno karata: " + ukupnoKarata);
    System.out.printf("Ukupan prihod: %.2f eura%n", ukupanPrihod);
  }

  public void prikaziStatistikuKupnje(String datum) {
    List<KartaMemento> sveKarte = kartaCaretaker.dohvatiSveKarte();
    int ukupnoKarata = 0;
    double ukupanPrihod = 0;

    for (KartaMemento karta : sveKarte) {
      if (karta.getVrijemeKupnje().format(DateTimeFormatter.ofPattern("dd.MM.yyyy."))
          .equals(datum)) {
        ukupnoKarata++;
        ukupanPrihod += karta.getKonacnaCijena();
      }
    }

    System.out.println("STATISTIKA KUPNJE (datum kupnje na karti) ZA " + datum);
    System.out.println("Ukupno karata: " + ukupnoKarata);
    System.out.printf("Ukupan prihod: %.2f eura%n", ukupanPrihod);
  }
}

