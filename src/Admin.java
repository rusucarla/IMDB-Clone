import java.util.TreeSet;

public class Admin extends Staff{

    // lista pentru contributii comune ale echipei de admini
    private static TreeSet<Production> productionsContributionCommon = new TreeSet<>();
    private static TreeSet<Actor> actorsContributionCommon = new TreeSet<>();
    public Admin(Information info, AccountType cont, String username, int exp) {
        super(info, cont, username, exp);
    }
    // getter pentru lista de contributii comune
    public static TreeSet<Production> getProductionsContributionCommon() {
        return productionsContributionCommon;
    }
    // setter pentru lista de contributii comune
    public static void setProductionsContributionCommon(TreeSet<Production> productionsContributionCommon) {
        Admin.productionsContributionCommon = productionsContributionCommon;
    }
    // metoda pentru adaugarea unei contributii comune
    public static void addProductionSystemCommon(Production p) {
        productionsContributionCommon.add(p);
    }
    // metoda pentru stergerea unei contributii comune
    public static void removeProductionSystemCommon(Production p) {
        productionsContributionCommon.remove(p);
    }
    // getter pentru lista de contributii comune
    public static TreeSet<Actor> getActorsContributionCommon() {
        return actorsContributionCommon;
    }
    // setter pentru lista de contributii comune
    public static void setActorsContributionCommon(TreeSet<Actor> actorsContributionCommon) {
        Admin.actorsContributionCommon = actorsContributionCommon;
    }
    // metoda pentru adaugarea unei contributii comune
    public static void addActorSystemCommon(Actor a) {
        actorsContributionCommon.add(a);
    }
    // metoda pentru stergerea unei contributii comune
    public static void removeActorSystemCommon(Actor a) {
        actorsContributionCommon.remove(a);
    }
    // metoda pentru adaugarea unui actor in lista de contributii comune
    public static void addActorCommon(Actor a) {
        actorsContributionCommon.add(a);
    }
    // metoda pentru stergerea unui actor din lista de contributii comune
    public static void removeActorCommon(Actor a) {
        actorsContributionCommon.remove(a);
    }

}
