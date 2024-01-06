import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Request implements Subject {
    private static final String ADMIN_USERNAME = "ADMIN";
    private static final DateTimeFormatter format =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    private static IMDB imdb = IMDB.getInstance();
    @JsonProperty("type")
    private RequestType tipCerere;
    @JsonIgnore
    private LocalDateTime dataCerere;
    @JsonProperty("title")
    private String titluCerere;
    @JsonProperty("description")
    private String descriereCerere;
    @JsonProperty("username")
    private String usernameReclamant;
    @JsonProperty("to")
    private String usernameRezolvant;
    private List<Observer> observers = new ArrayList<>();
    private boolean solved = false;
    private boolean rejected = false;

    private Request(RequestType tipCerere, String titluCerere,
                    String descriereCerere, String usernameReclamant,
                    String usernameRezolvant) {
        this.tipCerere = tipCerere;
        this.dataCerere = LocalDateTime.now();
        this.titluCerere = titluCerere;
        this.descriereCerere = descriereCerere;
        this.usernameReclamant = usernameReclamant;
        this.usernameRezolvant = usernameRezolvant;
    }

    public Request() {
    }

    public static Request creareCerere(RequestType tipCerere, String titluCerere,
                                       String descriereCerere, User creator) {
        String usernameRezolvator;
        if (tipCerere == RequestType.DELETE_ACCOUNT || tipCerere == RequestType.OTHERS) {
            usernameRezolvator = ADMIN_USERNAME;
            Request new_request = new Request(tipCerere, titluCerere, descriereCerere,
                    creator.getUserName(), usernameRezolvator);
            // daca trebuie rezolvata de admin, o adaug in lista comuna de cereri din RequestHoler
            RequestsHolder.adaugaCerere(new_request);
            // trebuie sa adaug creatorul cereii in lista de observatori a cererii
            new_request.addObserver(creator);
            // trebuie sa adaug o notificare pentru toata echipa de admini
            for (User user : imdb.getUserList()) {
                if (user.getUserType() == AccountType.ADMIN) {
                    Admin admin = (Admin) user;
                    admin.update("A fost adaugata o noua cerere in lista de cereri comuna");
                }
            }
            return new_request;
        } else {
            // trebuie sa vad cine a introdus productia / actorul in sistem
            if (tipCerere == RequestType.MOVIE_ISSUE) {
                for (User user : imdb.getUserList()) {
                    if (user.getUserType() != AccountType.REGULAR) {
                        Staff staff = (Staff) user;
                        for (Production production : staff.getProductionsContribution()) {
                            if (production.getTitlu().equals(titluCerere)) {
                                usernameRezolvator = staff.getUserName();
                                // adaog cererea in lista de cereri a user-ului
                                Request new_request = new Request(tipCerere, titluCerere, descriereCerere,
                                        creator.getUserName(), usernameRezolvator);
                                staff.addRequest(new_request);
                                // trebuie sa adaug creatorul cereii in lista de observatori a cererii
                                new_request.addObserver(creator);
                                // vreau sa trimit o notificare utilizatorului rezolvator
                                // ca a fost adaugata o noua cerere in lista lui de cereri
                                staff.update("A fost adaugata o noua cerere in lista ta de cereri");
                                return new_request;
                            }
                        }
                        // daca user-ul este admin atunci trebuie sa caut in lista de productii comune
                        if (staff instanceof Admin){
                            for (Production production : Admin.getProductionsContributionCommon()){
                                if (production.getTitlu().equals(titluCerere)) {
                                    // cererea va fi rezolvata de catre ADMIN
                                    usernameRezolvator = ADMIN_USERNAME;
                                    Request new_request = new Request(tipCerere, titluCerere, descriereCerere,
                                            creator.getUserName(), usernameRezolvator);
                                    // daca trebuie rezolvata de admin, o adaug in lista comuna de cereri din RequestHoler
                                    RequestsHolder.adaugaCerere(new_request);
                                    // trebuie sa adaug creatorul cereii in lista de observatori a cererii
                                    new_request.addObserver(creator);
                                    // trebuie sa adaug o notificare pentru toata echipa de admini
                                    for (User user1 : imdb.getUserList()) {
                                        if (user1.getUserType() == AccountType.ADMIN) {
                                            Admin admin = (Admin) user1;
                                            admin.update("A fost adaugata o noua cerere in lista de cereri comuna");
                                        }
                                    }
                                    return new_request;

                                }
                            }
                        }
                    }
                }
            } else if (tipCerere == RequestType.ACTOR_ISSUE) {
                for (User user : imdb.getUserList()) {
                    if (user.getUserType() != AccountType.REGULAR) {
                        Staff staff = (Staff) user;
                        for (Actor actor : staff.getActorsContribution()) {
                            if (actor.getName().equals(titluCerere)) {
                                usernameRezolvator = staff.getUserName();
                                // adaug cererea in lista de cereri a user-ului
                                Request new_request = new Request(tipCerere, titluCerere, descriereCerere,
                                        creator.getUserName(), usernameRezolvator);
                                staff.addRequest(new_request);
                                // trebuie sa adaug creatorul cereii in lista de observatori a cererii
                                new_request.addObserver(creator);
                                // vreau sa trimit o notificare utilizatorului rezolvator
                                // ca a fost adaugata o noua cerere in lista lui de cereri
                                staff.update("A fost adaugata o noua cerere in lista ta de cereri");
                                return new_request;
                            }
                        }
                        // daca user-ul este admin atunci trebuie sa caut in lista de productii comune
                        if (staff instanceof Admin){
                            for (Actor actor : Admin.getActorsContributionCommon()){
                                if (actor.getName().equals(titluCerere)) {
                                    // cererea va fi rezolvata de catre ADMIN
                                    usernameRezolvator = ADMIN_USERNAME;
                                    Request new_request = new Request(tipCerere, titluCerere, descriereCerere,
                                            creator.getUserName(), usernameRezolvator);
                                    // daca trebuie rezolvata de admin, o adaug in lista comuna de cereri din RequestHoler
                                    RequestsHolder.adaugaCerere(new_request);
                                    // trebuie sa adaug creatorul cereii in lista de observatori a cererii
                                    new_request.addObserver(creator);
                                    // trebuie sa adaug o notificare pentru toata echipa de admini
                                    for (User user1 : imdb.getUserList()) {
                                        if (user1.getUserType() == AccountType.ADMIN) {
                                            Admin admin = (Admin) user1;
                                            admin.update("A fost adaugata o noua cerere in lista de cereri comuna");
                                        }
                                    }
                                    return new_request;
                                }
                            }
                        }
                    }
                }
            }

        }
        return null;
    }

    // getters
    public RequestType getTipCerere() {
        return tipCerere;
    }

    // setters
    public void setTipCerere(RequestType tipCerere) {
        this.tipCerere = tipCerere;
    }

    public LocalDateTime getDataCerere() {
        return dataCerere;
    }

    public void setDataCerere(LocalDateTime dataCerere) {
        this.dataCerere = dataCerere;
    }

    public String getTitluCerere() {
        return titluCerere;
    }

    public void setTitluCerere(String titluCerere) {
        this.titluCerere = titluCerere;
    }

    public String getDescriereCerere() {
        return descriereCerere;
    }

    public void setDescriereCerere(String descriereCerere) {
        this.descriereCerere = descriereCerere;
    }

    public String getUsernameReclamant() {
        return usernameReclamant;
    }

    public void setUsernameReclamant(String usernameReclamant) {
        this.usernameReclamant = usernameReclamant;
        // vreau sa adaug user-ul in lista de observatori a cererii
        User user = imdb.getUser(usernameReclamant);
        if (user != null) {
            this.addObserver(user);
        }
    }

    public String getUsernameRezolvant() {
        return usernameRezolvant;
    }

    public void setUsernameRezolvant(String usernameRezolvant) {
        this.usernameRezolvant = usernameRezolvant;
        // vreau sa adaug cerea in lista de cereri a user-ului care o rezolva
        User user = imdb.getUser(usernameRezolvant);
        if (user != null) {
            Staff staff = (Staff) user;
            staff.addRequest(this);
        }
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

    // metode de rezolvare
    public void rezolvaCerere(User user) {
        // vreau notificari cu mesaje diferite in functie de tipul cererii
        // si de user-ul care a rezolvat cererea
        String notification = "";
        if (this.tipCerere != RequestType.DELETE_ACCOUNT) {
            notification = "Cererea ta a fost rezolvata ! Ai primit exp-ul corespunzător.";
            notifyAllObservers(notification);
            // vreau sa adaug exp-ul user-ului care a semnalat cererea
            // care e observator al cererii
            User userReclamant = imdb.getUser(this.usernameReclamant);
            // admin-ul nu primeste exp
            if (!(userReclamant instanceof Admin)) {
                if (this.tipCerere == RequestType.MOVIE_ISSUE) {
                    userReclamant.setExperienceStrategy(new RequestProductionStrategy());
                    userReclamant.updateExperience();
                } else if (this.tipCerere == RequestType.ACTOR_ISSUE) {
                    userReclamant.setExperienceStrategy(new RequestActorStrategy());
                    userReclamant.updateExperience();
                }
            }
        }
        // sterg cererea din lista de cereri a user-ului care a rezolvat-o
        User userRezolvator = imdb.getUser(this.usernameRezolvant);
        if (!Objects.equals(usernameRezolvant, ADMIN_USERNAME)) {
            Staff staff = (Staff) userRezolvator;
            staff.removeRequest(this);
        } else {
            RequestsHolder.stergeCerere(this);
            Staff staff = (Staff) user;
            staff.removeRequest(this);
        }
        // sterge cererea din lista IMDb
        imdb.getRequestList().remove(this);
        // vreau sa semnalez ca cererea a fost rezolvata
        // dau o notificare user-ului care a rezolvat cererea
        // daca usernameRezolvator este ADMIN_USERNAME, atunci notific toata echipa de admini
        if (Objects.equals(usernameRezolvant, ADMIN_USERNAME)) {
            for (User user1 : imdb.getUserList()) {
                if (user1.getUserType() == AccountType.ADMIN) {
                    Admin admin = (Admin) user1;
                    admin.update("Cererea " + this.tipCerere + " a fost rezolvata");
                }
            }
        } else {
            Staff staff = (Staff) userRezolvator;
            staff.update("Cererea " + this.tipCerere + " a fost rezolvata");
        }
        this.solved = true;
    }

    public void respingeCerere(User user) {
        // vreau notificari cu mesaje diferite in functie de tipul cererii
        // si de user-ul care a respins cererea
        String notification = "";
        if (this.tipCerere != RequestType.DELETE_ACCOUNT) {
            notification = "Cererea ta a fost respinsa ! Te rog sa verifici ca datele introduse sunt corecte.";
            notifyAllObservers(notification);
        }
        // sterg cererea din lista de cereri a user-ului care a rezolvat-o
        User userRezolvator = imdb.getUser(this.usernameRezolvant);
//        System.out.println("Username rezolvator: " + userRezolvator.getUserName());
        if (!Objects.equals(usernameRezolvant, ADMIN_USERNAME)) {
            Staff staff = (Staff) userRezolvator;
            staff.removeRequest(this);
        } else {
            RequestsHolder.stergeCerere(this);
            Staff staff = (Staff) user;
            staff.removeRequest(this);
        }
        // sterge cererea din lista IMDb
        imdb.getRequestList().remove(this);
        if (Objects.equals(usernameRezolvant, ADMIN_USERNAME)) {
            for (User user1 : imdb.getUserList()) {
                if (user1.getUserType() == AccountType.ADMIN) {
                    Admin admin = (Admin) user1;
                    admin.update("Cererea " + this.tipCerere + " a fost rezolvata");
                }
            }
        } else {
            Staff staff = (Staff) userRezolvator;
            staff.update("Cererea " + this.tipCerere + " a fost rezolvata");
        }
        this.rejected = true;
    }

    public void retrageCerere(User user1) {
        // vreau sa sterg cererea din toate listele in care se afla

        // sterg cererea din lista de cereri a user-ului care a rezolvat-o
        if (!Objects.equals(usernameRezolvant, ADMIN_USERNAME)) {
            User userRezolvator = imdb.getUser(this.usernameRezolvant);
            Staff staff = (Staff) userRezolvator;
            staff.removeRequest(this);
            // vreau sa stie user-ul care trebuia sa rezolve cererea ca a fost retrasa
            staff.update("Cererea " + this.tipCerere + " a fost retrasa");
        } else {
            RequestsHolder.stergeCerere(this);
            // vreau ca toata echipa de admini sa stie ca a fost retrasa cererea
            for (User user : imdb.getUserList()) {
                if (user.getUserType() == AccountType.ADMIN) {
                    Admin admin1 = (Admin) user;
                    admin1.update("Cererea " + this.tipCerere + " a fost retrasa");
                }
            }
            if (user1 instanceof Admin) {
                Admin admin = (Admin) user1;
                admin.removeRequest(this);
            }
        }
        // sterg cererea din lista de cereri a user-ului care a introdus-o
        User userReclamant = imdb.getUser(this.usernameReclamant);
        if (userReclamant instanceof Contributor) {
            Contributor contributor = (Contributor) userReclamant;
            contributor.removeRequest(this);
        } else if (userReclamant instanceof Regular) {
            Regular regular = (Regular) userReclamant;
            regular.removeRequest(this);
        }
        // sterge cererea din lista IMDb
        imdb.getRequestList().remove(this);
    }
}
