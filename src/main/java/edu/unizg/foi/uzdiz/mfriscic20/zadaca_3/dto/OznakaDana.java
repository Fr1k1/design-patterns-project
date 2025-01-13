package edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.dto;

public class OznakaDana {

  private Integer oznaka;
  private String daniVoznje;

  public OznakaDana(Integer oznaka, String daniVoznje) {
    this.oznaka = oznaka;
    this.daniVoznje = daniVoznje;
  }

  public Integer getOznaka() {
    return oznaka;
  }

  public void setOznaka(Integer oznaka) {
    this.oznaka = oznaka;
  }

  public String getDaniVoznje() {
    return daniVoznje;
  }

  public void setDaniVoznje(String daniVoznje) {
    this.daniVoznje = daniVoznje;
  }

}
