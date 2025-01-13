package edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.csvcitacfactory;

public class OznakaDanaCsvCitacCreator extends CsvCitacCreator {

  @Override
  public CsvCitacProduct factoryMethod() {
    return new CsvCitacOznakaDanaProduct();
  }

}
