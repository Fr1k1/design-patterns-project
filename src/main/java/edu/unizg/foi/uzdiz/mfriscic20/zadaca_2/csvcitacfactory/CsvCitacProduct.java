package edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.csvcitacfactory;

import edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.dataparser.DataParser;


public abstract class CsvCitacProduct {

  protected DataParser parser = new DataParser();

  public abstract void ucitajPodatke(String putanja);

}
