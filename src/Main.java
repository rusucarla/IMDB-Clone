public class Main {
    public static void main(String[] args) {
        IMDB imdb = IMDB.getInstance();
        // vreau sa transmit un mesaj
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Esti om normal(GUI) sau HACKKKKER(CLI) ?");
//        String raspuns = scanner.nextLine().trim().toLowerCase();
        // ignor pe moment raspunsul - revenim

        // incarc listele
        String usersPath = "C:\\Users\\Carla\\Documents\\POO_Lab\\TemaPOO\\accounts.json";
        String actorsPath = "C:\\Users\\Carla\\Documents\\POO_Lab\\TemaPOO\\actors.json";
        String productionsPath = "C:\\Users\\Carla\\Documents\\POO_Lab\\TemaPOO\\production.json";
        String requestPath = "C:\\Users\\Carla\\Documents\\POO_Lab\\TemaPOO\\requests.json";
        imdb.loadActors(actorsPath);
        imdb.loadProductions(productionsPath);
        imdb.loadUsers(usersPath);
        imdb.loadRequests(requestPath);
        LoginFrame frame = new LoginFrame();
        frame.setVisible(true);
    }
}
