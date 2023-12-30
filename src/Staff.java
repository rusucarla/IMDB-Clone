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
        // vreau sa adaug actorul si in toate producitiile in care a jucat
        for (Production p : imdb.getProductionList()) {
            // caut in lista de producitiile in care a jucat
            for (Actor.Performance performance : a.getPerformances()) {
                if (p.getTitlu().equals(performance.getTitle())) {
                    p.getActoriList().add(a.getName());
                }
            }
        }
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
        // trebuie sa verific si ce tip de productie este si sa sterg si din lista de seriale/ filme
        // daca se afla in lista de seriale/ filme
        Iterator<Series> iterator3 = imdb.getSeriesList().iterator();
        while (iterator3.hasNext()) {
            Series s = iterator3.next();
            if (s.getTitlu().equals(name)) {
                iterator3.remove();
                break;
            }
        }
        Iterator<Movie> iterator4 = imdb.getMoviesList().iterator();
        while (iterator4.hasNext()) {
            Movie m = iterator4.next();
            if (m.getTitlu().equals(name)) {
                iterator4.remove();
                break;
            }
        }
        // trebuie sa sterg si din toate listele de producitiile preferate ale userilor
        for (User u : imdb.getUserList()) {
            TreeSet<Production> producitiPreferate = u.getFavoriteProductions();
            Iterator<Production> iterator2 = producitiPreferate.iterator();
            while (iterator2.hasNext()) {
                Production production = iterator2.next();
                if (production.getTitlu().equals(name)) {
                    iterator2.remove();
                    break;
                }
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
//        System.out.println("Sutn initial " + imdb.getActorList().size() + " actori");
        while (iterator1.hasNext()) {
            Actor a = iterator1.next();
            if (a.getName().equals(name)) {
//                System.out.println("AICI " + a.getName());
                iterator1.remove();
//                System.out.println("Au mai ramas " + imdb.getActorList().size() + " actori");
                break;
            }
        }
        // trebuie sa sterg si din toate listele de actori preferati ai userilor
        for (User u : imdb.getUserList()) {
            TreeSet<Actor> actoriPreferati = u.getFavoriteActors();
            Iterator<Actor> iterator2 = actoriPreferati.iterator();
            while (iterator2.hasNext()) {
                Actor actor = iterator2.next();
                if (actor.getName().equals(name)) {
                    iterator2.remove();
                    break;
                }
            }
        }
        // trebuie sa merg prin lista cu toate producitiile si sa sterg actorul din lista de actori
        // daca se afla in lista de actori
        for (Production p : imdb.getProductionList()) {
            // actorList este in Production
            p.getActoriList().removeIf(actor -> actor.equals(name));
        }
    }

    @Override
    public void updateProduction(Production new_p) {
        for (Production p : productionsContribution) {
            if (p.getTitlu().equals(new_p.getTitlu())) {
                if (isProductionMine(p)) {
                    System.out.println("AICI INAINTE");
                    p.displayInfo();
                    p.setActoriList(new_p.getActoriList());
                    p.setDescriereFilm(new_p.getDescriereFilm());
                    p.setGenreList(new_p.getGenreList());
                    p.setNotaFilm(new_p.getNotaFilm());
                    p.setTitlu(new_p.getTitlu());
                    p.setRegizoriList(new_p.getRegizoriList());
                    p.setRatingList(new_p.getRatingList());
                    System.out.println("AICI DUPA");
                    p.displayInfo();
                    break;
                } else {
                    System.out.println("Nu aveti permisiuni sa updatati");
                }
            }
        }
        for (Production p : imdb.getProductionList()) {
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
