package edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.vozilobuilder;


public interface VoziloBuilder {

  Vozilo build();

  VoziloBuilder setOznaka(String oznaka);

  VoziloBuilder setOpis(String opis);

  VoziloBuilder setProizvodac(String proizvodac);

  VoziloBuilder setGodina(int godina);

  VoziloBuilder setNamjena(String namjena);

  VoziloBuilder setVrstaPrijevoza(String vrstaPrijevoza);

  VoziloBuilder setVrstaPogona(String vrstaPogona);

  VoziloBuilder setMaksBrzina(int maksBrzina);

  VoziloBuilder setMaksSnaga(double maksSnaga);

  VoziloBuilder setBrojSjedecihMjesta(int brojSjedecihMjesta);

  VoziloBuilder setBrojStajacihMjesta(int brojStajacihMjesta);

  VoziloBuilder setBrojBicikala(int brojBicikala);

  VoziloBuilder setBrojKreveta(int brojKreveta);

  VoziloBuilder setBrojAutomobila(int brojAutomobila);

  VoziloBuilder setNosivost(double nosivost);

  VoziloBuilder setPovrsina(double povrsina);

  VoziloBuilder setZapremina(double zapremina);

  VoziloBuilder setStatus(String status);


}
