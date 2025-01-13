package edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.mediator;

public class KorisnikProvjeriteljPopunjenostiConcreteColleague
    implements AbstractColleagueKorisnik {
  private SustavPopunjenostiVlakovaMediator mediator;

  public KorisnikProvjeriteljPopunjenostiConcreteColleague(
      SustavPopunjenostiVlakovaMediator mediator) {
    this.mediator = mediator;
  }

  public int provjeriPopunjenost(String vlak) {
    return mediator.provjeriPopunjenost(this, vlak);
  }

  @Override
  public void primiObavijest(String message) {
    System.out.println(message);
  }
}
