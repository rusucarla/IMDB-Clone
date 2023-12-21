import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JSONParser {
    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Părsează fișierul JSON și obține lista de actori
            List<Actor> actors = objectMapper.readValue(
                    new File("actors.json"),
                    objectMapper.getTypeFactory().constructCollectionType
                            (List.class, Actor.class));

            // Afișează informațiile despre actori
            System.out.println("Actors:");
            for (Actor actor : actors) {
                System.out.println("Name: " + actor.getName());
                System.out.println("Biography: " + actor.getBiography());
                System.out.println("Performances:");
                for (Actor.Performance performance : actor.getPerformances()) {
                    System.out.println("  Title: " + performance.getTitle() + ", Type: " + performance.getType());
                }
                System.out.println();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
