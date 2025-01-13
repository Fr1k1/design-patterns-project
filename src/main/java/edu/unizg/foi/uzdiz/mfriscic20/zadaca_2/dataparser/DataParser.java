package edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.dataparser;

public class DataParser {

  public Integer tryParseInteger(String number) {
    try {
      return Integer.parseInt(number);
    } catch (Exception e) {
      return null;
    }

  }
}
