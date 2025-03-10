package edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.command;

public class StatistikaKupovineConcreteCommand implements Command {

  private final StatistikaReceiver receiver; // reference na Receiver..ko primjer 2 iz prezentacija
  private final String datum; // state...postoji na grafu

  public StatistikaKupovineConcreteCommand(StatistikaReceiver receiver, String datum) {
    this.receiver = receiver;
    this.datum = datum;
  }

  @Override
  public void execute() {
    receiver.prikaziStatistikuKupnje(datum); // receiver->Action()
  }

}
