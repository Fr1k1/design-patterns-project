package edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.vozilobuilder;

public class VoziloBuilderImpl implements VoziloBuilder {
  private Vozilo vozilo;

  public VoziloBuilderImpl() {
    vozilo = new Vozilo();
  }

  @Override
  public Vozilo build() {
    return vozilo;
  }

  @Override
  public VoziloBuilder setOznaka(String oznaka) {
    vozilo.setOznaka(oznaka);
    return this;
  }

  @Override
  public VoziloBuilder setOpis(String opis) {
    vozilo.setOpis(opis);
    return this;
  }

  @Override
  public VoziloBuilder setProizvodac(String proizvodac) {
    vozilo.setProizvodac(proizvodac);
    return this;
  }

  @Override
  public VoziloBuilder setGodina(int godina) {
    vozilo.setGodina(godina);
    return this;
  }

  @Override
  public VoziloBuilder setNamjena(String namjena) {
    vozilo.setNamjena(namjena);
    return this;
  }

  @Override
  public VoziloBuilder setVrstaPrijevoza(String vrstaPrijevoza) {
    vozilo.setVrstaPrijevoza(vrstaPrijevoza);
    return this;
  }

  @Override
  public VoziloBuilder setVrstaPogona(String vrstaPogona) {
    vozilo.setVrstaPogona(vrstaPogona);
    return this;
  }

  @Override
  public VoziloBuilder setMaksBrzina(int maksBrzina) {
    vozilo.setMaksBrzina(maksBrzina);
    return this;
  }

  @Override
  public VoziloBuilder setMaksSnaga(double maksSnaga) {
    vozilo.setMaksSnaga(maksSnaga);
    return this;
  }

  @Override
  public VoziloBuilder setBrojSjedecihMjesta(int brojSjedecihMjesta) {
    vozilo.setBrojSjedecihMjesta(brojSjedecihMjesta);
    return this;
  }

  @Override
  public VoziloBuilder setBrojStajacihMjesta(int brojStajacihMjesta) {
    vozilo.setBrojStajacihMjesta(brojStajacihMjesta);
    return this;
  }

  @Override
  public VoziloBuilder setBrojBicikala(int brojBicikala) {
    vozilo.setBrojBicikala(brojBicikala);
    return this;
  }

  @Override
  public VoziloBuilder setBrojKreveta(int brojKreveta) {
    vozilo.setBrojKreveta(brojKreveta);
    return this;
  }

  @Override
  public VoziloBuilder setBrojAutomobila(int brojAutomobila) {
    vozilo.setBrojAutomobila(brojAutomobila);
    return this;
  }

  @Override
  public VoziloBuilder setNosivost(double nosivost) {
    vozilo.setNosivost(nosivost);
    return this;
  }

  @Override
  public VoziloBuilder setPovrsina(double povrsina) {
    vozilo.setPovrsina(povrsina);
    return this;
  }

  @Override
  public VoziloBuilder setZapremina(double zapremina) {
    vozilo.setZapremina(zapremina);
    return this;
  }

  @Override
  public VoziloBuilder setStatus(String status) {
    vozilo.setStatus(status);
    return this;
  }
}
