public class Contributor extends Staff implements RequestManager{
    public Contributor(Information info, AccountType cont, String username, int exp) {
        super(info, cont, username, exp);
    }

    @Override
    public void createRequest(Request new_request) {

    }

    @Override
    public void removeRequest(Request removed_request) {

    }
}
