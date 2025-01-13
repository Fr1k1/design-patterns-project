package edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.mediator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConcreteMediator implements SustavPopunjenostiVlakovaMediator {
  private Map<String, List<PopunjenostRedak>> popunjenostVlakova = new HashMap<>();

  public ConcreteMediator() {}

  @Override
  public void dodajPopunjenost(AbstractColleagueKorisnik korisnik, String vlak, int popunjenost) {
    if (!popunjenostVlakova.containsKey(vlak)) {
      popunjenostVlakova.put(vlak, new ArrayList<>());
    }
    List<PopunjenostRedak> prijave = popunjenostVlakova.get(vlak);
    prijave.add(new PopunjenostRedak(popunjenost, LocalDateTime.now()));
    korisnik.primiObavijest("Popunjenost uspješno dodana za vlak " + vlak);
  }

  @Override
  public int provjeriPopunjenost(AbstractColleagueKorisnik korisnik, String vlak) {
    if (!popunjenostVlakova.containsKey(vlak)) {
      korisnik.primiObavijest("Nema podataka za vlak " + vlak);
      return 0;
    }

    List<PopunjenostRedak> prijave = popunjenostVlakova.get(vlak);
    LocalDateTime trenutnoVrijeme = LocalDateTime.now();
    LocalDateTime polaSataUnazad = trenutnoVrijeme.minusMinutes(30);

    List<PopunjenostRedak> zadnjih30Minuta = new ArrayList<>();
    int ukupno = 0;
    int brojPrijava = 0;

    for (PopunjenostRedak entry : prijave) {
      if (entry.getVrijeme().isAfter(polaSataUnazad)) {
        zadnjih30Minuta.add(entry);
        ukupno += entry.getPostotak();
        brojPrijava++;
      }
    }

    if (zadnjih30Minuta.isEmpty()) {
      korisnik.primiObavijest("Nema podataka za zadnjih 30 minuta.");
      return 0;
    }

    int sredina = ukupno / brojPrijava;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    StringBuilder message = new StringBuilder();
    message.append("Popunjenosti za vlak ").append(vlak).append(" (zadnjih 30 minuta):\n");
    for (PopunjenostRedak entry : zadnjih30Minuta) {
      message.append("Postotak: ").append(entry.getPostotak()).append("%, Vrijeme: ")
          .append(entry.getVrijeme().format(formatter)).append("\n");
    }
    message.append("Prosječna popunjenost: ").append(sredina).append("%");
    korisnik.primiObavijest(message.toString());

    return sredina;
  }

  private static class PopunjenostRedak {
    private final int postotak;
    private final LocalDateTime vrijeme;

    public PopunjenostRedak(int postotak, LocalDateTime vrijeme) {
      this.postotak = postotak;
      this.vrijeme = vrijeme;
    }

    public int getPostotak() {
      return postotak;
    }

    public LocalDateTime getVrijeme() {
      return vrijeme;
    }
  }
}
