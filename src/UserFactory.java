public class UserFactory {
    // builder pentru user
    // user poate fi de tip regular, contributor sau admin (staff)
    // information e clasa interna a userului
    public static User createUser(User.Information info,
                                  AccountType cont, String username, int exp) {
        if (cont == AccountType.REGULAR) {
            return new Regular(info, cont, username, exp);
        } else if (cont == AccountType.CONTRIBUTOR) {
            return new Contributor(info, cont, username, exp);
        } else if (cont == AccountType.ADMIN) {
            return new Admin(info, cont, username, exp);
        }
        return null;
    }
}
