//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class JSONParser {
//    public static void main(String[] args) {
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        try {
//            // Părsează fișierul JSON și obține lista de actori
//            List<Actor> actors = objectMapper.readValue(
//                    new File("actors.json"),
//                    objectMapper.getTypeFactory().constructCollectionType
//                            (List.class, Actor.class));
//
//            // Afișează informațiile despre actori
//            System.out.println("Actors:");
//            for (Actor actor : actors) {
//                System.out.println("Name: " + actor.getName());
//                System.out.println("Biography: " + actor.getBiography());
//                System.out.println("Performances:");
//                for (Actor.Performance performance : actor.getPerformances()) {
//                    System.out.println("  Title: " + performance.getTitle() + ", Type: " + performance.getType());
//                }
//                System.out.println();
//            }
//            File jsonFile = new File("path/to/your/accounts.json"); // Înlocuiește cu calea reală
//
//            // Deserializarea conținutului fișierului într-o listă de obiecte User
//            List<User> userList = objectMapper.readValue(jsonFile, new TypeReference<List<User>>() {});
//            List<Admin> adminList = new ArrayList<>();
//            for (User user : userList) {
//                if (user instanceof Admin) {
//                    adminList.add((Admin) user);
//                }
//            }
//            // afisez lista de admini
//            System.out.println("Admins:");
//            for (Admin admin : adminList) {
//                System.out.println(admin.getUserName());
//            }
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
