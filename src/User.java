import java.time.LocalDateTime;
import java.util.*;

public abstract class User implements Comparable<User> {
    private Information information;
    private AccountType tipCont;
    private String userName;
    private int userExp;
    private List<String> userNotifs;
    private SortedSet<Object> userFavorites;

    public User(Information info, AccountType cont, String username,
                int exp) {
        this.information = info;
        this.tipCont = cont;
        this.userName = username;
        this.userExp = exp;
        this.userNotifs = new ArrayList<>();
        this.userFavorites = new TreeSet<>();
    }

    public String getUserName() {
        return this.userName;
    }

    public void add_favorite_movie(Movie new_movie) {
        this.userFavorites.add(new_movie);
    }

    public void add_favorite_series(Series new_series) {
        this.userFavorites.add(new_series);
    }

    public void add_favorite_actor(Actor new_actor) {
        this.userFavorites.add(new_actor);
    }

    public void delete_favorite_movie(Movie removed_movie) {
        this.userFavorites.remove(removed_movie);
    }

    public void remove_favorite_series(Series removed_series) {
        this.userFavorites.remove(removed_series);
    }

    public void remove_favorite_actor(Actor removed_actor) {
        this.userFavorites.remove(removed_actor);
    }

    public void add_exp(int added_exp) {
        this.userExp += added_exp;
    }

    public static class Information {
        private Credentials userCredentials;
        private String userNume;
        private int userAge;
        private LocalDateTime userBirthday;

        public Information(Credentials c, String nume, int age,
                           LocalDateTime DOB) {
            this.userAge = age;
            this.userBirthday = DOB;
            this.userNume = nume;
            this.userCredentials = c;
        }

        // getter
        public String getUserNume() {
            return this.userNume;
        }

        public int getUserAge() {
            return userAge;
        }

        public LocalDateTime getUserBirthday() {
            return userBirthday;
        }

        public Credentials getUserCredentials() {
            return userCredentials;
        }

        // builder class for information
        private String generateUsername(String name) {
            int randomNr = new Random().nextInt(1000) + 1;
            return name.toLowerCase().replace(" ", "_") +
                    +randomNr;
        }
    }
}
