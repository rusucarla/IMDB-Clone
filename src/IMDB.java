import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

// singleton pattern pentru IMDB
// contine listele de regular, actor, request, movie, series
// contine metoda run
public class IMDB {
    // pentru singleton pattern
    private static IMDB instance;
    private List<Regular> regularList;
    private List<User> userList;
    private List<Admin> adminList;
    private List<Contributor> contributorList;
    private List<Actor> actorList;
    private List<Request> requestList;
    private List<Movie> moviesList;
    private List<Series> seriesList;
    private List<Production> productionList;

    // constructor privat
    private IMDB() {
        this.regularList = null;
        this.actorList = null;
        this.requestList = null;
        this.moviesList = null;
        this.seriesList = null;
    }

    // metoda publica pentru a returna instanta
    public static synchronized IMDB getInstance() {
        if (instance == null) {
            instance = new IMDB();
        }
        return instance;
    }

    // metode pentru a incarca listele
    // incarcare lista de actori
    public void loadActors(String actorsPath) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File actorsFile = new File(actorsPath);
            this.actorList = objectMapper.readValue(actorsFile, new TypeReference<List<Actor>>() {
            });
            // Afișează informațiile despre actori
//            System.out.println("Actors:");
//            for (Actor actor : actorList) {
//                System.out.println("Name: " + actor.getName());
//                System.out.println("Biography: " + actor.getBiography());
//                System.out.println("Performances:");
//                for (Actor.Performance performance : actor.getPerformances()) {
//                    System.out.println("  Title: " + performance.getTitle() + ", Type: " + performance.getType());
//                }
//                System.out.println();
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadProductions(String productionsPath) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            File productionsFile = new File(productionsPath);
            this.productionList = mapper.readValue(productionsFile,
                    TypeFactory.defaultInstance().constructCollectionType(List.class, Production.class));

            moviesList = new ArrayList<>();
            seriesList = new ArrayList<>();
            // Procesează sau afișează producțiile
            for (Production production : productionList) {
                // Verifică tipul instanței și procesează conform nevoilor
                if (production instanceof Movie) {
                    // Procesează Movie -> adauga in lista de movies
                    moviesList.add((Movie) production);
                } else if (production instanceof Series) {
                    // Procesează Series -> adauga in lista de series
                    seriesList.add((Series) production);
                }
            }
            // afiseaza lista de filme
//            System.out.println("Movies:");
//            for (Movie movie : moviesList) {
//                movie.displayInfo();
//            }
//            // afiseaza lista de seriale
//            System.out.println("Series:");
//            for (Series series : seriesList) {
//                series.displayInfo();
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // metoda pentru a incarca lista de useri cu JSON Simple
    public void loadUsers(String usersPath) {
        JSONParser parser = new JSONParser();
        regularList = new ArrayList<>();
        adminList = new ArrayList<>();
        contributorList = new ArrayList<>();
        try {
            JSONArray usersArray = (JSONArray) parser.parse(new FileReader(usersPath));
            userList = new ArrayList<>();
            for (Object o : usersArray) {
                JSONObject userObject = (JSONObject) o;
                // extrag informatiile despre user
                String username = (String) userObject.get("username");
                String experienceStr = (String) userObject.get("experience");
                if (experienceStr == null) {
                    experienceStr = "0";
                }
                Integer experience = Integer.parseInt(experienceStr);
                // INFORMATION
                JSONObject informationObject = (JSONObject) userObject.get("information");
                JSONObject credentialsObject = (JSONObject) informationObject.get("credentials");
                String email = (String) credentialsObject.get("email");
                System.out.println("EMAIL: " + email);
                String password = (String) credentialsObject.get("password");
                System.out.println("PASSWORD: " + password);
                Credentials credentials = new Credentials(email, password);
                String name = (String) informationObject.get("name");
                System.out.println(name);
                String country = (String) informationObject.get("country");
                System.out.println("COUNTRY: " + country);
                String gender = (String) informationObject.get("gender");
                System.out.println("GENDER: " + gender);

//                LocalDateTime birthDate = LocalDateTime.parse((String) userObject.get("birthDate"));
                String birthDateStr = (String) informationObject.get("birthDate");
                LocalDate birthDate = null;
                String pattern = "yyyy-MM-dd";
                DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern(pattern);
                if (birthDateStr != null) {
                    birthDate = LocalDate.parse(birthDateStr, customFormatter);
                    System.out.println("BIRTHDATE: " + birthDate);
                }
//                Integer age = ((Long) userObject.get("age")).intValue();
                Object ageObj = informationObject.get("age");
                Integer age = null;
                if (ageObj != null) {
                    age = ((Long) ageObj).intValue();
                }

                // construieste information
                User.Information information = new User.Information.Builder()
                        .setName(name)
                        .setCredentials(credentials)
                        .setBirthDate(birthDate)
                        .setAge(age)
                        .setCountry(country)
                        .setGender(gender)
                        .build();
                // FINISH INFORMATION
                String user_type = (String) userObject.get("userType");
                // user_type -> all caps
                // construieste user
                User user = UserFactory.createUser(information, AccountType.valueOf(user_type.toUpperCase()), username, experience);
                // adauga user in lista potrivita
                // plus sa adaug listele de favorite si de contributii (doar contributor si admin)
                // lista de favorite
                JSONArray favoriteArray = (JSONArray) userObject.get("favoriteActors");
                if (favoriteArray != null) {
                    TreeSet<Actor> favoriteActors = new TreeSet<>();
                    for (Object actorName : favoriteArray) {
                        // vreau sa adaug direct in lista de favorite
                        for (Actor actor : actorList) {
                            if (actor.getName().equals(actorName)) {
                                favoriteActors.add(actor);
                            }
                        }
                    }
                    assert user != null;
                    user.setFavoriteActors(favoriteActors);
                }
                // lista de producții favorite
                JSONArray favoriteProductionArray = (JSONArray) userObject.get("favoriteProductions");
                if (favoriteProductionArray != null) {
                    TreeSet<Production> favoriteProductions = new TreeSet<>();
                    for (Object productionName : favoriteProductionArray) {
                        // vreau sa adaug direct in lista de favorite
                        for (Production production : productionList) {
                            if (production.getTitlu().equals(productionName)) {
                                favoriteProductions.add(production);
                            }
                        }
                    }
                    assert user != null;
                    user.setFavoriteProductions(favoriteProductions);
                }
                // adaug si in lista user pentru login
                userList.add(user);
                // adauga user in lista potrivita
                if (user instanceof Regular) {
                    regularList.add((Regular) user);
                } else if (user instanceof Contributor) {
                    contributorList.add((Contributor) user);
                    // lista de contributii
                    JSONArray contributionArray = (JSONArray) userObject.get("actorsContribution");
                    if (contributionArray != null) {
                        TreeSet<Actor> actorsContribution = new TreeSet<>();
                        for (Object actorName : contributionArray) {
                            // vreau sa adaug direct in lista de contributii
                            for (Actor actor : actorList) {
                                if (actor.getName().equals(actorName)) {
                                    actorsContribution.add(actor);
                                }
                            }
                        }
                        ((Contributor) user).setActorsContribution(actorsContribution);
                    }
                    // lista de contributii
                    JSONArray contributionProductionArray = (JSONArray) userObject.get("productionsContribution");
                    if (contributionProductionArray != null) {
                        TreeSet<Production> productionsContribution = new TreeSet<>();
                        for (Object productionName : contributionProductionArray) {
                            // vreau sa adaug direct in lista de contributii
                            for (Production production : productionList) {
                                if (production.getTitlu().equals(productionName)) {
                                    productionsContribution.add(production);
                                }
                            }
                        }
                        ((Contributor) user).setProductionsContribution(productionsContribution);
                    }
                } else if (user instanceof Admin) {
                    adminList.add((Admin) user);
                    // lista de contributii
                    JSONArray contributionArray = (JSONArray) userObject.get("actorsContribution");
                    if (contributionArray != null) {
                        TreeSet<Actor> actorsContribution = new TreeSet<>();
                        for (Object actorName : contributionArray) {
                            // vreau sa adaug direct in lista de contributii
                            for (Actor actor : actorList) {
                                if (actor.getName().equals(actorName)) {
                                    actorsContribution.add(actor);
                                }
                            }
                        }
                        ((Admin) user).setActorsContribution(actorsContribution);
                    }
                    // lista de contributii
                    JSONArray contributionProductionArray = (JSONArray) userObject.get("productionsContribution");
                    if (contributionProductionArray != null) {
                        TreeSet<Production> productionsContribution = new TreeSet<>();
                        for (Object productionName : contributionProductionArray) {
                            // vreau sa adaug direct in lista de contributii
                            for (Production production : productionList) {
                                if (production.getTitlu().equals(productionName)) {
                                    productionsContribution.add(production);
                                }
                            }
                        }
                        ((Admin) user).setProductionsContribution(productionsContribution);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean checkLogin(String username, String password){
        for(User user : userList){
            if(user.getUserName().equals(username) && user.getInformation().getUserCredentials().getPassword().equals(password)){
                return true;
            }
        }
        return false;
    }
    // getter pentru lista de producții
    public List<Production> getProductionList() {
        return productionList;
    }
    // filtre pentru producții
    // filtru pentru an
    public List<Production> filterByRating(double minRating) {
        return productionList.stream()
                .filter(p -> p.getNotaFilm() >= minRating)
                .collect(Collectors.toList());
    }

}
