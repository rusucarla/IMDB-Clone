import java.util.List;

public class Movie extends Production {
    private int runTimp;
    private int releaseAn;

    public Movie(String titlu, List<String> regizori, List<String> actori,
                 List<String> genre, List<Rating> rating, String descriere,
                 int runTime, int releaseYear) {
        super(titlu, regizori, actori, genre, rating, descriere);
        this.runTimp = runTime;
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
        System.out.println("Durata: " + this.runTimp + " minute");
        System.out.println("An Lansare: " + this.releaseAn);
        System.out.println("Nota generala: " + this.notaFilm);
    }
}
