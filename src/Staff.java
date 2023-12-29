import java.util.*;

public class Staff extends User implements StaffInterface {
    private List<Request> requestList;
    private TreeSet<Production> productionsContribution;
    private TreeSet<Actor> actorsContribution;
    private IMDB imdb = IMDB.getInstance();

    public Staff(Information info, AccountType cont, String username, int exp) {
        super(info, cont, username, exp);
        this.requestList = new ArrayList<>();
        this.productionsContribution = new TreeSet<>();
        this.actorsContribution = new TreeSet<>();
    }

    public Staff() {
        super();
    }

    public List<Request> getRequestList() {
        return requestList;
    }

    public void setRequestList(List<Request> requestList) {
        this.requestList = requestList;
    }

    public SortedSet<Production> getProductionsContribution() {
        return productionsContribution;
    }

    public void setProductionsContribution(TreeSet<Production> productionsContribution) {
        this.productionsContribution = productionsContribution;
    }

    public TreeSet<Actor> getActorsContribution() {
        return actorsContribution;
    }

    public void setActorsContribution(TreeSet<Actor> actorsContribution) {
        this.actorsContribution = actorsContribution;
    }

    @Override
    public void addProductionSystem(Production p) {
        productionsContribution.add(p);
        imdb.getProductionList().add(p);
    }

    @Override
    public void addActorSystem(Actor a) {
        actorsContribution.add(a);
        imdb.getActorList().add(a);
    }

    private boolean isProductionMine(Production p) {
        return productionsContribution.contains(p);
    }

    private boolean isActorMine(Actor a) {
        return actorsContribution.contains(a);
    }
    private boolean isProductionInImdb(Production p) {
        return imdb.getProductionList().contains(p);
    }
    private boolean isActorInImdb(Actor a) {
        return imdb.getActorList().contains(a);
    }

    @Override
    public void removeProductionSystem(String name) {
        Iterator<Production> iterator = productionsContribution.iterator();
        while (iterator.hasNext()) {
            Production p = iterator.next();
            if (p.getTitlu().equals(name)) {
                iterator.remove();
                break;
            }
        }
        Iterator<Production> iterator1 = imdb.getProductionList().iterator();
        while (iterator1.hasNext()) {
            Production p = iterator1.next();
            if (p.getTitlu().equals(name)) {
                iterator1.remove();
                break;
            }
        }
    }

    @Override
    public void removeActorSystem(String name) {
        Iterator<Actor> iterator = actorsContribution.iterator();
        while (iterator.hasNext()) {
            Actor a = iterator.next();
            if (a.getName().equals(name)) {
                iterator.remove();
                break;
            }
        }
        Iterator<Actor> iterator1 = imdb.getActorList().iterator();
        while (iterator1.hasNext()) {
            Actor a = iterator1.next();
            if (a.getName().equals(name)) {
                iterator1.remove();
                break;
            }
        }
    }

    @Override
    public void updateProduction(Production p) {
        for (Production new_p : productionsContribution) {
            if (new_p.getTitlu().equals(p.getTitlu())) {
                if (isProductionMine(p)) {
                    p.setActoriList(new_p.getActoriList());
                    p.setDescriereFilm(new_p.getDescriereFilm());
                    p.setGenreList(new_p.getGenreList());
                    p.setNotaFilm(new_p.getNotaFilm());
                    p.setTitlu(new_p.getTitlu());
                    p.setRegizoriList(new_p.getRegizoriList());
                    p.setRatingList(new_p.getRatingList());
                    break;
                } else {
                    System.out.println("Nu aveti permisiuni sa updatati");
                }
            }
        }
        for (Production new_p : imdb.getProductionList()) {
            if (new_p.getTitlu().equals(p.getTitlu())) {
                if (isProductionMine(p)) {
                    new_p.setActoriList(p.getActoriList());
                    new_p.setDescriereFilm(p.getDescriereFilm());
                    new_p.setGenreList(p.getGenreList());
                    new_p.setNotaFilm(p.getNotaFilm());
                    new_p.setTitlu(p.getTitlu());
                    new_p.setRegizoriList(p.getRegizoriList());
                    new_p.setRatingList(p.getRatingList());
                    break;
                } else {
                    System.out.println("Nu aveti permisiuni sa updatati");
                }
            }
        }
    }

    @Override
    public void updateActor(Actor new_a) {
        for (Actor a : actorsContribution) {
            if (a.getName().equals(new_a.getName())) {
                if (isActorMine(a)) {
                    a.setBiography(new_a.getBiography());
                    a.setName(new_a.getName());
                    a.setPerformances(new_a.getPerformances());
                } else {
                    System.out.println("Nu aveti permisiuni sa updatati");
                }
            }
        }
        System.out.println("AICI");
        System.out.println("NAME " + new_a.getName());
        for (Actor a : imdb.getActorList()) {
            System.out.println("NUME a " + a.getName());
            if (a.getName().equals(new_a.getName())) {
                System.out.println("AICI2");
                if (isActorMine(a)) {
                    System.out.println("AICI3");
                    new_a.setBiography(a.getBiography());
                    new_a.setName(a.getName());
                    new_a.setPerformances(a.getPerformances());
                } else {
                    System.out.println("Nu aveti permisiuni sa updatati");
                }
            }
        }
    }

    @Override
    public int compareTo(User o) {
        return 0;
    }
}
