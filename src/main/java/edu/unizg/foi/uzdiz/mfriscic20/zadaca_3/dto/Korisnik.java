package edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.dto;

import edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.composite.VozniRedComponent;

public class Korisnik extends VozniRedComponent {

  private String ime;
  private String prezime;
  private String oznakaVlaka;
  private String pracenaStanica;

  public Korisnik(String ime, String prezime) {
    this.ime = ime;
    this.prezime = prezime;
  }

  // Za DPK komandu
  public void dodajPracenje(String oznakaVlaka, String pracenaStanica) {
    this.oznakaVlaka = oznakaVlaka;
    this.pracenaStanica = pracenaStanica;
  }

  public String getIme() {
    return ime;
  }

  public void setIme(String ime) {
    this.ime = ime;
  }

  public String getPrezime() {
    return prezime;
  }

  public void setPrezime(String prezime) {
    this.prezime = prezime;
  }

  public String getOznakaVlaka() {
    return oznakaVlaka;
  }

  public void setOznakaVlaka(String oznakaVlaka) {
    this.oznakaVlaka = oznakaVlaka;
  }

  public String getPracenaStanica() {
    return pracenaStanica;
  }

  public void setPracenaStanica(String pracenaStanica) {
    this.pracenaStanica = pracenaStanica;
  }

  @Override
  public void azuriraj(String poruka) {
    if (!poruka.contains("Vlak " + oznakaVlaka)) {
      return;
    }

    if (pracenaStanica != null && !poruka.contains(pracenaStanica)) {
      return;
    }

    System.out.println("Obavijest za " + this.ime + " " + this.prezime + " : " + poruka);
  }

  public void prikaziDetalje() {}

}
