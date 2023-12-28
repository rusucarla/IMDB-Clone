import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Movie.class, name = "Movie"),
        @JsonSubTypes.Type(value = Series.class, name = "Series")
})
public abstract class Production implements Comparable<Production> {
    @JsonProperty("title")
    protected String titlu;
    @JsonProperty("directors")
    protected List<String> regizoriList;
    @JsonProperty("actors")
    protected List<String> actoriList;
    @JsonProperty("genres")
    protected List<String> genreList;
    @JsonProperty("ratings")
    protected List<Rating> ratingList;
    @JsonProperty("plot")
    protected String descriereFilm;
    @JsonProperty("averageRating")
    protected double notaFilm;

    // constructor
    public Production(String titlu, List<String> regizori, List<String> actori,
                      List<String> genre, List<Rating> rating, String descriere) {
        this.titlu = titlu;
        this.regizoriList = regizori;
        this.actoriList = actori;
        this.genreList = genre;
        this.ratingList = rating;
        this.descriereFilm = descriere;
        // se va calcula nota
        calculNota();
    }
    // constructor gol
    public Production() {
    }
    // calcul nota finala
    private void calculNota() {
        if (ratingList != null && !ratingList.isEmpty()) {
            double sumaRating = 0;
            for (Rating rating : ratingList) {
                sumaRating += rating.getNotaRating();
            }
            notaFilm = sumaRating / ratingList.size();
        } else {
            notaFilm = 0;
        }
    }

    public String getTitlu(){
        return this.titlu;
    }

    public void setActoriList(List<String> actoriList) {
        this.actoriList = actoriList;
    }

    public void setDescriereFilm(String descriereFilm) {
        this.descriereFilm = descriereFilm;
    }

    public void setGenreList(List<String> genreList) {
        this.genreList = genreList;
    }

    public void setNotaFilm(double notaFilm) {
        this.notaFilm = notaFilm;
    }

    public void setRatingList(List<Rating> ratingList) {
        this.ratingList = ratingList;
    }

    public void setRegizoriList(List<String> regizoriList) {
        this.regizoriList = regizoriList;
    }

    public void setTitlu(String titlu) {
        this.titlu = titlu;
    }

    public double getNotaFilm() {
        return notaFilm;
    }

    public List<Rating> getRatingList() {
        return ratingList;
    }

    public List<String> getActoriList() {
        return actoriList;
    }

    public List<String> getGenreList() {
        return genreList;
    }

    public List<String> getRegizoriList() {
        return regizoriList;
    }

    public String getDescriereFilm() {
        return descriereFilm;
    }

    public abstract void displayInfo();

    @Override
    public int compareTo(Production o) {
        return this.titlu.compareTo(o.titlu);
    }

}
