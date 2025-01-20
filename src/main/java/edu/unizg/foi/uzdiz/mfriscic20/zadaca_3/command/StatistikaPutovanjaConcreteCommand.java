package edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.command;

public class StatistikaPutovanjaConcreteCommand implements Command {

  private final StatistikaReceiver receiver;
  private final String datum;

  public StatistikaPutovanjaConcreteCommand(StatistikaReceiver receiver, String datum) {
    this.receiver = receiver;
    this.datum = datum;
  }

  @Override
  public void execute() {
    receiver.prikaziStatistikuPutovanja(datum);
  }

}
