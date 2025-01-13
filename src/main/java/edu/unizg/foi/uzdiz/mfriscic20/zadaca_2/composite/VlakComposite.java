package edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.composite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.dto.Stanica;
import edu.unizg.foi.uzdiz.mfriscic20.zadaca_2.visitor.VozniRedVisitor;

public class VlakComposite extends PomocniBazniComposite {
  private String oznakaVlaka;
  private String vrstaVlaka;


  public VlakComposite(String oznakaVlaka, String vrstaVlaka) {
    this.oznakaVlaka = oznakaVlaka;
    this.vrstaVlaka = vrstaVlaka;
  }

  @Override
  public void azuriraj(String poruka) {
    // Kad vlak primi obavijest
    System.out.println("Vlak " + this.oznakaVlaka + " primio obavijest: " + poruka);
  }

  public void stigaoNaStanicu(String stanica, String vrijeme) {
    this.notifyObservers(this.oznakaVlaka + " stigao na stanicu " + stanica + " u " + vrijeme);
  }


  @Override
  public boolean dodaj(VozniRedComponent component) {
    if (component instanceof EtapaLeaf) {
      int index = 0;
      for (VozniRedComponent postojecaEtapa : svaDjeca) {
        if (postojecaEtapa instanceof EtapaLeaf) {
          EtapaLeaf etapa = (EtapaLeaf) postojecaEtapa;
          if (((EtapaLeaf) component).getVrijemePolaskaUMinutama() < etapa
              .getVrijemePolaskaUMinutama()) {
            break;
          }
        }
        index++;
      }
      svaDjeca.add(index, component);
    } else {
      svaDjeca.add(component);
    }
    return true;
  }

  @Override
  public void prihvati(VozniRedVisitor visitor) {
    visitor.posjetiElement(this);
  }

  @Override
  public VozniRedComponent dohvatiDijete(String oznakaPruge) {
    for (VozniRedComponent etapa : svaDjeca) {
      if (etapa instanceof EtapaLeaf && ((EtapaLeaf) etapa).getOznakaPruge().equals(oznakaPruge)) {
        return etapa;
      }
    }
    return null;
  }

  @Override
  public void prikaziDetalje() {

  }

  public String getOznakaVlaka() {
    return oznakaVlaka;
  }

  public void setOznakaVlaka(String oznakaVlaka) {
    this.oznakaVlaka = oznakaVlaka;
  }

  public String getVrstaVlaka() {
    return vrstaVlaka;
  }

  public void setVrstaVlaka(String vrstaVlaka) {
    this.vrstaVlaka = vrstaVlaka;
  }

  public boolean voziNaDan(String dan) {
    for (VozniRedComponent etapa : svaDjeca) {
      if (etapa instanceof EtapaLeaf) {
        String daniVoznje = ((EtapaLeaf) etapa).getDaniVoznje();
        if (!daniVoznje.contains(dan)) {
          return false;
        }
      }
    }
    return true;
  }

  public boolean voziUVremenskomRasponu(int odVrijeme, int doVrijeme) {
    if (svaDjeca.isEmpty()) {
      System.out.println("Vlak nema etapa");
      return false;
    }

    EtapaLeaf prvaEtapa = (EtapaLeaf) svaDjeca.get(0);
    EtapaLeaf zadnjaEtapa = (EtapaLeaf) svaDjeca.get(svaDjeca.size() - 1);
    boolean uvjet = prvaEtapa.getVrijemePolaskaUMinutama() >= odVrijeme
        && zadnjaEtapa.getVrijemeDolaskaUMinutama() <= doVrijeme;

    return uvjet;
  }

  public boolean prolaziTrazenimPutem(String polaznaStanica, String odredisnaStanica) {
    // System.out.println("\nProvjera vlaka za put " + polaznaStanica + " -> " + odredisnaStanica);
    boolean imaPocetnu = false;
    boolean imaZavrsnu = false;

    for (VozniRedComponent etapa : svaDjeca) {
      if (etapa instanceof EtapaLeaf) {
        EtapaLeaf etapaLeaf = (EtapaLeaf) etapa;
        // System.out.println("Provjera etape na pruzi: " + etapaLeaf.getOznakaPruge());
        // System.out.println("Stanice na etapi:");

        List<Stanica> stanice = etapaLeaf.getStaniceEtape();

        if (etapaLeaf.getSmjer().equals("O")) {
          stanice = new ArrayList<>(stanice);
          Collections.reverse(stanice);
        }

        for (Stanica stanica : stanice) {
          if (!imaPocetnu && stanica.getStanica().equals(polaznaStanica)) {
            imaPocetnu = true;
            continue;
          }
          if (imaPocetnu && !imaZavrsnu && stanica.getStanica().equals(odredisnaStanica)) {
            imaZavrsnu = true;
            break;
          }
        }
      }
    }


    return imaPocetnu && imaZavrsnu;
  }

  public String dohvatiVrijemePolaska(Stanica stanica) {
    for (VozniRedComponent etapa : svaDjeca) {
      if (etapa instanceof EtapaLeaf) {
        EtapaLeaf etapaLeaf = (EtapaLeaf) etapa;
        if (etapaLeaf.getStaniceEtape().stream()
            .anyMatch(s -> s.getStanica().equals(stanica.getStanica()))) {

          if (etapaLeaf.getSmjer().equals("O")) {
            if (stanica.getStanica().equals(etapaLeaf.getOdredisnaStanicaEtape())) {
              return etapaLeaf.formatirajVrijeme(etapaLeaf.getVrijemeDolaskaUMinutama());
            } else {
              int vrijemeDoStanice = izracunajVrijemeDoStaniceObrnutiSmjer(etapaLeaf, stanica);
              return etapaLeaf.formatirajVrijeme(vrijemeDoStanice);
            }
          } else {
            int vrijemeDoStanice = izracunajVrijemeDoStaniceNormalanSmjer(etapaLeaf, stanica);
            return etapaLeaf.formatirajVrijeme(vrijemeDoStanice);
          }
        }
      }
    }
    return "";
  }

  private int izracunajVrijemeDoStaniceNormalanSmjer(EtapaLeaf etapa, Stanica cilj) {
    int ukupnoVrijeme = etapa.getVrijemePolaskaUMinutama();
    int ukupniKm = 0;

    for (Stanica s : etapa.getStaniceEtape()) {
      if (s.getStanica().equals(cilj.getStanica())) {
        return ukupnoVrijeme
            + (int) (ukupniKm * etapa.getTrajanjeVoznjeUMinutama() / etapa.getUdaljenost());
      }
      ukupniKm += s.getDuzina();
    }
    return ukupnoVrijeme;
  }

  private int izracunajVrijemeDoStaniceObrnutiSmjer(EtapaLeaf etapa, Stanica cilj) {
    int ukupnoVrijeme = etapa.getVrijemeDolaskaUMinutama();
    int ukupniKm = 0;
    List<Stanica> obrnuteStanice = new ArrayList<>(etapa.getStaniceEtape());
    Collections.reverse(obrnuteStanice);

    for (Stanica s : obrnuteStanice) {
      if (s.getStanica().equals(cilj.getStanica())) {
        return ukupnoVrijeme
            - (int) (ukupniKm * etapa.getTrajanjeVoznjeUMinutama() / etapa.getUdaljenost());
      }
      ukupniKm += s.getDuzina();
    }
    return ukupnoVrijeme;
  }
}
