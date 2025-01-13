package edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.vozilobuilder;

import java.util.List;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.dataparser.DataParser;

public class VoziloDirector {
  private final VoziloBuilder builder;

  public VoziloDirector(final VoziloBuilder builder) {
    this.builder = builder;
  }

  DataParser parser = new DataParser();


  // N kategorija
  public Vozilo constructLokomotiva(List<String> podaci) {

    return builder.setOznaka(podaci.get(0)).setOpis(podaci.get(1)).setProizvodac(podaci.get(2))
        .setGodina(parser.tryParseInteger(podaci.get(3))).setNamjena(podaci.get(4))
        .setVrstaPrijevoza(podaci.get(5)).setVrstaPogona(podaci.get(6))
        .setMaksBrzina(parser.tryParseInteger(podaci.get(7)))
        .setMaksSnaga(Double.parseDouble(podaci.get(8).replace(',', '.'))).setStatus(podaci.get(17))
        .build();
  }

  // P kategorija

  public Vozilo constructPutnickiVagon(List<String> podaci) {
    return builder.setOznaka(podaci.get(0)).setOpis(podaci.get(1)).setProizvodac(podaci.get(2))
        .setGodina(parser.tryParseInteger(podaci.get(3))).setNamjena(podaci.get(4))
        .setVrstaPrijevoza(podaci.get(5)).setVrstaPogona(podaci.get(6))
        .setMaksBrzina(parser.tryParseInteger(podaci.get(7)))
        .setBrojSjedecihMjesta(parser.tryParseInteger(podaci.get(9)))
        .setBrojStajacihMjesta(parser.tryParseInteger(podaci.get(10)))
        .setBrojBicikala(parser.tryParseInteger(podaci.get(11))).setStatus(podaci.get(17)).build();
  }

  // TA kategorija

  public Vozilo constructTeretniVagonZaAutomobile(List<String> podaci) {
    return builder.setOznaka(podaci.get(0)).setOpis(podaci.get(1)).setProizvodac(podaci.get(2))
        .setGodina(parser.tryParseInteger(podaci.get(3))).setNamjena(podaci.get(4))
        .setVrstaPrijevoza(podaci.get(5)).setVrstaPogona(podaci.get(6))
        .setMaksBrzina(parser.tryParseInteger(podaci.get(7)))
        .setBrojAutomobila(parser.tryParseInteger(podaci.get(13)))
        .setNosivost(Double.parseDouble(podaci.get(14).replace(',', '.')))
        .setPovrsina(Double.parseDouble(podaci.get(15).replace(',', '.'))).setStatus(podaci.get(17))
        .build();
  }

  // TK kategorija

  public Vozilo constructTeretniVagonZaKontejnere(List<String> podaci) {
    return builder.setOznaka(podaci.get(0)).setOpis(podaci.get(1)).setProizvodac(podaci.get(2))
        .setGodina(parser.tryParseInteger(podaci.get(3))).setNamjena(podaci.get(4))
        .setVrstaPrijevoza(podaci.get(5)).setVrstaPogona(podaci.get(6))
        .setMaksBrzina(parser.tryParseInteger(podaci.get(7)))
        .setNosivost(Double.parseDouble(podaci.get(14).replace(',', '.')))
        .setPovrsina(Double.parseDouble(podaci.get(15).replace(',', '.'))).setStatus(podaci.get(17))
        .build();
  }

  // TRS kategorija

  public Vozilo constructTeretniVagonZaRobuURasutomStanju(List<String> podaci) {
    return builder.setOznaka(podaci.get(0)).setOpis(podaci.get(1)).setProizvodac(podaci.get(2))
        .setGodina(parser.tryParseInteger(podaci.get(3))).setNamjena(podaci.get(4))
        .setVrstaPrijevoza(podaci.get(5)).setVrstaPogona(podaci.get(6))
        .setMaksBrzina(parser.tryParseInteger(podaci.get(7)))
        .setNosivost(Double.parseDouble(podaci.get(14).replace(',', '.')))
        .setPovrsina(Double.parseDouble(podaci.get(15).replace(',', '.')))
        .setZapremina(Double.parseDouble(podaci.get(16).replace(',', '.')))
        .setStatus(podaci.get(17)).build();
  }

  // TTS kategorija

  public Vozilo constructTeretniVagonZaRobuUTekucemStanju(List<String> podaci) {
    return builder.setOznaka(podaci.get(0)).setOpis(podaci.get(1)).setProizvodac(podaci.get(2))
        .setGodina(parser.tryParseInteger(podaci.get(3))).setNamjena(podaci.get(4))
        .setVrstaPrijevoza(podaci.get(5)).setVrstaPogona(podaci.get(6))
        .setMaksBrzina(parser.tryParseInteger(podaci.get(7)))
        .setNosivost(Double.parseDouble(podaci.get(14).replace(',', '.')))
        .setZapremina(Double.parseDouble(podaci.get(16).replace(',', '.')))
        .setStatus(podaci.get(17)).build();
  }
}
