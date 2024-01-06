import java.time.LocalDate;
import java.util.*;

public class CLI {
    private IMDB imdb = IMDB.getInstance();
    private String username;
    private User user;

    public CLI(String username) {
        this.username = username;
        this.user = imdb.getUser(username);
    }

    public void run() {
        showMainMenu();
    }

    private void showMainMenu() {
        System.out.println("Bine ai venit, " + username + "! Alege o opțiune:");
        System.out.println("1. Log out");
        System.out.println("2. Vizualizare lista de producții");
        System.out.println("3. Vizualizare lista de actori");
        // vreau pentru admin si contributor sa am si optiunea de a vedea
        // productiile si actorii pe care i-am adaugat
        System.out.println("4. Vizualizare lista de producții adăugate");
        System.out.println("5. Vizualizare lista de actori adăugați");
        // vreau ca toata lumea sa vada productiile si actorii favoriti
        System.out.println("6. Vizualizare lista de producții favorite");
        System.out.println("7. Vizualizare lista de actori favorite");
        // vreau sa aiba si optiunea de a adauga productii si actori
        System.out.println("8. Adaugă producție");
        System.out.println("9. Adaugă actor");
        // vreau ca sa fac optiunile legate de request-uri
        System.out.println("10. Vizualizare lista de request-uri pe care le-am trimis");
        System.out.println("11. Vizualizare lista de request-uri pe care le am de rezolvat");
        System.out.println("12. Creare request");
        // vreau sa fac optiunea pentru useri (doar pentru admin)
        System.out.println("13. Vizualizare lista de useri");
        System.out.println("14. Creare user");
        // vreau sa fac optiunea pentru contul meu
        System.out.println("15. Contul meu");
        Scanner scanner = new Scanner(System.in);
        int option_home = 0;
        boolean valid = false;
        while (!valid) {
            try {
                option_home = scanner.nextInt();
                valid = true;
            } catch (Exception e) {
                System.out.println("Opțiune invalidă!");
                scanner.nextLine();
            }
        }
        switch (option_home) {
            case 1:
                // Log out
                System.out.println("Log out");
                LoginFrame frame = new LoginFrame();
                frame.setVisible(true);
                break;
            case 2:
                // Vizualizare lista de producții
                showProductions();
                break;
            case 3:
                // Vizualizare lista de actori
                showActors();
                break;
            case 4:
                // Vizualizare lista de producții adăugate
                if (user.getUserType() == AccountType.ADMIN || user.getUserType() == AccountType.CONTRIBUTOR) {
                    showProductionsContribution();
                } else {
                    System.out.println("Opțiune invalidă!");
                    showMainMenu();
                }
                break;
            case 5:
                // Vizualizare lista de actori adăugați
                if (user.getUserType() == AccountType.ADMIN || user.getUserType() == AccountType.CONTRIBUTOR) {
                    showActorsContribution();
                } else {
                    System.out.println("Opțiune invalidă!");
                    showMainMenu();
                }
                break;
            case 6:
                // Vizualizare lista de producții favorite
                showFavoriteProductions();
                break;
            case 7:
                // Vizualizare lista de actori favorite
                showFavoriteActors();
                break;
            case 8:
                // Adaugă producție
                if (user.getUserType() == AccountType.ADMIN || user.getUserType() == AccountType.CONTRIBUTOR) {
                    addProduction();
                } else {
                    System.out.println("Opțiune invalidă!");
                    showMainMenu();
                }
                break;
            case 9:
//                 Adaugă actor
                if (user.getUserType() == AccountType.ADMIN || user.getUserType() == AccountType.CONTRIBUTOR) {
                    addActor();
                } else {
                    System.out.println("Opțiune invalidă!");
                    showMainMenu();
                }
                break;
            case 10:
                // Vizualizare lista de request-uri pe care le-am trimis
                showSentRequests();
                break;
            case 11:
                // Vizualizare lista de request-uri pe care le am de rezolvat
                // doar pentru admin si contributor
                if (user.getUserType() == AccountType.ADMIN || user.getUserType() == AccountType.CONTRIBUTOR) {
                    showReceivedRequests();
                } else {
                    System.out.println("Opțiune invalidă!");
                    showMainMenu();
                }
                break;
            case 12:
                // Creare request
                createRequest();
                break;
            case 13:
                // Vizualizare lista de useri
                // doar pentru admin
                if (user.getUserType() == AccountType.ADMIN) {
                    showUsers();
                } else {
                    System.out.println("Opțiune invalidă!");
                    showMainMenu();
                }
                break;
            case 14:
                // Creare user
                // doar pentru admin
                if (user.getUserType() == AccountType.ADMIN) {
                    createUser();
                } else {
                    System.out.println("Opțiune invalidă!");
                    showMainMenu();
                }
                break;
            case 15:
                // Contul meu
                showAccount();
                break;
            default:
                System.out.println("Opțiune invalidă!");
                showMainMenu();
        }
    }

    private void showReceivedRequests() {
        // pentru request-uri care trebuie rezolvate
        // la admini intra si lista de request din RequestHolder (lista comuna)
        // vreau sa afisez request-urile care au user-ul reclamat user-ul curent
        // si vreau pentru fiecare request sa afisez niste detalii dar si
        // posibilitatea de a rezolva request-ul sau de a-l respinge
        System.out.println("Lista de request-uri de rezolvat:");
        Staff staff = (Staff) user;
        List<Request> requestList = staff.getRequestList();
        if (user.getUserType() == AccountType.ADMIN) {
            requestList.addAll(RequestsHolder.getListaCereri());
        }
        int i = 1;
        for (Request request : requestList) {
            System.out.println(i + ". " +
                    request.getTitluCerere() + " " +
                    request.getDescriereCerere() + " " +
                    request.getTipCerere());
            i++;
        }
        System.out.println("Alege o opțiune:");
        System.out.println("1. Inapoi la meniul principal");
        System.out.println("2. Rezolva request");
        System.out.println("3. Respinge request");
        Scanner scanner = new Scanner(System.in);
        int option_received_requests = 0;
        boolean valid_received_requests = false;
        while (!valid_received_requests) {
            try {
                option_received_requests = scanner.nextInt();
                valid_received_requests = true;
            } catch (Exception e) {
                System.out.println("Opțiune invalidă!");
                scanner.nextLine();
            }
        }
        switch (option_received_requests) {
            case 1:
                // Inapoi la meniul principal
                showMainMenu();
                break;
            case 2:
                // Rezolva request
                System.out.println("Alege un request:");
                int requestIndex = scanner.nextInt();
                scanner.nextLine();
                if (requestIndex < 1 || requestIndex > requestList.size()) {
                    System.out.println("Opțiune invalidă!");
                    showReceivedRequests();
                }
                int index = 1;
                Request selected_request = null;
                for (Request request : requestList) {
                    if (index == requestIndex) {
                        selected_request = request;
                        break;
                    }
                    index++;
                }
                if (selected_request == null) {
                    System.out.println("Opțiune invalidă!");
                    showReceivedRequests();
                } else {
                    // rezolv request-ul
                    selected_request.rezolvaCerere(user);
                    System.out.println("Request rezolvat!");
                    showReceivedRequests();
                }
                break;
            case 3:
                // Respinge request
                System.out.println("Alege un request:");
                int requestIndex2 = scanner.nextInt();
                scanner.nextLine();
                if (requestIndex2 < 1 || requestIndex2 > requestList.size()) {
                    System.out.println("Opțiune invalidă!");
                    showReceivedRequests();
                }
                int index2 = 1;
                Request selected_request2 = null;
                for (Request request : requestList) {
                    if (index2 == requestIndex2) {
                        selected_request2 = request;
                        break;
                    }
                    index2++;
                }
                if (selected_request2 == null) {
                    System.out.println("Opțiune invalidă!");
                    showReceivedRequests();
                } else {
                    // resping request-ul
                    selected_request2.respingeCerere(user);
                    System.out.println("Request respins!");
                    showReceivedRequests();
                }
                break;
            default:
                System.out.println("Opțiune invalidă!");
                showReceivedRequests();
        }
    }

    private void showSentRequests() {
        // la sentReuests sunt user-ul reclamant
        // vreau sa afisez doar request-urile care au user-ul reclamant user-ul curent
        // si vreau pentru fiecare request sa afisez niste detalii dar si
        // posibilitatea de a retrage request-ul
        System.out.println("Lista de request-uri trimise:");
        List<Request> requestList = imdb.getRequestList();
        int i = 1;
        for (Request request : requestList) {
            if (request.getUsernameReclamant().equals(user.getUserName())) {
                System.out.println(i + ". " +
                        request.getTitluCerere() + " " +
                        request.getDescriereCerere() + " " +
                        request.getTipCerere());
                i++;
            }
        }
        System.out.println("Alege o opțiune:");
        System.out.println("1. Inapoi la meniul principal");
        System.out.println("2. Retrage request");
        Scanner scanner = new Scanner(System.in);
        int option_sent_requests = 0;
        boolean valid_sent_requests = false;
        while (!valid_sent_requests) {
            try {
                option_sent_requests = scanner.nextInt();
                valid_sent_requests = true;
            } catch (Exception e) {
                System.out.println("Opțiune invalidă!");
                scanner.nextLine();
            }
        }
        switch (option_sent_requests) {
            case 1:
                // Inapoi la meniul principal
                showMainMenu();
                break;
            case 2:
                // Retrage request
                System.out.println("Alege un request:");
                int requestIndex = 0;
                boolean valid_request_index = false;
                while (!valid_request_index) {
                    try {
                        requestIndex = scanner.nextInt();
                        valid_request_index = true;
                    } catch (Exception e) {
                        System.out.println("Opțiune invalidă!");
                        scanner.nextLine();
                    }
                }
                scanner.nextLine();
                if (requestIndex < 1 || requestIndex > requestList.size()) {
                    System.out.println("Opțiune invalidă!");
                    showSentRequests();
                }
                int index = 1;
                Request selected_request = null;
                for (Request request : requestList) {
                    if (request.getUsernameReclamant().equals(user.getUserName())) {
                        if (index == requestIndex) {
                            selected_request = request;
                            break;
                        }
                        index++;
                    }
                }
                if (selected_request == null) {
                    System.out.println("Opțiune invalidă!");
                    showSentRequests();
                } else {
                    // sterg request-ul
                    selected_request.retrageCerere(user);
                    System.out.println("Request retras!");
                    showSentRequests();
                }
                break;
            default:
                System.out.println("Opțiune invalidă!");
                showSentRequests();
        }
    }

    private void createRequest() {
        // vreau sa aleg tipul de request
        System.out.println("Creare request:");
        System.out.println("Alege un tip de request:");
        System.out.println("1. MOVIE_ISSUE (probleme cu o productie)");
        System.out.println("2. ACTOR_ISSUE (probleme cu un actor)");
        System.out.println("3. OTHERS (alte probleme)");
        System.out.println("4. DELETE_ACCOUNT (stergere cont)");
        System.out.println("5. Inapoi");
        Scanner scanner = new Scanner(System.in);
        int option_create_request = 0;
        boolean valid_create_request = false;
        while (!valid_create_request) {
            try {
                option_create_request = scanner.nextInt();
                valid_create_request = true;
            } catch (Exception e) {
                System.out.println("Opțiune invalidă!");
                scanner.nextLine();
            }
        }
        switch (option_create_request) {
            case 1:
                // MOVIE_ISSUE
                createMovieIssue();
                break;
            case 2:
                // ACTOR_ISSUE
                createActorIssue();
                break;
            case 3:
                // OTHERS
                createOthers();
                break;
            case 4:
                // DELETE_ACCOUNT
                createDeleteAccount();
                break;
            case 5:
                // Inapoi
                showMainMenu();
                break;
            default:
                System.out.println("Opțiune invalidă!");
                createRequest();
        }
    }

    private void createOthers() {
        // daca am OTHERS trebuie sa ii dau user-ului optiunea
        // de a descrie problema
        System.out.println("Creare request OTHERS:");
        System.out.println("Descrie problema:");
        Scanner scanner = new Scanner(System.in);
        String description = scanner.nextLine();
        // creez request-ul
        Request request = Request.creareCerere(RequestType.OTHERS, "", description, user);
        imdb.getRequestList().add(request);
        System.out.println("Request creat!");
        showMainMenu();
    }

    private void createDeleteAccount() {
        // daca am DELETE_ACCOUNT trebuie sa ii dau user-ului optiunea
        // de a descrie problema
        System.out.println("Creare request DELETE_ACCOUNT:");
        System.out.println("Descrie problema:");
        Scanner scanner = new Scanner(System.in);
        String description = scanner.nextLine();
        // creez request-ul
        Request request = Request.creareCerere(RequestType.DELETE_ACCOUNT, "", description, user);
        imdb.getRequestList().add(request);
        System.out.println("Request creat!");
        showMainMenu();
    }

    private void createActorIssue() {
        // daca am ACTOR_ISSUE trebuie sa ii dau user-ului optiunea
        // de a alege actorul si de a descrie problema
        System.out.println("Creare request ACTOR_ISSUE:");
        System.out.println("Alege un actor:");
        List<Actor> actorList = imdb.getActorList().stream()
                .filter(a -> (user.getUserType() != AccountType.CONTRIBUTOR ||
                        !((Contributor) user).getActorsContribution().contains(a)) &&
                        (user.getUserType() != AccountType.ADMIN ||
                                !Admin.getActorsContributionCommon().contains(a) ||
                                !((Admin) user).getActorsContribution().contains(a)))
                .toList();
        int i = 1;
        for (Actor actor : actorList) {
            System.out.println(i + ". " + actor.getName());
            i++;
        }
        System.out.println("Alege o opțiune:");
        System.out.println("1. Inapoi");
        System.out.println("2. Alege actor");
        Scanner scanner = new Scanner(System.in);
        int option_create_actor_issue = 0;
        boolean valid_create_actor_issue = false;
        while (!valid_create_actor_issue) {
            try {
                option_create_actor_issue = scanner.nextInt();
                valid_create_actor_issue = true;
            } catch (Exception e) {
                System.out.println("Opțiune invalidă!");
                scanner.nextLine();
            }
        }
        switch (option_create_actor_issue) {
            case 1:
                // Inapoi
                createRequest();
                break;
            case 2:
                // Alege actor
                System.out.println("Alege un actor:");
                int actorIndex = 0;
                boolean valid_actor_index = false;
                while (!valid_actor_index) {
                    try {
                        actorIndex = scanner.nextInt();
                        valid_actor_index = true;
                    } catch (Exception e) {
                        System.out.println("Opțiune invalidă!");
                        scanner.nextLine();
                    }
                }
                scanner.nextLine();
                if (actorIndex < 1 || actorIndex > imdb.getActorList().size()) {
                    System.out.println("Opțiune invalidă!");
                    createActorIssue();
                }
                int index = 1;
                Actor selected_actor = null;
                for (Actor actor : imdb.getActorList()) {
                    if (index == actorIndex) {
                        selected_actor = actor;
                        break;
                    }
                    index++;
                }
                if (selected_actor == null) {
                    System.out.println("Opțiune invalidă!");
                    createActorIssue();
                } else {
                    System.out.println("Descrie problema:");
                    String description = scanner.nextLine();
                    // creez request-ul
                    String nume = selected_actor.getName();
                    Request request = Request.creareCerere(RequestType.ACTOR_ISSUE, nume, description, user);
                    imdb.getRequestList().add(request);
                    System.out.println("Request creat!");
                    showMainMenu();
                }
                break;
            default:
                System.out.println("Opțiune invalidă!");
                createActorIssue();
        }
    }

    private void createMovieIssue() {
        // daca am MOVIE_ISSUE trebuie sa ii dau user-ului optiunea
        // de a alege filmul si de a descrie problema
        System.out.println("Creare request MOVIE_ISSUE:");
        System.out.println("Alege un film:");
        List<Production> productionList = imdb.getProductionList().stream()
                .filter(p -> (user.getUserType() != AccountType.CONTRIBUTOR ||
                        !((Contributor) user).getProductionsContribution().contains(p)) &&
                        (user.getUserType() != AccountType.ADMIN ||
                                !Admin.getProductionsContributionCommon().contains(p) ||
                                !((Admin) user).getProductionsContribution().contains(p)))
                .toList();
        int i = 1;
        for (Production production : productionList) {
            System.out.println(i + ". " + production.getTitlu());
            i++;
        }
        System.out.println("Alege o opțiune:");
        System.out.println("1. Inapoi");
        System.out.println("2. Alege film");
        Scanner scanner = new Scanner(System.in);
        int option_create_movie_issue = 0;
        boolean valid_create_movie_issue = false;
        while (!valid_create_movie_issue) {
            try {
                option_create_movie_issue = scanner.nextInt();
                valid_create_movie_issue = true;
            } catch (Exception e) {
                System.out.println("Opțiune invalidă!");
                scanner.nextLine();
            }
        }
        switch (option_create_movie_issue) {
            case 1:
                // Inapoi
                createRequest();
                break;
            case 2:
                // Alege film
                System.out.println("Alege un film:");
                int movieIndex = 0;
                boolean valid_movie_index = false;
                while (!valid_movie_index) {
                    try {
                        movieIndex = scanner.nextInt();
                        valid_movie_index = true;
                    } catch (Exception e) {
                        System.out.println("Opțiune invalidă!");
                        scanner.nextLine();
                    }
                }
                scanner.nextLine();
                if (movieIndex < 1 || movieIndex > imdb.getProductionList().size()) {
                    System.out.println("Opțiune invalidă!");
                    createMovieIssue();
                }
                int index = 1;
                Production selected_movie = null;
                for (Production production : imdb.getProductionList()) {
                    if (index == movieIndex) {
                        selected_movie = production;
                        break;
                    }
                    index++;
                }
                if (selected_movie == null) {
                    System.out.println("Opțiune invalidă!");
                    createMovieIssue();
                } else {
                    System.out.println("Descrie problema:");
                    String description = scanner.nextLine();
                    // creez request-ul
                    String titlu = selected_movie.getTitlu();
                    Request request = Request.creareCerere(RequestType.MOVIE_ISSUE, titlu, description, user);
                    imdb.getRequestList().add(request);
                    System.out.println("Request creat!");
                    showMainMenu();
                }
                break;
            default:
                System.out.println("Opțiune invalidă!");
                createMovieIssue();
        }
    }

    private void showUsers() {
        System.out.println("Lista de useri:");
        List<User> userList = imdb.getUserList();
        int i = 1;
        for (User user : userList) {
            System.out.println(i + ". " + user.getUserName());
            i++;
        }
        System.out.println("Alege o opțiune:");
        System.out.println("1. Inapoi la meniul principal");
        System.out.println("2. Vizualizare detalii user");
        Scanner scanner = new Scanner(System.in);
        int option_users = 0;
        boolean valid_users = false;
        while (!valid_users) {
            try {
                option_users = scanner.nextInt();
                valid_users = true;
            } catch (Exception e) {
                System.out.println("Opțiune invalidă!");
                scanner.nextLine();
            }
        }
        switch (option_users) {
            case 1:
                // Inapoi la meniul principal
                showMainMenu();
                break;
            case 2:
                // Vizualizare detalii user
                System.out.println("Alege un user:");
                int userIndex = 0;
                boolean valid_user_index = false;
                while (!valid_user_index) {
                    try {
                        userIndex = scanner.nextInt();
                        valid_user_index = true;
                    } catch (Exception e) {
                        System.out.println("Opțiune invalidă!");
                        scanner.nextLine();
                    }
                }
                if (userIndex < 1 || userIndex > imdb.getUserList().size()) {
                    System.out.println("Opțiune invalidă!");
                    showUsers();
                }
                int index = 1;
                User selected_user = null;
                for (User user : imdb.getUserList()) {
                    if (index == userIndex) {
                        selected_user = user;
                        break;
                    }
                    index++;
                }
                if (selected_user == null) {
                    System.out.println("Opțiune invalidă!");
                    showUsers();
                } else {
                    showUserDetails(selected_user);
                }
                break;
            default:
                System.out.println("Opțiune invalidă!");
                showUsers();
        }
    }

    private void showUserDetails(User selectedUser) {
        // vreau sa arat doar partea de information
        System.out.println("Detalii user:");
        System.out.println("Nume: " + selectedUser.getInformation().getUserNume());
        System.out.println("Email: " + selectedUser.getInformation().getUserCredentials().getEmail());
        System.out.println("Data nasterii: " + selectedUser.getInformation().getBirthDate());
        System.out.println("Tara: " + selectedUser.getInformation().getUserCountry());
        System.out.println("Gen: " + selectedUser.getInformation().getGender());
        System.out.println("Username: " + selectedUser.getUserName());
        System.out.println("Parola: " + selectedUser.getInformation().getUserCredentials().getPassword());
        System.out.println("Tip cont: " + selectedUser.getUserType());
        System.out.println("Experienta: " + selectedUser.getExperience());
        System.out.println("Alege o opțiune:");
        System.out.println("1. Inapoi la lista de useri");
        System.out.println("2. Modifica user");
        System.out.println("3. Sterge user");
        Scanner scanner = new Scanner(System.in);
        int option_user_details = 0;
        boolean valid_user_details = false;
        while (!valid_user_details) {
            try {
                option_user_details = scanner.nextInt();
                valid_user_details = true;
            } catch (Exception e) {
                System.out.println("Opțiune invalidă!");
                scanner.nextLine();
            }
        }
        switch (option_user_details) {
            case 1:
                // Inapoi la lista de useri
                showUsers();
                break;
            case 2:
                // Modifica user
                updateUser(selectedUser);
                break;
            case 3:
                // Sterge user
                deleteUser(selectedUser);
                break;
            default:
                System.out.println("Opțiune invalidă!");
                showUserDetails(selectedUser);
        }

    }

    private void deleteUser(User selectedUser) {
        // vreau sa sterg user-ul din lista de useri
        // daca user-ul este contributor, contributiile se adauga in lista comuna a adminilor
        if (selectedUser.getUserType() == AccountType.CONTRIBUTOR) {
            Staff staff = (Staff) selectedUser;
            for (Actor actor : staff.getActorsContribution()) {
                Admin.getActorsContributionCommon().add(actor);
            }
            for (Production production : staff.getProductionsContribution()) {
                Admin.getProductionsContributionCommon().add(production);
                // vreau sa adaug intreaga echipa de admini ca observeri
                // pentru aceasta productie
                for (User user1 : imdb.getUserList()) {
                    if (user1.getUserType() == AccountType.ADMIN) {
                        Admin admin = (Admin) user1;
                        production.addObserver(admin);
                    }
                }
            }
        }
        imdb.getUserList().remove(selectedUser);
        System.out.println("User sters!");
        showUsers();
    }

    private void updateUser(User selectedUser) {
        // vreau sa pot modifica doar numele, data nasterii, tara si genul
        System.out.println("Modifica user:");
        System.out.println("Alege o opțiune:");
        System.out.println("1. Modifica nume");
        System.out.println("2. Modifica data nasterii");
        System.out.println("3. Modifica tara");
        System.out.println("4. Modifica gen");
        System.out.println("5. Inapoi");
        Scanner scanner = new Scanner(System.in);
        int option_update_user = 0;
        boolean valid_update_user = false;
        while (!valid_update_user) {
            try {
                option_update_user = scanner.nextInt();
                valid_update_user = true;
            } catch (Exception e) {
                System.out.println("Opțiune invalidă!");
                scanner.nextLine();
            }
        }
        switch (option_update_user) {
            case 1:
                // Modifica nume
                System.out.println("Nume nou:");
                String nume = scanner.nextLine();
                selectedUser.getInformation().setUserNume(nume);
                System.out.println("Nume modificat!");
                showUserDetails(selectedUser);
                break;
            case 2:
                // Modifica data nasterii
                System.out.println("Data nasterii noua: Exemplu: \"yy-MM-dd\"");
                String data_nasterii = scanner.nextLine();
                // vreau sa verific daca data nasterii este valida
                LocalDate birthDate = null;
                boolean valid = false;
                while (!valid) {
                    try {
                        birthDate = LocalDate.parse(data_nasterii);
                        valid = true;
                    } catch (Exception e) {
                        System.out.println("Data nasterii trebuie sa fie de forma yy-MM-dd!");
                    }
                }
                selectedUser.getInformation().setBirthDate(birthDate);
                System.out.println("Data nasterii modificata!");
                showUserDetails(selectedUser);
                break;
            case 3:
                // Modifica tara
                System.out.println("Tara noua:");
                String tara = scanner.nextLine();
                selectedUser.getInformation().setUserCountry(tara);
                System.out.println("Tara modificata!");
                showUserDetails(selectedUser);
                break;
            case 4:
                // Modifica gen
                System.out.println("Gen nou:");
                String gen = scanner.nextLine();
                selectedUser.getInformation().setGender(gen);
                System.out.println("Gen modificat!");
                showUserDetails(selectedUser);
                break;
            case 5:
                // Inapoi
                showUserDetails(selectedUser);
                break;
            default:
                System.out.println("Opțiune invalidă!");
                updateUser(selectedUser);
        }
    }

    private void createUser() {
        System.out.println("Creare user:");
        System.out.println("Alege un tip de cont:");
        System.out.println("1. Regular");
        System.out.println("2. Admin");
        System.out.println("3. Contributor");
        Scanner scanner = new Scanner(System.in);
        int option_create_user = 0;
        boolean valid_create_user = false;
        while (!valid_create_user) {
            try {
                option_create_user = scanner.nextInt();
                valid_create_user = true;
            } catch (Exception e) {
                System.out.println("Opțiune invalidă!");
                scanner.nextLine();
            }
        }
        switch (option_create_user) {
            case 1:
                // Regular
                createRegular();
                break;
            case 2:
                // Admin
                createAdmin();
                break;
            case 3:
                // Contributor
                createContributor();
                break;
            default:
                System.out.println("Opțiune invalidă!");
                createUser();
        }
    }

    private void createAdmin() {
        // va trebui sa furnizez informatii despre user : email, nume, data nasterii, tara
        // username si parola vor fi generate automat
        System.out.println("Creare user admin:");
        System.out.println("Email:");
        Scanner scanner = new Scanner(System.in);
        String email = scanner.nextLine();
        System.out.println("Nume:");
        String nume = scanner.nextLine();
        System.out.println("Data nasterii: Exemplu: \"yyyy-MM-dd\"");
        String data_nasterii = scanner.nextLine();
        // vreau sa verific daca data nasterii este valida
        LocalDate birthDate = null;
        boolean valid = false;
        while (!valid) {
            try {
                birthDate = LocalDate.parse(data_nasterii);
                valid = true;
            } catch (Exception e) {
                System.out.println("Data nasterii trebuie sa fie de forma yyyy-MM-dd!");
            }
        }
        System.out.println("Tara:");
        String tara = scanner.nextLine();
        System.out.println("Gen:");
        String gen = scanner.nextLine();
        if (!gen.equals("Female") && !gen.equals("Male")){
            System.out.println("Genul trebuie sa fie ori Female ori Male!");
            createUser();
        }
        // creez informatiile user-ului
        User.Information information = new User.Information();
        String password = PasswordGenerator.generatePassword();
        Credentials credentials = new Credentials(email, password);
        information.setUserCredentials(credentials);
        information.setUserNume(nume);
        if (valid) {
            information.setBirthDate(birthDate);
        }
        information.setUserCountry(tara);
        information.setGender(gen);
        String username = information.generateUsername(nume);
        // verific daca username-ul este unic
        for (User user : imdb.getUserList()) {
            if (user.getUserName().equals(username)) {
                System.out.println("Mai există un user cu acest username!");
                createUser();
                return;
            }
        }
        // creez user-ul
        Admin admin = new Admin(information, AccountType.ADMIN, username, 0);
        // adaug user-ul in lista de useri
        imdb.getUserList().add(admin);
        showMainMenu();
    }

    private void createContributor() {
        // va trebui sa furnizez informatii despre user : email, nume, data nasterii, tara
        // username si parola vor fi generate automat
        System.out.println("Creare user contributor:");
        System.out.println("Email:");
        Scanner scanner = new Scanner(System.in);
        String email = scanner.nextLine();
        System.out.println("Nume:");
        String nume = scanner.nextLine();
        System.out.println("Data nasterii: Exemplu: \"yyyy-MM-dd\"");
        String data_nasterii = scanner.nextLine();
        // vreau sa verific daca data nasterii este valida
        LocalDate birthDate = null;
        boolean valid = false;
        while (!valid) {
            try {
                birthDate = LocalDate.parse(data_nasterii);
                valid = true;
            } catch (Exception e) {
                System.out.println("Data nasterii trebuie sa fie de forma yyyy-MM-dd!");
            }
        }
        System.out.println("Tara:");
        String tara = scanner.nextLine();
        System.out.println("Gen:");
        String gen = scanner.nextLine();
        if (!gen.equals("Female") && !gen.equals("Male")){
            System.out.println("Genul trebuie sa fie ori Female ori Male!");
            createUser();
        }
        // creez informatiile user-ului
        User.Information information = new User.Information();
        String password = PasswordGenerator.generatePassword();
        Credentials credentials = new Credentials(email, password);
        information.setUserCredentials(credentials);
        information.setUserNume(nume);
        if (valid) {
            information.setBirthDate(birthDate);
        }
        information.setUserCountry(tara);
        information.setGender(gen);
        String username = information.generateUsername(nume);
        // verific daca username-ul este unic
        for (User user : imdb.getUserList()) {
            if (user.getUserName().equals(username)) {
                System.out.println("Mai există un user cu acest username!");
                createUser();
                return;
            }
        }
        // creez user-ul
        Contributor contributor = new Contributor(information, AccountType.CONTRIBUTOR, username, 0);
        // adaug user-ul in lista de useri
        imdb.getUserList().add(contributor);
        showMainMenu();
    }

    private void createRegular() {
        // va trebui sa furnizez informatii despre user : email, nume, data nasterii, tara
        // username si parola vor fi generate automat
        System.out.println("Creare user regular:");
        System.out.println("Email:");
        Scanner scanner = new Scanner(System.in);
        String email = scanner.nextLine();
        System.out.println("Nume:");
        String nume = scanner.nextLine();
        System.out.println("Data nasterii: Exemplu: \"yyyy-MM-dd\"");
        String data_nasterii = scanner.nextLine();
        // vreau sa verific daca data nasterii este valida
        LocalDate birthDate = null;
        boolean valid = false;
        while (!valid) {
            try {
                birthDate = LocalDate.parse(data_nasterii);
                valid = true;
            } catch (Exception e) {
                System.out.println("Data nasterii trebuie sa fie de forma yyyy-MM-dd!");
            }
        }
        System.out.println("Tara:");
        String tara = scanner.nextLine();
        System.out.println("Gen: Female/Male");
        String gen = scanner.nextLine();
        // trebuie sa verific daca genul este in formatul ok
        if (!gen.equals("Female") && !gen.equals("Male")){
            System.out.println("Genul trebuie sa fie ori Female ori Male!");
            createUser();
        }
        // creez informatiile user-ului
        User.Information information = new User.Information();
        String password = PasswordGenerator.generatePassword();
        Credentials credentials = new Credentials(email, password);
        information.setUserCredentials(credentials);
        information.setUserNume(nume);
        if (valid) {
            information.setBirthDate(birthDate);
        }
        information.setUserCountry(tara);
        information.setGender(gen);
        String username = information.generateUsername(nume);
        // verific daca username-ul este unic
        for (User user : imdb.getUserList()) {
            if (user.getUserName().equals(username)) {
                System.out.println("Mai există un user cu acest username!");
                createUser();
                return;
            }
        }
        // creez user-ul
        Regular regular = new Regular(information, AccountType.REGULAR, username, 0);
        // adaug user-ul in lista de useri
        imdb.getUserList().add(regular);
        showMainMenu();
    }

    private void showAccount() {
        // vreau sa afisez informatiile despre contul meu
        System.out.println("Contul meu:");
        System.out.println("Nume: " + user.getInformation().getUserNume());
        System.out.println("Email: " + user.getInformation().getUserCredentials().getEmail());
        System.out.println("Data nasterii: " + user.getInformation().getBirthDate());
        System.out.println("Tara: " + user.getInformation().getUserCountry());
        System.out.println("Username: " + user.getUserName());
        System.out.println("Tip cont: " + user.getUserType());
        System.out.println("Experienta: " + user.getExperience());
        // vreau sa arat notificarile
        System.out.println("Notificări:");
        for (String notification : user.getNotifications()) {
            System.out.println(notification);
        }
        System.out.println("Alege o opțiune:");
        System.out.println("1. Inapoi la meniul principal");
        Scanner scanner = new Scanner(System.in);
        int option_account = 0;
        boolean valid_account = false;
        while (!valid_account) {
            try {
                option_account = scanner.nextInt();
                valid_account = true;
            } catch (Exception e) {
                System.out.println("Opțiune invalidă!");
                scanner.nextLine();
            }
        }
        if (option_account == 1) {
            // Inapoi la meniul principal
            showMainMenu();
        } else {
            System.out.println("Opțiune invalidă!");
            showAccount();
        }
    }

    private void addActor() {
        // am de adaugat un actor care are un nume unic, o biografie si o lista de performante
        System.out.println("Adaugă actor:");
        System.out.println("Nume:");
        Scanner scanner = new Scanner(System.in);
        String nume = scanner.nextLine();
        // verific daca numele este unic
        for (Actor actor : imdb.getActorList()) {
            if (actor.getName().equals(nume)) {
                System.out.println("Mai există un actor cu acest nume!");
                addActor();
                return;
            }
        }
        System.out.println("Biografie:");
        String biografie = scanner.nextLine();
        System.out.println("Performante: Exemplu: Titlu1 - Tip1, Titlu2 - Tip2");
        String performante = scanner.nextLine();
        List<Actor.Performance> performanceList = new ArrayList<>();
        String[] performanceArray = performante.split(", ");
        for (String performance : performanceArray) {
            String[] performanceDetails = performance.split(" - ");
            // vreau sa verific validitatea performantei
            if (performanceDetails.length != 2) {
                System.out.println("Performanta trebuie sa fie de forma Titlu - Tip!");
                addActor();
                return;
            }
            // trebuie sa verific daca tipul este valid: Series sau Movie
            if (!performanceDetails[1].equals("Series") && !performanceDetails[1].equals("Movie")) {
                System.out.println("Tipul trebuie sa fie Series sau Movie!");
                addActor();
                return;
            }
            Actor.Performance performance1 = new Actor.Performance(performanceDetails[0], performanceDetails[1]);
            performanceList.add(performance1);
        }
        // creez actorul
        Actor actor = new Actor(nume, performanceList, biografie);
        // adaug actorul in lista de actori
        Staff staff = (Staff) user;
        staff.addActorSystem(actor);
        // adaug exp-ul user-ului
        user.setExperienceStrategy(new ContributionStrategy());
        user.updateExperience();
        System.out.println("Actor adăugat!");
        showMainMenu();
    }

    private void addProduction() {
        // trebuie sa vad daca se vrea adaugarea unui film sau a unui serial
        System.out.println("Adaugă producție:");
        System.out.println("Alege o opțiune:");
        System.out.println("1. Film");
        System.out.println("2. Serial");
        System.out.println("3. Inapoi");
        Scanner scanner = new Scanner(System.in);
        int option_add_production = 0;
        boolean valid_add_production = false;
        while (!valid_add_production) {
            try {
                option_add_production = scanner.nextInt();
                valid_add_production = true;
            } catch (Exception e) {
                System.out.println("Opțiune invalidă!");
                scanner.nextLine();
            }
        }
        switch (option_add_production) {
            case 1:
                // Film
                addMovie();
                break;
            case 2:
                // Serial
                addSeries();
                break;
            case 3:
                // Inapoi
                showMainMenu();
                break;
            default:
                System.out.println("Opțiune invalidă!");
                addProduction();
                break;
        }
    }

    private void addSeries() {
        // am de adaugat un serial care are un titlu unic, un an de aparitie, o descriere, un gen, o lista de actori
        // o lista de regizori si un numar de sezoane si un Map<String, List<Episode>> care trebuie completat
        System.out.println("Adaugă serial:");
        System.out.println("Titlu:");
        Scanner scanner = new Scanner(System.in);
        String titlu = scanner.nextLine();
        // verific daca titlul este unic
        for (Production production : imdb.getProductionList()) {
            if (production.getTitlu().equals(titlu)) {
                System.out.println("Mai există o producție cu acest titlu!");
                addSeries();
                return;
            }
        }
        System.out.println("An aparitie:");
        // anul de aparitie trebuie sa fie un numar
        int an_aparitie = 0;
        boolean valid = false;
        while (!valid) {
            try {
                an_aparitie = scanner.nextInt();
                valid = true;
            } catch (Exception e) {
                System.out.println("Anul de aparitie trebuie sa fie un numar!");
            }
        }
        System.out.println("Descriere:");
        scanner.nextLine();
        String descriere = scanner.nextLine();
        System.out.println("Gen:");
        String gen = scanner.nextLine();
        List<String> genreList = new ArrayList<>();
        String[] genreArray = gen.split(", ");
        for (String genre : genreArray) {
            genreList.add(genre.trim());
        }
        System.out.println("Lista de actori: Exemplu: \"Actor1, Actor2\"");
        String actori = scanner.nextLine();
        List<String> actoriList = new ArrayList<>();
        String[] actoriArray = actori.split(", ");
        for (String actor : actoriArray) {
            actoriList.add(actor.trim());
        }
        System.out.println("Lista de regizori: Exemplu: \"Regizor1, Regizor2\"");
        String regizori = scanner.nextLine();
        List<String> regizoriList = new ArrayList<>();
        String[] regizoriArray = regizori.split(", ");
        for (String regizor : regizoriArray) {
            regizoriList.add(regizor.trim());
        }
        System.out.println("Numar sezoane:");
        // numarul de sezoane trebuie sa fie un numar
        int numar_sezoane = 0;
        valid = false;
        while (!valid) {
            try {
                numar_sezoane = scanner.nextInt();
                valid = true;
            } catch (Exception e) {
                System.out.println("Numarul de sezoane trebuie sa fie un numar!");
            }
        }
        scanner.nextLine();
        Map<String, List<Episode>> mapSerial = new HashMap<>();
        for (int i = 1; i <= numar_sezoane; i++) {
            System.out.println("Sezonul " + i + ":");
            System.out.println("Numar episoade:");
            // numarul de episoade trebuie sa fie un numar
            int numar_episoade = 0;
            valid = false;
            while (!valid) {
                try {
                    numar_episoade = scanner.nextInt();
                    valid = true;
                } catch (Exception e) {
                    System.out.println("Numarul de episoade trebuie sa fie un numar!");
                }
            }
            scanner.nextLine();
            List<Episode> episodes = new ArrayList<>();
            for (int j = 1; j <= numar_episoade; j++) {
                System.out.println("Episodul " + j + ":");
                System.out.println("Titlu:");
                String titlu_episod = scanner.nextLine();
                System.out.println("Durata: Exemplu: \"x minutes\"");
                String durata_episod = scanner.nextLine();
                // vreau sa verific daca durata este valida
                if (!durata_episod.contains("minutes")) {
                    System.out.println("Durata trebuie sa fie de forma x minutes!");
                    addSeries();
                    return;
                }
                Episode episode = new Episode(titlu_episod, durata_episod);
                episodes.add(episode);
            }
            mapSerial.put("Sezonul " + i, episodes);
        }
        // creez serialul
        Series series = new Series(titlu, regizoriList, actoriList,
                genreList, new ArrayList<>(), descriere, an_aparitie, numar_sezoane);
        series.setMapSerial(mapSerial);
        // adaug serialul in lista de seriale
        Staff staff = (Staff) user;
        staff.addProductionSystem(series);
        // adaug exp-ul user-ului
        user.setExperienceStrategy(new ContributionStrategy());
        user.updateExperience();
        System.out.println("Serial adăugat!");
        showMainMenu();
    }

    private void addMovie() {
        // am de adaugat un film care are un titlu unic, un an de aparitie, o descriere, un gen, o lista de actori
        // si o lista de regizori
        System.out.println("Adaugă film:");
        System.out.println("Titlu:");
        Scanner scanner = new Scanner(System.in);
        String titlu = scanner.nextLine();
        // verific daca titlul este unic
        for (Production production : imdb.getProductionList()) {
            if (production.getTitlu().equals(titlu)) {
                System.out.println("Mai există o producție cu acest titlu!");
                addMovie();
                return;
            }
        }
        System.out.println("An aparitie:");
        // anul de aparitie trebuie sa fie un numar
        int an_aparitie = 0;
        boolean valid = false;
        while (!valid) {
            try {
                an_aparitie = scanner.nextInt();
                valid = true;
            } catch (Exception e) {
                System.out.println("Anul de aparitie trebuie sa fie un numar!");
            }
        }
        System.out.println("Descriere:");
        scanner.nextLine();
        String descriere = scanner.nextLine();
        System.out.println("Gen:");
        String gen = scanner.nextLine();
        List<String> genreList = new ArrayList<>();
        String[] genreArray = gen.split(", ");
        for (String genre : genreArray) {
            genreList.add(genre.trim());
        }
        System.out.println("Lista de actori: Exemplu: \"Actor1, Actor2\"");
        String actori = scanner.nextLine();
        List<String> actoriList = new ArrayList<>();
        String[] actoriArray = actori.split(", ");
        for (String actor : actoriArray) {
            actoriList.add(actor.trim());
        }
        System.out.println("Lista de regizori: Exemplu: \"Regizor1, Regizor2\"");
        String regizori = scanner.nextLine();
        List<String> regizoriList = new ArrayList<>();
        String[] regizoriArray = regizori.split(", ");
        for (String regizor : regizoriArray) {
            regizoriList.add(regizor.trim());
        }
        System.out.println("Durata: Exemplu: \"x minutes\"");
        String durata = scanner.nextLine();
        // vreau sa verific daca durata este valida
        if (!durata.contains("minutes")) {
            System.out.println("Durata trebuie sa fie de forma x minutes!");
            addMovie();
            return;
        }
        // creez filmul
        Movie movie = new Movie(titlu, regizoriList, actoriList,
                genreList, new ArrayList<>(), descriere, durata, an_aparitie);
        // adaug filmul in lista de filme
        Staff staff = (Staff) user;
        staff.addProductionSystem(movie);
        // adaug exp-ul user-ului
        user.setExperienceStrategy(new ContributionStrategy());
        user.updateExperience();
        System.out.println("Film adăugat!");
        showMainMenu();
    }

    private void showFavoriteActors() {
        TreeSet<Actor> actors = user.getFavoriteActors();
        int i = 1;
        for (Actor actor : actors) {
            System.out.println(i + ". " + actor.getName());
        }
        System.out.println("Alege o optiune:");
        System.out.println("1. Inapoi la meniul principal");
        System.out.println("2. Vizualizare detalii actor");
        Scanner scanner = new Scanner(System.in);
        int option_actors = 0;
        boolean valid_actors = false;
        while (!valid_actors) {
            try {
                option_actors = scanner.nextInt();
                valid_actors = true;
            } catch (Exception e) {
                System.out.println("Opțiune invalidă!");
                scanner.nextLine();
            }
        }
        switch (option_actors) {
            case 1:
                // Inapoi la meniul principal
                showMainMenu();
                break;
            case 2:
                // Vizualizare detalii actor
                System.out.println("Alege un actor:");
                int actorIndex = 0;
                boolean valid_actor_index = false;
                while (!valid_actor_index) {
                    try {
                        actorIndex = scanner.nextInt();
                        valid_actor_index = true;
                    } catch (Exception e) {
                        System.out.println("Opțiune invalidă!");
                        scanner.nextLine();
                    }
                }
                if (actorIndex < 1 || actorIndex > actors.size()) {
                    System.out.println("Opțiune invalidă!");
                    showFavoriteActors();
                }
                int index = 1;
                Actor selected_actor = null;
                for (Actor actor : actors) {
                    if (index == actorIndex) {
                        selected_actor = actor;
                        break;
                    }
                    index++;
                }
                if (selected_actor == null) {
                    System.out.println("Opțiune invalidă!");
                    showFavoriteActors();
                } else {
                    showActorDetails(selected_actor);
                }
                break;
            default:
                System.out.println("Opțiune invalidă!");
                showFavoriteActors();
        }
    }

    private void showFavoriteProductions() {
        TreeSet<Production> productions = user.getFavoriteProductions();
        int i = 1;
        for (Production production : productions) {
            System.out.println(i + ". " + production.getTitlu());
        }
        System.out.println("Alege o optiune:");
        System.out.println("1. Inapoi la meniul principal");
        System.out.println("2. Vizualizare detalii producție");
        Scanner scanner = new Scanner(System.in);
        int option_productions = 0;
        boolean valid_productions = false;
        while (!valid_productions) {
            try {
                option_productions = scanner.nextInt();
                valid_productions = true;
            } catch (Exception e) {
                System.out.println("Opțiune invalidă!");
                scanner.nextLine();
            }
        }
        switch (option_productions) {
            case 1:
                // Inapoi la meniul principal
                showMainMenu();
                break;
            case 2:
                // Vizualizare detalii producție
                System.out.println("Alege o producție:");
                int productionIndex = 0;
                boolean valid_production_index = false;
                while (!valid_production_index) {
                    try {
                        productionIndex = scanner.nextInt();
                        valid_production_index = true;
                    } catch (Exception e) {
                        System.out.println("Opțiune invalidă!");
                        scanner.nextLine();
                    }
                }
                if (productionIndex < 1 || productionIndex > productions.size()) {
                    System.out.println("Opțiune invalidă!");
                    showFavoriteProductions();
                }
                int index = 1;
                Production selected_production = null;
                for (Production production : productions) {
                    if (index == productionIndex) {
                        selected_production = production;
                        break;
                    }
                    index++;
                }
                if (selected_production == null) {
                    System.out.println("Opțiune invalidă!");
                    showFavoriteProductions();
                } else {
                    showProductionDetails(selected_production);
                }
                break;
            default:
                System.out.println("Opțiune invalidă!");
                showFavoriteProductions();
        }
    }

    private void showActorsContribution() {
        // daca user-ul e admin atunci trebuie sa afisez si actorii din lista comuna
        List<Actor> actors = new ArrayList<>();
        if (user.getUserType() == AccountType.ADMIN) {
            actors.addAll(Admin.getActorsContributionCommon());
        }
        actors.addAll(((Staff) user).getActorsContribution());
        Collections.sort(actors, (a1, a2) -> a1.getName().compareToIgnoreCase(a2.getName()));
        System.out.println("Lista de actori adăugați:");
        for (int i = 0; i < actors.size(); i++) {
            System.out.println((i + 1) + ". " + actors.get(i).getName());
        }
        System.out.println("Alege o optiune:");
        System.out.println("1. Inapoi la meniul principal");
        System.out.println("2. Vizualizare detalii actor");
        Scanner scanner = new Scanner(System.in);
        int option_actors = 0;
        boolean valid_actors = false;
        while (!valid_actors) {
            try {
                option_actors = scanner.nextInt();
                valid_actors = true;
            } catch (Exception e) {
                System.out.println("Opțiune invalidă!");
                scanner.nextLine();
            }
        }
        switch (option_actors) {
            case 1:
                // Inapoi la meniul principal
                showMainMenu();
                break;
            case 2:
                // Vizualizare detalii actor
                System.out.println("Alege un actor:");
                int actorIndex = 0;
                boolean valid_actor = false;
                while (!valid_actor) {
                    try {
                        actorIndex = scanner.nextInt();
                        valid_actor = true;
                    } catch (Exception e) {
                        System.out.println("Opțiune invalidă!");
                        scanner.nextLine();
                    }
                }
                if (actorIndex < 1 || actorIndex > actors.size()) {
                    System.out.println("Opțiune invalidă!");
                    showActorsContribution();
                }
                Actor actor = actors.get(actorIndex - 1);
                showActorDetails(actor);
                break;
            default:
                System.out.println("Opțiune invalidă!");
                showActorsContribution();
        }
    }

    private void showProductionsContribution() {
        // daca user-ul e admin atunci trebuie sa afisez si productiile din lista comuna
        List<Production> productions = new ArrayList<>();
        if (user.getUserType() == AccountType.ADMIN) {
            productions.addAll(Admin.getProductionsContributionCommon());
        }
        productions.addAll(((Staff) user).getProductionsContribution());
        Collections.sort(productions, (p1, p2) -> p1.getTitlu().compareToIgnoreCase(p2.getTitlu()));
        System.out.println("Lista de producții adăugate:");
        for (int i = 0; i < productions.size(); i++) {
            System.out.println((i + 1) + ". " + productions.get(i).getTitlu());
        }
        System.out.println("Alege o optiune:");
        System.out.println("1. Inapoi la meniul principal");
        System.out.println("2. Vizualizare detalii producție");
        Scanner scanner = new Scanner(System.in);
        int option_productions = 0;
        boolean valid_productions = false;
        while (!valid_productions) {
            try {
                option_productions = scanner.nextInt();
                valid_productions = true;
            } catch (Exception e) {
                System.out.println("Opțiune invalidă!");
                scanner.nextLine();
            }
        }
        switch (option_productions) {
            case 1:
                // Inapoi la meniul principal
                showMainMenu();
                break;
            case 2:
                // Vizualizare detalii producție
                System.out.println("Alege o producție:");
                int productionIndex = 0;
                boolean valid_production = false;
                while (!valid_production) {
                    try {
                        productionIndex = scanner.nextInt();
                        valid_production = true;
                    } catch (Exception e) {
                        System.out.println("Opțiune invalidă!");
                        scanner.nextLine();
                    }
                }
                if (productionIndex < 1 || productionIndex > productions.size()) {
                    System.out.println("Opțiune invalidă!");
                    showProductionsContribution();
                }
                Production production = productions.get(productionIndex - 1);
                showProductionDetails(production);
                break;
            default:
                System.out.println("Opțiune invalidă!");
                showProductionsContribution();
        }
    }

    private void showActors() {
        List<Actor> actors = imdb.getActorList();
        Collections.sort(actors, (a1, a2) -> a1.getName().compareToIgnoreCase(a2.getName()));
        System.out.println("Lista de actori:");
        for (int i = 0; i < actors.size(); i++) {
            System.out.println((i + 1) + ". " + actors.get(i).getName());
        }
        System.out.println("Alege o optiune:");
        System.out.println("1. Inapoi la meniul principal");
        System.out.println("2. Vizualizare detalii actor");
        Scanner scanner = new Scanner(System.in);
        int option_actors = 0;
        boolean valid_actors = false;
        while (!valid_actors) {
            try {
                option_actors = scanner.nextInt();
                valid_actors = true;
            } catch (Exception e) {
                System.out.println("Opțiune invalidă!");
                scanner.nextLine();
            }
        }
        switch (option_actors) {
            case 1:
                // Inapoi la meniul principal
                showMainMenu();
                break;
            case 2:
                // Vizualizare detalii actor
                System.out.println("Alege un actor:");
                int actorIndex = 0;
                boolean valid_actor = false;
                while (!valid_actor) {
                    try {
                        actorIndex = scanner.nextInt();
                        valid_actor = true;
                    } catch (Exception e) {
                        System.out.println("Opțiune invalidă!");
                        scanner.nextLine();
                    }
                }
                if (actorIndex < 1 || actorIndex > actors.size()) {
                    System.out.println("Opțiune invalidă!");
                    showActors();
                }
                Actor actor = actors.get(actorIndex - 1);
                showActorDetails(actor);
                break;
            default:
                System.out.println("Opțiune invalidă!");
                showActors();
        }
    }

    private void modifyActor(Actor actor) {
        System.out.println("Modificare actor:");
        System.out.println("Alege o opțiune:");
        System.out.println("1. Nume");
        System.out.println("2. Biografie");
        System.out.println("3. Performante");
        System.out.println("4. Inapoi");
        Scanner scanner = new Scanner(System.in);
        int option_modify_actor = 0;
        boolean valid_modify_actor = false;
        while (!valid_modify_actor) {
            try {
                option_modify_actor = scanner.nextInt();
                valid_modify_actor = true;
            } catch (Exception e) {
                System.out.println("Opțiune invalidă!");
                scanner.nextLine();
            }
        }
        switch (option_modify_actor) {
            case 1:
                // Nume
                System.out.println("Nume nou:");
                scanner.nextLine();
                String nume = scanner.nextLine();
                // verific daca numele este unic
                for (Actor actor1 : imdb.getActorList()) {
                    if (actor1.getName().equals(nume)) {
                        System.out.println("Mai există un actor cu acest nume!");
                        modifyActor(actor);
                        return;
                    }
                }
                actor.setName(nume);
                System.out.println("Nume modificat!");
                modifyActor(actor);
                break;
            case 2:
                // Biografie
                System.out.println("Biografie nouă:");
                scanner.nextLine();
                String biografie = scanner.nextLine();
                actor.setBiography(biografie);
                System.out.println("Biografie modificată!");
                modifyActor(actor);
                break;
            case 3:
                // Performante
                System.out.println("Alege o opțiune:");
                System.out.println("1. Adaugă performanță");
                System.out.println("2. Modifică performanță");
                System.out.println("3. Șterge performanță");
                System.out.println("4. Inapoi");
                int option_performances = 0;
                boolean valid_performances = false;
                while (!valid_performances) {
                    try {
                        option_performances = scanner.nextInt();
                        valid_performances = true;
                    } catch (Exception e) {
                        System.out.println("Opțiune invalidă!");
                        scanner.nextLine();
                    }
                }
                switch (option_performances) {
                    case 1:
                        // Adaugă performanță
                        System.out.println("Adaugă performanță:");
                        System.out.println("Titlu:");
                        scanner.nextLine();
                        String titlu = scanner.nextLine();
                        System.out.println("Tip:");
                        String tip = scanner.nextLine();
                        Actor.Performance performance = new Actor.Performance(titlu, tip);
                        actor.getPerformances().add(performance);
                        System.out.println("Performanță adăugată!");
                        modifyActor(actor);
                        break;
                    case 2:
                        // Modifică performanță
                        System.out.println("Alege o performanță:");
                        for (int i = 0; i < actor.getPerformances().size(); i++) {
                            Actor.Performance performance1 = actor.getPerformances().get(i);
                            System.out.println((i + 1) + ". " + performance1.getTitle() + " - " + performance1.getType());
                        }
                        int option_modify_performance = 0;
                        boolean valid_modify_performance = false;
                        while (!valid_modify_performance) {
                            try {
                                option_modify_performance = scanner.nextInt();
                                valid_modify_performance = true;
                            } catch (Exception e) {
                                System.out.println("Opțiune invalidă!");
                                scanner.nextLine();
                            }
                        }
                        if (option_modify_performance < 1 || option_modify_performance > actor.getPerformances().size()) {
                            System.out.println("Opțiune invalidă!");
                            modifyActor(actor);
                            return;
                        }
                        Actor.Performance performance3 = actor.getPerformances().get(option_modify_performance - 1);
                        System.out.println("Modificare performanță:");
                        System.out.println("Alege o opțiune:");
                        System.out.println("1. Titlu");
                        System.out.println("2. Tip");
                        int option_modify_performance2 = 0;
                        boolean valid_modify_performance2 = false;
                        while (!valid_modify_performance2) {
                            try {
                                option_modify_performance2 = scanner.nextInt();
                                valid_modify_performance2 = true;
                            } catch (Exception e) {
                                System.out.println("Opțiune invalidă!");
                                scanner.nextLine();
                            }
                        }
                        switch (option_modify_performance2) {
                            case 1:
                                // Titlu
                                System.out.println("Titlu nou:");
                                scanner.nextLine();
                                String titlu2 = scanner.nextLine();
                                performance3.setTitle(titlu2);
                                System.out.println("Titlu modificat!");
                                modifyActor(actor);
                                break;
                            case 2:
                                // Tip
                                System.out.println("Tip nou:");
                                scanner.nextLine();
                                String tip2 = scanner.nextLine();
                                performance3.setType(tip2);
                                System.out.println("Tip modificat!");
                                modifyActor(actor);
                                break;
                            default:
                                System.out.println("Opțiune invalidă!");
                                modifyActor(actor);
                                break;
                        }
                        break;
                    case 3:
                        // Șterge performanță
                        System.out.println("Alege o performanță:");
                        for (int i = 0; i < actor.getPerformances().size(); i++) {
                            Actor.Performance performance2 = actor.getPerformances().get(i);
                            System.out.println((i + 1) + ". " + performance2.getTitle() + " - " + performance2.getType());
                        }
                        int option_delete_performance = 0;
                        boolean valid_delete_performance = false;
                        while (!valid_delete_performance) {
                            try {
                                option_delete_performance = scanner.nextInt();
                                valid_delete_performance = true;
                            } catch (Exception e) {
                                System.out.println("Opțiune invalidă!");
                                scanner.nextLine();
                            }
                        }
                        if (option_delete_performance < 1 || option_delete_performance > actor.getPerformances().size()) {
                            System.out.println("Opțiune invalidă!");
                            modifyActor(actor);
                            return;
                        }
                        Actor.Performance performance2 = actor.getPerformances().get(option_delete_performance - 1);
                        actor.getPerformances().remove(performance2);
                        System.out.println("Performanță ștearsă!");
                        modifyActor(actor);
                        break;
                    case 4:
                        // Inapoi
                        modifyActor(actor);
                        break;
                    default:
                        System.out.println("Opțiune invalidă!");
                        modifyActor(actor);
                        break;
                }
                break;
            case 4:
                // Inapoi
                showActorDetails(actor);
                break;
            default:
                System.out.println("Opțiune invalidă!");
                modifyActor(actor);
                break;
        }
    }

    private void showActorDetails(Actor actor) {
        System.out.println("Detalii actor:");
        actor.displayInfo();
        System.out.println("Alege o optiune:");
        System.out.println("1. Inapoi la lista de actori");
        // trebuie sa vad daca user-ul a adaugat sau nu actorul la actorii favoriti
        if (user.getFavoriteActors().contains(actor)) {
            System.out.println("2. Elimină de la favorite");
        } else {
            System.out.println("2. Adaugă la favorite");
        }
        boolean canModify = false;
        if (user.getUserType() == AccountType.ADMIN || user.getUserType() == AccountType.CONTRIBUTOR) {
            // verific daca actorul este adaugat de user
            if (user.getUserType() == AccountType.CONTRIBUTOR) {
                Contributor contributor = (Contributor) user;
                if (contributor.getActorsContribution().contains(actor)) {
                    System.out.println("3. Modifica actor");
                    canModify = true;
                }
            } else {
                Admin admin = (Admin) user;
                if (admin.getActorsContribution().contains(actor) ||
                        Admin.getActorsContributionCommon().contains(actor)) {
                    System.out.println("3. Modifica actor");
                    canModify = true;
                }
            }
        }
        // vreau si posibilitatea de a dauga rating actorului
        boolean rated = false;
        if (user.getUserType() == AccountType.REGULAR) {
            if (actor.getRatingList() == null) {
                System.out.println("4. Adaugă recenzie");
            } else {
                for (Rating rating : actor.getRatingList()) {
                    if (Objects.equals(rating.getUsernameRater(), username)) {
                        rated = true;
                        break;
                    }
                }
                if (!rated) {
                    System.out.println("4. Adaugă recenzie");
                } else {
                    System.out.println("4. Sterge recenzie");
                }
            }
        }
        Scanner scanner = new Scanner(System.in);
        int option_actor_details = 0;
        boolean valid_actor_details = false;
        while (!valid_actor_details) {
            try {
                option_actor_details = scanner.nextInt();
                valid_actor_details = true;
            } catch (Exception e) {
                System.out.println("Opțiune invalidă!");
                scanner.nextLine();
            }
        }
        switch (option_actor_details) {
            case 1:
                // Inapoi la lista de actori
                showActors();
                break;
            case 2:
                if (user.getFavoriteActors().contains(actor)) {
                    // Elimină de la favorite
                    user.remove_favorite_actor(actor);
                    System.out.println("Actorul a fost eliminat de la favorite!");
                } else {
                    // Adaugă la favorite
                    user.add_favorite_actor(actor);
                    System.out.println("Actorul a fost adăugat la favorite!");
                }
                showActorDetails(actor);
                break;
            case 3:
                // Modifica actor
                if (canModify) {
                    // metoda de modificare
                    modifyActor(actor);
                } else {
                    System.out.println("Nu aveți dreptul să modificați acest actor!");
                    showActorDetails(actor);
                }
                break;
            case 4:
                if (user.getUserType() != AccountType.REGULAR) {
                    System.out.println("Opțiune invalidă!");
                    showActorDetails(actor);
                    return;
                }
                if (rated) {
                    // Sterge recenzie
                    Regular regular = (Regular) user;
                    regular.remove_rating();
                    actor.removeRating(username);
                } else {
                    // Adaugă recenzie
                    System.out.println("Adaugă recenzie:");
                    System.out.println("Nota (1-10):");
                    int nota = 0;
                    // verific daca nota este valida
                    // sau daca nu a fost introdusa o litera
                    boolean valid_nota = false;
                    while (!valid_nota) {
                        try {
                            nota = scanner.nextInt();
                            if (nota < 1 || nota > 10) {
                                System.out.println("Nota trebuie sa fie intre 1 si 10!");
                            } else {
                                valid_nota = true;
                            }
                        } catch (Exception e) {
                            System.out.println("Nota trebuie sa fie un numar!");
                            scanner.nextLine();
                        }
                    }
                    scanner.nextLine();
                    System.out.println("Comentarii:");
                    String comentarii = scanner.nextLine();
                    Regular regular = (Regular) user;
                    regular.add_rating(nota, comentarii);
                    actor.addRating(new Rating(username, nota, comentarii));
                    // trebuie sa adaug exp-ul user-ului
                    boolean rated_before = false;
                    if (actor.getDeletedRatings() == null) {
                        actor.setDeletedRatings(new ArrayList<>());
                    } else {
                        for (Rating rating : actor.getDeletedRatings()) {
                            if (Objects.equals(rating.getUsernameRater(), username)) {
                                rated_before = true;
                                break;
                            }
                        }
                    }
                    if (!rated_before) {
                        user.setExperienceStrategy(new ReviewStrategy());
                        user.updateExperience();
                    }
                }
                showActorDetails(actor);
                break;
            default:
                System.out.println("Opțiune invalidă!");
                showActorDetails(actor);
                break;
        }
    }

    private void showProductions() {
        List<Production> productions = imdb.getProductionList();
        Collections.sort(productions, (p1, p2) -> p1.getTitlu().compareToIgnoreCase(p2.getTitlu()));
        System.out.println("Lista de producții:");
        for (int i = 0; i < productions.size(); i++) {
            System.out.println((i + 1) + ". " + productions.get(i).getTitlu());
        }
        System.out.println("Alege o optiune:");
        System.out.println("1. Inapoi la meniul principal");
        System.out.println("2. Vizualizare detalii producție");
        System.out.println("3. Optiuni de filtrare");
        Scanner scanner = new Scanner(System.in);
        int option_productions = 0;
        boolean valid_productions = false;
        while (!valid_productions) {
            try {
                option_productions = scanner.nextInt();
                valid_productions = true;
            } catch (Exception e) {
                System.out.println("Opțiune invalidă!");
                scanner.nextLine();
            }
        }
        switch (option_productions) {
            case 1:
                // Inapoi la meniul principal
                showMainMenu();
                break;
            case 2:
                // Vizualizare detalii producție
                System.out.println("Alege o producție:");
                int productionIndex = 0;
                boolean valid_production = false;
                while (!valid_production) {
                    try {
                        productionIndex = scanner.nextInt();
                        valid_production = true;
                    } catch (Exception e) {
                        System.out.println("Opțiune invalidă!");
                        scanner.nextLine();
                    }
                }
                if (productionIndex < 1 || productionIndex > productions.size()) {
                    System.out.println("Opțiune invalidă!");
                    showProductions();
                }
                Production production = productions.get(productionIndex - 1);
                showProductionDetails(production);
                break;
            case 3:
                // vizualizare optiuni de filtrare
                filterProductions();
                break;
            default:
                System.out.println("Opțiune invalidă!");
                showProductions();
        }
    }

    private void filterProductions() {
        System.out.println("Alege un filtru:");
        System.out.println("1. Filtru după gen");
        System.out.println("2. Filtru dupa tip (movie/series)");
        System.out.println("3. Filtru după actor");
        System.out.println("4. Filtru după rating (minim)");
        System.out.println("5. Filtru după durata (maxima)");
        System.out.println("6. Filtru dupa anul de aparitie (minim)");
        System.out.println("7. Filtru dupa numarul de sezoane (maxim)");
        System.out.println("8. Filtru dupa numarul de episoade (minim)");
        System.out.println("9. Filtru dupa numarul de review-uri (minim)");
        System.out.println("10. Inapoi");
        Scanner scanner = new Scanner(System.in);
        int option_filter = 0;
        boolean valid_filter = false;
        while (!valid_filter) {
            try {
                option_filter = scanner.nextInt();
                valid_filter = true;
            } catch (Exception e) {
                System.out.println("Opțiune invalidă!");
                scanner.nextLine();
            }
        }
        List<Production> filteredProductions = imdb.getProductionList();
        switch (option_filter) {
            case 1:
                // pentru filtru dupa gen am nevoie sa
                // arat toate genurile si sa pun user-ul sa aleaga unul
                // pentru a nu avea probleme cu case sensitive sau etc
                System.out.println("Alege un gen:");
                int index = 1;
                for (Genre genre : Genre.values()) {
                    System.out.println(index + ". " + genre);
                    index++;
                }
                int option_genre = 0;
                boolean valid_genre = false;
                while (!valid_genre) {
                    try {
                        option_genre = scanner.nextInt();
                        valid_genre = true;
                    } catch (Exception e) {
                        System.out.println("Opțiune invalidă!");
                        scanner.nextLine();
                    }
                }
                if (option_genre < 1 || option_genre > Genre.values().length) {
                    System.out.println("Opțiune invalidă!");
                    filterProductions();
                    return;
                }
                Genre selected_genre = Genre.values()[option_genre - 1];
                filteredProductions = imdb.filterByGenre(selected_genre.toString(), filteredProductions);
                showFilteredProductions(filteredProductions);
                break;
            case 2:
                // vreau sa afisez toate tipurile de productii
                // si sa pun user-ul sa aleaga unul
                System.out.println("Alege un tip:");
                System.out.println("1. Movie");
                System.out.println("2. Series");
                int option_type = 0;
                boolean valid_type = false;
                while (!valid_type) {
                    try {
                        option_type = scanner.nextInt();
                        valid_type = true;
                    } catch (Exception e) {
                        System.out.println("Opțiune invalidă!");
                        scanner.nextLine();
                    }
                }
                if (option_type < 1 || option_type > 2) {
                    System.out.println("Opțiune invalidă!");
                    filterProductions();
                    return;
                }
                if (option_type == 1) {
                    filteredProductions = imdb.filterByType("Movie", filteredProductions);
                    showFilteredProductions(filteredProductions);
                } else {
                    filteredProductions = imdb.filterByType("Series", filteredProductions);
                    showFilteredProductions(filteredProductions);
                }
                break;
            case 3:
                // vreau sa afisez toti actorii
                // si sa pun user-ul sa aleaga unul
                List<Actor> actors = imdb.getActorList();
                Collections.sort(actors, (a1, a2) -> a1.getName().compareToIgnoreCase(a2.getName()));
                System.out.println("Alege un actor:");
                for (int i = 0; i < actors.size(); i++) {
                    System.out.println((i + 1) + ". " + actors.get(i).getName());
                }
                int option_actor = 0;
                boolean valid_actor = false;
                while (!valid_actor) {
                    try {
                        option_actor = scanner.nextInt();
                        valid_actor = true;
                    } catch (Exception e) {
                        System.out.println("Opțiune invalidă!");
                        scanner.nextLine();
                    }
                }
                if (option_actor < 1 || option_actor > actors.size()) {
                    System.out.println("Opțiune invalidă!");
                    filterProductions();
                    return;
                }
                Actor selected_actor = actors.get(option_actor - 1);
                filteredProductions = imdb.filterByActorP(selected_actor.getName(), filteredProductions);
                showFilteredProductions(filteredProductions);
                break;
            case 4:
                // vreau sa pun user-ul sa introduca un rating minim
                System.out.println("Rating minim:");
                int rating_min = 0;
                boolean valid_rating_min = false;
                while (!valid_rating_min) {
                    try {
                        rating_min = scanner.nextInt();
                        valid_rating_min = true;
                    } catch (Exception e) {
                        System.out.println("Opțiune invalidă!");
                        scanner.nextLine();
                    }
                }
                filteredProductions = imdb.filterByRating(rating_min, filteredProductions);
                showFilteredProductions(filteredProductions);
                break;
            case 5:
                // vreau sa pun user-ul sa introduca o durata maxima
                System.out.println("Durata maxima:");
                int durata_max = 0;
                boolean valid_durata_max = false;
                while (!valid_durata_max) {
                    try {
                        durata_max = scanner.nextInt();
                        valid_durata_max = true;
                    } catch (Exception e) {
                        System.out.println("Opțiune invalidă!");
                        scanner.nextLine();
                    }
                }
                filteredProductions = imdb.filterByDuration(durata_max, filteredProductions);
                showFilteredProductions(filteredProductions);
                break;
            case 6:
                // vreau sa pun user-ul sa introduca un an minim
                System.out.println("An minim:");
                int an_min = 0;
                boolean valid_an_min = false;
                while (!valid_an_min) {
                    try {
                        an_min = scanner.nextInt();
                        valid_an_min = true;
                    } catch (Exception e) {
                        System.out.println("Opțiune invalidă!");
                        scanner.nextLine();
                    }
                }
                filteredProductions = imdb.filterByReleaseYear(an_min, filteredProductions);
                showFilteredProductions(filteredProductions);
                break;
            case 7:
                // vreau sa pun user-ul sa introduca un numar maxim de sezoane
                System.out.println("Numar maxim de sezoane:");
                int sezoane_max = 0;
                boolean valid_sezoane_max = false;
                while (!valid_sezoane_max) {
                    try {
                        sezoane_max = scanner.nextInt();
                        valid_sezoane_max = true;
                    } catch (Exception e) {
                        System.out.println("Opțiune invalidă!");
                        scanner.nextLine();
                    }
                }
                filteredProductions = imdb.filterByNumberOfSeasons(sezoane_max, filteredProductions);
                showFilteredProductions(filteredProductions);
                break;
            case 8:
                // vreau sa pun user-ul sa introduca un numar minim de episoade
                System.out.println("Numar minim de episoade:");
                int episoade_min = 0;
                boolean valid_episoade_min = false;
                while (!valid_episoade_min) {
                    try {
                        episoade_min = scanner.nextInt();
                        valid_episoade_min = true;
                    } catch (Exception e) {
                        System.out.println("Opțiune invalidă!");
                        scanner.nextLine();
                    }
                }
                filteredProductions = imdb.filterByNumberOfEpisodes(episoade_min, filteredProductions);
                showFilteredProductions(filteredProductions);
                break;
            case 9:
                // vreau sa pun user-ul sa introduca un numar minim de review-uri
                System.out.println("Numar minim de review-uri:");
                int review_min = 0;
                boolean valid_review_min = false;
                while (!valid_review_min) {
                    try {
                        review_min = scanner.nextInt();
                        valid_review_min = true;
                    } catch (Exception e) {
                        System.out.println("Opțiune invalidă!");
                        scanner.nextLine();
                    }
                }
                filteredProductions = imdb.filterByMinimumReviews(review_min, filteredProductions);
                showFilteredProductions(filteredProductions);
                break;
            case 10:
                // Inapoi
                showProductions();
                break;
        }
    }

    private void showFilteredProductions(List<Production> filteredProductions){
        if (filteredProductions.isEmpty()){
            System.out.println("Nu exista productii care sa corespunda criteriilor de filtrare!");
            showProductions();
            return;
        } else {
            Collections.sort(filteredProductions, (p1, p2) -> p1.getTitlu().compareToIgnoreCase(p2.getTitlu()));
            System.out.println("Lista de producții:");
            for (int i = 0; i < filteredProductions.size(); i++) {
                System.out.println((i + 1) + ". " + filteredProductions.get(i).getTitlu());
            }
            System.out.println("Alege o optiune:");
            System.out.println("1. Inapoi la meniul principal");
            System.out.println("2. Inapoi la toate producțiile");
            System.out.println("3. Vizualizare detalii producție");
            Scanner scanner = new Scanner(System.in);
            int option_productions = 0;
            boolean valid_productions = false;
            while (!valid_productions) {
                try {
                    option_productions = scanner.nextInt();
                    valid_productions = true;
                } catch (Exception e) {
                    System.out.println("Opțiune invalidă!");
                    scanner.nextLine();
                }
            }
            switch (option_productions) {
                case 1:
                    // Inapoi la meniul principal
                    showMainMenu();
                    break;
                case 2:
                    // Inapoi la toate producțiile
                    showProductions();
                    break;
                case 3:
                    // Vizualizare detalii producție
                    System.out.println("Alege o producție:");
                    int productionIndex = 0;
                    boolean valid_production = false;
                    while (!valid_production) {
                        try {
                            productionIndex = scanner.nextInt();
                            valid_production = true;
                        } catch (Exception e) {
                            System.out.println("Opțiune invalidă!");
                            scanner.nextLine();
                        }
                    }
                    if (productionIndex < 1 || productionIndex > filteredProductions.size()) {
                        System.out.println("Opțiune invalidă!");
                        showFilteredProductions(filteredProductions);
                    }
                    Production production = filteredProductions.get(productionIndex - 1);
                    showProductionDetails(production);
                    break;
                default:
                    System.out.println("Opțiune invalidă!");
                    showFilteredProductions(filteredProductions);
            }
        }
    }

    private void showProductionDetails(Production production) {
        System.out.println("Detalii producție:");
        if (production instanceof Movie) {
            Movie movie = (Movie) production;
            movie.displayInfo();
        } else {
            Series series = (Series) production;
            series.displayInfo();
        }
        System.out.println("Alege o optiune:");
        System.out.println("1. Inapoi la lista de producții");
        // trebuie sa vad daca user-ul a adaugat sau nu recenzie la aceasta productie
        boolean rated = false;
        if (user.getUserType() == AccountType.REGULAR) {
            for (Rating rating : production.getRatingList()) {
                if (Objects.equals(rating.getUsernameRater(), username)) {
                    rated = true;
                    break;
                }
            }
            if (!rated) {
                System.out.println("2. Adaugă recenzie");
            } else {
                System.out.println("2. Sterge recenzie");
            }
        }
        if (user.getFavoriteProductions().contains(production)) {
            System.out.println("3. Elimină de la favorite");
        } else {
            System.out.println("3. Adaugă la favorite");
        }
        boolean canModify = false;
        if (user.getUserType() == AccountType.ADMIN || user.getUserType() == AccountType.CONTRIBUTOR) {
            // verific daca productia este adaugata de user
            if (user.getUserType() == AccountType.CONTRIBUTOR) {
                Contributor contributor = (Contributor) user;
                if (contributor.getProductionsContribution().contains(production)) {
                    System.out.println("4. Modifica producție");
                    canModify = true;
                }
            } else {
                Admin admin = (Admin) user;
                if (admin.getProductionsContribution().contains(production) ||
                        Admin.getProductionsContributionCommon().contains(production)) {
                    System.out.println("4. Modifica producție");
                    canModify = true;
                }
            }
        }
        Scanner scanner = new Scanner(System.in);
        int option_production_details = 0;
        // ma asigur ca se introduce un numar
        boolean valid = false;
        while (!valid) {
            try {
                option_production_details = scanner.nextInt();
                if (option_production_details < 1 || option_production_details > 4) {
                    System.out.println("Opțiune invalidă!");
                } else {
                    valid = true;
                }
            } catch (Exception e) {
                System.out.println("Opțiune invalidă!");
                scanner.nextLine();
            }
        }
        switch (option_production_details) {
            case 1:
                // Inapoi la lista de producții
                showProductions();
                break;
            case 2:
                if (user.getUserType() != AccountType.REGULAR) {
                    System.out.println("Opțiune invalidă!");
                    showProductionDetails(production);
                    return;
                }
                if (rated) {
                    // Sterge recenzie
                    Regular regular = (Regular) user;
                    regular.remove_rating();
                    production.removeRating(username);
                } else {
                    // Adaugă recenzie
                    System.out.println("Adaugă recenzie:");
                    System.out.println("Nota (1-10):");
                    int nota = 0;
                    // verific daca nota este valida
                    // sau daca nu a fost introdusa o litera
                    boolean valid_nota = false;
                    while (!valid_nota) {
                        try {
                            nota = scanner.nextInt();
                            if (nota < 1 || nota > 10) {
                                System.out.println("Nota trebuie sa fie intre 1 si 10!");
                            } else {
                                valid_nota = true;
                            }
                        } catch (Exception e) {
                            System.out.println("Nota trebuie sa fie un numar!");
                            scanner.nextLine();
                        }
                    }
                    scanner.nextLine();
                    System.out.println("Comentarii:");
                    String comentarii = scanner.nextLine();
                    Regular regular = (Regular) user;
                    regular.add_rating(nota, comentarii);
                    production.addRating(new Rating(username, nota, comentarii));
                    // trebuie sa updatez exp-ul userului
                    boolean rated_before = false;
                    for (Rating rating1 : production.getDeletedRatings()) {
                        if (Objects.equals(rating1.getUsernameRater(), username)) {
                            rated_before = true;
                            break;
                        }
                    }
                    if (!rated_before) {
                        user.setExperienceStrategy(new ReviewStrategy());
                        user.updateExperience();
                    }
                }
                showProductionDetails(production);
                break;
            case 3:
                if (user.getFavoriteProductions().contains(production)) {
                    // Elimină de la favorite
                    user.remove_favorite_production(production);
                    System.out.println("Producția a fost eliminată de la favorite!");
                } else {
                    // Adaugă la favorite
                    user.add_favorite_production(production);
                    System.out.println("Producția a fost adăugată la favorite!");
                }
                showProductionDetails(production);
                break;
            case 4:
                // Modifica producție
                if (canModify) {
                    // metoda de modificare
                    modifyProduction(production);
                } else {
                    System.out.println("Nu aveți dreptul să modificați această producție!");
                    showProductionDetails(production);
                }
                break;
            default:
                System.out.println("Opțiune invalidă!");
                showProductionDetails(production);
        }
    }

    private void modifyProduction(Production production) {
        // trebuie sa vad daca este film sau serial
        if (production instanceof Movie) {
            // modificare film
            modifyMovie((Movie) production);
        } else {
            // modificare serial
            modifySeries((Series) production);
        }
    }

    private void modifySeries(Production production) {
        Series series = (Series) production;
        System.out.println("Modificare serial:");
        // serialul are titlu, regizori, actori, gen, descriere, an lansare, numar sezoane si episoade
        // care sunt modificabile
        System.out.println("Alege o opțiune:");
        System.out.println("1. Titlu");
        System.out.println("2. Regizori");
        System.out.println("3. Actori");
        System.out.println("4. Gen");
        System.out.println("5. Descriere");
        System.out.println("6. An lansare");
        System.out.println("7. Numar sezoane");
        System.out.println("8. Detalii sezoane");
        System.out.println("9. Inapoi");
        Scanner scanner = new Scanner(System.in);
        // vreau sa ma asigur ca se introduce un numar
        int option_modify_series = 0;
        boolean valid = false;
        while (!valid) {
            try {
                option_modify_series = scanner.nextInt();
                if (option_modify_series < 1 || option_modify_series > 9) {
                    System.out.println("Opțiune invalidă!");
                } else {
                    valid = true;
                }
            } catch (Exception e) {
                System.out.println("Opțiune invalidă!");
                scanner.nextLine();
            }
        }
        switch (option_modify_series) {
            case 1:
                // Titlu
                System.out.println("Titlu nou:");
                scanner.nextLine();
                String titlu = scanner.nextLine();
                // verific daca titlul este unic
                for (Production production1 : imdb.getProductionList()) {
                    if (production1.getTitlu().equals(titlu)) {
                        System.out.println("Mai există o producție cu acest titlu!");
                        modifySeries(production);
                        return;
                    }
                }
                production.setTitlu(titlu);
                System.out.println("Titlu modificat!");
                modifySeries(production);
                break;
            case 2:
                // Regizori
                System.out.println("Regizori noi:");
                scanner.nextLine();
                String regizori = scanner.nextLine();
                // regizorii sunt separati prin virgula
                String[] regizoriNou = regizori.split(", ");
                // regizori trebuie sa fie o lista
                production.setRegizoriList(List.of(regizoriNou));
                System.out.println("Regizori modificati!");
                modifySeries(production);
                break;
            case 3:
                // Actori
                System.out.println("Actori noi:");
                scanner.nextLine();
                String actori = scanner.nextLine();
                // actorii sunt separati prin virgula
                String[] actoriNou = actori.split(", ");
                // actori trebuie sa fie o lista
                production.setActoriList(List.of(actoriNou));
                System.out.println("Actori modificati!");
                modifySeries(production);
                break;
            case 4:
                // Gen
                System.out.println("Gen nou:");
                scanner.nextLine();
                String gen = scanner.nextLine();
                // genurile sunt separate prin virgula
                String[] genuri = gen.split(", ");
                // genuri trebuie sa fie o lista
                production.setGenreList(List.of(genuri));
                System.out.println("Gen modificat!");
                modifySeries(production);
                break;
            case 5:
                // Descriere
                System.out.println("Descriere nouă:");
                scanner.nextLine();
                String descriere = scanner.nextLine();
                production.setDescriereFilm(descriere);
                System.out.println("Descriere modificată!");
                modifySeries(production);
                break;
            case 6:
                // An lansare
                System.out.println("An lansare nou:");
                // verific daca anul este valid
                int an = 0;
                valid = false;
                while (!valid) {
                    try {
                        an = scanner.nextInt();
                        valid = true;
                    } catch (Exception e) {
                        System.out.println("Anul trebuie sa fie un numar!");
                        scanner.nextLine();
                    }
                }
                series.setReleaseAn(an);
                System.out.println("An lansare modificat!");
                modifySeries(production);
                break;
            case 7:
                // Numar sezoane
                System.out.println("Numar sezoane nou:");
                // verific daca numarul de sezoane este valid
                int numarSezoane = 0;
                valid = false;
                while (!valid) {
                    try {
                        numarSezoane = scanner.nextInt();
                        // verific sa nu fie mai mic decat numarul de sezoane deja existent
                        if (numarSezoane < series.getNumarSezoane()) {
                            System.out.println("Numarul de sezoane trebuie sa fie mai mare decat " +
                                    series.getNumarSezoane() + "!");
                        } else {
                            valid = true;
                        }
                    } catch (Exception e) {
                        System.out.println("Numarul de sezoane trebuie sa fie un numar!");
                        scanner.nextLine();
                    }
                }
                series.setNumarSezoane(numarSezoane);
                System.out.println("Numar sezoane modificat!");
                modifySeries(production);
                break;
            case 8:
                // Detalii sezoane
                // trebuie sa aleaga un sezon
                System.out.println("Alege un sezon:");
                for (int i = 0; i < series.getNumarSezoane(); i++) {
                    System.out.println((i + 1) + ". Sezonul " + (i + 1));
                }
                int sezon = 0;
                valid = false;
                while (!valid) {
                    try {
                        sezon = scanner.nextInt();
                        if (sezon < 1 || sezon > series.getNumarSezoane()) {
                            System.out.println("Opțiune invalidă!");
                        } else {
                            valid = true;
                        }
                    } catch (Exception e) {
                        System.out.println("Opțiune invalidă!");
                        scanner.nextLine();
                    }
                }
                // pentru sezonul ales arat episoadele din acel sezon
                System.out.println("Episoadele din sezonul " + sezon + ":");
                // trebuie sa treg din int in String
                String seasonKey = "Season " + sezon; // Formează cheia pentru sezonul ales
                List<Episode> episodes = series.getEpisodes(seasonKey); // Obține lista de episoade pentru sezonul ales
                if (episodes != null) {
                    for (int i = 0; i < episodes.size(); i++) {
                        Episode episode = episodes.get(i);
                        System.out.println((i + 1) + ". " + episode.getNumeEpisod() + " - " + episode.getDurataEpisod());
                    }
                    System.out.println("Alege o opțiune:");
                    System.out.println("1. Inapoi");
                    System.out.println("2. Adaugă episoade");
                    System.out.println("3. Modifică episoade");
                    System.out.println("4. Șterge episoade");
                    int option_detail_seasons = 0;
                    boolean valid_detail = false;
                    while (!valid_detail) {
                        try {
                            option_detail_seasons = scanner.nextInt();
                            if (option_detail_seasons < 1 || option_detail_seasons > 4) {
                                System.out.println("Opțiune invalidă!");
                            } else {
                                valid_detail = true;
                            }
                        } catch (Exception e) {
                            System.out.println("Opțiune invalidă!");
                            scanner.nextLine();
                        }
                    }
                    switch (option_detail_seasons) {
                        case 1:
                            // Inapoi
                            modifySeries(production);
                            break;
                        case 2:
                            // Adaugă episoade
                            addEpisode(seasonKey, episodes, series);
                            modifySeries(production);
                            break;
                        case 3:
                            // Modifică episoade
                            modifyEpisode(seasonKey, episodes, series);
                            modifySeries(production);
                            break;
                        case 4:
                            // Șterge episoade
                            deleteEpisode(seasonKey, episodes, series);
                            modifySeries(production);
                            break;
                        default:
                            System.out.println("Opțiune invalidă!");
                            modifySeries(production);
                            break;
                    }
                } else {
                    System.out.println("Nu există episoade înregistrate pentru acest sezon.");
                    System.out.println("Alege o opțiune:");
                    System.out.println("1. Inapoi");
                    System.out.println("2. Adaugă episoade");
                    int option_detail_seasons = 0;
                    boolean valid_detail_season = false;
                    while (!valid_detail_season) {
                        try {
                            option_detail_seasons = scanner.nextInt();
                            if (option_detail_seasons < 1 || option_detail_seasons > 2) {
                                System.out.println("Opțiune invalidă!");
                            } else {
                                valid_detail_season = true;
                            }
                        } catch (Exception e) {
                            System.out.println("Opțiune invalidă!");
                            scanner.nextLine();
                        }
                    }
                    switch (option_detail_seasons) {
                        case 1:
                            // Inapoi
                            modifySeries(production);
                            break;
                        case 2:
                            // Adaugă episoade
                            addEpisode(seasonKey, new ArrayList<>(), series);
                            modifySeries(production);
                            break;
                        default:
                            System.out.println("Opțiune invalidă!");
                            modifySeries(production);
                            break;
                    }
                }
                break;
            case 9:
                // Inapoi
                showProductionDetails(production);
                break;
            default:
                System.out.println("Opțiune invalidă!");
                modifySeries(production);
                break;
        }
    }

    private void deleteEpisode(String sezonKey, List<Episode> episodes, Series production) {
        // trebuie sa sterg episoade
        System.out.println("Șterge episod:");
        System.out.println("Alege un episod:");
        for (int i = 0; i < episodes.size(); i++) {
            Episode episode = episodes.get(i);
            System.out.println((i + 1) + ". " + episode.getNumeEpisod() + " - " + episode.getDurataEpisod());
        }
        Scanner scanner = new Scanner(System.in);
        int option = 0;
        boolean valid_episode = false;
        while (!valid_episode) {
            try {
                option = scanner.nextInt();
                if (option < 1 || option > episodes.size()) {
                    System.out.println("Opțiune invalidă!");
                } else {
                    valid_episode = true;
                }
            } catch (Exception e) {
                System.out.println("Opțiune invalidă!");
                scanner.nextLine();
            }
        }
        if (option < 1 || option > episodes.size()) {
            System.out.println("Opțiune invalidă!");
            deleteEpisode(sezonKey, episodes, production);
            return;
        }
        Episode episode = episodes.get(option - 1);
        episodes.remove(episode);
        production.getMapSerial().put(sezonKey, episodes);
        System.out.println("Episod șters!");
    }

    private void modifyEpisode(String sezonKey, List<Episode> episodes, Series production) {
        // vreau sa modific un episod din sezonul ales
        System.out.println("Alege un episod:");
        for (int i = 0; i < episodes.size(); i++) {
            Episode episode = episodes.get(i);
            System.out.println((i + 1) + ". " + episode.getNumeEpisod() + " - " + episode.getDurataEpisod());
        }
        Scanner scanner = new Scanner(System.in);
        int option = 0;
        boolean valid_episode_choice = false;
        while (!valid_episode_choice) {
            try {
                option = scanner.nextInt();
                valid_episode_choice = true;
            } catch (Exception e) {
                System.out.println("Opțiune invalidă!");
                scanner.nextLine();
            }
        }
        if (option < 1 || option > episodes.size()) {
            System.out.println("Opțiune invalidă!");
            modifyEpisode(sezonKey, episodes, production);
            return;
        }
        Episode episode = episodes.get(option - 1);
        System.out.println("Modificare episod:");
        System.out.println("Alege o opțiune:");
        System.out.println("1. Nume episod");
        System.out.println("2. Durata episod");
        int option2 = 0;
        boolean valid_episode = false;
        while (!valid_episode) {
            try {
                option2 = scanner.nextInt();
                if (option2 < 1 || option2 > 2) {
                    System.out.println("Opțiune invalidă!");
                } else {
                    valid_episode = true;
                }
            } catch (Exception e) {
                System.out.println("Opțiune invalidă!");
                scanner.nextLine();
            }
        }
        switch (option2) {
            case 1:
                // Nume episod
                System.out.println("Nume episod nou:");
                scanner.nextLine();
                String numeEpisod = scanner.nextLine();
                episode.setNumeEpisod(numeEpisod);
                System.out.println("Nume episod modificat!");
                break;
            case 2:
                // Durata episod
                System.out.println("Durata episod nou:");
                scanner.nextLine();
                String durataEpisod = scanner.nextLine();
                episode.setDurataEpisod(durataEpisod);
                System.out.println("Durata episod modificată!");
                break;
            default:
                System.out.println("Opțiune invalidă!");
                modifyEpisode(sezonKey, episodes, production);
                break;
        }
    }

    private void addEpisode(String sezonKey, List<Episode> episodes, Series production) {
        // trebuie sa adaug episoade
        System.out.println("Adaugă episod:");
        System.out.println("Nume episod:");
        Scanner scanner = new Scanner(System.in);
        String numeEpisod = scanner.nextLine();
        System.out.println("Durata episod:");
        String durataEpisod = scanner.nextLine();
        Episode episode = new Episode(numeEpisod, durataEpisod);
        episodes.add(episode);
        production.getMapSerial().put(sezonKey, episodes);
        System.out.println("Episod adăugat!");
    }

    private void modifyMovie(Production production) {
        Movie movie = (Movie) production;
        System.out.println("Modificare film:");
        System.out.println("Alege o opțiune:");
        // filmul are titlu, regizori, actori, gen, descriere, durata, an lansare
        // care sunt modificabile
        System.out.println("1. Titlu");
        System.out.println("2. Regizori");
        System.out.println("3. Actori");
        System.out.println("4. Gen");
        System.out.println("5. Descriere");
        System.out.println("6. Durata");
        System.out.println("7. An lansare");
        System.out.println("8. Inapoi");
        Scanner scanner = new Scanner(System.in);
        int option = 0;
        // vreau sa ma asigur ca se introduce un numar
        boolean valid_movie = false;
        while (!valid_movie) {
            try {
                option = scanner.nextInt();
                if (option < 1 || option > 8) {
                    System.out.println("Opțiune invalidă!");
                } else {
                    valid_movie = true;
                }
            } catch (Exception e) {
                System.out.println("Opțiune invalidă!");
                scanner.nextLine();
            }
        }
        switch (option) {
            case 1:
                // Titlu
                System.out.println("Titlu nou:");
                scanner.nextLine();
                String titlu = scanner.nextLine();
                // verific daca titlul este unic
                for (Production production1 : imdb.getProductionList()) {
                    if (production1.getTitlu().equals(titlu)) {
                        System.out.println("Mai există o producție cu acest titlu!");
                        modifyMovie(production);
                        return;
                    }
                }
                production.setTitlu(titlu);
                System.out.println("Titlu modificat!");
                modifyMovie(production);
                break;
            case 2:
                // Regizori
                System.out.println("Regizori noi:");
                scanner.nextLine();
                String regizori = scanner.nextLine();
                // regizorii sunt separati prin virgula
                String[] regizoriNou = regizori.split(", ");
                // regizori trebuie sa fie o lista
                production.setRegizoriList(List.of(regizoriNou));
                System.out.println("Regizori modificati!");
                modifyMovie(production);
                break;
            case 3:
                // Actori
                System.out.println("Actori noi:");
                scanner.nextLine();
                String actori = scanner.nextLine();
                // actorii sunt separati prin virgula
                String[] actoriNou = actori.split(", ");
                // actori trebuie sa fie o lista
                production.setActoriList(List.of(actoriNou));
                System.out.println("Actori modificati!");
                modifyMovie(production);
                break;
            case 4:
                // Gen
                System.out.println("Gen nou:");
                scanner.nextLine();
                String gen = scanner.nextLine();
                // genurile sunt separate prin virgula
                String[] genuri = gen.split(", ");
                // genuri trebuie sa fie o lista
                production.setGenreList(List.of(genuri));
                System.out.println("Gen modificat!");
                modifyMovie(production);
                break;
            case 5:
                // Descriere
                System.out.println("Descriere nouă:");
                scanner.nextLine();
                String descriere = scanner.nextLine();
                production.setDescriereFilm(descriere);
                System.out.println("Descriere modificată!");
                modifyMovie(production);
                break;
            case 6:
                // Durata
                System.out.println("Durata nouă:");
                scanner.nextLine();
                String durata = scanner.nextLine();
                movie.setRunTimp(durata);
                System.out.println("Durata modificată!");
                modifyMovie(production);
                break;
            case 7:
                // An lansare
                System.out.println("An lansare nou:");
                int an = 0;
                // verific daca anul este valid
                boolean valid_an = false;
                while (!valid_an) {
                    try {
                        an = scanner.nextInt();
                        valid_an = true;
                    } catch (Exception e) {
                        System.out.println("Anul trebuie sa fie un numar!");
                        scanner.nextLine();
                    }
                }
                movie.setReleaseAn(an);
                System.out.println("An lansare modificat!");
                modifyMovie(production);
                break;
            case 8:
                // Inapoi
                showProductionDetails((Production) production);
                break;
            default:
                System.out.println("Opțiune invalidă!");
                modifyMovie(production);
                break;
        }
    }
}
