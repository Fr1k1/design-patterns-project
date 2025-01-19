package edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.command;

public class StatistikaKartiConcreteCommand implements Command {

  private final StatistikaReceiver receiver; // reference na Receiver..ko primjer 2 iz prezentacija
  private final String datum; // state...nisam siguran je li ovo dovoljno za state

  public StatistikaKartiConcreteCommand(StatistikaReceiver receiver, String datum) {
    this.receiver = receiver;
    this.datum = datum;
  }

  @Override
  public void execute() {
    receiver.prikaziStatistiku(datum); // receiver->Action()
  }

}
