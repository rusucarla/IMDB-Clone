import java.util.ArrayList;
import java.util.List;

public class Regular extends User implements RequestManager {
    private List<Rating> userRatings;

    public Regular(Information info, AccountType cont, String username, int exp) {
        super(info, cont, username, exp);
        userRatings = new ArrayList<>();
    }

    // metoda creare/stergere cerere
    // metoda adugire rating
    public void add_rating(int new_nota, String new_comentarii) {
        Rating new_rating = new Rating(this.getUserName(), new_nota,
                new_comentarii);
        this.userRatings.add(new_rating);
    }

    @Override
    public int compareTo(User o) {
        return 0;
    }

    @Override
    public void createRequest(Request new_request) {

    }

    @Override
    public void removeRequest(Request removed_request) {

    }

    public void displayInfo() {
        System.out.println("Nume: " + this.getInformation().getUserNume());
        System.out.println("Email: " + this.getInformation().getUserCredentials().getEmail());
        System.out.println("Data nasterii: " + this.getInformation().getBirthDate());
        System.out.println("Limba preferata: " + this.getInformation().getUserCountry());
        System.out.println("Username: " + this.getUserName());
        System.out.println("Tip cont: " + this.getUserType());
        System.out.println("Experienta: " + this.getExperience());
//        System.out.println("Rating-uri: ");
//        for (Rating rating : userRatings) {
//            System.out.println(rating.getNota() + " " + rating.getComentariiRater());
//        }
//        System.out.println("Actori favoriti: ");
//        for (Actor actor : this.favoriteActors) {
//            System.out.println(actor.getName());
//        }
//        System.out.println("Productii favorite: ");
//        for (Production production : this.favoriteProductions) {
//            System.out.println(production.getTitlu());
//        }
    }
}
