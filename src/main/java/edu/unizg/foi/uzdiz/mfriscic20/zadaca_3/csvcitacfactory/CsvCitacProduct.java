package edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.csvcitacfactory;

import edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.dataparser.DataParser;


public abstract class CsvCitacProduct {

  protected DataParser parser = new DataParser();

  public abstract void ucitajPodatke(String putanja);

}
