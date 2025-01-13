package edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.mediator;

public interface SustavPopunjenostiVlakovaMediator {
  void dodajPopunjenost(AbstractColleagueKorisnik korisnik, String vlak, int popunjenost);

  int provjeriPopunjenost(AbstractColleagueKorisnik korisnik, String vlak);
}
