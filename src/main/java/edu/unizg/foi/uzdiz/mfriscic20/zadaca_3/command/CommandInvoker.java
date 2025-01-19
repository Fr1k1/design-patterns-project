package edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.command;

public class CommandInvoker {
  private Command command;

  public void setCommand(Command command) {
    this.command = command;
  }

  public void executeCommand() {
    command.execute(); // trazi od komande da provede zahtjev
  }
}
