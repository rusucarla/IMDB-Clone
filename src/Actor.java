import java.util.ArrayList;
import java.util.List;

public class Actor implements Comparable<Actor>, Subject{
    private String name;
    private List<Performance> performances;
    private String biography;
    private double notaActor;
    private List<Rating> ratingList;
    private List<Observer> observers;
    private List<Rating> deletedRatings;

    // Constructor
    public Actor(){}
    public Actor(String name, List<Performance> performances, String biography) {
        this.name = name;
        this.performances = performances;
        this.biography = biography;
        this.ratingList = new ArrayList<>();
        this.observers = new ArrayList<>();
        this.deletedRatings = new ArrayList<>();
        calculNota();
    }

    @Override
    public String toString() {
        return this.getName();
    }

    // Getter methods
    public String getName() {
        return name;
    }

    public List<Performance> getPerformances() {
        return performances;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPerformances(List<Performance> performances) {
        this.performances = performances;
    }
    public double getNotaActor() {
        return notaActor;
    }
    public void setNotaActor(double notaActor) {
        this.notaActor = notaActor;
    }
    public List<Rating> getRatingList() {
        return ratingList;
    }
    public void setRatingList(List<Rating> ratingList) {
        this.ratingList = ratingList;
    }
    public List<Rating> getDeletedRatings() {
        return deletedRatings;
    }

    private void calculNota() {
        if (ratingList != null && !ratingList.isEmpty()) {
            double sumaRating = 0;
            for (Rating rating : ratingList) {
                sumaRating += rating.getNotaRating();
            }
            notaActor = sumaRating / ratingList.size();
        } else {
            notaActor = 0;
        }
    }

    public void addRating(Rating rating){
        if (ratingList == null)
            ratingList = new ArrayList<>();
        this.ratingList.add(rating);
        calculNota();
        // vreau sa adaug si utilizatorul care a dat rating ca observer
        User user = IMDB.getInstance().getUser(rating.getUsernameRater());
        addObserver(user);
        notifyAllObservers("Actor " + this.name + " a primit rating de la " + rating.getUsernameRater());
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
        if (observers == null)
            observers = new ArrayList<>();
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
    }

    @Override
    public int compareTo(Actor o) {
        return this.name.compareTo(o.getName());
    }

    public void displayInfo() {
        System.out.println("Nume: " + this.getName());
        System.out.println("Biografie: " + this.getBiography());
        System.out.println("Nota: " + this.getNotaActor());
        System.out.println("Performante: ");
        for (Performance performance : this.getPerformances()) {
            System.out.println(performance.getTitle() + " - " + performance.getType());
        }
        if(this.getRatingList() == null || this.getRatingList().isEmpty()){
            System.out.println("Nu are rating-uri");
            return;
        }
        System.out.println("Ratings: ");
        for (Rating rating : this.getRatingList()) {
            System.out.println(rating.getUsernameRater() + " - " + rating.getNotaRating() + " - " + rating.getComentariiRater());
        }
    }

    public void setDeletedRatings(ArrayList<Rating> ratingList) {
        this.deletedRatings = ratingList;
    }

    // Inner class representing performances
    public static class Performance {
        private String title;
        private String type;

        // Constructor for Performance
        public Performance(){}
        public Performance(String title, String type) {
            this.title = title;
            this.type = type;
        }

        // Getter methods
        public String getTitle() {
            return title;
        }

        public String getType() {
            return type;
        }
        // Setter methods
        public void setTitle(String title) {
            this.title = title;
        }
        public void setType(String type) {
            this.type = type;
        }
    }
}

