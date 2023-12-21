import java.util.*;

public class Staff extends User implements StaffInterface {
    private List<Request> requestList;
    private SortedSet<Production> addedProductions;
    private SortedSet<Actor> addedActors;

    public Staff(Information info, AccountType cont, String username, int exp) {
        super(info, cont, username, exp);
        this.requestList = new ArrayList<>();
        this.addedProductions = new TreeSet<>();
        this.addedActors = new TreeSet<>();
    }

    @Override
    public void addProductionSystem(Production p) {
        addedProductions.add(p);
    }

    @Override
    public void addActorSystem(Actor a) {
        addedActors.add(a);
    }

    private boolean isProductionMine(Production p) {
        return addedProductions.contains(p);
    }

    private boolean isActorMine(Actor a) {
        return addedActors.contains(a);
    }

    @Override
    public void removeProductionSystem(String name) {
        Iterator<Production> iterator = addedProductions.iterator();
        while (iterator.hasNext()) {
            Production p = iterator.next();
            if (p.getTitlu().equals(name)) {
                iterator.remove();
                break;
            }
        }
    }

    @Override
    public void removeActorSystem(String name) {
        Iterator<Actor> iterator = addedActors.iterator();
        while (iterator.hasNext()) {
            Actor a = iterator.next();
            if (a.getName().equals(name)) {
                iterator.remove();
                break;
            }
        }

    }

    @Override
    public void updateProduction(Production p) {
        for (Production new_p : addedProductions) {
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
    }

    @Override
    public void updateActor(Actor a) {
        for (Actor new_a : addedActors) {
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

    }

    @Override
    public int compareTo(User o) {
        return 0;
    }
}
