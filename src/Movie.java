import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Movie extends Production {
    @JsonProperty("duration")
    private String runTimp;
    @JsonProperty("releaseYear")
    private int releaseAn;

    public Movie(String titlu, List<String> regizori, List<String> actori,
                 List<String> genre, List<Rating> rating, String descriere,
                 String runTime, int releaseYear) {
        super(titlu, regizori, actori, genre, rating, descriere);
        this.runTimp = runTime;
        this.releaseAn = releaseYear;
    }
    public Movie() {
    }

    // gettere
    public String getRunTimp() {
        return runTimp;
    }
    @JsonProperty("releaseYear")
    public int getReleaseAn() {
        return releaseAn;
    }
    // settere
    public void setRunTimp(String runTimp) {
        this.runTimp = runTimp;
    }
    @JsonProperty("releaseYear")
    public void setReleaseAn(int releaseAn) {
        this.releaseAn = releaseAn;
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
        System.out.println("Durata: " + this.runTimp + " minute");
        System.out.println("An Lansare: " + this.releaseAn);
        System.out.println("Nota generala: " + this.notaFilm);
        // afisare rating
        if (this.ratingList != null) {
            System.out.println("Rating: ");
            for (Rating rating : this.ratingList) {
                System.out.println(rating.getUsernameRater() + " " +
                        rating.getNotaRating() + " " + rating.getComentariiRater());
            }
        }
    }

    @Override
    public void addObserver(Observer observer) {
        super.addObserver(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        super.removeObserver(observer);
    }
}
