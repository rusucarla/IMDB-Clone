import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Request {
    private static final String ADMIN_USERNAME = "ADMIN";
    private static final DateTimeFormatter format =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    private RequestType tipCerere;
    private LocalDateTime dataCerere;
    private String titluCerere;
    private String descriereCerere;
    private String usernameReclamant;
    private String usernameRezolvant;

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

    public static Request creareCerere(RequestType tipCerere, String titluCerere,
                                       String descriereCerere, User creator){
        String usernameRezolvator;
//        if (tipCerere == RequestType.DELETE_ACCOUNT || tipCerere == RequestType.OTHERS){
//            usernameRezolvator = "ADMIN";
//        } else {
////            usernameRezolvator a celui care a introdus in sistem production-ul
//        }
        return null;
    }

}
