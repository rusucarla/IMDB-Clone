import java.util.List;

public abstract class Production implements Comparable<Production> {
    protected String titlu;
    protected List<String> regizoriList;
    protected List<String> actoriList;
    protected List<String> genreList;
    protected List<Rating> ratingList;
    protected String descriereFilm;
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
