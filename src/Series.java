import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class Series extends Production {

    @JsonProperty("releaseYear")
    private int releaseAn;
    @JsonProperty("numSeasons")
    private int numarSezoane;
    @JsonProperty("seasons")
    private Map<String, List<Episode>> mapSerial;

    public Series(String titlu, List<String> regizori, List<String> actori,
                  List<String> genre, List<Rating> rating, String descriere,
                  int releaseYear, int numarSeasons) {
        super(titlu, regizori, actori, genre, rating, descriere);
        this.numarSezoane = numarSeasons;
        this.releaseAn = releaseYear;
    }
    public Series() {
    }

    // gettere
    public int getReleaseAn() {
        return releaseAn;
    }
    public int getNumarSezoane() {
        return numarSezoane;
    }
    public Map<String, List<Episode>> getMapSerial() {
        return mapSerial;
    }
    // settere
    public void setReleaseAn(int releaseAn) {
        this.releaseAn = releaseAn;
    }
    public void setNumarSezoane(int numarSezoane) {
        this.numarSezoane = numarSezoane;
    }
    public void setMapSerial(Map<String, List<Episode>> mapSerial) {
        this.mapSerial = mapSerial;
    }
    public List<Episode> getEpisodes(String sezon) {
        return mapSerial.get(sezon);
    }

    @Override
    public void displayInfo() {
        if (this.titlu != null)
            System.out.println("Titlu: " + this.titlu);
        if (this.regizoriList != null)
            System.out.println("Regizori: " + this.regizoriList);
        System.out.println("Actori: " + this.actoriList);
        System.out.println("Genul programului: " + this.genreList);
        System.out.println("Descriere: " + this.descriereFilm);
        System.out.println("An Lansare: " + this.releaseAn);
        System.out.println("Numar sezoane: " + this.numarSezoane);
        System.out.println("Nota generala: " + this.notaFilm);
        // afisare episoade
        for (Map.Entry<String, List<Episode>> sezon : mapSerial.entrySet()) {
            System.out.println("Sezon: " + sezon.getKey());
            System.out.println("Episoade: ");
            for (Episode episod : sezon.getValue()) {
                System.out.println(episod);  // va apela episod.toString()
            }
        }
        // afisare rating
        if (this.ratingList != null) {
            System.out.println("Rating: ");
            this.ratingList.sort((o1, o2) -> {
                User user1 = IMDB.getInstance().getUser(o1.getUsernameRater());
                User user2 = IMDB.getInstance().getUser(o2.getUsernameRater());
                // sortez descrescator
                return Integer.compare(user2.getExperience(), user1.getExperience());
            });
            for (Rating rating : this.ratingList) {
                System.out.println(rating.getUsernameRater() + " " +
                        rating.getNotaRating() + " " + rating.getComentariiRater());
            }
        }
    }

    @Override
    public void addObserver(Observer observer) {

    }

    @Override
    public void removeObserver(Observer observer) {

    }

    @Override
    public void notifyAllObservers(String notification) {

    }
}
