package edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.composite;

import java.util.ArrayList;
import java.util.List;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.observer.VlakObserver;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.observer.VlakObserverSubjekt;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.visitor.VozniRedVisitor;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.visitor.VozniRedVisitorElement;

public abstract class VozniRedComponent
    implements VozniRedVisitorElement, VlakObserver, VlakObserverSubjekt {

  protected List<VlakObserver> observers = new ArrayList<>();

  public void attachObserver(VlakObserver vlakObserver) {
    this.observers.add(vlakObserver);
  }

  public void detachObserver(VlakObserver vlakObserver) {
    this.observers.remove(vlakObserver);
  }

  public void notifyObservers(String poruka) {
    for (VlakObserver c : this.observers) {
      c.azuriraj(poruka);
    }
  }

  public abstract void prikaziDetalje();

  public boolean dodaj(VozniRedComponent component) {
    if (!(this instanceof VozniRedComponent)) {
      return false;
    }
    return this.dodaj(component);
  }

  public boolean ukloni(VozniRedComponent component) {
    if (!(this instanceof VozniRedComponent)) {
      return false;
    }
    return this.ukloni(component);
  }

  public VozniRedComponent dohvatiDijete(int i) {
    if (!(this instanceof VlakComposite)) {
      return null;
    }
    return this.dohvatiDijete(i);
  }

  public VozniRedComponent dohvatiDijete(String i) {
    if (!(this instanceof VlakComposite)) {
      return null;
    }
    return this.dohvatiDijete(i);
  }

  @Override
  public void prihvati(VozniRedVisitor visitor) {
    if (this instanceof PomocniBazniComposite) {
      visitor.posjetiElement((PomocniBazniComposite) this);
    } else if (this instanceof EtapaLeaf) {
      visitor.posjetiElement((EtapaLeaf) this);
    }

  }
}

