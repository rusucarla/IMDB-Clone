import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.ArrayList;
import java.util.List;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Movie.class, name = "Movie"),
        @JsonSubTypes.Type(value = Series.class, name = "Series")
})
public abstract class Production implements Comparable<Production>, Subject {
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
    private List<Observer> observers = new ArrayList<>();
    private List<Rating> deletedRatings = new ArrayList<>();

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

    @Override
    public String toString() {
        return this.getTitlu();
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

    public abstract void displayInfo(
    );

    public List<Rating> getDeletedRatings() {
        return deletedRatings;
    }

    @Override
    public int compareTo(Production o) {
        return this.titlu.compareTo(o.titlu);
    }

    public void addRating(Rating rating) {
        this.ratingList.add(rating);
        calculNota();
        // vreau sa sortez lista de rating-uri in functie de exp-ul userilor
        this.ratingList.sort((o1, o2) -> {
            User user1 = IMDB.getInstance().getUser(o1.getUsernameRater());
            User user2 = IMDB.getInstance().getUser(o2.getUsernameRater());
            // sortez descrescator
            return Integer.compare(user2.getExperience(), user1.getExperience());
        });
        // vreau sa adaug si utilizatorul care a dat rating ca observer
        User user = IMDB.getInstance().getUser(rating.getUsernameRater());
        addObserver(user);
        // vreau sa notific userii care au adaugat acest film la favorite
        // si user-ul care a adaugat productia in sistem
        String notification = "New rating for " + this.titlu + " from " + rating.getUsernameRater() +
                " with " + rating.getNotaRating() + " and comment: " + rating.getComentariiRater();
        notifyAllObservers(notification);
    }

    public void notifyAllObservers(String notification) {
        System.out.println("Notifying all observers");
        System.out.println("Number of observers: " + observers.size());
        for (Observer observer : observers) {
            System.out.println("Observer " + observer);
            observer.update(notification);
        }
    }
    public void addObserver(Observer observer) {
//        System.out.println("Adding observer " + observer);
        observers.add(observer);
    }
    public void removeObserver(Observer observer) {
//        System.out.println("Removing observer " + observer);
        observers.remove(observer);
    }

    public void removeRating(String username) {
        for (Rating rating : ratingList) {
            if (rating.getUsernameRater().equals(username)) {
                ratingList.remove(rating);
                deletedRatings.add(rating);
                // vreau sa scot de la observer user-ul care a dat rating
                User user = IMDB.getInstance().getUser(username);
                removeObserver(user);
                calculNota();
                break;
            }
        }
        // vreau sa resorteze lista de rating-uri in functie de exp-ul userilor
        this.ratingList.sort((o1, o2) -> {
            User user1 = IMDB.getInstance().getUser(o1.getUsernameRater());
            User user2 = IMDB.getInstance().getUser(o2.getUsernameRater());
            // sortez descrescator
            return Integer.compare(user2.getExperience(), user1.getExperience());
        });
    }

    public String getTip() {
        if (this instanceof Movie) {
            return "Movie";
        } else {
            return "Series";
        }
    }
}
