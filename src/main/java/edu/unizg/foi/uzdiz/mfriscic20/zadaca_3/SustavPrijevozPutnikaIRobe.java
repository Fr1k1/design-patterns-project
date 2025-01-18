package edu.unizg.foi.uzdiz.mfriscic20.zadaca_3;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.composite.VlakComposite;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.composite.VozniRedComponent;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.composite.VozniRedComposite;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.dataparser.DataParser;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.dto.Kompozicija;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.dto.Korisnik;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.dto.OznakaDana;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.dto.Pruga;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.dto.Putovanje;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.dto.Stanica;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.mediator.ConcreteMediator;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.mediator.KorisnikPrijaviteljPopunjenostiConcreteColleague;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.mediator.KorisnikProvjeriteljPopunjenostiConcreteColleague;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.mediator.SustavPopunjenostiVlakovaMediator;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.memento.KartaCaretaker;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.memento.KartaMemento;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.memento.KartaOriginator;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.state.RelacijaPrugeContext;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.strategy.BlagajnaIzracun;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.strategy.CijenovniContext;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.strategy.DirektnoUVlakuIzracun;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.strategy.WebMobilnaIzracun;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.visitor.IspisEtapaPremaDanimaVisitor;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.visitor.IspisEtapaVisitor;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.visitor.IspisVlakovaVisitor;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.visitor.IspisVoznogRedaVlakaVisitor;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.visitor.SimulacijaVisitor;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.visitor.VozniRedVisitor;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_3.vozilobuilder.Vozilo;

public class SustavPrijevozPutnikaIRobe {

  private static volatile SustavPrijevozPutnikaIRobe INSTANCE;
  private int brojacUkupnihGresaka = 0;

  static {
    INSTANCE = new SustavPrijevozPutnikaIRobe();
  }

  private SustavPrijevozPutnikaIRobe() {

  }

  public static SustavPrijevozPutnikaIRobe dohvatiInstancu() {
    return INSTANCE;
  }

  public void dodajGresku() {
    brojacUkupnihGresaka++;
  }

  public int dohvatiUkupniBrojGresaka() {
    return brojacUkupnihGresaka;
  }

  private final List<Vozilo> vozila = new ArrayList<>();
  private final List<Kompozicija> kompozicije = new ArrayList<>();
  private final List<Pruga> pruge = new ArrayList<>();
  private final List<Stanica> stanice = new ArrayList<>();
  private final List<OznakaDana> oznakeDana = new ArrayList<>();
  private final VozniRedComposite vozniRed = new VozniRedComposite();
  private final List<Korisnik> korisnici = new ArrayList<>();
  SustavPopunjenostiVlakovaMediator mediator = new ConcreteMediator();
  private KartaOriginator kartaOriginator = new KartaOriginator();
  private KartaCaretaker kartaCaretaker = new KartaCaretaker();



  DataParser parser = new DataParser();

  protected void zapocniRad() {
    prihvacajKorisnickiUnos();
  }

  private boolean obradiIspKomandu(String[] dijelovi) {

    if (dijelovi.length != 3) {
      System.out.println("Komanda ISP zahtijeva oznaku pruge i redoslijed (N/O)!");
      return false;
    }

    String oznakaPruge = dijelovi[1];
    String redoslijed = dijelovi[2].toUpperCase();

    if (!redoslijed.equals("N") && !redoslijed.equals("O")) {
      System.out.println("Redoslijed mora biti N (normalan) ili O (obrnuti)!");
      return false;
    }

    prikaziStaniceNaPruzi(oznakaPruge, redoslijed);
    return true;
  }

  private boolean obradiIsi2sKomandu(String[] dijelovi, boolean crticaNadena,
      StringBuilder polaznaStanica, StringBuilder odredisnaStanica) {


    for (int i = 1; i < dijelovi.length; i++) {
      if (dijelovi[i].equals("-")) {
        crticaNadena = true;
        continue;
      }

      if (!crticaNadena) {
        if (polaznaStanica.length() > 0)
          polaznaStanica.append(" ");
        polaznaStanica.append(dijelovi[i]);
      } else {
        if (odredisnaStanica.length() > 0)
          odredisnaStanica.append(" ");
        odredisnaStanica.append(dijelovi[i]);
      }
    }

    if (!crticaNadena || polaznaStanica.length() == 0 || odredisnaStanica.length() == 0) {
      System.out.println(
          "Neispravan format komande ISI2S. Koristite: ISI2S [polazna stanica] - [odredišna stanica]");
      return false;

    }

    return true;

  }

  private void ispisiTablicuStanica(Putovanje put) {
    if (put.isEmpty()) {
      System.out.println("Nema stanica za prikaz.");
      return;
    }

    System.out.println("+------------------------------+----------+--------+");
    System.out.println("| Naziv stanice                | Vrsta    | Km     |");
    System.out.println("+------------------------------+----------+--------+");

    List<Stanica> stanice = put.getStanice();
    int progresivniKm = 0;

    boolean obrnutRedoslijed = false;
    for (Pruga pruga : put.getPruge()) {
      List<Stanica> staniceNaPruzi = pruga.getStanice();
      if (staniceNaPruzi.indexOf(stanice.get(0)) > staniceNaPruzi
          .indexOf(stanice.get(stanice.size() - 1))) {
        obrnutRedoslijed = true;
        break;
      }
    }

    for (int i = 0; i < stanice.size(); i++) {
      Stanica stanica = stanice.get(i);
      System.out.printf("| %-28s | %-8s | %6d |%n", stanica.getStanica(), stanica.getVrstaStanice(),
          progresivniKm);

      if (i < stanice.size() - 1) {
        if (!obrnutRedoslijed) {
          progresivniKm += stanice.get(i + 1).getDuzina();
        } else {
          progresivniKm += stanice.get(i).getDuzina();
        }
      }
    }

    System.out.println("+------------------------------+----------+--------+");
  }


  private boolean obradiIkKomandu(String[] dijelovi) {
    if (dijelovi.length != 2) {
      System.out.println("Komanda IK zahtijeva oznaku kompozicije!");
      return false;
    }
    String oznakaKompozicije = dijelovi[1];
    obradiPregledKompozicije(oznakaKompozicije);
    return true;
  }

  private boolean obradiIpKomandu(String[] dijelovi) {

    if (dijelovi.length != 1) {
      System.out.println("Komanda IP se koristi bez parametara!");
      return false;
    }
    obradiPregledPruga();
    return true;

  }

  private boolean obradiOznakuVlaka(String[] dijelovi, StringBuilder oznakaVlaka, String poruka) {
    if (dijelovi.length < 2) {
      System.out.println(poruka);
      return false;
    }

    for (int i = 1; i < dijelovi.length; i++) {
      oznakaVlaka.append(dijelovi[i]);
      if (i < dijelovi.length - 1) {
        oznakaVlaka.append(" ");
      }
    }
    return true;
  }

  private boolean obradiIEVProvjeru(String[] dijelovi, StringBuilder oznakaVlaka) {
    return obradiOznakuVlaka(dijelovi, oznakaVlaka, "Komanda IEV zahtijeva oznaku vlaka!");
  }

  private void obradiUpvKomandu(String[] dijelovi) {
    if (!validirajUpvKomandu(dijelovi)) {
      return;
    }
    String postotak = dijelovi[dijelovi.length - 1];


    // što ako vlak ima oznaku B 777
    StringBuilder oznakaVlaka = new StringBuilder();
    for (int i = 1; i < dijelovi.length - 1; i++) {
      oznakaVlaka.append(dijelovi[i]);
      if (i < dijelovi.length - 2) {
        oznakaVlaka.append(" ");
      }
    }

    Integer popunjenost = parser.tryParseInteger(postotak);
    if (popunjenost == null) {
      System.out.println("Greška: Popunjenost vlaka mora biti cijeli broj. Uneseno: " + postotak);
      return;
    }

    String oznakaVlakaStringovna = oznakaVlaka.toString();
    if (!postojiVlak(oznakaVlakaStringovna)) {
      System.out.println(
          "Greška: Vlak s oznakom " + oznakaVlakaStringovna + " ne postoji u voznom redu.");
      return;
    }

    KorisnikPrijaviteljPopunjenostiConcreteColleague korisnikPrijavitelj =
        new KorisnikPrijaviteljPopunjenostiConcreteColleague(mediator);
    korisnikPrijavitelj.prijaviPopunjenost(oznakaVlakaStringovna, popunjenost);
  }

  private boolean validirajUpvKomandu(String[] dijelovi) {
    if (dijelovi.length < 3) {
      System.out.println("Greška: Naredba UPV zahtijeva oznaku vlaka i postotak popunjenosti.");
      return false;
    }
    return true;
  }

  private void obradiPpvKomandu(String[] dijelovi) {
    if (!validirajPpvKomandu(dijelovi)) {
      return;
    }

    String oznakaVlaka = izdvojiOznakuVlaka(dijelovi, dijelovi.length);

    if (!postojiVlak(oznakaVlaka)) {
      System.out.println("Greška: Vlak s oznakom " + oznakaVlaka + " ne postoji u voznom redu.");
      return;
    }

    KorisnikProvjeriteljPopunjenostiConcreteColleague korisnikProvjeritelj =
        new KorisnikProvjeriteljPopunjenostiConcreteColleague(mediator);
    korisnikProvjeritelj.provjeriPopunjenost(oznakaVlaka);
  }

  private boolean validirajPpvKomandu(String[] dijelovi) {
    if (dijelovi.length < 2) {
      System.out.println("Greška: Naredba PPV zahtijeva oznaku vlaka.");
      return false;
    }
    return true;
  }

  private boolean postojiVlak(String oznakaVlak) {
    return vozniRed.dohvatiDijete(oznakaVlak) != null;
  }

  private void prihvacajKorisnickiUnos() {
    String unos = "";
    Scanner skenerUnosa = new Scanner(System.in);

    do {
      System.out.println(
          "\nUnesite komandu (IP, ISP, ISI2S, IK, IV, IEV, IEVD, IVRV, IVI2S, DK, PK, DPK, SVV, UPV, PPV, Q za izlaz):");
      try {
        unos = skenerUnosa.nextLine().trim();
        if (!unos.isEmpty()) {
          String[] dijelovi = unos.split("\\s+");
          if ("Q".equalsIgnoreCase(dijelovi[0])) {
            System.out.println("Kraj rada programa.");
            break;
          }
          izvrsiKomandu(dijelovi);

          if (dijelovi[0].equals("SVV")) {
            try {

              while (System.in.available() > 0) {
                System.in.read();
              }
            } catch (IOException e) {
              System.out.println("Greška pri čišćenju buffera: " + e.getMessage());
            }
          }
        }
      } catch (Exception e) {
        System.out.println("Došlo je do pogreške: " + e.getMessage());
      }
    } while (!"Q".equalsIgnoreCase(unos));

    skenerUnosa.close();
  }

  private void izvrsiKomandu(String[] dijelovi) {
    String komanda = dijelovi[0];
    switch (komanda) {
      case "IP":
        obradiIpKomandu(dijelovi);
        break;
      case "ISP":
        obradiIspKomandu(dijelovi);
        break;
      case "ISI2S":
        obradiIsi2sKomanduProsireno(dijelovi);
        break;
      case "IK":
        obradiIkKomandu(dijelovi);
        break;
      case "IV":
        obradiIvKomandu();
        break;
      case "IEV":
        obradiIevKomandu(dijelovi);
        break;
      case "IEVD":
        obradiIevdKomandu(dijelovi);
        break;
      case "IVRV":
        obradiIvrvKomandu(dijelovi);
        break;
      case "DK":
        obradiDkKomandu(dijelovi);
        break;
      case "PK":
        obradiPkKomandu();
        break;
      case "DPK":
        obradiDpkKomandu(dijelovi);
        break;
      case "SVV":
        obradiSvvKomandu(dijelovi);
        break;
      case "UPV":
        obradiUpvKomandu(dijelovi);
        break;
      case "PPV":
        obradiPpvKomandu(dijelovi);
        break;
      case "IVI2S":
        obradiIvi2sKomanduProsireno(dijelovi);
        break;
      case "CVP":
        obradiCvpKomandu(dijelovi);
        break;
      case "KKPV2S":
        obradiKkpv2sKomandu(dijelovi);
        break;
      case "IKKPV":
        obradiIkkpvKomandu(dijelovi);
        break;
      case "PSP2S":
        obradiPromjenuStatusaPruge(dijelovi);
        break;
      case "IRPS":
        obradiIspisRelacijaPruga(dijelovi);
        break;
      default:
        ispisiNepoznatuKomandu();
    }
  }

  private void obradiIsi2sKomanduProsireno(String[] dijelovi) {
    StringBuilder polaznaStanica = new StringBuilder();
    StringBuilder odredisnaStanica = new StringBuilder();
    if (obradiIsi2sKomandu(dijelovi, false, polaznaStanica, odredisnaStanica)) {
      obradiPutovanje(polaznaStanica.toString().trim(), odredisnaStanica.toString().trim());
    }
  }

  private void obradiPutovanje(String polazna, String odredisna) {
    Putovanje put = pronadiPutNaIstojPruzi(polazna, odredisna);
    if (put.isEmpty()) {
      put = pronadiPutPrekoVisePruga(polazna, odredisna);
    }
    if (put.isEmpty()) {
      System.out.println("Ne postoji put između stanica " + polazna + " i " + odredisna);
    } else {
      ispisiTablicuStanica(put);
    }
  }

  private void obradiIvKomandu() {
    vozniRed.prihvati(new IspisVlakovaVisitor());
  }

  private void obradiIevKomandu(String[] dijelovi) {
    StringBuilder oznakaVlaka = new StringBuilder();
    if (obradiIEVProvjeru(dijelovi, oznakaVlaka)) {
      vozniRed.prihvati(new IspisEtapaVisitor(oznakaVlaka.toString()));
    }
  }

  private void obradiIevdKomandu(String[] dijelovi) {
    if (dijelovi.length != 2) {
      System.out.println("Komanda IEVD zahtijeva dane vožnje!");
      return;
    }
    IspisEtapaPremaDanimaVisitor visitor = new IspisEtapaPremaDanimaVisitor(dijelovi[1]);
    vozniRed.prihvati(visitor);
    visitor.ispisiEtape();
  }

  private void obradiIvrvKomandu(String[] dijelovi) {
    StringBuilder oznakaVlaka = new StringBuilder();
    if (!obradiOznakuVlaka(dijelovi, oznakaVlaka, "Komanda IVRV zahtijeva oznaku vlaka!")) {
      return;
    }
    VozniRedVisitor visitor = new IspisVoznogRedaVlakaVisitor(oznakaVlaka.toString());
    vozniRed.prihvati(visitor);
  }

  private void obradiDkKomandu(String[] dijelovi) {
    String rezultat = dodajKorisnika(dijelovi);
    System.out.println(rezultat);
  }

  private void obradiPkKomandu() {
    if (korisnici.isEmpty()) {
      System.out.println("Nema registriranih korisnika u sustavu.");
      return;
    }
    ispisiKorisnike();
  }

  private void obradiDpkKomandu(String[] dijelovi) {
    String rezultat = dodajPracenjeKorisniku(dijelovi);
    System.out.println(rezultat);
  }

  private void ispisiKorisnike() {
    System.out.println("\nRegistrirani korisnici:");
    System.out.println("-".repeat(110));
    for (Korisnik k : korisnici) {
      ispisiKorisnika(k);
    }
    System.out.println("-".repeat(110));
  }

  private void ispisiKorisnika(Korisnik k) {
    System.out
        .println(
            k.getIme() + " " + k.getPrezime()
                + (k.getOznakaVlaka() != null
                    ? " - prati vlak " + k.getOznakaVlaka()
                        + (k.getPracenaStanica() != null ? " na stanici " + k.getPracenaStanica()
                            : "")
                    : ""));
  }

  private void obradiSvvKomandu(String[] dijelovi) {
    if (!provjeriSvvUlaz(dijelovi))
      return;
    String[] podaci = parsirajSvvPodatke(dijelovi);
    if (podaci == null)
      return;
    izvrsiSimulaciju(podaci[0], podaci[1], Integer.parseInt(podaci[2]));
  }

  private boolean provjeriSvvUlaz(String[] dijelovi) {
    if (dijelovi.length < 6) {
      System.out.println("Komanda SVV zahtijeva oznaku vlaka, dan i koeficijent!");
      return false;
    }
    return true;
  }

  private String[] parsirajSvvPodatke(String[] dijelovi) {
    int[] separatori = pronadiSeparatore(dijelovi);
    if (!suSeparatoriIspravni(separatori))
      return null;
    return izvuciPodatke(dijelovi, separatori[0], separatori[1]);
  }

  private int[] pronadiSeparatore(String[] dijelovi) {
    int prviSeparator = -1, drugiSeparator = -1;
    for (int i = 0; i < dijelovi.length; i++) {
      if (dijelovi[i].equals("-")) {
        if (prviSeparator == -1)
          prviSeparator = i;
        else {
          drugiSeparator = i;
          break;
        }
      }
    }
    return new int[] {prviSeparator, drugiSeparator};
  }

  private boolean suSeparatoriIspravni(int[] separatori) {
    if (separatori[0] == -1 || separatori[1] == -1) {
      System.out.println("Neispravan format komande SVV!");
      return false;
    }
    return true;
  }

  private String[] izvuciPodatke(String[] dijelovi, int prviSeparator, int drugiSeparator) {
    try {
      String oznaka = izdvojiOznakuVlaka(dijelovi, prviSeparator);
      String dan = dijelovi[prviSeparator + 1].trim();
      int omjer = Integer.parseInt(dijelovi[drugiSeparator + 1].trim());
      return new String[] {oznaka, dan, String.valueOf(omjer)};
    } catch (NumberFormatException e) {
      System.out.println("Neispravan format koeficijenta!");
      return null;
    }
  }

  private String izdvojiOznakuVlaka(String[] dijelovi, int prviSeparator) {
    StringBuilder oznakaVlaka = new StringBuilder();
    for (int i = 1; i < prviSeparator; i++) {
      oznakaVlaka.append(dijelovi[i]);
      if (i < prviSeparator - 1)
        oznakaVlaka.append(" ");
    }
    return oznakaVlaka.toString();
  }

  private void izvrsiSimulaciju(String oznakaVlaka, String dan, int omjer) {
    SimulacijaVisitor visitor = new SimulacijaVisitor(oznakaVlaka, dan, omjer);
    vozniRed.prihvati(visitor);
    visitor.ocistiBuffer();
  }

  private void ispisiNepoznatuKomandu() {
    System.out.println(
        "Nepoznata komanda! Dostupne komande su: IP, ISP, ISI2S, IK, IV, IEV, IEVD, IVRV, IVI2S, DK, PK, DPK, SVV, UPV, PPV, Q");
  }

  private void obradiIvi2sKomanduProsireno(String[] dijelovi) {
    String[] parametri = izvuciParametreIvi2s(dijelovi);
    if (parametri != null) {
      obradiIvi2sKomandu(parametri);
    }
  }

  private String[] izvuciParametreIvi2s(String[] dijelovi) {
    String unosKomande = String.join(" ", dijelovi);
    String[] parametri = unosKomande.substring(unosKomande.indexOf(' ')).trim().split(" - ");

    if (parametri.length != 6) {
      System.out.println(
          "Neispravan format komande IVI2S! Očekivano 6 parametara, dobiveno " + parametri.length);
      return null;
    }

    return new String[] {parametri[0].trim(), parametri[1].trim(), parametri[2].trim(),
        parametri[3].trim(), parametri[4].trim(), parametri[5].trim()};
  }



  public void unesiVozilo(Vozilo vozilo) {
    this.vozila.add(vozilo);
  }

  public void unesiKompoziciju(Kompozicija kompozicija) {
    this.kompozicije.add(kompozicija);
  }

  public void unesiStanicu(Stanica stanica) {
    this.stanice.add(stanica);
  }

  public List<Kompozicija> dohvatiKompozicije() {
    return kompozicije;
  }

  public List<Vozilo> dohvatiVozila() {
    return vozila;
  }

  public void unesiPrugu(Pruga pruga) {
    this.pruge.add(pruga);
  }

  public Pruga dohvatiPrugu(String oznaka) {
    return pruge.stream().filter(p -> p.getOznaka().equals(oznaka)).findFirst().orElse(null);
  }

  public List<Pruga> getSvePruge() {
    return new ArrayList<>(pruge);
  }

  public void obradiPregledPruga() {
    System.out.println("Oznaka    Početna stanica            Završna stanica            Ukupno km");
    System.out.println(
        "-----------------------------------------------------------------------------------");

    for (Pruga pruga : SustavPrijevozPutnikaIRobe.dohvatiInstancu().getSvePruge()) {
      if (pruga.getPocetnaStanica() != null && pruga.getZavrsnaStanica() != null) {
        System.out.printf("%-10s %-25s %-25s %d%n", pruga.getOznaka(),
            pruga.getPocetnaStanica().getStanica(), pruga.getZavrsnaStanica().getStanica(),
            pruga.getUkupnoKilometara());
      }
    }
  }

  public void prikaziStaniceNaPruzi(String oznakaPruge, String redoslijed) {
    if (oznakaPruge == null || redoslijed == null) {
      System.out.println("Pogrešni ulazni parametri!");
      return;
    }

    Pruga pruga = dohvatiPrugu(oznakaPruge);
    if (pruga == null) {
      System.out.println("Ne postoji pruga: " + oznakaPruge);
      return;
    }

    System.out.printf("%-30s %-15s %-10s%n", "Naziv stanice", "Vrsta", "Kilometri");
    System.out.println("-----------------------------------------------------------------");

    List<Stanica> staniceZaPrikaz =
        redoslijed.equalsIgnoreCase("N") ? pruga.getStanice() : pruga.getStaniceObrnuto();

    int akumuliraniKm = 0;
    for (Stanica stanica : staniceZaPrikaz) {
      if (redoslijed.equalsIgnoreCase("N")) {
        akumuliraniKm += stanica.getDuzina();
        ispisiStanicu(stanica, akumuliraniKm);
      } else {
        ispisiStanicu(stanica, akumuliraniKm);
        akumuliraniKm += stanica.getDuzina();
      }
    }
  }

  private void ispisiStanicu(Stanica stanica, int kilometri) {
    System.out.printf("%-30s %-15s %-10s%n", stanica.getStanica(), stanica.getVrstaStanice(),
        kilometri);
  }

  private void obradiPregledKompozicije(String oznakaKompozicije) {
    int oznaka = parser.tryParseInteger(oznakaKompozicije);
    Kompozicija kompozicija =
        dohvatiKompozicije().stream().filter(k -> k.getOznaka() == oznaka).findFirst().orElse(null);

    if (kompozicija == null) {
      System.out.println("Ne postoji kompozicija s oznakom: " + oznakaKompozicije);
      return;
    }

    System.out.printf("%-10s %-10s %-60s %-6s %-20s %-15s %-12s%n", "Oznaka", "Uloga", "Opis",
        "Godina", "Namjena", "Vrsta pogona", "Maks. brzina");
    System.out.println("-".repeat(140));

    for (int i = 0; i < kompozicija.getBrojVozila(); i++) {
      Vozilo vozilo = kompozicija.getVozilo(i);
      System.out.printf("%-10s %-10s %-60s %-6s %-20s %-15s %-12s%n", vozilo.getOznaka(),
          kompozicija.getUloga(i), vozilo.getOpis(), vozilo.getGodina(), vozilo.getNamjena(),
          vozilo.getVrstaPogona(), vozilo.getMaksBrzina());
    }
  }

  public List<Integer> izracunajUdaljenosti(List<Stanica> staniceNaPutu) {
    List<Integer> udaljenosti = new ArrayList<>();
    int trenutnaUdaljenost = 0;

    for (int i = 0; i < staniceNaPutu.size(); i++) {
      if (i == 0) {
        udaljenosti.add(0);
      } else {
        trenutnaUdaljenost += staniceNaPutu.get(i).getUdaljenostOdProsle();
        udaljenosti.add(trenutnaUdaljenost);
      }
    }

    return udaljenosti;
  }

  private Putovanje pronadiPutNaIstojPruzi(String polaznaStanica, String odredisnaStanica) {
    Putovanje put = new Putovanje();
    Pruga trazenaPruga = null;
    List<Stanica> staniceNaPutu = null;

    for (Pruga pruga : pruge) {
      List<Stanica> staniceIzmedu = pruga.fixedGetStaniceIzmedu(polaznaStanica, odredisnaStanica);

      if (!staniceIzmedu.isEmpty()) {
        trazenaPruga = pruga;
        staniceNaPutu = staniceIzmedu;
        break;
      }

      List<Stanica> obrnutStaniceIzmedu =
          pruga.fixedGetStaniceIzmedu(odredisnaStanica, polaznaStanica);
      if (!obrnutStaniceIzmedu.isEmpty()) {
        trazenaPruga = pruga;
        staniceNaPutu = new ArrayList<>(obrnutStaniceIzmedu);
        Collections.reverse(staniceNaPutu);
        break;
      }
    }

    if (trazenaPruga == null) {
      System.out.println(
          "Nema direktne pruge koja povezuje " + polaznaStanica + " i " + odredisnaStanica);
      return put;
    }

    List<Integer> udaljenosti = izracunajUdaljenosti(staniceNaPutu);

    put.setPruge(Arrays.asList(trazenaPruga));
    put.setStanice(staniceNaPutu);

    return put;
  }



  private void inicijalizirajMape(Map<String, Set<String>> staniceNaPrugama,
      Map<String, Pruga> svePruge, List<Pruga> pruge) {
    for (Pruga pruga : pruge) {
      svePruge.put(pruga.getOznaka(), pruga);
      for (Stanica stanica : pruga.getStanice()) {
        String nazivStanice = stanica.getStanica();
        if (!staniceNaPrugama.containsKey(nazivStanice)) {
          staniceNaPrugama.put(nazivStanice, new HashSet<>());
        }
        staniceNaPrugama.get(nazivStanice).add(pruga.getOznaka());
      }
    }
  }

  private void dodajNoveStaniceNaPut(Putovanje put, List<Stanica> staniceNaSegmentu,
      Map<String, Integer> udaljenosti) {
    for (Stanica stanica : staniceNaSegmentu) {
      if (!put.getStanice().contains(stanica)) {
        Stanica novaStanica = new Stanica(stanica.getStanica(), stanica.getOznakaPruge(),
            stanica.getVrstaStanice(), stanica.getStatusStanice(), stanica.getPutniciUlazeIIzlaze(),
            stanica.getRobaUtovarIstovar(), stanica.getKategorija(), stanica.getBrojPerona(),
            stanica.getVrstaPruge(), stanica.getBrojKolosjeka(), stanica.getDoPoOsovini(),
            stanica.getDoPoDuznomM(), stanica.getStatusPruge(),
            udaljenosti.getOrDefault(stanica.getStanica(), 0), stanica.getVrijemeNormalniVlak(),
            stanica.getVrijemeUbrzaniVlak(), stanica.getVrijemeBrziVlak());
        put.dodajStanicu(novaStanica);
      }
    }
  }

  private Putovanje pronadiPutPrekoVisePruga(String polaznaStanica, String odredisnaStanica) {
    Putovanje put = new Putovanje();
    Map<String, Set<String>> staniceNaPrugama = new HashMap<>();
    Map<String, Pruga> svePruge = new HashMap<>();
    Map<String, Integer> udaljenosti = new HashMap<>();
    Queue<List<String>> queue = new LinkedList<>();
    Set<String> posjeceneStanice = new HashSet<>();
    Map<String, String> prethodnici = new HashMap<>();
    inicijalizirajMape(staniceNaPrugama, svePruge, pruge);
    boolean putPronadjen = false;
    List<String> pronadjenPut = null;
    queue.add(Arrays.asList(polaznaStanica));
    posjeceneStanice.add(polaznaStanica);
    udaljenosti.put(polaznaStanica, 0);

    while (!queue.isEmpty() && !putPronadjen) {
      List<String> trenutniPut = queue.poll();
      String zadnjaStanica = trenutniPut.get(trenutniPut.size() - 1);
      if (zadnjaStanica.equals(odredisnaStanica)) {
        putPronadjen = true;
        pronadjenPut = trenutniPut;
        break;
      }
      // nadi sve dostupne stanice
      Set<String> prugeZadnjeStanice = staniceNaPrugama.get(zadnjaStanica);
      if (prugeZadnjeStanice != null) {
        for (String oznakaPruge : prugeZadnjeStanice) {
          Pruga pruga = svePruge.get(oznakaPruge);
          for (Stanica sljedecaStanica : pruga.getStanice()) {
            if (!posjeceneStanice.contains(sljedecaStanica.getStanica())) {
              List<String> noviPut = new ArrayList<>(trenutniPut);
              noviPut.add(sljedecaStanica.getStanica());
              queue.add(noviPut);
              posjeceneStanice.add(sljedecaStanica.getStanica());
              prethodnici.put(sljedecaStanica.getStanica(), zadnjaStanica);
              int trenutnaUdaljenost = udaljenosti.get(zadnjaStanica);
              udaljenosti.put(sljedecaStanica.getStanica(),
                  trenutnaUdaljenost + sljedecaStanica.getDuzina());
            }
          }
        }
      }
    }

    if (pronadjenPut != null) {
      for (int i = 0; i < pronadjenPut.size() - 1; i++) {
        String trenutnaStanica = pronadjenPut.get(i);
        String sljedecaStanica = pronadjenPut.get(i + 1);
        Set<String> prugeTrenutne = staniceNaPrugama.get(trenutnaStanica);
        Set<String> prugeSljedece = staniceNaPrugama.get(sljedecaStanica);
        Set<String> zajednickePruge = new HashSet<>();
        for (String pruga : prugeTrenutne) {
          if (prugeSljedece.contains(pruga)) {
            zajednickePruge.add(pruga);
          }
        }
        if (!zajednickePruge.isEmpty()) {
          String oznakaPruge = zajednickePruge.iterator().next();
          Pruga pruga = svePruge.get(oznakaPruge);
          List<Stanica> staniceNaSegmentu =
              pruga.fixedGetStaniceIzmedu(trenutnaStanica, sljedecaStanica);
          dodajNoveStaniceNaPut(put, staniceNaSegmentu, udaljenosti);
          put.dodajPrugu(pruga);
        }
      }
    }
    return put;
  }

  // ============================================================================================
  // DZ2
  // ============================================================================================

  public VozniRedComposite dohvatiVozniRed() {
    return vozniRed;
  }

  public void dodajVlak(VlakComposite vlak) {
    vozniRed.dodaj(vlak);
  }

  public void dohvatiVlak(String oznakaVlaka) {
    vozniRed.dohvatiDijete(oznakaVlaka);
  }

  public void ispisiVozniRed() {
    vozniRed.prikaziDetalje();
  }

  public void unesiOznakuDana(OznakaDana oznakaDana) {
    this.oznakeDana.add(oznakaDana);
  }

  public OznakaDana dohvatiOznakuDana(Integer oznaka) {
    return oznakeDana.stream().filter(p -> p.getOznaka().equals(oznaka)).findFirst().orElse(null);
  }

  private String dodajKorisnika(String[] dijelovi) {
    if (dijelovi.length != 3) {
      return "Komanda DK zahtijeva ime i prezime korisnika!";
    }

    String ime = dijelovi[1];
    String prezime = dijelovi[2];

    if (postojiKorisnik(ime, prezime)) {
      return "Korisnik " + ime + " " + prezime + " već postoji u sustavu!";
    }

    Korisnik noviKorisnik = new Korisnik(ime, prezime);
    korisnici.add(noviKorisnik);
    return "Korisnik " + ime + " " + prezime + " uspješno dodan u sustav.";
  }

  private boolean postojiKorisnik(String ime, String prezime) {
    for (Korisnik korisnik : korisnici) {
      if (korisnik.getIme().equals(ime) && korisnik.getPrezime().equals(prezime)) {
        return true;
      }
    }
    return false;
  }

  private Korisnik dohvatiKorisnika(String ime, String prezime) {
    for (Korisnik korisnik : korisnici) {
      if (korisnik.getIme().equals(ime) && korisnik.getPrezime().equals(prezime)) {
        return korisnik;
      }
    }
    return null;
  }

  private String dodajPracenjeKorisniku(String[] dijelovi) {
    if (dijelovi.length < 4) {
      return "Komanda DPK zahtijeva minimalno ime, prezime i oznaku vlaka!";
    }

    String ime = dijelovi[1];
    String prezime = dijelovi[2];

    int indexCrtice = 3;
    if (!dijelovi[indexCrtice].equals("-")) {
      return "Neispravan format! Koristite: DPK ime prezime - oznakaVlaka [- stanica]";
    }
    if (dijelovi.length < 5) {
      return "Nedostaje oznaka vlaka nakon crtice!";
    }

    StringBuilder oznakaVlakaBuilder = new StringBuilder();
    int i = 4;
    while (i < dijelovi.length && !dijelovi[i].equals("-")) {
      if (oznakaVlakaBuilder.length() > 0) {
        oznakaVlakaBuilder.append(" ");
      }
      oznakaVlakaBuilder.append(dijelovi[i]);
      i++;
    }
    String oznakaVlaka = oznakaVlakaBuilder.toString();

    String pracenaStanica = null;

    if (i < dijelovi.length && dijelovi[i].equals("-")) {
      StringBuilder stanicaBuilder = new StringBuilder();
      for (int j = i + 1; j < dijelovi.length; j++) {
        stanicaBuilder.append(dijelovi[j]);
        if (j < dijelovi.length - 1) {
          stanicaBuilder.append(" ");
        }
      }
      pracenaStanica = stanicaBuilder.toString();
    }

    Korisnik korisnik = dohvatiKorisnika(ime, prezime);
    if (korisnik == null) {
      return "Korisnik " + ime + " " + prezime
          + " nije registriran u sustavu! Prvo koristite DK komandu.";
    }

    VlakComposite vlak = (VlakComposite) vozniRed.dohvatiDijete(oznakaVlaka);
    if (vlak == null) {
      return "Nije pronađen vlak s oznakom: " + oznakaVlaka;
    }

    korisnik.dodajPracenje(oznakaVlaka, pracenaStanica);
    vlak.attachObserver(korisnik);

    return "Korisnik " + ime + " " + prezime + " uspješno dodan za praćenje vlaka " + oznakaVlaka
        + (pracenaStanica != null ? " na stanici " + pracenaStanica : "");
  }


  private void obradiIvi2sKomandu(String[] parametri) {
    try {
      final IspisnikIVI2S ispisnik = new IspisnikIVI2S();
      String polaznaStanica = parametri[0];
      String odredisnaStanica = parametri[1];
      String dan = parametri[2];
      int odVrijeme = pretvoriVrijemeUMinute(parametri[3]);
      int doVrijeme = pretvoriVrijemeUMinute(parametri[4]);
      String formatPrikaza = parametri[5];

      Putovanje put = pronadiPutNaIstojPruzi(polaznaStanica, odredisnaStanica);
      if (put.isEmpty()) {
        put = pronadiPutPrekoVisePruga(polaznaStanica, odredisnaStanica);
      }

      if (put.isEmpty()) {
        System.out
            .println("Ne postoji put između stanica " + polaznaStanica + " i " + odredisnaStanica);
        return;
      }

      // znaci do tud samo pronalazi put, bez ikakvog doticaja s dz2
      List<VlakComposite> odgovarajuciVlakovi = new ArrayList<>();
      for (VozniRedComponent komponenta : vozniRed.svaDjeca) {
        if (komponenta instanceof VlakComposite) {
          VlakComposite kompozitniVlak = (VlakComposite) komponenta;
          if (kompozitniVlak.voziNaDan(dan)
              && kompozitniVlak.voziUVremenskomRasponu(odVrijeme, doVrijeme)
              && kompozitniVlak.prolaziTrazenimPutem(polaznaStanica, odredisnaStanica)) {
            odgovarajuciVlakovi.add(kompozitniVlak);
          }
        }
      }

      ispisnik.ispisiTablicuVoznogReda(put, odgovarajuciVlakovi, formatPrikaza);

    } catch (Exception e) {
      System.out.println("Greška u obradi IVI2S komande: " + e.getMessage());
      e.printStackTrace();
    }
  }



  private int pretvoriVrijemeUMinute(String vrijeme) {
    String[] dijelovi = vrijeme.split(":");
    return Integer.parseInt(dijelovi[0]) * 60 + Integer.parseInt(dijelovi[1]);
  }

  // ============================================================================================
  // DZ3
  // ============================================================================================

  private CijenovniContext cjenovniKontekst;

  private void obradiCvpKomandu(String[] dijelovi) {
    if (dijelovi.length != 7) {
      System.out.println("Pogrešan format CVP komande!");
      return;
    }

    try {
      double cijenaNormalni = Double.parseDouble(dijelovi[1].replace(',', '.'));
      double cijenaUbrzani = Double.parseDouble(dijelovi[2].replace(',', '.'));
      double cijenaBrzi = Double.parseDouble(dijelovi[3].replace(',', '.'));
      double popustVikend = Double.parseDouble(dijelovi[4].replace(',', '.')) / 100.0;
      double popustWebMob = Double.parseDouble(dijelovi[5].replace(',', '.')) / 100.0;
      double uvecanjeVlak = Double.parseDouble(dijelovi[6].replace(',', '.')) / 100.0;

      CijenovniContext kontekst = new CijenovniContext();
      kontekst.postaviCijene(cijenaNormalni, cijenaUbrzani, cijenaBrzi, popustVikend, popustWebMob,
          uvecanjeVlak);

      kontekst.postaviStrategiju(new BlagajnaIzracun(popustVikend));
      this.cjenovniKontekst = kontekst;

      try {
        double testCijena = kontekst.izracunajCijenu(cijenaNormalni, 100, false);
        System.out.println("Cijene uspješno postavljene!");
        System.out.printf("Normalni vlak: %.2f €/km%n", cijenaNormalni);
        System.out.printf("Ubrzani vlak: %.2f €/km%n", cijenaUbrzani);
        System.out.printf("Brzi vlak: %.2f €/km%n", cijenaBrzi);
        System.out.printf("Popust vikend: %.1f%%%n", popustVikend * 100);
        System.out.printf("Popust web/mob: %.1f%%%n", popustWebMob * 100);
        System.out.printf("Uvećanje u vlaku: %.1f%%%n", uvecanjeVlak * 100);
        System.out.printf("Test izračun za 100km normalnim vlakom: %.2f €%n", testCijena);
      } catch (Exception e) {
        System.out.println("Greška pri testnom izračunu: " + e.getMessage());
      }
    } catch (NumberFormatException e) {
      System.out.println("Nevažeći format brojeva u CVP komandi!");
    }
  }

  public int izracunajKilometreZaKartu(String polaznaStanica, String odredisnaStanica) {
    Putovanje put = pronadiPutNaIstojPruzi(polaznaStanica, odredisnaStanica);
    if (put.isEmpty())
      return 0;

    List<Stanica> stanice = put.getStanice();
    int ukupniKm = 0;
    boolean obrnutRedoslijed = false;

    for (Pruga pruga : put.getPruge()) {
      List<Stanica> staniceNaPruzi = pruga.getStanice();
      if (staniceNaPruzi.indexOf(stanice.get(0)) > staniceNaPruzi
          .indexOf(stanice.get(stanice.size() - 1))) {
        obrnutRedoslijed = true;
        break;
      }
    }

    for (int i = 0; i < stanice.size() - 1; i++) {
      if (!obrnutRedoslijed) {
        ukupniKm += stanice.get(i + 1).getDuzina();
      } else {
        ukupniKm += stanice.get(i).getDuzina();
      }
    }

    return ukupniKm;
  }

  // needs refactor
  private void obradiKkpv2sKomandu(String[] dijelovi) {
    if (cjenovniKontekst == null) {
      System.out.println("Cijene nisu postavljene! Prvo koristite CVP komandu.");
      return;
    }

    String unosKomande = String.join(" ", dijelovi);
    String[] parametri = unosKomande.substring(unosKomande.indexOf(' ')).trim().split(" - ");

    if (parametri.length != 5) {
      System.out.println(
          "Neispravan format komande KKPV2S! Očekujem: KKPV2S oznaka - polaznaStanica - odredisnaStanica - datum - načinKupovine");
      return;
    }

    String oznakaVlaka = parametri[0].trim();
    String polaznaStanica = parametri[1].trim();
    String odredisnaStanica = parametri[2].trim();
    String datum = parametri[3].trim();
    String nacinKupnje = parametri[4].trim();

    VlakComposite vlak = (VlakComposite) vozniRed.dohvatiDijete(oznakaVlaka);
    if (vlak == null) {
      System.out.println("Vlak s oznakom " + oznakaVlaka + " ne postoji!");
      return;
    }
    Putovanje put = pronadiPutNaIstojPruzi(polaznaStanica, odredisnaStanica);
    if (put.isEmpty()) {
      put = pronadiPutPrekoVisePruga(polaznaStanica, odredisnaStanica);
    }
    if (put.isEmpty()) {
      System.out
          .println("Ne postoji put između stanica " + polaznaStanica + " i " + odredisnaStanica);
      return;
    }

    System.out.println("Vrsta vlaka je " + vlak.getVrstaVlaka()); // Prvo ispišite vrstu vlaka

    double osnovnaCijena = switch (vlak.getVrstaVlaka()) {
      case "U" -> cjenovniKontekst.getCijenaUbrzaniKm();
      case "B" -> cjenovniKontekst.getCijenaBrziKm();
      default -> cjenovniKontekst.getCijenaNormalniKm();
    };

    // Postavljanje odgovarajuće strategije prema načinu kupnje
    switch (nacinKupnje.toUpperCase()) {
      case "WM":
        cjenovniKontekst.postaviStrategiju(new WebMobilnaIzracun(cjenovniKontekst.getPopustVikend(),
            cjenovniKontekst.getPopustWebMob()));
        break;
      case "V":
        cjenovniKontekst.postaviStrategiju(new DirektnoUVlakuIzracun(
            cjenovniKontekst.getPopustVikend(), cjenovniKontekst.getUvecanjeVlak()));
        break;
      case "B":
        cjenovniKontekst.postaviStrategiju(new BlagajnaIzracun(cjenovniKontekst.getPopustVikend()));
        break;
      default:
        System.out.println("Nevažeći način kupnje! Koristite: B, WM ili V");
        return;
    }

    boolean jeVikend = provjeriDaliJeVikend(datum);
    double udaljenostKm = 0;
    List<Stanica> stanice = put.getStanice();
    boolean obrnutRedoslijed = false;

    // Provjeri smjer
    for (Pruga pruga : put.getPruge()) {
      List<Stanica> staniceNaPruzi = pruga.getStanice();
      if (staniceNaPruzi.indexOf(stanice.get(0)) > staniceNaPruzi
          .indexOf(stanice.get(stanice.size() - 1))) {
        obrnutRedoslijed = true;
        break;
      }
    }

    // Izračunaj kilometre
    for (int i = 0; i < stanice.size() - 1; i++) {
      if (!obrnutRedoslijed) {
        udaljenostKm += stanice.get(i + 1).getDuzina();
      } else {
        udaljenostKm += stanice.get(i).getDuzina();
      }
    }

    try {
      // tu se onda poziva ovisno o strategiji koja je postavljena u konstruktoru
      double konacnaCijena =
          cjenovniKontekst.izracunajCijenu(osnovnaCijena, udaljenostKm, jeVikend);

      kartaOriginator.setStanje(oznakaVlaka, polaznaStanica, odredisnaStanica, datum, nacinKupnje,
          osnovnaCijena, konacnaCijena, udaljenostKm, jeVikend);
      kartaCaretaker.spremiKartu(kartaOriginator.createMemento());

      System.out.println("\n=== KARTA ZA VLAK ===");
      System.out.println("Oznaka vlaka: " + oznakaVlaka);
      System.out.println("Relacija: " + polaznaStanica + " - " + odredisnaStanica);
      System.out.println("Datum putovanja: " + datum);
      System.out.println("Udaljenost: " + udaljenostKm + " km");
      System.out.println("Način kupnje: " + nacinKupnje);
      System.out
          .println("Osnovna cijena: " + String.format("%.2f", osnovnaCijena * udaljenostKm) + " €");

      if (jeVikend) {
        System.out.println("Popust za vikend: "
            + String.format("%.1f", cjenovniKontekst.getPopustVikend() * 100) + "%");
      }
      if (nacinKupnje.equals("WM")) {
        System.out.println("Web/mobilni popust: "
            + String.format("%.1f", cjenovniKontekst.getPopustWebMob() * 100) + "%");
      } else if (nacinKupnje.equals("V")) {
        System.out.println("Uvećanje za kupnju u vlaku: "
            + String.format("%.1f", cjenovniKontekst.getUvecanjeVlak() * 100) + "%");
      }

      System.out.println("Konačna cijena: " + String.format("%.2f", konacnaCijena) + " €");
      System.out.println("Datum i vrijeme kupnje: "
          + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm")));
      System.out.println("==================");

    } catch (Exception e) {
      System.out.println("Greška pri izračunu cijene: " + e.getMessage());
    }
  }



  private boolean provjeriDaliJeVikend(String datum) {
    try {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
      LocalDate date = LocalDate.parse(datum, formatter);
      DayOfWeek dan = date.getDayOfWeek();
      return dan == DayOfWeek.SATURDAY || dan == DayOfWeek.SUNDAY;
    } catch (Exception e) {
      System.out.println("Greška pri parsiranju datuma: " + e.getMessage());
      return false;
    }
  }

  private void obradiIkkpvKomandu(String[] dijelovi) {
    if (dijelovi.length > 2) {
      System.out.println("Neispravan format komande IKKPV!");
      return;
    }

    if (dijelovi.length == 1) {
      List<KartaMemento> sveKarte = kartaCaretaker.dohvatiSveKarte();
      if (sveKarte.isEmpty()) {
        System.out.println("Nema kupljenih karata!");
        return;
      }

      for (int i = 0; i < sveKarte.size(); i++) {
        System.out.println("\nKarta #" + (i + 1));
        ispisiKartu(sveKarte.get(i));
      }
    } else {
      try {
        int brojKarte = Integer.parseInt(dijelovi[1]);
        KartaMemento karta = kartaCaretaker.dohvatiKartu(brojKarte);

        if (karta == null) {
          System.out.println("Karta broj " + brojKarte + " ne postoji!");
          return;
        }

        ispisiKartu(karta);
      } catch (NumberFormatException e) {
        System.out.println("Broj karte mora biti cijeli broj!");
      }
    }
  }

  private void ispisiKartu(KartaMemento karta) {
    System.out.println("=== KARTA ZA VLAK ===");
    System.out.println("Oznaka vlaka: " + karta.getOznakaVlaka());
    System.out
        .println("Relacija: " + karta.getPolaznaStanica() + " - " + karta.getOdredisnaStanica());
    System.out.println("Datum putovanja: " + karta.getDatum());
    System.out.println("Udaljenost: " + karta.getUdaljenostKm() + " km");
    System.out.println("Način kupnje: " + karta.getNacinKupnje());
    System.out.println("Osnovna cijena: "
        + String.format("%.2f", karta.getOsnovnaCijena() * karta.getUdaljenostKm()) + " €");
    System.out.println("Konačna cijena: " + String.format("%.2f", karta.getKonacnaCijena()) + " €");
    System.out.println("Datum i vrijeme kupnje: "
        + karta.getVrijemeKupnje().format(DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm")));
    System.out.println("==================");
  }

  private void obradiPromjenuStatusaPruge(String[] dijelovi) {
    // Spoji sve dijelove natrag u string
    String unosKomande = String.join(" ", dijelovi);

    // Razdvoji po crticama
    String[] parametri = unosKomande.substring(unosKomande.indexOf(' ')).trim().split(" - ");

    if (parametri.length != 4) {
      System.out.println(
          "Neispravan format komande PSP2S! Očekujem: PSP2S oznakaPruge - polaznaStanica - odredisnaStanica - status");
      return;
    }

    String oznakaPruge = parametri[0].trim();
    String pocetnaStanica = parametri[1].trim();
    String zavrsnaStanica = parametri[2].trim();
    String novoStanje = parametri[3].trim();

    System.out.println("\nParsirani podaci:");
    System.out.println("Oznaka pruge: '" + oznakaPruge + "'");
    System.out.println("Početna stanica: '" + pocetnaStanica + "'");
    System.out.println("Završna stanica: '" + zavrsnaStanica + "'");
    System.out.println("Novo stanje: '" + novoStanje + "'");

    Pruga pruga = dohvatiPrugu(oznakaPruge);
    if (pruga == null) {
      System.out.println("Pruga " + oznakaPruge + " ne postoji");
      return;
    }

    List<Stanica> staniceNaRelaciji = pruga.getStaniceIzmedu(pocetnaStanica, zavrsnaStanica);
    System.out.println("Stanice između su:");
    for (Stanica stanica : staniceNaRelaciji) {
      System.out.println(stanica.getStanica());
    }

    if (staniceNaRelaciji.isEmpty()) {
      System.out.println("Ne postoji direktna relacija između zadanih stanica");
      return;
    }

    pruga.request(pocetnaStanica, zavrsnaStanica, novoStanje);
  }

  private void obradiIspisRelacijaPruga(String[] dijelovi) {
    if (dijelovi.length < 2) {
      System.out.println("Neispravan format komande IRPS");
      return;
    }

    String status = dijelovi[1];
    String oznakaPruge = null;
    if (dijelovi.length > 2) {
      oznakaPruge = dijelovi[2];
    }

    boolean pronadjeneRelacije = false;

    if (oznakaPruge != null) {
      Pruga pruga = dohvatiPrugu(oznakaPruge);
      if (pruga == null) {
        System.out.println("Pruga " + oznakaPruge + " ne postoji");
        return;
      }
      ispisRelacijaZaPrugu(pruga, status);
    } else {
      for (Pruga pruga : pruge) {
        ispisRelacijaZaPrugu(pruga, status);
      }
    }
  }

  private void ispisRelacijaZaPrugu(Pruga pruga, String status) {
    Map<String, RelacijaPrugeContext> relacije = pruga.getRelacije();
    List<RelacijaPrugeContext> relacijeStatusom = new ArrayList<>();

    for (RelacijaPrugeContext relacija : relacije.values()) {
      if (relacija.getTrenutnoStanje().getStatus().equals(status)) {
        relacijeStatusom.add(relacija);
      }
    }

    if (!relacijeStatusom.isEmpty()) {
      System.out
          .println("\nRelacije na pruzi " + pruga.getOznaka() + " sa statusom " + status + ":");
      for (RelacijaPrugeContext relacija : relacijeStatusom) {
        System.out.println(relacija.getPocetnaStanica() + " - " + relacija.getZavrsnaStanica());
      }
    }
  }


}
