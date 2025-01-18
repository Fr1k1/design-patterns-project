package edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.state;

public interface StanjePrugeState {

  void handle(RelacijaPrugeContext relacija);

  String getStatus();

}
