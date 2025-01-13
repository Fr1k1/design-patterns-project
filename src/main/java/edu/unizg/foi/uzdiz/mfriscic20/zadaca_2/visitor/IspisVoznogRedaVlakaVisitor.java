package edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.visitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.composite.EtapaLeaf;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.composite.PomocniBazniComposite;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.composite.VlakComposite;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.composite.VozniRedComponent;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.dto.Stanica;

public class IspisVoznogRedaVlakaVisitor implements VozniRedVisitor {
  private String oznakaVlaka;
  private int udaljenost = 0;
  private VlakComposite vlak = null;

  public IspisVoznogRedaVlakaVisitor(String oznakaVlaka) {
    this.oznakaVlaka = oznakaVlaka;
  }

  @Override
  public void posjetiElement(PomocniBazniComposite vozniRedBaseComposite) {
    if (vozniRedBaseComposite instanceof VlakComposite) {
      vlak = (VlakComposite) vozniRedBaseComposite;
      if (vlak.getOznakaVlaka().equals(oznakaVlaka)) {
        ispisiZaglavlje();
        for (VozniRedComponent dijete : vozniRedBaseComposite.svaDjeca) {
          dijete.prihvati(this);
        }
      }
    }
  }

  private void ispisiZaglavlje() {
    System.out.printf("%-6s | %-8s | %-30s | %-8s | %-5s%n", "Vlak", "Pruga", "Stanica", "Polazak",
        "Km");
    System.out.println("-".repeat(110));
  }

  @Override
  public void posjetiElement(EtapaLeaf etapaLeaf) {
    if (etapaLeaf.getOznakaVlaka().equals(oznakaVlaka)) {
      List<Stanica> staniceEtape = new ArrayList<>(etapaLeaf.getStaniceEtape());
      if (etapaLeaf.getSmjer().equals("O")) {
        Collections.reverse(staniceEtape);
      }
      obradiStaniceVlaka(staniceEtape, etapaLeaf);
    }
  }

  private void obradiStaniceVlaka(List<Stanica> staniceEtape, EtapaLeaf etapaLeaf) {
    Integer vrijeme = 0;
    Integer pozicija = 0;
    Integer vrijemeZaIspisivanje = 0;
    Stanica prethodna = null;

    for (Stanica s : staniceEtape) {
      String smjer = etapaLeaf.getSmjer();
      String vrstaVlaka = vlak.getVrstaVlaka();

      if (smjer.equals("N")) {
        if (!(pozicija == 0 && s.getDuzina() != 0)) {
          udaljenost += s.getDuzina();
        }
      } else if (smjer.equals("O") && pozicija > 0) {
        udaljenost += staniceEtape.get(pozicija - 1).getDuzina();
      }

      if (trebaPreskocitiStanicu(s, prethodna, smjer, vrstaVlaka, pozicija, staniceEtape.size())) {
        prethodna = s;
        pozicija = pozicija + 1;
        continue;
      }

      if (smjer.equals("N")) {
        int dohvacenoVrijeme = dohvatiVrijemeStanice(s, vrstaVlaka);
        vrijeme += Math.max(dohvacenoVrijeme, 0);
        vrijemeZaIspisivanje = etapaLeaf.getVrijemePolaskaUMinutama() + vrijeme;
      } else if (smjer.equals("O")) {
        if (pozicija > 0) {
          int dohvacenoVrijeme = dohvatiVrijemeStanice(staniceEtape.get(pozicija - 1), vrstaVlaka);
          vrijeme += Math.max(dohvacenoVrijeme, 0);
        }
        vrijemeZaIspisivanje = etapaLeaf.getVrijemePolaskaUMinutama() + vrijeme;
      }

      System.out.printf("%-6s | %-8s | %-30s | %-8s | %-5d%n", etapaLeaf.getOznakaVlaka(),
          etapaLeaf.getOznakaPruge(), s.getStanica(), pretvoriMinuteUVrijeme(vrijemeZaIspisivanje),
          udaljenost);

      prethodna = s;
      pozicija = pozicija + 1;
    }
  }

  private int dohvatiVrijemeStanice(Stanica stanica, String vrstaVlaka) {
    Integer vrijeme = switch (vrstaVlaka) {
      case "N" -> stanica.getVrijemeNormalniVlak();
      case "U" -> stanica.getVrijemeUbrzaniVlak();
      case "B" -> stanica.getVrijemeBrziVlak();
      default -> -1;
    };

    if (vrijeme == null) {
      return -1;
    }
    return vrijeme;
  }

  private boolean trebaPreskocitiStanicu(Stanica trenutna, Stanica prethodna, String smjer,
      String vrstaVlaka, int index, int ukupnoStanica) {

    if (vrstaVlaka.equals("B")) {
      return trenutna.getVrijemeBrziVlak() == null;
    }
    if (vrstaVlaka.equals("U")) {
      return trenutna.getVrijemeUbrzaniVlak() == null;
    }

    // ako su stanice identicne, ne trebaju tu biti
    if (prethodna != null && trenutna.getStanica().equals(prethodna.getStanica())) {
      return true;
    }

    return (prethodna != null && smjer.equals("N") && vrstaVlaka.equals("N")
        && trenutna.getStanica().equals(prethodna.getStanica()) && index == ukupnoStanica - 1);
  }

  private String pretvoriMinuteUVrijeme(int minute) {
    int sati = minute / 60;
    int preostaleMinute = minute % 60;
    return String.format("%02d:%02d", sati, preostaleMinute);
  }
}
