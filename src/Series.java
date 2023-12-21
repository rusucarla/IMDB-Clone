import java.util.List;
import java.util.Map;

public class Series extends Production {

    private int releaseAn;
    private int numarSezoane;
    private Map<String, List<Episode>> mapSerial;

    public Series(String titlu, List<String> regizori, List<String> actori,
                  List<String> genre, List<Rating> rating, String descriere,
                  int releaseYear, int numarSeasons) {
        super(titlu, regizori, actori, genre, rating, descriere);
        this.numarSezoane = numarSeasons;
        this.releaseAn = releaseYear;
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
        for (Map.Entry<String, List<Episode>> episod : mapSerial.entrySet()) {
            System.out.println("Sezon: " + episod.getKey() + "Episoade: " +
                    episod.getValue());
        }
    }
}
