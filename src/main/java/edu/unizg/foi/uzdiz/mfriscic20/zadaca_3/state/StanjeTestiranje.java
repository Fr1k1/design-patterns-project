package edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.state;

public class StanjeTestiranje implements StanjePrugeState {


  // Context može proslijediti sebe kao argument za State objekt koji rukovodi zahtjevom. To dopušta
  // da
  // State objekt pristupa do Context-a ako je potrebno
  @Override
  public void handle(RelacijaPrugeContext relacija) {
    relacija.setTrenutnoStanje(this);
  }

  @Override
  public String getStatus() {
    return "T";
  }

}
