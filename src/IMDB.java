import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

// singleton pattern pentru IMDB
// contine listele de regular, actor, request, movie, series
// contine metoda run
public class IMDB {
    // pentru singleton pattern
    private static IMDB instance = null;
    private List<Regular> regularList;
    private List<Actor> actorList;
    private List<Request> requestList;
    private List<Movie> moviesList;
    private List<Series> seriesList;

    // constructor privat
    private IMDB() {
        this.regularList = null;
        this.actorList = null;
        this.requestList = null;
        this.moviesList = null;
        this.seriesList = null;
    }

    // metoda publica pentru a returna instanta
    public static IMDB getInstance() {
        if (instance == null) {
            instance = new IMDB();
        }
        return instance;
    }

    public void run() // runtime function
    {
        // trebuie sa extrag datele din JSON-uri
        // si sa le pun in listele corespunzatoare
        // pentru a putea face operatiile cerute
        // in cerinta
        // extrag datele din JSON-uri cu Jackson
        // si le pun in listele corespunzatoare

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // JSON-urile sunt : actors.json, accounts.json, productions.json
            // extrag datele din actors.json
            this.actorList = objectMapper.readValue(
                    new File("actors.json"),
                    objectMapper.getTypeFactory().constructCollectionType
                            (List.class, Actor.class));
            // extrag datele din accounts.json
            // si trebuie sa construiesc lista de useri cu ajutorul UserFactory
            this.regularList = objectMapper.readValue(
                    new File("accounts.json"),
                    objectMapper.getTypeFactory().constructCollectionType
                            (List.class, Regular.class));
            // extrag datele din productions.json
            // si trebuie sa construiesc listele de filme si seriale
            // cu ajutorul ProductionFactory
            this.moviesList = objectMapper.readValue(
                    new File("productions.json"),
                    objectMapper.getTypeFactory().constructCollectionType
                            (List.class, Movie.class));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
