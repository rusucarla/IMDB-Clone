import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Request implements Subject {
    private static final String ADMIN_USERNAME = "ADMIN";
    private static final DateTimeFormatter format =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
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
                                       String descriereCerere, User creator){
        String usernameRezolvator;
        return null;
    }
    // getters
    public RequestType getTipCerere() {
        return tipCerere;
    }
    public LocalDateTime getDataCerere() {
        return dataCerere;
    }
    public String getTitluCerere() {
        return titluCerere;
    }
    public String getDescriereCerere() {
        return descriereCerere;
    }
    public String getUsernameReclamant() {
        return usernameReclamant;
    }
    public String getUsernameRezolvant() {
        return usernameRezolvant;
    }
    // setters
    public void setTipCerere(RequestType tipCerere) {
        this.tipCerere = tipCerere;
    }
    public void setDataCerere(LocalDateTime dataCerere) {
        this.dataCerere = dataCerere;
    }
    public void setTitluCerere(String titluCerere) {
        this.titluCerere = titluCerere;
    }
    public void setDescriereCerere(String descriereCerere) {
        this.descriereCerere = descriereCerere;
    }
    public void setUsernameReclamant(String usernameReclamant) {
        this.usernameReclamant = usernameReclamant;
    }
    public void setUsernameRezolvant(String usernameRezolvant) {
        this.usernameRezolvant = usernameRezolvant;
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
    public void rezolvaCerere(User user){
        // vreau notificari cu mesaje diferite in functie de tipul cererii
        // si de user-ul care a rezolvat cererea
        String notification = "";
        if (this.tipCerere != RequestType.DELETE_ACCOUNT) {
            notification = "Cererea ta a fost rezolvata ! Te rog sa verifici ca datele introduse sunt corecte.";
            notifyAllObservers(notification);
        }
    }
}
