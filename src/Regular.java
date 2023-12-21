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
}
