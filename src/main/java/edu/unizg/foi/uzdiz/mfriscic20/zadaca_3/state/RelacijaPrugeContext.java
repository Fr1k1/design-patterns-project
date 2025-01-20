package edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.state;

public class RelacijaPrugeContext {
  private StanjePrugeState trenutnoStanje;
  private String oznakaPruge;
  private String pocetnaStanica;
  private String zavrsnaStanica;
  private int brojKolosijeka;
  private String smjer;

  public RelacijaPrugeContext(String oznakaPruge, String pocetnaStanica, String zavrsnaStanica,
      int brojKolosijeka, String smjer) {
    this.oznakaPruge = oznakaPruge;
    this.pocetnaStanica = pocetnaStanica;
    this.zavrsnaStanica = zavrsnaStanica;
    this.brojKolosijeka = brojKolosijeka;
    this.smjer = smjer;
    this.trenutnoStanje = new IspravnoStanje(); // ovo se mora promijeniti da se cita iz datoteke
  }

  public void promijeniStanje(String novoStanje) {
    switch (novoStanje) {
      case "I":
        trenutnoStanje = new IspravnoStanje();
        break;
      case "K":
        trenutnoStanje = new StanjeUKvaru();
        break;
      case "T":
        trenutnoStanje = new StanjeTestiranje();
        break;
      case "Z":
        trenutnoStanje = new StanjeZatvorena();
        break;
    }
    trenutnoStanje.handle(this);
  }

  public boolean mozePutovatiVlak() {
    return trenutnoStanje.getStatus().equals("I");
  }

  public StanjePrugeState getTrenutnoStanje() {
    return trenutnoStanje;
  }

  public void setTrenutnoStanje(StanjePrugeState trenutnoStanje) {
    this.trenutnoStanje = trenutnoStanje;
    System.out.println("Relacija " + pocetnaStanica + " - " + zavrsnaStanica
        + " promijenila stanje u: " + trenutnoStanje.getStatus());
  }

  public String getOznakaPruge() {
    return oznakaPruge;
  }

  public String getPocetnaStanica() {
    return pocetnaStanica;
  }

  public String getZavrsnaStanica() {
    return zavrsnaStanica;
  }

  public int getBrojKolosijeka() {
    return brojKolosijeka;
  }

  public String getSmjer() {
    return smjer;
  }
}
