package edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.dataparser;

public class DataParser {

  public Integer tryParseInteger(String number) {
    try {
      return Integer.parseInt(number);
    } catch (Exception e) {
      return null;
    }

  }
}
