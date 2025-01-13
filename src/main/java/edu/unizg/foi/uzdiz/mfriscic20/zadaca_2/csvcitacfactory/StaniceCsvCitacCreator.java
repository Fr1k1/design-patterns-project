package edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.csvcitacfactory;

public class StaniceCsvCitacCreator extends CsvCitacCreator {
  @Override
  public CsvCitacProduct factoryMethod() {
    return new CsvCitacStanicaProduct();
  }

}
