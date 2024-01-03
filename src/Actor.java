import java.util.List;

public class Actor implements Comparable<Actor>{
    private String name;
    private List<Performance> performances;
    private String biography;

    // Constructor
    public Actor(){}
    public Actor(String name, List<Performance> performances, String biography) {
        this.name = name;
        this.performances = performances;
        this.biography = biography;
    }

    @Override
    public String toString() {
        return this.getName();
    }

    // Getter methods
    public String getName() {
        return name;
    }

    public List<Performance> getPerformances() {
        return performances;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPerformances(List<Performance> performances) {
        this.performances = performances;
    }

    @Override
    public int compareTo(Actor o) {
        return this.name.compareTo(o.getName());
    }

    public void displayInfo() {
        System.out.println("Nume: " + this.getName());
        System.out.println("Biografie: " + this.getBiography());
        System.out.println("Performante: ");
        for (Performance performance : this.getPerformances()) {
            System.out.println(performance.getTitle() + " - " + performance.getType());
        }
    }

    // Inner class representing performances
    public static class Performance {
        private String title;
        private String type;

        // Constructor for Performance
        public Performance(){}
        public Performance(String title, String type) {
            this.title = title;
            this.type = type;
        }

        // Getter methods
        public String getTitle() {
            return title;
        }

        public String getType() {
            return type;
        }
        // Setter methods
        public void setTitle(String title) {
            this.title = title;
        }
        public void setType(String type) {
            this.type = type;
        }
    }
}

