package edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.visitor;

import edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.composite.EtapaLeaf;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.composite.PomocniBazniComposite;

public interface VozniRedVisitor {

  void posjetiElement(PomocniBazniComposite kompozit);

  void posjetiElement(EtapaLeaf etapa);

}
