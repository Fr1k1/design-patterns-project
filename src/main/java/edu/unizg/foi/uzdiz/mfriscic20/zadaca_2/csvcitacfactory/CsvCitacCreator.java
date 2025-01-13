package edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.csvcitacfactory;

public abstract class CsvCitacCreator {

  public abstract CsvCitacProduct factoryMethod();

  public void procesirajDatoteku(String filename) {
    CsvCitacProduct reader = factoryMethod();
    reader.ucitajPodatke(filename);
  }

}
