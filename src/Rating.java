import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Rating implements Subject {
    @JsonProperty("username")
    String usernameRater;

    @JsonProperty("rating")
    int notaRating;

    @JsonProperty("comment")
    String comentariiRater;

    List<Observer> observers = new ArrayList<>();

    public Rating(String username, int nota, String comentarii){
        this.comentariiRater = comentarii;
        this.usernameRater = username;
        int rezultat = validareNota(nota);
        this.notaRating = nota;
    }
    public Rating(){
    }
    public String getUsernameRater(){
        return usernameRater;
    }
    public String getComentariiRater(){
        return comentariiRater;
    }
    public Integer getNota(){
        return notaRating;
    }
    public void setUsernameRater(String usernameRater) {
        this.usernameRater = usernameRater;
    }
    public void setComentariiRater(String comentariiRater) {
        this.comentariiRater = comentariiRater;
    }
    public void setNotaRating(int notaRating) {
        this.notaRating = notaRating;
    }


    public int getNotaRating() {
        return this.notaRating;
    }
    private int validareNota(int notaRating){
        return Math.max(1, Math.min(10, notaRating));
    }

    @Override
    public void addObserver(Observer observer) {
        this.observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        this.observers.remove(observer);
    }

    @Override
    public void notifyAllObservers(String notification) {
        for (Observer observer : observers) {
            observer.update(notification);
        }
    }
}
