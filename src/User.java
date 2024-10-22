import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;


public abstract class User implements Comparable<User>, Observer {
    public TreeSet<Actor> favoriteActors;
    public TreeSet<Production> favoriteProductions;
    private Information information;
    private AccountType userType;
    private String username;
    private int experience;
    private ExperienceStrategy experienceStrategy;
    private List<String> notifications;

    public User(Information info, AccountType cont, String username,
                int exp) {
        this.information = info;
        this.userType = cont;
        this.username = username;
        this.experience = exp;
        this.notifications = new ArrayList<>();
        this.favoriteActors = new TreeSet<>();
        this.favoriteProductions = new TreeSet<>();
        addObservers();
    }

    public User() {

    }

    public String getUserName() {
        return this.username;
    }

    public void setUserName(String new_username) {
        this.username = new_username;
    }

    public Information getInformation() {
        return this.information;
    }

    public void setInformation(Information new_information) {
        this.information = new_information;
    }

    public AccountType getUserType() {
        return this.userType;
    }

    public void setUserType(AccountType new_userType) {
        this.userType = new_userType;
    }

    public int getExperience() {
        return this.experience;
    }

    public void setExperience(int new_experience) {
        this.experience = new_experience;
    }

    public List<String> getNotifications() {
        return this.notifications;
    }

    public void setNotifications(List<String> new_notifications) {
        this.notifications = new_notifications;
    }

    public TreeSet<Actor> getFavoriteActors() {
        return this.favoriteActors;
    }

    public void setFavoriteActors(TreeSet<Actor> new_favoriteActors) {
        this.favoriteActors = new_favoriteActors;
    }

    public TreeSet<Production> getFavoriteProductions() {
        return this.favoriteProductions;
    }

    public void setFavoriteProductions(TreeSet<Production> new_favoriteProductions) {
        this.favoriteProductions = new_favoriteProductions;
        this.addObservers();
    }

    //    public void add_favorite_movie(Movie new_movie) {
//        this.favoriteProductions.add(new_movie);
//    }
//
//    public void add_favorite_series(Series new_series) {
//        this.favoriteProductions.add(new_series);
//    }
    public void add_favorite_production(Production new_production) {
        this.favoriteProductions.add(new_production);
//        System.out.println("New favorite production: " + new_production.getTitlu());
        // trebuie sa devina observer pentru aceasta productie
        new_production.addObserver(this);
    }

    public void add_favorite_actor(Actor new_actor) {
        this.favoriteActors.add(new_actor);
    }

    //    public void delete_favorite_movie(Movie removed_movie) {
//        this.favoriteActors.remove(removed_movie);
//    }
//
//    public void remove_favorite_series(Series removed_series) {
//        this.favoriteProductions.remove(removed_series);
//    }
    public void remove_favorite_production(Production removed_production) {
        this.favoriteProductions.remove(removed_production);
        removed_production.removeObserver(this);
    }

    public void remove_favorite_actor(Actor removed_actor) {
        this.favoriteActors.remove(removed_actor);
    }

    public void add_exp(int added_exp) {
        this.experience += added_exp;
    }
    public void setExperienceStrategy(ExperienceStrategy experienceStrategy) {
        this.experienceStrategy = experienceStrategy;
    }
    public void updateExperience() {
        if (experienceStrategy != null) {
            experience += experienceStrategy.calculateExperience();
        }
    }
    public void update(String notification) {
        this.notifications.add(notification);
//        System.out.println("Notification for " + this.username + ": " + notification);
    }
    // metoda pentru a adauga observer la toate productiile din lista de favorite
    public void addObservers() {
        if (this instanceof Contributor || this instanceof Admin) {
            Staff staff = (Staff) this;
            staff.addObservers();
        } else {
            if (this.favoriteProductions == null || this.favoriteProductions.isEmpty()) {
                return;
            }
            for (Production production : this.favoriteProductions) {
                production.addObserver(this);
            }
            if (this.favoriteActors == null || this.favoriteActors.isEmpty()) {
                return;
            }
            for (Actor actor : this.favoriteActors) {
                actor.addObserver(this);
            }
            // vreau sa devina observer si pentru toate productiile la care a dat rating
            // doar daca user e Regular
            if (this instanceof Regular) {
                Regular regular = (Regular) this;
                if (regular.getUserRatings() == null) {
                    return;
                }
                for (Rating rating : regular.getUserRatings()) {
                    rating.addObserver(this);
                }
            }
        }
    }

    public static class Information {
        private Credentials credentials;
        private String name;
        private Integer age;
        private LocalDate birthDate;
        private String birthDay;
        private String country;
        private String gender;

        public Information(Credentials c, String nume, Integer age,
                           LocalDate DOB) {
            this.age = age;
            this.birthDate = DOB;
            this.name = nume;
            this.credentials = c;
        }

        public Information() {

        }

        private Information(Builder builder) {
            this.name = builder.name;
            this.age = builder.age;
            this.birthDay = builder.birthDay;
            this.credentials = builder.credentials;
            this.birthDate = builder.birthDate;
            this.country = builder.country;
            this.gender = builder.gender;
        }

        // getter
        public String getUserNume() {
            return this.name;
        }

        // setter
        public void setUserNume(String new_name) {
            this.name = new_name;
        }

        public int getUserAge() {
            return this.age;
        }

        public void setUserAge(Integer new_age) {
            this.age = new_age;
        }

        public String getUserBirthday() {
            return this.birthDay;
        }

        public void setUserBirthday(String new_birthday) {
            this.birthDay = new_birthday;
        }

        public String getUserCountry() {
            return this.country;
        }

        public void setUserCountry(String new_country) {
            this.country = new_country;
        }

        public String getGender() {
            return this.gender;
        }

        public void setGender(String new_gender) {
            this.gender = new_gender;
        }

        public LocalDate getBirthDate() {
            return this.birthDate;
        }

        public void setBirthDate(LocalDate new_birthDate) {
            this.birthDate = new_birthDate;
        }

        public Credentials getUserCredentials() {
            return this.credentials;
        }

        public void setUserCredentials(Credentials new_credentials) {
            this.credentials = new_credentials;
        }

        String generateUsername(String name) {
            int randomNr = new Random().nextInt(1000) + 1;
            return name.toLowerCase().replace(" ", "_") +
                    +randomNr;
        }

        public static class Builder {
            private Credentials credentials;
            private String name;
            private Integer age;
            private LocalDate birthDate;
            private String birthDay;
            private String country;
            private String gender;

            public Builder setCredentials(Credentials credentials) {
                this.credentials = credentials;
                return this;
            }

            public Builder setName(String name) {
                this.name = name;
                return this;
            }

            public Builder setAge(Integer age) {
                this.age = age;
                return this;
            }

            public Builder setBirthDate(LocalDate birthDate) {
                this.birthDate = birthDate;
                return this;
            }

            public Builder setBirthDay(String birthDay) {
                this.birthDay = birthDay;
                return this;
            }

            public Builder setCountry(String country) {
                this.country = country;
                return this;
            }

            public Builder setGender(String gender) {
                this.gender = gender;
                return this;
            }

            public Information build() {
                return new Information(this);
            }
        }
    }
}
