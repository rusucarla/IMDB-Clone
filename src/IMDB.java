import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
//             afiseaza lista de filme
//            System.out.println("Movies:");
//            for (Movie movie : moviesList) {
//                movie.displayInfo();
//            }
//            // afiseaza lista de seriale
//            System.out.println("Series:");
//            for (Series series : seriesList) {
//                series.displayInfo();
//            }
//            System.out.println("Productions:");
//            for (Production production : productionList) {
//                System.out.println("Title: " + production.getTitlu());
//                System.out.println("Actors:");
//                for (String actor : production.getActoriList()) {
//                    System.out.println("  Name: " + actor);
//                }
//                System.out.println("Directors:");
//                for (String director : production.getRegizoriList()) {
//                    System.out.println("  Name: " + director);
//                }
//                System.out.println("Genres:");
//                for (String genre : production.getGenreList()) {
//                    System.out.println("  Name: " + genre);
//                }
//                System.out.println("Description: " + production.getDescriereFilm());
//                System.out.println("Rating: " + production.getNotaFilm());
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
//                System.out.println("EMAIL: " + email);
                String password = (String) credentialsObject.get("password");
//                System.out.println("PASSWORD: " + password);
                Credentials credentials = new Credentials(email, password);
                String name = (String) informationObject.get("name");
//                System.out.println(name);
                String country = (String) informationObject.get("country");
//                System.out.println("COUNTRY: " + country);
                String gender = (String) informationObject.get("gender");
//                System.out.println("GENDER: " + gender);

//                LocalDateTime birthDate = LocalDateTime.parse((String) userObject.get("birthDate"));
                String birthDateStr = (String) informationObject.get("birthDate");
                LocalDate birthDate = null;
                String pattern = "yyyy-MM-dd";
                DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern(pattern);
                if (birthDateStr != null) {
                    birthDate = LocalDate.parse(birthDateStr, customFormatter);
//                    System.out.println("BIRTHDATE: " + birthDate);
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

    public boolean checkLogin(String username, String password) {
        for (User user : userList) {
            if (user.getUserName().equals(username) && user.getInformation().getUserCredentials().getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    // verificare tip de user
    public String checkUserType(String username) {
        for (User user : userList) {
            if (user.getUserName().equals(username)) {
                return user.getUserType().toString();
            }
        }
        return null;
    }

    // getter pentru lista de producții
    public List<Production> getProductionList() {
        return this.productionList;
    }

    // getter pentru lista de actori
    public List<Actor> getActorList() {
        return this.actorList;
    }

    // getter pentru anumit user dupa username
    public User getUser(String username) {
        for (User user : userList) {
            if (user.getUserName().equals(username)) {
                return user;
            }
        }
        return null;
    }

    // getter pentru lista de useri
    public List<User> getUserList() {
        return this.userList;
    }

    // filtre pentru producții
    // filtru pentru an
    public List<Production> filterByRating(double minRating, List<Production> productionList) {
        return productionList.stream()
                .filter(p -> p.getNotaFilm() >= minRating)
                .collect(Collectors.toList());
    }

    // filtru pentru gen
    public List<Production> filterByGenre(String genre, List<Production> productionList) {
        return productionList.stream()
                .filter(p -> p.getGenreList().contains(genre))
                .collect(Collectors.toList());
    }

    // filtru dupa regizor
    public List<Production> filterByDirector(String director, List<Production> productionList) {
        return productionList.stream()
                .filter(p -> {
                    for (String directorName : p.getRegizoriList()) {
                        if (extractLastName(directorName).equalsIgnoreCase(director)) {
                            return true; // Regizorul se potrivește cu numele de familie dat
                        }
                    }
                    return false;  // Niciun regizor nu se potrivește
                })
                .collect(Collectors.toList());
    }

    private String extractLastName(String directorName) {
        String[] names = directorName.split(" ");
        return names[names.length - 1];
    }

    public List<Production> filterByTitle(String searchText, List<Production> filteredProductions) {
        return filteredProductions.stream()
                .filter(p -> p.getTitlu().toLowerCase().contains(searchText.toLowerCase()))
                .collect(Collectors.toList());
    }

    public boolean checkActor(String name) {
        for (Actor actor : actorList) {
            if (actor.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public void modifyActor(Actor selectedActor, Actor actor) {
        selectedActor.setName(actor.getName());
        selectedActor.setBiography(actor.getBiography());
        selectedActor.setPerformances(actor.getPerformances());
    }

    // getter pentru lista de filme
    public List<Movie> getMoviesList() {
        return this.moviesList;
    }

    // getter pentru lista de seriale
    public List<Series> getSeriesList() {
        return this.seriesList;
    }

    public List<Actor> filterByActor(String searchText, List<Actor> filteredActors) {
        return filteredActors.stream()
                .filter(a -> a.getName().toLowerCase().contains(searchText.toLowerCase()))
                .collect(Collectors.toList());
    }

    public void loadRequests(String pathRequest){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            // objectMapper nu va recunoaste campul de data, asa ca trebuie sa ii spun eu
            // sa nu se opreasca daca nu recunoaste un camp
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            File requestsFile = new File(pathRequest);
            this.requestList = objectMapper.readValue(requestsFile, new TypeReference<List<Request>>() {
            });
            // vreau sa extrag manual din JSON data de la request
            // pentru ca nu pot sa o extrag cu objectMapper
            List<LocalDateTime> dataRequestList = new ArrayList<>();
            JSONParser parser = new JSONParser();
            JSONArray requestsArray = (JSONArray) parser.parse(new FileReader(pathRequest));
            for (int i = 0; i < requestsArray.size(); i++) {
                JSONObject requestObject = (JSONObject) requestsArray.get(i);
                String data = (String) requestObject.get("createdDate");
                // adaug data la request
                dataRequestList.add(LocalDateTime.parse(data));
            }
            // adaug data la fiecare request
            for (Request request : requestList) {
                request.setDataCerere(dataRequestList.get(requestList.indexOf(request)));
            }
            // pentru fiecare request trebuie sa adaug request in lista cerui care trebuie sa rezolve
            // si trebuie ca cel care a facut-o sa devina observer pentru request
            for (Request request : requestList) {
                // adaug request in lista cerui care trebuie sa rezolve
                for (User user : userList) {
                    if (user.getUserName().equals(request.getUsernameRezolvant())) {
                        Staff staff = (Staff) user;
                        staff.addRequest(request);
                    }
                }
                // cel care a facut request devine observer pentru request
                for (User user : userList) {
                    if (user.getUserName().equals(request.getUsernameReclamant())) {
                        request.addObserver(user);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
