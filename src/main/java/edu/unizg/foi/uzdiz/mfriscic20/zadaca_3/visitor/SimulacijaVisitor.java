package edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.visitor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.composite.EtapaLeaf;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.composite.PomocniBazniComposite;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.composite.VlakComposite;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.composite.VozniRedComponent;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.dto.Stanica;

public class SimulacijaVisitor implements VozniRedVisitor {
  private String oznakaVlaka;
  private String dan;
  private int omjer;
  private VlakComposite vlak = null;
  private AtomicBoolean simulacijaAktivna;

  public SimulacijaVisitor(String oznakaVlaka, String dan, int omjer) {
    this.oznakaVlaka = oznakaVlaka;
    this.dan = dan;
    this.omjer = omjer;
    this.simulacijaAktivna = new AtomicBoolean(true);
    pokreniThreadZaPrekid();
  }

  private void pokreniThreadZaPrekid() {
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    Thread inputThread = new Thread(() -> {
      try {
        while (simulacijaAktivna.get()) {
          if (reader.ready()) {
            int input = System.in.read();
            if (input == 'X' || input == 'x') {
              simulacijaAktivna.set(false);
              System.out.println("Simulacija prekinuta od strane korisnika.");
              break;
            }
          }
        }
      } catch (IOException e) {
        System.out.println("Greška prilikom čitanja unosa: " + e.getMessage());
      }
    });
    inputThread.setDaemon(true);
    inputThread.start();
  }

  @Override
  public void posjetiElement(PomocniBazniComposite vozniRedBaseComposite) {
    if (vozniRedBaseComposite instanceof VlakComposite) {
      vlak = (VlakComposite) vozniRedBaseComposite;
      if (vlak.getOznakaVlaka().equals(oznakaVlaka)) {
        izvrsiSimulaciju();
      }
    }
  }

  private void izvrsiSimulaciju() {
    List<EtapaLeaf> etape = new ArrayList<>();
    for (VozniRedComponent komponenta : vlak.svaDjeca) {
      if (komponenta instanceof EtapaLeaf) {
        EtapaLeaf etapa = (EtapaLeaf) komponenta;
        if (etapa.getDaniVoznje().contains(dan)) {
          etape.add(etapa);
        }
      }
    }

    if (etape.isEmpty()) {
      System.out.println("Vlak ne vozi na dan koji je izabran");
      return;
    }

    String trenutnaPruga = null;

    for (EtapaLeaf etapa : etape) {
      List<Stanica> sveStanice = new ArrayList<>(etapa.getStaniceEtape());
      List<Stanica> staniceZaVrstu =
          filtrirajStanicePremaVrstiVlaka(sveStanice, etapa.getVrstaVlaka());

      if (staniceZaVrstu.isEmpty())
        continue;

      if (etapa.getSmjer().equals("O")) {
        Collections.reverse(staniceZaVrstu);
      }

      int vrijemePolaska = etapa.getVrijemePolaskaUMinutama();
      LocalTime virtualnoVrijeme = LocalTime.of(vrijemePolaska / 60, vrijemePolaska % 60);
      System.out.printf("Vlak %s krenuo iz stanice %s (pruga %s) u %02d:%02d%n", oznakaVlaka,
          etapa.getPolaznaStanicaEtape(), etapa.getOznakaPruge(), virtualnoVrijeme.getHour(),
          virtualnoVrijeme.getMinute());

      vlak.notifyObservers(String.format("Vlak %s krenuo sa stanice %s (pruga %s) u %02d:%02d",
          oznakaVlaka, etapa.getPolaznaStanicaEtape(), etapa.getOznakaPruge(),
          virtualnoVrijeme.getHour(), virtualnoVrijeme.getMinute()));

      trenutnaPruga = etapa.getOznakaPruge();

      for (int i = 0; i < staniceZaVrstu.size() - 1 && simulacijaAktivna.get(); i++) {
        Stanica trenutnaStanica = staniceZaVrstu.get(i);
        Stanica sljedecaStanica = staniceZaVrstu.get(i + 1);

        if (trenutnaStanica.getStanica().equals(sljedecaStanica.getStanica())) {
          continue;
        }

        int vrijemeVoznje = izracunajVrijemeVoznje(trenutnaStanica, sljedecaStanica,
            etapa.getVrstaVlaka(), etapa.getSmjer());
        LocalTime vrijemeDolaska = virtualnoVrijeme.plusMinutes(vrijemeVoznje);

        while (virtualnoVrijeme.isBefore(vrijemeDolaska) && simulacijaAktivna.get()) {
          try {
            Thread.sleep(1000 * 60 / omjer);
            virtualnoVrijeme = virtualnoVrijeme.plusMinutes(1);
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            simulacijaAktivna.set(false);
            break;
          }
        }

        if (simulacijaAktivna.get()) {
          System.out.printf("Vlak %s na stanici %s (pruga %s) u %02d:%02d%n", oznakaVlaka,
              sljedecaStanica.getStanica(), trenutnaPruga, virtualnoVrijeme.getHour(),
              virtualnoVrijeme.getMinute());

          String poruka = String.format("Vlak %s na stanici %s (pruga %s) u %02d:%02d", oznakaVlaka,
              sljedecaStanica.getStanica(), trenutnaPruga, virtualnoVrijeme.getHour(),
              virtualnoVrijeme.getMinute());
          vlak.notifyObservers(poruka);
        }
      }

      if (!simulacijaAktivna.get())
        break;
    }

    if (simulacijaAktivna.get()) {
      System.out.println("Vlak stigao na odredišnu stanicu.");
    }
  }

  private List<Stanica> filtrirajStanicePremaVrstiVlaka(List<Stanica> stanice, String vrstaVlaka) {
    List<Stanica> filtriraneStanice = new ArrayList<>();

    for (Stanica stanica : stanice) {
      boolean dodajStanicu = false;
      switch (vrstaVlaka) {
        case "B":
          Integer vrijemeBrzi = stanica.getVrijemeBrziVlak();
          if (vrijemeBrzi != null && vrijemeBrzi >= 0)
            dodajStanicu = true;
          break;
        case "U":
          Integer vrijemeUbrzani = stanica.getVrijemeUbrzaniVlak();
          if (vrijemeUbrzani != null && vrijemeUbrzani >= 0)
            dodajStanicu = true;
          break;
        default:
          Integer vrijemeNormalni = stanica.getVrijemeNormalniVlak();
          if (vrijemeNormalni != null && vrijemeNormalni >= 0)
            dodajStanicu = true;
          break;
      }
      if (dodajStanicu) {
        filtriraneStanice.add(stanica);
      }
    }
    return filtriraneStanice;
  }

  private int izracunajVrijemeVoznje(Stanica trenutnaStanica, Stanica sljedecaStanica,
      String vrstaVlaka, String smjer) {

    int vrijemeVoznje;
    if (smjer.equals("N")) {
      vrijemeVoznje = dohvatiVrijemeStanice(sljedecaStanica, vrstaVlaka);
    } else {
      vrijemeVoznje = dohvatiVrijemeStanice(trenutnaStanica, vrstaVlaka);
    }

    if (vrijemeVoznje == -1) {

      return -20; // sto ako ni jedna stanica nema ni jedno vrijeme
    }

    return vrijemeVoznje;
  }

  private int dohvatiVrijemeStanice(Stanica stanica, String vrstaVlaka) {
    Integer vrijeme = switch (vrstaVlaka) {
      case "B" -> stanica.getVrijemeBrziVlak();
      case "U" -> stanica.getVrijemeUbrzaniVlak();
      default -> stanica.getVrijemeNormalniVlak();
    };

    if (vrijeme == null) {
      return -1;
    }
    return vrijeme;
  }

  @Override
  public void posjetiElement(EtapaLeaf etapaLeaf) {
    // Nije potrebna implementacija
  }

  public void ocistiBuffer() {
    try {
      while (System.in.available() > 0) {
        System.in.read();
      }
    } catch (IOException e) {
      System.out.println("Greška prilikom čišćenja buffer-a: " + e.getMessage());
    }
  }
}
