package edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.state;

public class StanjeZatvorena implements StanjePrugeState {

  @Override
  public void handle(RelacijaPrugeContext relacija) {
    relacija.setTrenutnoStanje(this);
  }

  @Override
  public String getStatus() {
    return "Z";
  }

}
