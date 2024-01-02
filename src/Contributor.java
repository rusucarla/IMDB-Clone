public class Contributor extends Staff implements RequestManager {
    //constructor fara nimic
    public Contributor() {
        super();
    }

    public Contributor(Information info, AccountType cont, String username, int exp) {
        super(info, cont, username, exp);
    }

    @Override
    public void createRequest(Request new_request) {
        this.getRequestList().add(new_request);
    }

    @Override
    public void removeRequest(Request removed_request) {
        this.getRequestList().remove(removed_request);
    }
}
