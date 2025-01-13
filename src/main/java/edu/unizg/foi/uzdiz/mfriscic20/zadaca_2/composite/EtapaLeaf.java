package edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.composite;

import java.util.ArrayList;
import java.util.List;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.SustavPrijevozPutnikaIRobe;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.dto.OznakaDana;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.dto.Pruga;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.dto.Stanica;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.visitor.VozniRedVisitor;

public class EtapaLeaf extends VozniRedComponent {
  private String oznakaPruge;
  private String oznakaVlaka;
  private String vrstaVlaka;
  private String polaznaStanicaEtape;
  private String odredisnaStanicaEtape;
  private int trajanjeVoznjeUMinutama;
  private int vrijemePolaskaUMinutama;
  private int vrijemeDolaskaUMinutama;
  private int udaljenost;
  private String smjer;
  private String oznakaDana;
  private List<Stanica> staniceEtape;

  public EtapaLeaf(String oznakaPruge, String oznakaVlaka, String vrstaVlaka,
      String polaznaStanicaEtape, String odredisnaStanicaEtape, int trajanjeVoznjeUMinutama,
      int vrijemePolaskaUMinutama, int vrijemeDolaskaUMinutama, int udaljenost, String smjer,
      String oznakaDana) {
    this.oznakaPruge = oznakaPruge;
    this.oznakaVlaka = oznakaVlaka;
    this.vrstaVlaka = vrstaVlaka;
    this.polaznaStanicaEtape = polaznaStanicaEtape;
    this.odredisnaStanicaEtape = odredisnaStanicaEtape;
    this.trajanjeVoznjeUMinutama = trajanjeVoznjeUMinutama;
    this.vrijemePolaskaUMinutama = vrijemePolaskaUMinutama;
    this.vrijemeDolaskaUMinutama = vrijemeDolaskaUMinutama;
    this.udaljenost = udaljenost;
    this.smjer = smjer;
    this.oznakaDana = oznakaDana;
    this.staniceEtape = dohvatiStaniceEtape();
  }

  private List<Stanica> dohvatiStaniceEtape() {
    Pruga pruga = SustavPrijevozPutnikaIRobe.dohvatiInstancu().dohvatiPrugu(oznakaPruge);
    if (pruga != null) {
      return pruga.getStaniceIzmedu(polaznaStanicaEtape, odredisnaStanicaEtape);
    }
    return new ArrayList<>();
  }


  @Override
  public void azuriraj(String message) {
    System.out.println("Etapa " + this.oznakaPruge + " (" + this.polaznaStanicaEtape + " -> "
        + this.odredisnaStanicaEtape + ") primila obavijest: " + message);
  }

  public int izracunajUdaljenost() {
    if (staniceEtape.isEmpty())
      return 0;

    int udaljenost = 0;
    for (int i = 0; i < staniceEtape.size() - 1; i++) {
      udaljenost += staniceEtape.get(i + 1).getDuzina();
    }
    return udaljenost;
  }

  public String formatirajVrijeme(int minuteOdPonoci) {
    int sati = minuteOdPonoci / 60;
    int minute = minuteOdPonoci % 60;
    return String.format("%02d:%02d", sati, minute);
  }

  @Override
  public void prikaziDetalje() {
    System.out.println("Etapa vlaka " + oznakaVlaka + " na pruzi " + oznakaPruge);
    System.out.println("Od: " + polaznaStanicaEtape);
    System.out.println("Do: " + odredisnaStanicaEtape);
    System.out.println("Polazak: " + formatirajVrijeme(vrijemePolaskaUMinutama));
    System.out.println("Dolazak: " + formatirajVrijeme(vrijemeDolaskaUMinutama));
    System.out.println("Udaljenost: " + izracunajUdaljenost() + " km");
    System.out.println("Dani vožnje: " + getDaniVoznje());
  }

  @Override
  public void prihvati(VozniRedVisitor visitor) {
    visitor.posjetiElement(this);
  }

  public String getOznakaPruge() {
    return oznakaPruge;
  }

  public void setOznakaPruge(String oznakaPruge) {
    this.oznakaPruge = oznakaPruge;
  }

  public String getOznakaVlaka() {
    return oznakaVlaka;
  }

  public void setOznakaVlaka(String oznakaVlaka) {
    this.oznakaVlaka = oznakaVlaka;
  }

  public String getVrstaVlaka() {
    return vrstaVlaka;
  }

  public void setVrstaVlaka(String vrstaVlaka) {
    this.vrstaVlaka = vrstaVlaka;
  }

  public String getPolaznaStanicaEtape() {
    return polaznaStanicaEtape;
  }

  public void setPolaznaStanicaEtape(String polaznaStanicaEtape) {
    this.polaznaStanicaEtape = polaznaStanicaEtape;
  }

  public String getOdredisnaStanicaEtape() {
    return odredisnaStanicaEtape;
  }

  public void setOdredisnaStanicaEtape(String odredisnaStanicaEtape) {
    this.odredisnaStanicaEtape = odredisnaStanicaEtape;
  }

  public int getTrajanjeVoznjeUMinutama() {
    return trajanjeVoznjeUMinutama;
  }

  public void setTrajanjeVoznjeUMinutama(int trajanjeVoznjeUMinutama) {
    this.trajanjeVoznjeUMinutama = trajanjeVoznjeUMinutama;
  }

  public int getVrijemePolaskaUMinutama() {
    return vrijemePolaskaUMinutama;
  }

  public void setVrijemePolaskaUMinutama(int vrijemePolaskaUMinutama) {
    this.vrijemePolaskaUMinutama = vrijemePolaskaUMinutama;
  }

  public int getVrijemeDolaskaUMinutama() {
    return vrijemeDolaskaUMinutama;
  }

  public void setVrijemeDolaskaUMinutama(int vrijemeDolaskaUMinutama) {
    this.vrijemeDolaskaUMinutama = vrijemeDolaskaUMinutama;
  }

  public String getSmjer() {
    return smjer;
  }

  public void setSmjer(String smjer) {
    this.smjer = smjer;
  }

  public String getOznakaDana() {
    return oznakaDana;
  }

  public void setOznakaDana(String oznakaDana) {
    this.oznakaDana = oznakaDana;
  }

  public List<Stanica> getStaniceEtape() {
    return staniceEtape;
  }

  public int getUdaljenost() {
    return izracunajUdaljenost();
  }

  public void setUdaljenost(int udaljenost) {
    this.udaljenost = udaljenost;
  }


  public String getDaniVoznje() {
    if (oznakaDana == null || oznakaDana.isEmpty()) {
      return "PoUSrČPeSuN"; // u uputama pise da se treba popuniti ako nema oznake
    }

    try {
      int oznaka = Integer.parseInt(oznakaDana);
      OznakaDana oznakaDana =
          SustavPrijevozPutnikaIRobe.dohvatiInstancu().dohvatiOznakuDana(oznaka);

      if (oznakaDana != null) {
        String daniVoznje = oznakaDana.getDaniVoznje();
        return daniVoznje.isEmpty() ? "PoUSrČPeSuN" : daniVoznje;
      }
    } catch (NumberFormatException e) {
      System.out.println("Neispravna oznaka dana: " + oznakaDana);
    }

    return "PoUSrČPeSuN";
  }

  public int izracunajVrijemeZaStanicu(String imeStanice) {
    int ukupnoMinuta = vrijemePolaskaUMinutama;
    int ukupnaUdaljenost = 0;

    for (Stanica stanica : staniceEtape) {
      if (stanica.getStanica().equals(imeStanice)) {
        return ukupnoMinuta + (int) (ukupnaUdaljenost * trajanjeVoznjeUMinutama / getUdaljenost());
      }
      ukupnaUdaljenost += stanica.getDuzina();
    }
    return -1;
  }
}
