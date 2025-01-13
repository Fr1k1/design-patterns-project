package edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.csvcitacfactory;

public class VozniRedCsvCitacCreator extends CsvCitacCreator {

  @Override
  public CsvCitacProduct factoryMethod() {
    return new CsvCitacVoznogRedaProduct();
  }

}
