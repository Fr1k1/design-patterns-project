package edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.composite;

import java.util.ArrayList;
import java.util.List;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.visitor.VozniRedVisitor;

public abstract class PomocniBazniComposite extends VozniRedComponent {

  public List<VozniRedComponent> svaDjeca = new ArrayList<>();

  // ovo tu postoji da mi se metode ne ponavljaju iste...vise je do boljeg koda nego do uzorka

  @Override
  public void azuriraj(String poruka) {
    System.out.println("Obavijest: " + poruka);
    for (VozniRedComponent c : svaDjeca) {
      c.azuriraj(poruka);
    }
  }

  @Override
  public boolean dodaj(VozniRedComponent component) {
    this.svaDjeca.add(component);
    return true;
  }

  @Override
  public boolean ukloni(VozniRedComponent component) {
    return svaDjeca.remove(component);
  }

  @Override
  public VozniRedComponent dohvatiDijete(int index) {
    return svaDjeca.get(index);
  }


  @Override
  public void prihvati(VozniRedVisitor visitor) {
    visitor.posjetiElement(this);
    // rekurzivni put kroz djecu
    for (VozniRedComponent dijete : svaDjeca) {
      dijete.prihvati(visitor);
    }

  }
}


