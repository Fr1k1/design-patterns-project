package edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.memento;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// ova klasa se isto cini dobro...jedino kaj kermek tu koristi object...al ne znam zakaj bi to bilo
// bolje
public class KartaCaretaker {
  private Map<Integer, KartaMemento> spremljeneKarte = new HashMap<>();
  private int brojacKarata = 0;

  public void spremiKartu(KartaMemento memento) {
    brojacKarata++;
    spremljeneKarte.put(brojacKarata, memento);
  }

  public KartaMemento dohvatiKartu(int brojKarte) {
    return spremljeneKarte.get(brojKarte);
  }

  public List<KartaMemento> dohvatiSveKarte() {
    return new ArrayList<>(spremljeneKarte.values());
  }
}
