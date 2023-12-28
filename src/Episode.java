import com.fasterxml.jackson.annotation.JsonProperty;

public class Episode {
    @JsonProperty("episodeName")
    private String numeEpisod;
    @JsonProperty("duration")
    private String durataEpisod;

    public Episode(String titlu, String minute) {
        this.numeEpisod = titlu;
        this.durataEpisod = minute;
    }
    public Episode() {
    }
    // getter
    public String getNumeEpisod() {
        return numeEpisod;
    }
    public String getDurataEpisod() {
        return durataEpisod;
    }
    // setter
    public void setNumeEpisod(String new_name) {
        this.numeEpisod = new_name;
    }
    public void setDurataEpisod(String new_durata) {
        this.durataEpisod = new_durata;
    }
    // display
    public String toString() {
        return "Episode{" +
                "numeEpisod='" + numeEpisod + '\'' +
                ", durataEpisod='" + durataEpisod + '\'' +
                '}';
    }

}
