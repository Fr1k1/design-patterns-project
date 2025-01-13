package edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.dto;

public class Stanica {

  private String stanica;
  private String oznakaPruge;
  private String vrstaStanice;
  private String statusStanice;
  private String putniciUlazeIIzlaze;
  private String robaUtovarIstovar;
  private String kategorija;
  private Integer brojPerona;
  private String vrstaPruge;
  private Integer brojKolosjeka;
  private Double doPoOsovini;
  private Double doPoDuznomM;
  private String statusPruge;
  private Integer duzina;
  private Integer vrijemeNormalniVlak;
  private Integer vrijemeUbrzaniVlak;
  private Integer vrijemeBrziVlak;

  public Stanica(String stanica, String oznakaPruge, String vrstaStanice, String statusStanice,
      String putniciUlazeIIzlaze, String robaUtovarIstovar, String kategorija, Integer brojPerona,
      String vrstaPruge, Integer brojKolosjeka, Double doPoOsovini, Double doPoDuznomM,
      String statusPruge, Integer duzina, Integer vrijemeNormalniVlak, Integer vrijemeUbrzaniVlak,
      Integer vrijemeBrziVlak) {
    super();
    this.stanica = stanica;
    this.oznakaPruge = oznakaPruge;
    this.vrstaStanice = vrstaStanice;
    this.statusStanice = statusStanice;
    this.putniciUlazeIIzlaze = putniciUlazeIIzlaze;
    this.robaUtovarIstovar = robaUtovarIstovar;
    this.kategorija = kategorija;
    this.brojPerona = brojPerona;
    this.vrstaPruge = vrstaPruge;
    this.brojKolosjeka = brojKolosjeka;
    this.doPoOsovini = doPoOsovini;
    this.doPoDuznomM = doPoDuznomM;
    this.statusPruge = statusPruge;
    this.duzina = duzina;
    this.vrijemeNormalniVlak = vrijemeNormalniVlak;
    this.vrijemeUbrzaniVlak = vrijemeUbrzaniVlak;
    this.vrijemeBrziVlak = vrijemeBrziVlak;
  }

  public Integer getVrijemeNormalniVlak() {
    return vrijemeNormalniVlak;
  }

  public void setVrijemeNormalniVlak(Integer vrijemeNormalniVlak) {
    this.vrijemeNormalniVlak = vrijemeNormalniVlak;
  }

  public Integer getVrijemeUbrzaniVlak() {
    return vrijemeUbrzaniVlak;
  }

  public void setVrijemeUbrzaniVlak(Integer vrijemeUbrzaniVlak) {
    this.vrijemeUbrzaniVlak = vrijemeUbrzaniVlak;
  }

  public Integer getVrijemeBrziVlak() {
    return vrijemeBrziVlak;
  }

  public void setVrijemeBrziVlak(Integer vrijemeBrziVlak) {
    this.vrijemeBrziVlak = vrijemeBrziVlak;
  }

  public String getStanica() {
    return stanica;
  }

  public void setStanica(String stanica) {
    this.stanica = stanica;
  }

  public String getOznakaPruge() {
    return oznakaPruge;
  }

  public void setOznakaPruge(String oznakaPruge) {
    this.oznakaPruge = oznakaPruge;
  }

  public String getVrstaStanice() {
    return vrstaStanice;
  }

  public void setVrstaStanice(String vrstaStanice) {
    this.vrstaStanice = vrstaStanice;
  }

  public String getStatusStanice() {
    return statusStanice;
  }

  public void setStatusStanice(String statusStanice) {
    this.statusStanice = statusStanice;
  }

  public String getPutniciUlazeIIzlaze() {
    return putniciUlazeIIzlaze;
  }

  public void setPutniciUlazeIIzlaze(String putniciUlazeIIzlaze) {
    this.putniciUlazeIIzlaze = putniciUlazeIIzlaze;
  }

  public String getKategorija() {
    return kategorija;
  }

  public void setKategorija(String kategorija) {
    this.kategorija = kategorija;
  }

  public Integer getBrojPerona() {
    return brojPerona;
  }

  public void setBrojPerona(Integer brojPerona) {
    this.brojPerona = brojPerona;
  }

  public String getVrstaPruge() {
    return vrstaPruge;
  }

  public void setVrstaPruge(String vrstaPruge) {
    this.vrstaPruge = vrstaPruge;
  }

  public Integer getBrojKolosjeka() {
    return brojKolosjeka;
  }

  public void setBrojKolosjeka(Integer brojKolosjeka) {
    this.brojKolosjeka = brojKolosjeka;
  }

  public Double getDoPoOsovini() {
    return doPoOsovini;
  }

  public void setDoPoOsovini(Double doPoOsovini) {
    this.doPoOsovini = doPoOsovini;
  }

  public Double getDoPoDuznomM() {
    return doPoDuznomM;
  }

  public void setDoPoDuznomM(Double doPoDuznomM) {
    this.doPoDuznomM = doPoDuznomM;
  }

  public String getStatusPruge() {
    return statusPruge;
  }

  public void setStatusPruge(String statusPruge) {
    this.statusPruge = statusPruge;
  }

  public Integer getDuzina() {
    return duzina;
  }

  public void setDuzina(Integer duzina) {
    this.duzina = duzina;
  }

  public String getRobaUtovarIstovar() {
    return robaUtovarIstovar;
  }

  public void setRobaUtovarIstovar(String robaUtovarIstovar) {
    this.robaUtovarIstovar = robaUtovarIstovar;
  }

}
