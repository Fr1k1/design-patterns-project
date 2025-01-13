package edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.csvcitacfactory;

public class StaniceCsvCitacCreator extends CsvCitacCreator {
  @Override
  public CsvCitacProduct factoryMethod() {
    return new CsvCitacStanicaProduct();
  }

}
