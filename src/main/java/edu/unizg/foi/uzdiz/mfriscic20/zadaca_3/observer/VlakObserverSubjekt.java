package edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.observer;

public interface VlakObserverSubjekt {

  // pruza sucelje za dodavanje i brisanje observer objekata

  void attachObserver(VlakObserver vlakObserver);

  void detachObserver(VlakObserver vlakObserver);

  void notifyObservers(String poruka);

}
