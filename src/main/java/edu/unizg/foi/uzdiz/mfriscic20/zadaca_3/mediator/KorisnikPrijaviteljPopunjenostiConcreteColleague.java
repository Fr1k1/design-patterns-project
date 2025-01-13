package edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.mediator;

public class KorisnikPrijaviteljPopunjenostiConcreteColleague implements AbstractColleagueKorisnik {

  private SustavPopunjenostiVlakovaMediator mediator;

  public KorisnikPrijaviteljPopunjenostiConcreteColleague(
      SustavPopunjenostiVlakovaMediator mediator) {
    this.mediator = mediator;
  }

  public void prijaviPopunjenost(String vlak, int popunjenost) {
    mediator.dodajPopunjenost(this, vlak, popunjenost);
  }

  @Override
  public void primiObavijest(String message) {
    System.out.println(message);
  }
}
