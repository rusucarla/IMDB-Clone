import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.*;

public class MainPage extends JFrame {
    private IMDB imdb = IMDB.getInstance(); // O instanță a clasei IMDB pentru a accesa datele

    private String userAccountType;
    private String username;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private DefaultListModel<Production> productionListModel;
    private DefaultListModel<Actor> actorListModel;

    public MainPage(String username) {
        this.username = username;
        this.userAccountType = imdb.checkUserType(username);
        System.out.println(userAccountType);
        System.out.println(username);
        initUI();
    }

    private void initUI() {
        setTitle("Pagina Principală - IMDB");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // sidebar
        JPanel sidebarPanel = createSidebarPanel();
        add(sidebarPanel, BorderLayout.WEST);

        // home page
        JPanel homePagePanel = createHomePagePanel(username, userAccountType);
        cardPanel.add(homePagePanel, "home");

        // actors page
        JPanel actorsPagePanel = createActorsPagePanel(username, userAccountType);
        cardPanel.add(actorsPagePanel, "actors");

        // favorite actors page si favorite productions page
        // doar pentru utilizatorii regular sau contributor
        User user = imdb.getUser(username);

        JPanel favoriteActorsPagePanel = createFavoriteActorsPagePanel(username, userAccountType);
        cardPanel.add(favoriteActorsPagePanel, "favoriteActors");
        JPanel favoriteProductionsPagePanel = createFavoriteProductionsPagePanel(username, userAccountType);
        cardPanel.add(favoriteProductionsPagePanel, "favoriteProductions");

        // doar pentru utilizatorii contributor sau admin
        if (user.getUserType() == AccountType.CONTRIBUTOR || user.getUserType() == AccountType.ADMIN) {
            // contributions page
            JPanel actorcontributionsPagePanel = actorcreateContributionsPagePanel(username, userAccountType);
            cardPanel.add(actorcontributionsPagePanel, "contributionsActor");
            JPanel productioncontributionsPagePanel = productioncreateContributionsPagePanel(username, userAccountType);
            cardPanel.add(productioncontributionsPagePanel, "contributionsProductions");
            // adaugare actor
            JPanel addActorPagePanel = createAddActorPagePanel(username, userAccountType);
            cardPanel.add(addActorPagePanel, "addActor");
            // adaugare producție
            JPanel addProductionPagePanel = createAddProductionPagePanel(username, userAccountType);
            cardPanel.add(addProductionPagePanel, "addProduction");
        }
        // vreau pentru toti userii sa pot vedea informatii despre propriul cont
        JPanel accountPagePanel = createAccountPagePanel(username, userAccountType);
        cardPanel.add(accountPagePanel, "account");

        add(cardPanel, BorderLayout.CENTER);
    }

    private JPanel createAccountPagePanel(String username, String userAccountType) {
        User user = imdb.getUser(username);
        JPanel accountPagePanel = new JPanel(new BorderLayout());
        JPanel accountPanel = new JPanel(new GridLayout(0, 1));
        accountPanel.add(new JLabel("Username: " + user.getUserName()));
        accountPanel.add(new JLabel("Account Type: " + user.getUserType()));
        accountPanel.add(new JLabel("Experience: " + user.getExperience()));
        accountPanel.add(new JLabel("Notifications: "));
        JTextArea notificationsArea = new JTextArea(5, 20); // 5 rows, 20 columns
        notificationsArea.setWrapStyleWord(true);
        notificationsArea.setLineWrap(true);
        notificationsArea.setCaretPosition(0);
        notificationsArea.setEditable(false);
        for (String notification : user.getNotifications()) {
            notificationsArea.append(notification + "\n");
        }
        JScrollPane notificationsScrollPane = new JScrollPane(notificationsArea);
        accountPanel.add(notificationsScrollPane);
        accountPagePanel.add(accountPanel, BorderLayout.CENTER);
        // ame nevoie de un buton de refresh
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> {
            // resetez panelul
            accountPanel.removeAll();
            accountPanel.revalidate();
            accountPanel.repaint();
            accountPanel.add(new JLabel("Username: " + user.getUserName()));
            accountPanel.add(new JLabel("Account Type: " + user.getUserType()));
            accountPanel.add(new JLabel("Experience: " + user.getExperience()));
            accountPanel.add(new JLabel("Notifications: "));
            JTextArea notificationsArea1 = new JTextArea(5, 20); // 5 rows, 20 columns
            notificationsArea1.setWrapStyleWord(true);
            notificationsArea1.setLineWrap(true);
            notificationsArea1.setCaretPosition(0);
            notificationsArea1.setEditable(false);
            for (String notification : user.getNotifications()) {
                notificationsArea1.append(notification + "\n");
            }
            JScrollPane notificationsScrollPane1 = new JScrollPane(notificationsArea1);
            accountPanel.add(notificationsScrollPane1);
            accountPagePanel.add(accountPanel, BorderLayout.CENTER);
        });
        accountPagePanel.add(refreshButton, BorderLayout.SOUTH);
        return accountPagePanel;
    }

    private JPanel createAddProductionPagePanel(String username, String userAccountType) {
        User user = imdb.getUser(username);
        // vreau sa am 2 butoane pentru a sti ce tip de producție vreau sa adaug
        JPanel addProductionPagePanel = new JPanel(new BorderLayout());
        JPanel addProductionPanel = new JPanel(new GridLayout(0, 1));
        JButton movieButton = new JButton("Adauga Movie");
        JButton seriesButton = new JButton("Adauga Series");
        addProductionPanel.add(movieButton);
        addProductionPanel.add(seriesButton);
        addProductionPagePanel.add(addProductionPanel, BorderLayout.CENTER);
        // adaug un listener pentru butonul de movie
        seriesButton.addActionListener(e -> {
            // resetez panelul
            addProductionPanel.removeAll();
            addProductionPanel.revalidate();
            addProductionPanel.repaint();
            JTextField titleField = new JTextField();
            addProductionPanel.add(new JLabel("Titlu: "));
            addProductionPanel.add(titleField);
            JTextField yearField = new JTextField();
            addProductionPanel.add(new JLabel("An Lansare: "));
            addProductionPanel.add(yearField);
            JTextField sesonsField = new JTextField();
            addProductionPanel.add(new JLabel("Numar Sezoane: "));
            addProductionPanel.add(sesonsField);
            JTextArea regizorArea = new JTextArea(5, 20); // 5 rows, 20 columns
            regizorArea.setWrapStyleWord(true);
            regizorArea.setLineWrap(true);
            regizorArea.setCaretPosition(0);
            regizorArea.setEditable(true);
            addProductionPanel.add(new JLabel("Regizor: "));
            JScrollPane regizorScrollPane = new JScrollPane(regizorArea);
            addProductionPanel.add(regizorScrollPane);
            JTextArea actorsArea = new JTextArea(5, 20); // 5 rows, 20 columns
            actorsArea.setWrapStyleWord(true);
            actorsArea.setLineWrap(true);
            actorsArea.setCaretPosition(0);
            actorsArea.setEditable(true);
            addProductionPanel.add(new JLabel("Actori: "));
            JScrollPane actorsScrollPane = new JScrollPane(actorsArea);
            addProductionPanel.add(actorsScrollPane);
            JTextArea genresArea = new JTextArea(5, 20); // 5 rows, 20 columns
            genresArea.setWrapStyleWord(true);
            genresArea.setLineWrap(true);
            genresArea.setCaretPosition(0);
            genresArea.setEditable(true);
            addProductionPanel.add(new JLabel("Genuri: "));
            JScrollPane genresScrollPane = new JScrollPane(genresArea);
            addProductionPanel.add(genresScrollPane);
            JTextArea descriptionArea = new JTextArea(5, 20); // 5 rows, 20 columns
            descriptionArea.setWrapStyleWord(true);
            descriptionArea.setLineWrap(true);
            descriptionArea.setCaretPosition(0);
            descriptionArea.setEditable(true);
            addProductionPanel.add(new JLabel("Descriere: "));
            JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);
            addProductionPanel.add(descriptionScrollPane);
            // vreau un buton penrtu a continua cu adaugarea producției
            JButton continueButton = new JButton("Continua");
            addProductionPanel.add(continueButton);
            // adaug un listener pentru butonul de continue
            continueButton.addActionListener(e1 -> {
                // vreau mai intai sa verific validitatea datelor de pana acum
                if (titleField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Titlul nu poate fi gol!");
                    return;
                }
                try {
                    int year = Integer.parseInt(yearField.getText());
                    // verific daca anul este mai mare ca 0
                    if (year <= 0) {
                        JOptionPane.showMessageDialog(this, "Anul trebuie sa fie mai mare ca 0!");
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Anul trebuie sa fie un numar!");
                    return;
                }
                // verific daca regizorul exista
                if (regizorArea.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Productia trebuie sa aiba macar un regizor!");
                    return;
                }
                // verific daca actorii exista
                if (actorsArea.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Productia trebuie sa aiba macar un actor!");
                    return;
                }
                // verific daca genurile exista
                if (genresArea.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Productia trebuie sa aiba macar un gen!");
                    return;
                }
                // verific daca descrierea exista
                if (descriptionArea.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Productia trebuie sa aiba o descriere!");
                    return;
                }
                // vreau sa stiu cate sezoane sunt ca sa stiu cate episoade sa adaug
                try {
                    int seasons = Integer.parseInt(sesonsField.getText());
                    // verific daca numarul de sezoane este mai mare ca 0
                    if (seasons <= 0) {
                        JOptionPane.showMessageDialog(this, "Numarul de sezoane trebuie sa fie mai mare ca 0!");
                        return;
                    }
                    List<JTextArea> episodesAreaList = new ArrayList<>();
                    for (int i = 1; i <= seasons; i++) {
                        JTextArea episodesArea = new JTextArea(5, 20); // 5 rows, 20 columns
                        episodesArea.setWrapStyleWord(true);
                        episodesArea.setLineWrap(true);
                        episodesArea.setCaretPosition(0);
                        episodesArea.setEditable(true);
                        addProductionPanel.add(new JLabel("Episoade sezon " + i + ": "));
                        episodesAreaList.add(episodesArea);
                        JScrollPane episodesScrollPane = new JScrollPane(episodesArea);
                        addProductionPanel.add(episodesScrollPane);
                    }
                    // vreau un buton pentru a continua cu adaugarea producției
                    JButton saveButton = new JButton("Salveaza");
                    addProductionPanel.add(saveButton);
                    // adaug un listener pentru butonul de save
                    saveButton.addActionListener(e2 -> {
                        // vreau sa creez producția
                        String title = titleField.getText();
                        // verific daca productia nu exista deja
                        for (Production production : imdb.getProductionList()) {
                            if (production.getTitlu().equals(title)) {
                                JOptionPane.showMessageDialog(this, "Productia exista deja!");
                                return;
                            }
                        }
                        int year = Integer.parseInt(yearField.getText());
                        String[] regizori = regizorArea.getText().split("\n");
                        String[] actori = actorsArea.getText().split("\n");
                        String[] genres = genresArea.getText().split("\n");
                        String descriere = descriptionArea.getText();
                        int numarSezoane = Integer.parseInt(sesonsField.getText());
                        Map<String, List<Episode>> mapSerial = new HashMap<>();
                        for (int i = 1 ; i <= numarSezoane; i++){
                            String[] episodes = episodesAreaList.get(i - 1).getText().split("\n");
                            List<Episode> episodeList = new ArrayList<>();
                            for (String episode : episodes) {
                                String[] episodeDetails = episode.trim().split(" - ");
                                if (episodeDetails.length != 2) {
                                    // Inform the user about the improperly formatted episode
                                    JOptionPane.showMessageDialog(this, "The episode '" + episode + "' is not formatted correctly. Please use 'Title - Type'.");
                                    return; // Exit the method to prevent further processing
                                }
                                String titleEpisode = episodeDetails[0].trim();
                                String typeEpisode = episodeDetails[1].trim();
                                if (titleEpisode.isEmpty() || typeEpisode.isEmpty()) {
                                    JOptionPane.showMessageDialog(this, "Title and type cannot be empty.");
                                    return; // Exit the method to prevent further processing
                                }
                                // verific tipul episodului - trebuie sa fie de tipul "number minutes"
                                String[] typeEpisodeDetails = typeEpisode.split(" ");
                                try {
                                    int number = Integer.parseInt(typeEpisodeDetails[0]);
                                    if (number <= 0) {
                                        JOptionPane.showMessageDialog(this, "The episode '" + episode + "' is not formatted correctly. Please use 'number minutes'.");
                                        return; // Exit the method to prevent further processing
                                    }
                                } catch (NumberFormatException ex) {
                                    JOptionPane.showMessageDialog(this, "The episode'" + episode + "' has to have a valid number of minutes.'");
                                    return; // Exit the method to prevent further processing
                                }
                                if (typeEpisodeDetails.length != 2 || !typeEpisodeDetails[1].equals("minutes")) {
                                    JOptionPane.showMessageDialog(this, "The episode '" + episode + "' is not formatted correctly. Please use 'number minutes'.");
                                    return; // Exit the method to prevent further processing
                                }
                                episodeList.add(new Episode(titleEpisode, typeEpisode));
                            }
                            mapSerial.put("Sezonul " + i, episodeList);
                        }
                        // Contruiesc producția
                        Series series = new Series(title, Arrays.asList(regizori), Arrays.asList(actori),
                                Arrays.asList(genres), new ArrayList<>(), descriere, year, numarSezoane);
                        series.setMapSerial(mapSerial);
                        // adaug producția
                        Staff staff = (Staff) user;
                        staff.addProductionSystem(series);
                        // adaug exp-ul userului
                        user.setExperienceStrategy(new ContributionStrategy());
                        user.updateExperience();
                        // dau mesaj ca s-a adaugat cu succes
                        JOptionPane.showMessageDialog(this, "Productia a fost adaugata cu succes! Ai primit experienta!");
                    });
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Numarul de sezoane trebuie sa fie un numar!");
                    return;
                }

            });
            JScrollPane scrollPane = new JScrollPane(addProductionPanel);
            addProductionPagePanel.add(scrollPane, BorderLayout.CENTER);
        });
        // adaug un listener pentru butonul de movie
        movieButton.addActionListener(e -> {
            // resetez panelul
            addProductionPanel.removeAll();
            addProductionPanel.revalidate();
            addProductionPanel.repaint();
            JTextField titleField = new JTextField();
            addProductionPanel.add(new JLabel("Titlu: "));
            addProductionPanel.add(titleField);
            JTextField yearField = new JTextField();
            addProductionPanel.add(new JLabel("An Lansare: "));
            addProductionPanel.add(yearField);
            JTextField durationField = new JTextField();
            addProductionPanel.add(new JLabel("Durata: "));
            addProductionPanel.add(durationField);
            JTextArea regizorArea = new JTextArea(5, 20); // 5 rows, 20 columns
            regizorArea.setWrapStyleWord(true);
            regizorArea.setLineWrap(true);
            regizorArea.setCaretPosition(0);
            regizorArea.setEditable(true);
            addProductionPanel.add(new JLabel("Regizor: "));
            JScrollPane regizorScrollPane = new JScrollPane(regizorArea);
            addProductionPanel.add(regizorScrollPane);
            JTextArea actorsArea = new JTextArea(5, 20); // 5 rows, 20 columns
            actorsArea.setWrapStyleWord(true);
            actorsArea.setLineWrap(true);
            actorsArea.setCaretPosition(0);
            actorsArea.setEditable(true);
            addProductionPanel.add(new JLabel("Actori: "));
            JScrollPane actorsScrollPane = new JScrollPane(actorsArea);
            addProductionPanel.add(actorsScrollPane);
            JTextArea genresArea = new JTextArea(5, 20); // 5 rows, 20 columns
            genresArea.setWrapStyleWord(true);
            genresArea.setLineWrap(true);
            genresArea.setCaretPosition(0);
            genresArea.setEditable(true);
            addProductionPanel.add(new JLabel("Genuri: "));
            JScrollPane genresScrollPane = new JScrollPane(genresArea);
            addProductionPanel.add(genresScrollPane);
            JTextArea descriptionArea = new JTextArea(5, 20); // 5 rows, 20 columns
            descriptionArea.setWrapStyleWord(true);
            descriptionArea.setLineWrap(true);
            descriptionArea.setCaretPosition(0);
            descriptionArea.setEditable(true);
            addProductionPanel.add(new JLabel("Descriere: "));
            JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);
            addProductionPanel.add(descriptionScrollPane);
            // vreau un buton pentru a salva producția
            JButton saveButton = new JButton("Salveaza");
            addProductionPanel.add(saveButton);
            // adaug un listener pentru butonul de save
            saveButton.addActionListener(e1 -> {
                // vreau mai intai sa verific validitatea datelor de pana acum
                if (titleField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Titlul nu poate fi gol!");
                    return;
                }
                try {
                    int year = Integer.parseInt(yearField.getText());
                    // verific daca anul este mai mare ca 0
                    if (year <= 0) {
                        JOptionPane.showMessageDialog(this, "Anul trebuie sa fie mai mare ca 0!");
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Anul trebuie sa fie un numar!");
                    return;
                }
                // verific daca regizorul exista
                if (regizorArea.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Productia trebuie sa aiba macar un regizor!");
                    return;
                }
                // verific daca actorii exista
                if (actorsArea.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Productia trebuie sa aiba macar un actor!");
                    return;
                }
                // verific daca genurile exista
                if (genresArea.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Productia trebuie sa aiba macar un gen!");
                    return;
                }
                // verific daca descrierea exista
                if (descriptionArea.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Productia trebuie sa aiba o descriere!");
                    return;
                }
                // verific daca durata exista
                if (durationField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Productia trebuie sa aiba o durata!");
                    return;
                }
                // verific daca durata este un numar
                try {
                    int duration = Integer.parseInt(durationField.getText());
                    // verific daca durata este mai mare ca 0
                    if (duration <= 0) {
                        JOptionPane.showMessageDialog(this, "Durata trebuie sa fie mai mare ca 0!");
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Durata trebuie sa fie un numar!");
                    return;
                }
                // vreau sa creez producția
                String title = titleField.getText();
                // verific daca productia nu exista deja
                for (Production production : imdb.getProductionList()) {
                    if (production.getTitlu().equals(title)) {
                        JOptionPane.showMessageDialog(this, "Productia exista deja!");
                        return;
                    }
                }
                int year = Integer.parseInt(yearField.getText());
                String[] regizori = regizorArea.getText().split("\n");
                String[] actori = actorsArea.getText().split("\n");
                String[] genres = genresArea.getText().split("\n");
                String descriere = descriptionArea.getText();
                // Contruiesc producția
                Movie movie = new Movie(title, Arrays.asList(regizori), Arrays.asList(actori),
                        Arrays.asList(genres), new ArrayList<>(), descriere, durationField.getText(), year);
                // adaug producția
                Staff staff = (Staff) user;
                staff.addProductionSystem(movie);
                // adaug exp-ul userului
                user.setExperienceStrategy(new ContributionStrategy());
                user.updateExperience();
                // dau mesaj ca s-a adaugat cu succes
                JOptionPane.showMessageDialog(this, "Productia a fost adaugata cu succes! Ai primit experienta!");
            });
        });
        return addProductionPagePanel;
    }

    private JPanel createAddActorPagePanel(String username, String userAccountType) {
        User user = imdb.getUser(username);
        actorListModel = new DefaultListModel<>();
        JList<Actor> actorList = new JList<>(actorListModel);
        actorList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                Actor actor = (Actor) value;
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setText(actor.getName());
                return label;
            }
        });
        JPanel addActorPagePanel = new JPanel(new BorderLayout());
        JPanel addActorPanel = new JPanel(new GridLayout(0, 1));
        JTextField nameField = new JTextField();
        addActorPanel.add(new JLabel("Name: "));
        addActorPanel.add(nameField);
        JTextArea bioArea = new JTextArea(5, 20); // 5 rows, 20 columns
        bioArea.setWrapStyleWord(true);
        bioArea.setLineWrap(true);
        bioArea.setCaretPosition(0);
        addActorPanel.add(new JLabel("Biography: "));
        JScrollPane bioScrollPane = new JScrollPane(bioArea);
        addActorPanel.add(bioScrollPane);
        JTextArea performancesArea = new JTextArea(5, 20); // 5 rows, 20 columns
        performancesArea.setWrapStyleWord(true);
        performancesArea.setLineWrap(true);
        performancesArea.setCaretPosition(0);
        performancesArea.setEditable(true);
        addActorPanel.add(new JLabel("Performances: "));
        JScrollPane performancesScrollPane = new JScrollPane(performancesArea);
        addActorPanel.add(performancesScrollPane);
        JButton addButton = new JButton("Adauga Actor");
        addButton.addActionListener(e -> {
            // adaug actorul
            String name = nameField.getText();
            String biography = bioArea.getText();
            List<Actor.Performance> performances = new ArrayList<>();
            String performancesText = performancesArea.getText();
            String[] performancesDetails = performancesText.split("\n");
            for (String performance : performancesDetails) {
                String[] performanceDetails = performance.trim().split(" - ");
                if (performanceDetails.length != 2) {
                    // Inform the user about the improperly formatted performance
                    JOptionPane.showMessageDialog(this, "The performance '" + performance + "' is not formatted correctly. Please use 'Title - Type'.");
                    return; // Exit the method to prevent further processing
                }
                String title = performanceDetails[0].trim();
                String type = performanceDetails[1].trim();
                if (title.isEmpty() || type.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Title and type cannot be empty.");
                    return; // Exit the method to prevent further processing
                }
                performances.add(new Actor.Performance(title, type));
            }
            // verific daca numele exista deja
            // verific daca actorul exista deja
            boolean actorExists = false;
            for (Actor actor : imdb.getActorList()) {
                if (actor.getName().equals(name)) {
//                    JOptionPane.showMessageDialog(this, "Actorul exista deja!");
                    actorExists = true;
                    break;
                }
            }
            // verific daca numele este gol
            if (name.isEmpty() || actorExists) {
                JOptionPane.showMessageDialog(this, "Numele nu poate fi asta! Scrie altceva!");
            } else {
                // adaug actorul
                Staff staff = (Staff) user;
                Actor actor = new Actor(name, performances, biography);
                staff.addActorSystem(actor);
                // updatez exp-ul userului
                user.setExperienceStrategy(new ContributionStrategy());
                user.updateExperience();
                // dau mesaj ca s-a adaugat cu succes
                JOptionPane.showMessageDialog(this, "Actorul a fost adaugat cu succes! Ai primit experienta!");
            }
        });
        addActorPagePanel.add(addActorPanel, BorderLayout.CENTER);
        addActorPagePanel.add(addButton, BorderLayout.SOUTH);
        return addActorPagePanel;
    }

    private JPanel productioncreateContributionsPagePanel(String username, String userAccountType) {
        JPanel productioncontributionsPagePanel = new JPanel(new BorderLayout());
        productionListModel = new DefaultListModel<>();
        JList<Production> productionList = new JList<>(productionListModel);
        productionList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                Production production = (Production) value;
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setText(production.getTitlu());
                return label;
            }
        });
        // adauga buton pentru refresh la lista de producții
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> {
            // afisez lista de producții
            productionListModel.clear();
            User user1 = imdb.getUser(username);
            if (user1 instanceof Contributor) {
                Contributor contributor = (Contributor) user1;
                SortedSet<Production> initialProductions = contributor.getProductionsContribution();
                for (Production production : initialProductions) {
                    productionListModel.addElement(production);
                }
                productionList.setModel(productionListModel);
            } else {
                Admin admin = (Admin) user1;
                SortedSet<Production> initialProductions = admin.getProductionsContribution();
                // adaug si productiile comune
                initialProductions.addAll(Admin.getProductionsContributionCommon());
                for (Production production : initialProductions) {
                    productionListModel.addElement(production);
                }
                productionList.setModel(productionListModel);
            }
        });
        productioncontributionsPagePanel.add(refreshButton, BorderLayout.NORTH);
        User user = imdb.getUser(username);
        if (user instanceof Contributor) {
            Contributor contributor = (Contributor) user;
            SortedSet<Production> initialProductions = contributor.getProductionsContribution();
            for (Production production : initialProductions) {
                productionListModel.addElement(production);
            }
            productionList.setModel(productionListModel);
        } else {
            Admin admin = (Admin) user;
            SortedSet<Production> initialProductions = admin.getProductionsContribution();
            // adaug si productiile comune
            initialProductions.addAll(Admin.getProductionsContributionCommon());
            for (Production production : initialProductions) {
                productionListModel.addElement(production);
            }
            productionList.setModel(productionListModel);
        }
        JScrollPane scrollPane = new JScrollPane(productionList);
        productioncontributionsPagePanel.add(scrollPane, BorderLayout.CENTER);

        productionList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Production selectedProduction = productionList.getSelectedValue();
                    openProductionDetails(selectedProduction, user);
                    // afisez lista de producții
                    productionListModel.clear();
                    User user1 = imdb.getUser(username);
                    if (user1 instanceof Contributor) {
                        Contributor contributor = (Contributor) user1;
                        SortedSet<Production> initialProductions = contributor.getProductionsContribution();
                        for (Production production : initialProductions) {
                            productionListModel.addElement(production);
                        }
                        productionList.setModel(productionListModel);
                    } else {
                        Admin admin = (Admin) user1;
                        SortedSet<Production> initialProductions = admin.getProductionsContribution();
                        // adaug si productiile comune
                        initialProductions.addAll(Admin.getProductionsContributionCommon());
                        for (Production production : initialProductions) {
                            productionListModel.addElement(production);
                        }
                        productionList.setModel(productionListModel);
                    }
                }
            }
        });
        return productioncontributionsPagePanel;
    }

    private JPanel actorcreateContributionsPagePanel(String username, String userAccountType) {
        JPanel actorcontributionsPagePanel = new JPanel(new BorderLayout());
        actorListModel = new DefaultListModel<>();
        JList<Actor> actorList = new JList<>(actorListModel);
        actorList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                Actor actor = (Actor) value;
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setText(actor.getName());
                return label;
            }
        });
        // adauga buton pentru refresh la lista de actori
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> {
            // afisez lista de actori
            actorListModel.clear();
            User user1 = imdb.getUser(username);
            if (user1 instanceof Contributor) {
                Contributor contributor = (Contributor) user1;
                TreeSet<Actor> initialActors = contributor.getActorsContribution();
                for (Actor actor : initialActors) {
                    actorListModel.addElement(actor);
                }
                actorList.setModel(actorListModel);
            } else {
                Admin admin = (Admin) user1;
                TreeSet<Actor> initialActors = admin.getActorsContribution();
                // adaug si actorii comuni
                initialActors.addAll(Admin.getActorsContributionCommon());
                for (Actor actor : initialActors) {
                    actorListModel.addElement(actor);
                }
                actorList.setModel(actorListModel);
            }
        });
        actorcontributionsPagePanel.add(refreshButton, BorderLayout.NORTH);
        User user = imdb.getUser(username);
        if (user instanceof Contributor) {
            Contributor contributor = (Contributor) user;
            TreeSet<Actor> initialActors = contributor.getActorsContribution();
            for (Actor actor : initialActors) {
                actorListModel.addElement(actor);
            }
            actorList.setModel(actorListModel);
        } else {
            Admin admin = (Admin) user;
            TreeSet<Actor> initialActors = admin.getActorsContribution();
            // adaug si actorii comuni
            initialActors.addAll(Admin.getActorsContributionCommon());
            for (Actor actor : initialActors) {
                actorListModel.addElement(actor);
            }
            actorList.setModel(actorListModel);
        }
        JScrollPane scrollPane = new JScrollPane(actorList);
        actorcontributionsPagePanel.add(scrollPane, BorderLayout.CENTER);
        actorList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Actor selectedActor = actorList.getSelectedValue();
                    openActorDetails(selectedActor, username, userAccountType);
                    // afisez lista de actori
                    actorListModel.clear();
                    User user1 = imdb.getUser(username);
                    if (user1 instanceof Contributor) {
                        Contributor contributor = (Contributor) user1;
                        TreeSet<Actor> initialActors = contributor.getActorsContribution();
                        for (Actor actor : initialActors) {
                            actorListModel.addElement(actor);
                        }
                        actorList.setModel(actorListModel);
                    } else {
                        Admin admin = (Admin) user1;
                        TreeSet<Actor> initialActors = admin.getActorsContribution();
                        // adaug si actorii comuni
                        initialActors.addAll(Admin.getActorsContributionCommon());
                        for (Actor actor : initialActors) {
                            actorListModel.addElement(actor);
                        }
                        actorList.setModel(actorListModel);
                    }
                }
            }
        });
        return actorcontributionsPagePanel;
    }

    private JPanel createFavoriteProductionsPagePanel(String username, String userAccountType) {
        JPanel favoriteProductionsPagePanel = new JPanel(new BorderLayout());
        // vreau sa afisez lista de producții
        productionListModel = new DefaultListModel<>();
        JList<Production> productionList = new JList<>(productionListModel);
        productionList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                Production production = (Production) value;
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setText(production.getTitlu());
                return label;
            }
        });
        // vreau un buton care sa dea refresh la lista de producții
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> {
            // afisez lista de producții
            productionListModel.clear();
            User user1 = imdb.getUser(username);
            TreeSet<Production> initialProductions = user1.getFavoriteProductions();
            for (Production production : initialProductions) {
                productionListModel.addElement(production);
            }
            productionList.setModel(productionListModel);
        });
        favoriteProductionsPagePanel.add(refreshButton, BorderLayout.NORTH);
        User user = imdb.getUser(username);
        System.out.println("In favorite: " + user.getFavoriteProductions());
        TreeSet<Production> initialProductions = user.getFavoriteProductions();
        for (Production production : initialProductions) {
            productionListModel.addElement(production);
        }
        JScrollPane scrollPane = new JScrollPane(productionList);
        favoriteProductionsPagePanel.add(scrollPane, BorderLayout.CENTER);
        productionList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Production selectedProduction = productionList.getSelectedValue();
                    openProductionDetails(selectedProduction, user);
                    // afisez lista de producții
                    productionListModel.clear();
                    TreeSet<Production> initialProductions = user.getFavoriteProductions();
                    for (Production production : initialProductions) {
                        productionListModel.addElement(production);
                    }
                }
            }
        });
        return favoriteProductionsPagePanel;
    }

    private JPanel createFavoriteActorsPagePanel(String username, String userAccountType) {
        JPanel favoriteActorsPagePanel = new JPanel(new BorderLayout());
        // vreau sa afisez lista de actori
        actorListModel = new DefaultListModel<>();
        JList<Actor> actorList = new JList<>(actorListModel);
        actorList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                Actor actor = (Actor) value;
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setText(actor.getName());
                return label;
            }
        });
        // vreau un buton care sa dea refresh la lista de actori
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> {
            // afisez lista de actori
            actorListModel.clear();
            User user1 = imdb.getUser(username);
            TreeSet<Actor> initialActors = user1.getFavoriteActors();
            for (Actor actor : initialActors) {
                actorListModel.addElement(actor);
            }
            actorList.setModel(actorListModel);
        });
        favoriteActorsPagePanel.add(refreshButton, BorderLayout.NORTH);
        User user = imdb.getUser(username);
        System.out.println("In favorite: " + user.getFavoriteActors());
        TreeSet<Actor> initialActors = user.getFavoriteActors();
        for (Actor actor : initialActors) {
            actorListModel.addElement(actor);
        }
        JScrollPane scrollPane = new JScrollPane(actorList);
        favoriteActorsPagePanel.add(scrollPane, BorderLayout.CENTER);
        actorList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Actor selectedActor = actorList.getSelectedValue();
                    openActorDetails(selectedActor, username, userAccountType);
                    // afisez lista de actori
                    actorListModel.clear();
                    TreeSet<Actor> initialActors = user.getFavoriteActors();
                    for (Actor actor : initialActors) {
                        actorListModel.addElement(actor);
                    }
                }
            }
        });
        return favoriteActorsPagePanel;
    }

    private JPanel createActorsPagePanel(String username, String userAccountType) {
        JPanel actorsPagePanel = new JPanel(new BorderLayout());
        // vreau sa afisez lista de actori
        actorListModel = new DefaultListModel<>();
        JList<Actor> actorList = new JList<>(actorListModel);
        actorList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                Actor actor = (Actor) value;
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setText(actor.getName());
                return label;
            }
        });
        // adauga buton pentru refresh la lista de actori
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> {
            // afisez lista de actori
            actorListModel.clear();
            List<Actor> refreshActors = imdb.getActorList();
            // arat actorii in ordine alfabetica
            Collections.sort(refreshActors, new Comparator<Actor>() {
                @Override
                public int compare(Actor o1, Actor o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
            for (Actor actor : refreshActors) {
                actorListModel.addElement(actor);
                System.out.println("Dupa refresh : " + actor.getName());
            }
            actorList.setModel(actorListModel);
        });
//        actorsPagePanel.add(refreshButton, BorderLayout.NORTH);
        // vreau sa adaug un search field pentru actori
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Caută");
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, refreshButton, searchPanel);
        actorsPagePanel.add(splitPane, BorderLayout.NORTH);
//        actorsPagePanel.add(searchPanel, BorderLayout.SOUTH);
        // adaug un listener pentru butonul de search
        searchButton.addActionListener(e -> {
            String searchText = searchField.getText();
            List<Actor> filteredActors = imdb.getActorList();
            // Altfel, afișează actorii care corespund criteriilor
            if (!searchText.isEmpty()) {
                filteredActors = imdb.filterByActor(searchText, filteredActors);
            }
            actorListModel.clear();
            for (Actor actor : filteredActors) {
                actorListModel.addElement(actor);
            }
            // verific daca lista e goala
            if (actorListModel.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nu exista actori care sa corespunda criteriilor! Incercati din nou!");
                // resetez lista
                refreshButton.doClick();
            }
        });
        List<Actor> initialActors = imdb.getActorList();
        Collections.sort(initialActors, new Comparator<Actor>() {
            @Override
            public int compare(Actor o1, Actor o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        for (Actor actor : initialActors) {
            actorListModel.addElement(actor);
            System.out.println("Initial : " + actor.getName());
        }
        JScrollPane scrollPane = new JScrollPane(actorList);
        actorsPagePanel.add(scrollPane, BorderLayout.CENTER);
        actorList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Actor selectedActor = actorList.getSelectedValue();
                    openActorDetails(selectedActor, username, userAccountType);
                    // refresh la lista de actori
                    actorListModel.clear();
                    List<Actor> refreshActors = imdb.getActorList();
                    for (Actor actor : refreshActors) {
                        actorListModel.addElement(actor);
                        System.out.println("Dupa click: " + actor.getName());
                    }
                }
            }
        });
        return actorsPagePanel;
    }

    private void openActorDetails(Actor selectedActor, String username, String userAccountType) {
        JDialog detailsDialog = new JDialog(this, "Actor Details", true);
        detailsDialog.setLayout(new BorderLayout());

        // Create a panel for the name and biography
        JPanel textPanel = new JPanel(new GridLayout(0, 1));
        textPanel.add(new JLabel("Name: " + selectedActor.getName()));
        JTextArea bioArea = new JTextArea(5, 20); // 5 rows, 20 columns
        bioArea.setText(selectedActor.getBiography());
        bioArea.setWrapStyleWord(true);
        bioArea.setLineWrap(true);
        bioArea.setCaretPosition(0);
        bioArea.setEditable(false);
        JScrollPane bioScrollPane = new JScrollPane(bioArea);
        textPanel.add(bioScrollPane);

        // Create a model and list for the performances
        DefaultListModel<String> performanceModel = new DefaultListModel<>();
        for (Actor.Performance performance : selectedActor.getPerformances()) {
            performanceModel.addElement(performance.getTitle() + " - " + performance.getType());
        }
        JList<String> performanceList = new JList<>(performanceModel);
        performanceList.setBorder(BorderFactory.createTitledBorder("Performances"));

        // Scroll pane for performance list
        JScrollPane performanceScrollPane = new JScrollPane(performanceList);

        // Add components to the dialog
        detailsDialog.add(textPanel, BorderLayout.NORTH);
        detailsDialog.add(performanceScrollPane, BorderLayout.CENTER);

        // Set size, position, and modality
        detailsDialog.setSize(400, 600);
        detailsDialog.setLocationRelativeTo(this); // Center on parent (this)
        detailsDialog.setModal(true); // Block input to other windows

        // Adauga butoane pentru adaugare si stergere si modificare
        User user = imdb.getUser(username);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        if (user.getUserType() == AccountType.REGULAR || user.getUserType() == AccountType.CONTRIBUTOR) {
            JButton add_or_removeButton = new JButton("");
            checkifabutton(add_or_removeButton, selectedActor, user);
            buttonPanel.add(add_or_removeButton);
        }

        // verific daca utilizatorul curent este admin sau contributor
        // pentru a putea modifica actorul
        if (user.getUserType() == AccountType.ADMIN || user.getUserType() == AccountType.CONTRIBUTOR) {
            boolean showButton = false;
            if (user.getUserType() == AccountType.CONTRIBUTOR) {
                Contributor contributor = (Contributor) user;
                if (contributor.getActorsContribution().contains(selectedActor)) {
                    showButton = true;
                }
            } else {
                Admin admin = (Admin) user;
                if (admin.getActorsContribution().contains(selectedActor) ||
                        Admin.getActorsContributionCommon().contains(selectedActor)) {
                    showButton = true;
                }
            }
            if (showButton) {
                JButton modifyButton = new JButton("Modifica Actor");
                modifyButton.addActionListener(e -> {
                    // deschide fereastra de modificare
                    JDialog modifyDialog = new JDialog(this, "Modifica Actor", true);
                    modifyDialog.setLayout(new BorderLayout());

                    // Create a panel for the name and biography
                    JPanel modifyPanel = new JPanel(new GridLayout(0, 1));
                    JTextField nameField = new JTextField(selectedActor.getName());
                    modifyPanel.add(new JLabel("Name: "));
                    modifyPanel.add(nameField);
                    JTextArea bioArea2 = new JTextArea(5, 20); // 5 rows, 20 columns
                    bioArea2.setText(selectedActor.getBiography());
                    bioArea2.setWrapStyleWord(true);
                    bioArea2.setLineWrap(true);
                    bioArea2.setCaretPosition(0);
                    modifyPanel.add(new JLabel("Biography: "));
                    JScrollPane bioScrollPane2 = new JScrollPane(bioArea2);
                    modifyPanel.add(bioScrollPane2);

//                // Create a model and list for the performances
//                DefaultListModel<String> performanceModel2 = new DefaultListModel<>();
//                for (Actor.Performance performance : selectedActor.getPerformances()) {
//                    performanceModel2.addElement(performance.getTitle() + " - " + performance.getType());
//                }
//                JList<String> performanceList2 = new JList<>(performanceModel2);
//                performanceList2.setBorder(BorderFactory.createTitledBorder("Performances"));
                    JTextArea performancesArea = new JTextArea(5, 20); // 5 rows, 20 columns
                    StringBuilder performancesTextFill = new StringBuilder();
                    for (Actor.Performance performance : selectedActor.getPerformances()) {
                        performancesTextFill.append(performance.getTitle()).append(" - ").append(performance.getType()).append("\n");
                    }
                    performancesArea.setText(performancesTextFill.toString());
                    performancesArea.setWrapStyleWord(true);
                    performancesArea.setLineWrap(true);
                    performancesArea.setCaretPosition(0);
                    performancesArea.setEditable(true);
                    modifyPanel.add(new JLabel("Performances: "));
                    JScrollPane performancesScrollPane = new JScrollPane(performancesArea);
                    modifyPanel.add(performancesScrollPane);

                    // Add components to the dialog
                    modifyDialog.add(modifyPanel, BorderLayout.NORTH);
//                modifyDialog.add(performanceScrollPane2, BorderLayout.CENTER);

                    // Set size, position, and modality
                    modifyDialog.setSize(400, 600);
                    modifyDialog.setLocationRelativeTo(this); // Center on parent (this)
                    modifyDialog.setModal(true); // Block input to other windows

                    // Adauga butoane pentru adaugare si stergere si modificare
                    JPanel buttonPanel2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                    JButton saveButton = new JButton("Salveaza");
                    saveButton.addActionListener(e2 -> {
                        // salveaza modificarile
                        String name = nameField.getText();
                        String biography = bioArea2.getText();
                        List<Actor.Performance> performances = new ArrayList<>();
                        String performancesText = performancesArea.getText();
                        String[] performancesDetails = performancesText.split("\n");
                        for (String performance : performancesDetails) {
                            String[] performanceDetails = performance.trim().split(" - ");
                            if (performanceDetails.length != 2) {
                                // Inform the user about the improperly formatted performance
                                JOptionPane.showMessageDialog(this, "The performance '" + performance + "' is not formatted correctly. Please use 'Title - Type'.");
                                return; // Exit the method to prevent further processing
                            }
                            String title = performanceDetails[0].trim();
                            String type = performanceDetails[1].trim();
                            if (title.isEmpty() || type.isEmpty()) {
                                JOptionPane.showMessageDialog(this, "Title and type cannot be empty.");
                                return; // Exit the method to prevent further processing
                            }
                            performances.add(new Actor.Performance(title, type));
                        }
                        // verific daca numele este gol
                        if (name.isEmpty()) {
                            JOptionPane.showMessageDialog(this, "Numele nu poate fi gol!");
                        } else {
                            // modific actorul
                            Staff staff = (Staff) user;
                            Actor actor = new Actor(name, performances, biography);
                            if (Objects.equals(actor.getName(), selectedActor.getName())) {
                                staff.updateActor(actor);
                                //afisez lista de actori
//                            actorListModel.clear();
//                            List<Actor> initialActors = imdb.getActorList();
//                            for (Actor actor1 : initialActors) {
//                                actorListModel.addElement(actor1);
//                            }
                            } else {
                                staff.removeActorSystem(selectedActor.getName());
                                staff.addActorSystem(actor);
                                //afisez lista de actori
//                            actorListModel.clear();
//                            List<Actor> initialActors = imdb.getActorList();
//                            for (Actor actor1 : initialActors) {
//                                actorListModel.addElement(actor1);
//                            }
                            }
                            // inchid fereastra de modificare
                            modifyDialog.dispose();
                            // inchid fereastra de detalii
                            detailsDialog.dispose();
                            // deschid fereastra de detalii cu actorul modificat
                        }
                    });
                    buttonPanel2.add(saveButton);
                    // adaug buton de delete actor
                    JButton deleteButton = new JButton("Sterge Actor");
                    deleteButton.addActionListener(e2 -> {
                        // sterg actorul
                        Staff staff = (Staff) user;
                        staff.removeActorSystem(selectedActor.getName());
                        //afisez lista de actori
                        System.out.println("1.sunt aici - " + actorListModel.size());
                        actorListModel.clear();
                        List<Actor> initialActors = imdb.getActorList();
                        System.out.println("2.sunt aici - " + initialActors.size());
                        for (Actor actor1 : initialActors) {
                            actorListModel.addElement(actor1);
                            System.out.println("3.sunt aici - " + actor1.getName());
                        }
                        // inchid fereastra de modificare
                        modifyDialog.dispose();
                        // inchid fereastra de detalii
                        detailsDialog.dispose();
                    });
                    buttonPanel2.add(deleteButton);
                    modifyDialog.add(buttonPanel2, BorderLayout.SOUTH);
                    // Show the dialog
                    modifyDialog.setVisible(true);
                });
                buttonPanel.add(modifyButton);
            }
        }

        detailsDialog.add(buttonPanel, BorderLayout.SOUTH);
        detailsDialog.setVisible(true);
        detailsDialog.revalidate();
        detailsDialog.repaint();
    }

    private void checkifabutton(JButton addOrRemoveButton, Actor selectedActor, User user) {
        if (user.getUserType() == AccountType.REGULAR || user.getUserType() == AccountType.CONTRIBUTOR || user.getUserType() == AccountType.ADMIN) {
            if (user.getFavoriteActors().contains(selectedActor)) {
                addOrRemoveButton.setText("Sterge din Favorite");
                addOrRemoveButton.addActionListener(e -> {
                    // sterge actorul din lista de actori a utilizatorului curent
//                    Regular regular = (Regular) user;
//                    regular.remove_favorite_actor(selectedActor);
                    user.remove_favorite_actor(selectedActor);
                    // afisez lista de actori preferati
                    System.out.println("sunt aici -");
                    System.out.println(user.getFavoriteActors());

                });
            } else {
                addOrRemoveButton.setText("Adauga la Favorite");
                addOrRemoveButton.addActionListener(e -> {
                    // adauga actorul in lista de actori a utilizatorului curent
//                    Regular regular = (Regular) user;
//                    regular.add_favorite_actor(selectedActor);
                    user.add_favorite_actor(selectedActor);
                    // afisez lista de actori preferati
                    System.out.println("sunt aici +");
                    System.out.println(user.getFavoriteActors());
                });
            }
        }
    }

    private JPanel createHomePagePanel(String username, String userAccountType) {
        JPanel homePagePanel = new JPanel(new BorderLayout());

        // Adaugă zona de filtrare
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Caută");
        JComboBox<Genre> genreComboBox = new JComboBox<>(Genre.values());
        JTextField ratingField = new JTextField(5);
        ratingField.setMaximumSize(new Dimension(50, 20));
        filterPanel.add(searchField);
        filterPanel.add(genreComboBox);
        filterPanel.add(new JLabel("Rating minim:"));
        filterPanel.add(ratingField);
        filterPanel.add(searchButton);
        productionListModel = new DefaultListModel<>();
        JList<Production> productionList = new JList<>(productionListModel);
        JButton resetFiltersButton = new JButton("Refresh");
        filterPanel.add(resetFiltersButton);

        resetFiltersButton.addActionListener(e -> {
            List<Production> initialProductions = imdb.getProductionList();
            // ordonez lista de producții in ordine alfabetica
            Collections.sort(initialProductions, new Comparator<Production>() {
                @Override
                public int compare(Production o1, Production o2) {
                    return o1.getTitlu().compareTo(o2.getTitlu());
                }
            });
            productionListModel.clear();
            for (Production production : initialProductions) {
                productionListModel.addElement(production);
            }
            productionList.setModel(productionListModel);
        });

        // Adaugă listener pentru butonul de căutare
        searchButton.addActionListener(e -> {
            String searchText = searchField.getText();
            Genre selectedGenre = (Genre) genreComboBox.getSelectedItem();
            List<Production> filteredProductions = imdb.getProductionList();
            String ratingText = ratingField.getText();
            // Altfel, afișează producțiile care corespund criteriilor
            try {
                if (!ratingText.isEmpty()) {
                    double rating = Double.parseDouble(ratingText);
                    filteredProductions = imdb.filterByRating(rating, filteredProductions);
                }
                if (!searchText.isEmpty()) {
                    filteredProductions = imdb.filterByTitle(searchText, filteredProductions);
                }
                if (selectedGenre != null) {
                    filteredProductions = imdb.filterByGenre(selectedGenre.toString(), filteredProductions);
                }
                productionListModel.clear();
                for (Production production : filteredProductions) {
                    productionListModel.addElement(production);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Rating-ul trebuie să fie un număr! Pune un număr sau lasă câmpul gol!");

            }
            // verific daca lista e goala
            if (productionListModel.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nu exista produtii care sa corespunda criteriilor! Incercati din nou!");
                // resetez lista
                resetFiltersButton.doClick();
            }

        });
        productionList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                                                          boolean cellHasFocus) {
                Production production = (Production) value;
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setText(production.getTitlu());
                return label;
            }
        });
        List<Production> initialProductions = imdb.getProductionList();
        Collections.sort(initialProductions, new Comparator<Production>() {
            @Override
            public int compare(Production o1, Production o2) {
                return o1.getTitlu().compareTo(o2.getTitlu());
            }
        });
        for (Production production : initialProductions) {
            productionListModel.addElement(production);
        }
        JScrollPane scrollPane = new JScrollPane(productionList);
        User user = imdb.getUser(username);
        productionList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Production selectedProduction = productionList.getSelectedValue();
                    openProductionDetails(selectedProduction, user);
                }
            }
        });
        homePagePanel.add(filterPanel, BorderLayout.NORTH);
        homePagePanel.add(scrollPane, BorderLayout.CENTER);
        return homePagePanel;
    }

    private JPanel createSidebarPanel() {
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        // buton de logout
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener((event) -> {
            // Logică de delogare
            // arata fereastra de login
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
            // inchide fereastra curenta
            MainPage.this.dispose();

        });
        sidebarPanel.add(logoutButton);
        // buton de home page
        JButton homePageButton = new JButton("Home Page");
        homePageButton.addActionListener(e -> {
            cardLayout.show(cardPanel, "home");
        });
        sidebarPanel.add(homePageButton);
        // buton de actori
        JButton actorsPageButton = new JButton("Actors Page");
        actorsPageButton.addActionListener(e -> {
            cardLayout.show(cardPanel, "actors");
        });
        sidebarPanel.add(actorsPageButton);
        User user = imdb.getUser(username);

        JButton favoriteActorsPageButton = new JButton("Favorite Actors Page");
        favoriteActorsPageButton.addActionListener(e -> {
            cardLayout.show(cardPanel, "favoriteActors");
        });
        sidebarPanel.add(favoriteActorsPageButton);
        JButton favoriteProductionsPageButton = new JButton("Favorite Productions Page");
        favoriteProductionsPageButton.addActionListener(e -> {
            cardLayout.show(cardPanel, "favoriteProductions");
        });
        sidebarPanel.add(favoriteProductionsPageButton);

        // doar pentru utilizatorii contributor sau admin
        if (user.getUserType() == AccountType.CONTRIBUTOR || user.getUserType() == AccountType.ADMIN) {
            JButton actorcontributionsPageButton = new JButton("Actor Contributions Page");
            actorcontributionsPageButton.addActionListener(e -> {
                cardLayout.show(cardPanel, "contributionsActor");
            });
            sidebarPanel.add(actorcontributionsPageButton);
            JButton productioncontributionsPageButton = new JButton("Production Contributions Page");
            productioncontributionsPageButton.addActionListener(e -> {
                cardLayout.show(cardPanel, "contributionsProductions");
            });
            sidebarPanel.add(productioncontributionsPageButton);
            // vreau sa adaug un buton pentru adaugare actor
            JButton addActorButton = new JButton("Adauga Actor");
            addActorButton.addActionListener(e -> {
                cardLayout.show(cardPanel, "addActor");
            });
            sidebarPanel.add(addActorButton);
            // vreau sa adaug un buton pentru adaugare producție
            JButton addProductionButton = new JButton("Adauga Producție");
            addProductionButton.addActionListener(e -> {
                cardLayout.show(cardPanel, "addProduction");
            });
            sidebarPanel.add(addProductionButton);
        }
        // account page
        JButton accountPageButton = new JButton("Contul Meu");
        accountPageButton.addActionListener(e -> {
            cardLayout.show(cardPanel, "account");
        });
        sidebarPanel.add(accountPageButton);
        return sidebarPanel;
    }

    private void openProductionDetails(Production selectedproduction, User user) {
        // Crează și afișează o nouă fereastră sau un dialog cu detaliile producției
        JDialog detailsDialog = new JDialog(this, "Detalii Producție", true);
        detailsDialog.setSize(400, 300);
        detailsDialog.setLayout(new BorderLayout());

        // Productia are un titlu, regizori, actori, genuri, rating, descriere
        // Create a panel for the title and description
        JPanel textPanel = new JPanel(new GridLayout(0, 2));
        textPanel.add(new JLabel("Titlu: " + selectedproduction.getTitlu()));
        // adauga nota filmului
        textPanel.add(new JLabel("Nota: " + selectedproduction.getNotaFilm()));
        if (selectedproduction instanceof Series) {
            // Add release year to the text panel
            textPanel.add(new JLabel("An Lansare: " + ((Series) selectedproduction).getReleaseAn()));
            textPanel.add(new JLabel("Numar Sezoane: " + ((Series) selectedproduction).getNumarSezoane()));
        } else {
            // add runtime and release year to the text panel
            textPanel.add(new JLabel("Durata: " + ((Movie) selectedproduction).getRunTimp() + " minute"));
            textPanel.add(new JLabel("An Lansare: " + ((Movie) selectedproduction).getReleaseAn()));
        }
        JTextArea descriptionArea = new JTextArea(5, 20); // 5 rows, 20 columns
        descriptionArea.setText(selectedproduction.getDescriereFilm());
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setLineWrap(true);
        descriptionArea.setCaretPosition(0);
        descriptionArea.setEditable(false);
        JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);
        textPanel.add(descriptionScrollPane);
        // Create a model and list for the directors
        DefaultListModel<String> directorModel = new DefaultListModel<>();
        for (String director : selectedproduction.getRegizoriList()) {
            directorModel.addElement(director);
        }
        JList<String> directorList = new JList<>(directorModel);
        directorList.setBorder(BorderFactory.createTitledBorder("Regizori"));
        // Create a model and list for the actors
        DefaultListModel<String> actorModel = new DefaultListModel<>();
        for (String actor : selectedproduction.getActoriList()) {
            actorModel.addElement(actor);
        }
        JList<String> actorList = new JList<>(actorModel);
        actorList.setBorder(BorderFactory.createTitledBorder("Actori"));
        // Create a model and list for the genres
        DefaultListModel<String> genreModel = new DefaultListModel<>();
        for (String genre : selectedproduction.getGenreList()) {
            genreModel.addElement(genre);
        }
        JList<String> genreList = new JList<>(genreModel);
        genreList.setBorder(BorderFactory.createTitledBorder("Genuri"));
        // Create a model and list for the ratings
        DefaultListModel<String> ratingModel = new DefaultListModel<>();
        for (Rating rating : selectedproduction.getRatingList()) {
            ratingModel.addElement(rating.getUsernameRater() + " - " + rating.getNotaRating() + " - " + rating.getComentariiRater());
        }
        JList<String> ratingList = new JList<>(ratingModel);
        ratingList.setBorder(BorderFactory.createTitledBorder("Rating"));
        // Scroll pane for director list
        JScrollPane directorScrollPane = new JScrollPane(directorList);
        // Scroll pane for actor list
        JScrollPane actorScrollPane = new JScrollPane(actorList);
        // Scroll pane for genre list
        JScrollPane genreScrollPane = new JScrollPane(genreList);
        // Scroll pane for rating list
        JScrollPane ratingScrollPane = new JScrollPane(ratingList);
        // check if production is series and also show seasons and episodes
        if (selectedproduction instanceof Series) {
            Series series = (Series) selectedproduction;
            // Create a model and list for the seasons
            DefaultListModel<String> seasonModel = new DefaultListModel<>();
            for (String season : series.getMapSerial().keySet()) {
                seasonModel.addElement(season);
            }
            JList<String> seasonList = new JList<>(seasonModel);
            seasonList.setBorder(BorderFactory.createTitledBorder("Sezoane"));
            // Create a model and list for the episodes
            DefaultListModel<String> episodeModel = new DefaultListModel<>();
            for (String season : series.getMapSerial().keySet()) {
                for (Episode episode : series.getMapSerial().get(season)) {
                    episodeModel.addElement(episode.getNumeEpisod() + " - " + episode.getDurataEpisod());
                }
            }
            JList<String> episodeList = new JList<>(episodeModel);
            episodeList.setBorder(BorderFactory.createTitledBorder("Episoade"));
            // Scroll pane for season list
            JScrollPane seasonScrollPane = new JScrollPane(seasonList);
            // Scroll pane for episode list
            JScrollPane episodeScrollPane = new JScrollPane(episodeList);
            // Add components to the dialog without overlapping others
            detailsDialog.add(textPanel, BorderLayout.NORTH);
            detailsDialog.add(genreList, BorderLayout.EAST);
            JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, seasonScrollPane, episodeScrollPane);
            detailsDialog.add(splitPane, BorderLayout.CENTER);
            JSplitPane splitPane2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, directorScrollPane, actorScrollPane);
            detailsDialog.add(splitPane2, BorderLayout.WEST);
        } else {
            // Add components to the dialog without overlapping others
            detailsDialog.add(textPanel, BorderLayout.NORTH);
            detailsDialog.add(directorList, BorderLayout.WEST);
            detailsDialog.add(actorList, BorderLayout.CENTER);
            detailsDialog.add(genreList, BorderLayout.EAST);
        }
        // Set size, position, and modality
        detailsDialog.setSize(400, 600);
        detailsDialog.setLocationRelativeTo(this); // Center on parent (this)
        detailsDialog.setModal(true); // Block input to other windows
        // Add buttons for adding and removing
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton addOrRemoveButton = new JButton("");
        checkifabuttonP(addOrRemoveButton, selectedproduction, user);
        buttonPanel.add(addOrRemoveButton);
        // verific daca este user REGULAR pentru a adauga rating
        if (user.getUserType() == AccountType.REGULAR) {
            JButton addRatingButton = new JButton("Adauga Rating");
            addRatingButton.addActionListener(e -> {
                // deschide fereastra de adaugare rating
                JDialog addRatingDialog = new JDialog(this, "Adauga Rating", true);
                addRatingDialog.setLayout(new BorderLayout());
                addRatingDialog.setSize(400, 600);
                addRatingDialog.setLocationRelativeTo(this); // Center on parent (this)
                addRatingDialog.setModal(true); // Block input to other windows
                // Create a panel for the title and description
                JPanel addRatingPanel = new JPanel();
                addRatingPanel.setLayout(new BoxLayout(addRatingPanel, BoxLayout.Y_AXIS));
                // Create a text field for rating
                JTextField ratingField = new JTextField();
                ratingField.setMaximumSize(new Dimension(50, 20));
                addRatingPanel.add(new JLabel("Rating: "));
                addRatingPanel.add(ratingField);
                // create a text area for the comments
                JTextArea commentsArea = new JTextArea(5, 20); // 5 rows, 20 columns
                commentsArea.setText("");
                commentsArea.setWrapStyleWord(true);
                commentsArea.setLineWrap(true);
                commentsArea.setCaretPosition(0);
                commentsArea.setEditable(true);
                addRatingPanel.add(new JLabel("Comentarii: "));
                JScrollPane commentsScrollPane = new JScrollPane(commentsArea);
                addRatingPanel.add(commentsScrollPane);
                // Add components to the dialog
                addRatingDialog.add(addRatingPanel, BorderLayout.NORTH);
                // Adauga butoane pentru adaugare si stergere si modificare
                JPanel buttonPanel2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                JButton saveButton = new JButton("Salveaza");
                saveButton.addActionListener(e2 -> {
                    // salveaza ratingul
                    String ratingText = ratingField.getText();
                    String commentsText = commentsArea.getText();
                    // verific daca ratingul este gol
                    if (ratingText.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Ratingul nu poate fi gol!");
                    } else {
                        // adaug ratingul
                        Regular regular = (Regular) user;
                        int rating = Integer.parseInt(ratingText);
                        regular.add_rating(rating, commentsText);
                        selectedproduction.addRating(new Rating(username, rating, commentsText));
                        // updatez exp-ul utilizatorului
                        user.setExperienceStrategy(new ReviewStrategy());
                        user.updateExperience();
                        // inchid fereastra de adaugare rating
                        addRatingDialog.dispose();
                        // inchid fereastra de detalii
                        detailsDialog.dispose();
                    }
                });
                buttonPanel2.add(saveButton);
                addRatingDialog.add(buttonPanel2, BorderLayout.SOUTH);

                // Show the dialog
                addRatingDialog.setVisible(true);
            });
            buttonPanel.add(addRatingButton);
            // sau pentru a sterge rating daca am adaugat rating aici
            // caut username-ul in lista de review-uri
            for (Rating rating : selectedproduction.getRatingList()) {
                if (Objects.equals(rating.getUsernameRater(), username)) {
                    JButton deleteRatingButton = new JButton("Sterge Rating");
                    deleteRatingButton.addActionListener(e -> {
                        // sterge ratingul
                        Regular regular = (Regular) user;
                        regular.remove_rating();
                        selectedproduction.removeRating(username);
                        // inchid fereastra de detalii
                        detailsDialog.dispose();
                    });
                    buttonPanel.add(deleteRatingButton);
                }
            }
        }
        // verific daca utilizatorul curent este admin sau contributor
        // pentru a putea modifica productia
        if (user.getUserType() == AccountType.ADMIN || user.getUserType() == AccountType.CONTRIBUTOR) {
            //vreau sa apara butonul doar daca productia este contributia utilizatorului curent
            // daca utilizatorul este Admin trebuie verificata si lista de contributii comune
            boolean showButton = false;
            if (user.getUserType() == AccountType.CONTRIBUTOR) {
                Contributor contributor = (Contributor) user;
                if (contributor.getProductionsContribution().contains(selectedproduction)) {
                    showButton = true;
                }
            } else {
                Admin admin = (Admin) user;
                if (admin.getProductionsContribution().contains(selectedproduction) ||
                        Admin.getProductionsContributionCommon().contains(selectedproduction)) {
                    showButton = true;
                }
            }
            if (showButton) {
                JButton modifyButton = new JButton("Modifica Productie");
                modifyButton.addActionListener(e -> {
                    // deschide fereastra de modificare
                    JDialog modifyDialog = new JDialog(this, "Modifica Productie", true);
                    modifyDialog.setLayout(new BorderLayout());
                    modifyDialog.setSize(400, 600);
                    modifyDialog.setLocationRelativeTo(this); // Center on parent (this)
                    modifyDialog.setModal(true); // Block input to other windows
                    // Create a panel for the title and description
                    JPanel modifyPanel = new JPanel();
                    modifyPanel.setLayout(new BoxLayout(modifyPanel, BoxLayout.Y_AXIS));
                    JTextField titleField = new JTextField(selectedproduction.getTitlu());
                    modifyPanel.add(new JLabel("Titlu: "));
                    modifyPanel.add(titleField);
                    JTextArea descriptionArea2 = new JTextArea(5, 20); // 5 rows, 20 columns
                    descriptionArea2.setText(selectedproduction.getDescriereFilm());
                    descriptionArea2.setWrapStyleWord(true);
                    descriptionArea2.setLineWrap(true);
                    descriptionArea2.setCaretPosition(0);
                    modifyPanel.add(new JLabel("Descriere: "));
                    JScrollPane descriptionScrollPane2 = new JScrollPane(descriptionArea2);
                    modifyPanel.add(descriptionScrollPane2);
                    // Create a text field for release year
                    JTextField releaseYearField = selectedproduction instanceof Series ?
                            new JTextField(((Series) selectedproduction).getReleaseAn() + "") :
                            new JTextField(((Movie) selectedproduction).getReleaseAn() + "");

                    JTextField runtimeField = selectedproduction instanceof Movie ?
                            new JTextField(((Movie) selectedproduction).getRunTimp()) :
                            new JTextField(); // Default or some other handling

                    JTextField numarSezoaneField = selectedproduction instanceof Series ?
                            new JTextField(((Series) selectedproduction).getNumarSezoane() + "") :
                            new JTextField(); // Default or some other handling

                    if (selectedproduction instanceof Series) {
                        // Create a text field for release year
//                    releaseYearField = new JTextField(((Series) selectedproduction).getReleaseAn() + "");
                        modifyPanel.add(new JLabel("An Lansare: "));
                        modifyPanel.add(releaseYearField);
                        // Create a text field for number of seasons
//                    numarSezoaneField = new JTextField(((Series) selectedproduction).getNumarSezoane() + "");
                        modifyPanel.add(new JLabel("Numar Sezoane: "));
                        modifyPanel.add(numarSezoaneField);
                    } else {
                        // Create a text field for release year
//                    releaseYearField = new JTextField(((Movie) selectedproduction).getReleaseAn() + "");
                        modifyPanel.add(new JLabel("An Lansare: "));
                        modifyPanel.add(releaseYearField);
                        // Create a text field for runtime
//                    runtimeField = new JTextField(((Movie) selectedproduction).getRunTimp());
                        modifyPanel.add(new JLabel("Durata: "));
                        modifyPanel.add(runtimeField);
                    }
                    // Create a text filed for the rating
                    JTextField ratingField2 = new JTextField(selectedproduction.getNotaFilm() + "");
                    ratingField2.setMaximumSize(new Dimension(50, 20));
                    modifyPanel.add(new JLabel("Rating: "));
                    modifyPanel.add(ratingField2);
                    // create a text area for the directors
                    JTextArea directorsArea = new JTextArea(5, 20); // 5 rows, 20 columns
                    StringBuilder directorsTextFill = new StringBuilder();
                    for (String director : selectedproduction.getRegizoriList()) {
                        directorsTextFill.append(director).append("\n");
                    }
                    directorsArea.setText(directorsTextFill.toString());
                    directorsArea.setWrapStyleWord(true);
                    directorsArea.setLineWrap(true);
                    directorsArea.setCaretPosition(0);
                    directorsArea.setEditable(true);
                    modifyPanel.add(new JLabel("Regizori: "));
                    JScrollPane directorsScrollPane = new JScrollPane(directorsArea);
                    modifyPanel.add(directorsScrollPane);
                    // create a text area for the actors
                    JTextArea actorsArea = new JTextArea(5, 20); // 5 rows, 20 columns
                    StringBuilder actorsTextFill = new StringBuilder();
                    for (String actor : selectedproduction.getActoriList()) {
                        actorsTextFill.append(actor).append("\n");
                    }
                    actorsArea.setText(actorsTextFill.toString());
                    actorsArea.setWrapStyleWord(true);
                    actorsArea.setLineWrap(true);
                    actorsArea.setCaretPosition(0);
                    actorsArea.setEditable(true);
                    modifyPanel.add(new JLabel("Actori: "));
                    JScrollPane actorsScrollPane = new JScrollPane(actorsArea);
                    modifyPanel.add(actorsScrollPane);
                    // create a text area for the genres
                    JTextArea genresArea = new JTextArea(5, 20); // 5 rows, 20 columns
                    StringBuilder genresTextFill = new StringBuilder();
                    for (String genre : selectedproduction.getGenreList()) {
                        genresTextFill.append(genre).append("\n");
                    }
                    genresArea.setText(genresTextFill.toString());
                    genresArea.setWrapStyleWord(true);
                    genresArea.setLineWrap(true);
                    genresArea.setCaretPosition(0);
                    genresArea.setEditable(true);
                    modifyPanel.add(new JLabel("Genuri: "));
                    JScrollPane genresScrollPane = new JScrollPane(genresArea);
                    modifyPanel.add(genresScrollPane);
                    // create a text area for the ratings
                    JTextArea ratingsArea = new JTextArea(5, 20); // 5 rows, 20 columns
                    StringBuilder ratingsTextFill = new StringBuilder();
                    for (Rating rating : selectedproduction.getRatingList()) {
                        ratingsTextFill.append(rating.getNotaRating()).append(" - ").append(rating.getComentariiRater()).append("\n");
                    }
                    ratingsArea.setText(ratingsTextFill.toString());
                    ratingsArea.setWrapStyleWord(true);
                    ratingsArea.setLineWrap(true);
                    ratingsArea.setCaretPosition(0);
                    ratingsArea.setEditable(true);
                    modifyPanel.add(new JLabel("Rating: "));
                    JScrollPane ratingsScrollPane = new JScrollPane(ratingsArea);
                    modifyPanel.add(ratingsScrollPane);
                    // create a text area for the seasons and episodes if the production is a series
                    JTextArea seasonsArea = new JTextArea(5, 20); // 5 rows, 20 columns;
                    JTextArea episodesArea = new JTextArea(5, 20); // 5 rows, 20 columns;
                    if (selectedproduction instanceof Series) {
                        modifyPanel.add(new JLabel("Sezoane si Episoade: "));
                        Series series = (Series) selectedproduction;
                        // Create a model and list for the seasons
                        StringBuilder seasonsTextFill = new StringBuilder();
                        for (String season : series.getMapSerial().keySet()) {
                            seasonsTextFill.append(season).append("\n");
                        }
                        seasonsArea.setText(seasonsTextFill.toString());
                        seasonsArea.setWrapStyleWord(true);
                        seasonsArea.setLineWrap(true);
                        seasonsArea.setCaretPosition(0);
                        seasonsArea.setEditable(true);
//                    modifyPanel.add(new JLabel("Sezoane: "));
                        JScrollPane seasonsScrollPane = new JScrollPane(seasonsArea);
//                    modifyPanel.add(seasonsScrollPane);
                        // Create a model and list for the episodes
                        StringBuilder episodesTextFill = new StringBuilder();
                        for (String season : series.getMapSerial().keySet()) {
                            for (Episode episode : series.getMapSerial().get(season)) {
                                episodesTextFill.append(episode.getNumeEpisod()).append(" - ").append(episode.getDurataEpisod()).append("\n");
                            }
                        }
                        episodesArea.setText(episodesTextFill.toString());
                        episodesArea.setWrapStyleWord(true);
                        episodesArea.setLineWrap(true);
                        episodesArea.setCaretPosition(0);
                        episodesArea.setEditable(true);
//                    modifyPanel.add(new JLabel("Episoade: "));
                        JScrollPane episodesScrollPane = new JScrollPane(episodesArea);
//                    modifyPanel.add(episodesScrollPane);
                        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, seasonsScrollPane, episodesScrollPane);
                        modifyPanel.add(splitPane);
                    }
                    JScrollPane modifyScrollPane = new JScrollPane(modifyPanel);
                    modifyScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                    // Add components to the dialog without overlapping others
                    modifyDialog.add(modifyScrollPane, BorderLayout.CENTER);
//                modifyDialog.setContentPane(modifyScrollPane);
                    // Adauga butoane pentru salvare modificari
                    JPanel buttonPanel2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                    JButton saveButton = new JButton("Salveaza");
                    buttonPanel2.add(saveButton);
                    saveButton.addActionListener(e2 -> {
                        // salveaza modificarile
                        String title = titleField.getText();
                        String description = descriptionArea2.getText();
                        String ratingText = ratingField2.getText();
                        String directorsText = directorsArea.getText();
                        String actorsText = actorsArea.getText();
                        String genresText = genresArea.getText();
                        String ratingsText = ratingsArea.getText();
                        Map<String, List<Episode>> mapSerial = new HashMap<>();
                        if (selectedproduction instanceof Series) {
                            String releaseYearText = releaseYearField.getText();
                            String[] seasonsDetails = seasonsArea.getText().split("\n");
                            String[] episodesDetails = episodesArea.getText().split("\n");
                            for (String season : seasonsDetails) {
                                if (season.isEmpty()) {
                                    JOptionPane.showMessageDialog(this, "Numele sezonului nu poate fi gol!");
                                    return; // Exit the method to prevent further processing
                                }
                                List<Episode> episodeList = new ArrayList<>();
                                for (String episode : episodesDetails) {
                                    String[] episodeDetails = episode.trim().split(" - ");
                                    if (episodeDetails.length != 2) {
                                        // Inform the user about the improperly formatted episode
                                        JOptionPane.showMessageDialog(this, "The episode '" + episode + "' is not formatted correctly. Please use 'Title - Runtime'.");
                                        return; // Exit the method to prevent further processing
                                    }
                                    String episodeTitle = episodeDetails[0].trim();
                                    String episodeRuntime = episodeDetails[1].trim();
                                    if (episodeTitle.isEmpty() || episodeRuntime.isEmpty()) {
                                        JOptionPane.showMessageDialog(this, "Title and runtime cannot be empty.");
                                        return; // Exit the method to prevent further processing
                                    }
                                    episodeList.add(new Episode(episodeTitle, episodeRuntime));
                                }
                                mapSerial.put(season, episodeList);
                            }

                        } else {
                            // pentru movie am runtiem si release year
                            String runtimeText = runtimeField.getText();
                            String releaseYearText = releaseYearField.getText();
                        }
                        List<String> directors = new ArrayList<>();
                        String[] directorsDetails = directorsText.split("\n");
                        for (String director : directorsDetails) {
                            if (director.isEmpty()) {
                                JOptionPane.showMessageDialog(this, "Numele regizorului nu poate fi gol!");
                                return; // Exit the method to prevent further processing
                            }
                            directors.add(director);
                        }
                        List<String> actors = new ArrayList<>();
                        String[] actorsDetails = actorsText.split("\n");
                        for (String actor : actorsDetails) {
                            if (actor.isEmpty()) {
                                JOptionPane.showMessageDialog(this, "Numele actorului nu poate fi gol!");
                                return; // Exit the method to prevent further processing
                            }
                            actors.add(actor);
                        }
                        List<String> genres = new ArrayList<>();
                        String[] genresDetails = genresText.split("\n");
                        for (String genre : genresDetails) {
                            if (genre.isEmpty()) {
                                JOptionPane.showMessageDialog(this, "Numele genului nu poate fi gol!");
                                return; // Exit the method to prevent further processing
                            }
                            genres.add(genre);
                        }
                        List<Rating> ratings = new ArrayList<>();
                        String[] ratingsDetails = ratingsText.split("\n");
                        for (String rating : ratingsDetails) {
                            String[] ratingDetails = rating.trim().split(" - ");
                            if (ratingDetails.length != 2) {
                                // Inform the user about the improperly formatted rating
                                JOptionPane.showMessageDialog(this, "The rating '" + rating + "' is not formatted correctly. Please use 'Rating - Comment'.");
                                return; // Exit the method to prevent further processing
                            }
                            String ratingString = ratingDetails[0].trim();
                            String comment = ratingDetails[1].trim();
                            if (ratingString.isEmpty() || comment.isEmpty()) {
                                JOptionPane.showMessageDialog(this, "Rating and comment cannot be empty.");
                                return; // Exit the method to prevent further processing
                            }
                            try {
                                double ratingDouble = Double.parseDouble(ratingString);
                                ratings.add(new Rating(user.getUserName(), (int) ratingDouble, comment));
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(this, "Rating-ul trebuie sa fie un numar!");
                                return; // Exit the method to prevent further processing
                            }
                        }
                        // verific daca titlul este gol
                        if (title.isEmpty()) {
                            JOptionPane.showMessageDialog(this, "Titlul nu poate fi gol!");
                        } else {
                            // modific productia
                            Staff staff = (Staff) user;
                            // verific ce tip de productie este
                            if (selectedproduction instanceof Movie) {
                                int releaseYear = Integer.parseInt(releaseYearField.getText());
                                Movie movie = new Movie(title, directors, actors, genres, ratings, description, runtimeField.getText(), releaseYear);
                                System.out.println("MOVIE :");
                                movie.displayInfo();
                                if (Objects.equals(movie.getTitlu(), selectedproduction.getTitlu())) {
                                    staff.updateProduction(movie);
                                    //afisez lista de productii
                                    productionListModel.clear();
                                    List<Production> initialProductions = imdb.getProductionList();
                                    for (Production production1 : initialProductions) {
                                        productionListModel.addElement(production1);
                                    }
                                } else {
                                    staff.removeProductionSystem(selectedproduction.getTitlu());
                                    staff.addProductionSystem(movie);
                                    //afisez lista de productii
                                    productionListModel.clear();
                                    List<Production> initialProductions = imdb.getProductionList();
                                    for (Production production1 : initialProductions) {
                                        productionListModel.addElement(production1);
                                    }
                                }
                            } else {
                                int releaseYear = Integer.parseInt(releaseYearField.getText());
                                int numarSezoane = Integer.parseInt(numarSezoaneField.getText());
                                Series series = new Series(title, directors, actors, genres, ratings, description, releaseYear, numarSezoane);
                                series.setMapSerial(mapSerial);
                                System.out.println("SERIES :");
                                series.displayInfo();
                                if (Objects.equals(series.getTitlu(), selectedproduction.getTitlu())) {
                                    staff.updateProduction(series);
                                    Series selectedseries = (Series) selectedproduction;
                                    selectedseries.setMapSerial(mapSerial);
                                    //afisez lista de productii
//                                productionListModel.clear();
//                                List<Production> initialProductions = imdb.getProductionList();
//                                for (Production production1 : initialProductions) {
//                                    productionListModel.addElement(production1);
//                                }
                                } else {
                                    staff.removeProductionSystem(selectedproduction.getTitlu());
                                    staff.addProductionSystem(series);
//                                //afisez lista de productii
//                                productionListModel.clear();
//                                List<Production> initialProductions = imdb.getProductionList();
//                                for (Production production1 : initialProductions) {
//                                    productionListModel.addElement(production1);
//                                }
                                }
                            }
                            // inchid fereastra de modificare
                            modifyDialog.dispose();
                            // inchid fereastra de detalii
                            detailsDialog.dispose();
                            // deschid fereastra de detalii cu productia modificata
                        }
                    });
                    // adauga buton doar pentru stergere de productie
                    if (user.getUserType() == AccountType.ADMIN || user.getUserType() == AccountType.CONTRIBUTOR) {
                        JButton deleteButton = new JButton("Sterge Productie");
                        deleteButton.addActionListener(e2 -> {
                            // sterge productia
                            Staff staff = (Staff) user;
                            staff.removeProductionSystem(selectedproduction.getTitlu());
                            // inchid fereastra de modificare
                            modifyDialog.dispose();
                            // inchid fereastra de detalii
                            detailsDialog.dispose();
                        });
                        buttonPanel2.add(deleteButton);
                    }
                    modifyDialog.add(buttonPanel2, BorderLayout.SOUTH);
                    // Show the dialog
                    modifyDialog.setVisible(true);

                });
                buttonPanel.add(modifyButton);
            }
        }
//        detailsDialog.add(buttonPanel, BorderLayout.SOUTH);
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, ratingScrollPane, buttonPanel);
//        splitPane.setResizeWeight(0.5);
        detailsDialog.add(splitPane, BorderLayout.SOUTH);
        detailsDialog.setVisible(true);
        detailsDialog.revalidate();
        detailsDialog.repaint();
    }

    private void checkifabuttonP(JButton addOrRemoveButton, Production production, User user) {
        if (user.getUserType() == AccountType.REGULAR || user.getUserType() == AccountType.CONTRIBUTOR || user.getUserType() == AccountType.ADMIN) {
            if (user.getFavoriteProductions().contains(production)) {
                addOrRemoveButton.setText("Sterge din Favorite");
                addOrRemoveButton.addActionListener(e -> {
                    // sterge productia din lista de productii a utilizatorului curent
                    user.remove_favorite_production(production);
                });
            } else {
                addOrRemoveButton.setText("Adauga la Favorite");
                addOrRemoveButton.addActionListener(e -> {
                    // adauga productia in lista de productii a utilizatorului curent
                    user.add_favorite_production(production);
                });
            }
        }
    }


    //    private JTable productionsTable;
//    private ProductionTableModel tableModel;
//    private String userAccountType;
//
//    public MainPage(String userAccountType) {
//        this.userAccountType = userAccountType;
//        initUI();
//    }
//
//    private void initUI() {
//        setTitle("Pagina Principală - IMDB");
//        setSize(800, 600);
//        setLocationRelativeTo(null);
//        setDefaultCloseOperation(EXIT_ON_CLOSE);
//
//        // Setează layout-ul principal
//        setLayout(new BorderLayout());
//
//        // Adaugă bara de meniu
//        setJMenuBar(createMenuBar());
//
//        // Adaugă panoul cu filtre și conținutul central
////        add(createFilterPanel(), BorderLayout.WEST);
//        add(createContentPanel(), BorderLayout.CENTER);
//    }
//
//
//    private JMenuBar createMenuBar() {
//        JMenuBar menuBar = new JMenuBar();
//
//        // Meniu File
//        JMenu fileMenu = new JMenu("Get Out");
//        JMenuItem logoutItem = new JMenuItem("Logout");
//        logoutItem.addActionListener((event) -> {
//            // Logică de delogare
//            System.exit(0); // sau arată din nou fereastra de login
//        });
//        fileMenu.add(logoutItem);
//
//        // Meniu Navigare
//        JMenu navigateMenu = new JMenu("Navigate");
//        JMenuItem actorsPageItem = new JMenuItem("Actors Page");
//        actorsPageItem.addActionListener((event) -> {
//            // Logică pentru a deschide pagina actorilor
//        });
//        navigateMenu.add(actorsPageItem);
//
//        menuBar.add(fileMenu);
//        menuBar.add(navigateMenu);
//        // Adaugă alte meniuri după necesitate
//
//        return menuBar;
//    }
//
//
//    private JPanel createFilterPanel() {
//        JPanel filterPanel = new JPanel(new GridBagLayout());
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.gridwidth = GridBagConstraints.REMAINDER;  // Componentele se termină la sfârșitul rândului
//        gbc.anchor = GridBagConstraints.CENTER;  // Ancorează componentele în centrul spațiului lor
//        gbc.insets = new Insets(5, 0, 5, 0);  // Adaugă un spațiu vertical între componente
//        // Adaugă filtre (gen, rating etc.)
//        filterPanel.add(new JLabel("Filtru dupa Gen:"), gbc);
//        JComboBox<Genre> genreComboBox = new JComboBox<>(Genre.values());
////        genreComboBox.setMaximumSize(new Dimension(150, 20));
//        filterPanel.add(genreComboBox, gbc);
//        filterPanel.add(new JLabel("Filtru dupa Regizor:"), gbc);
//        JComboBox<String> directorComboBox = new JComboBox<>(new String[]{"Orice Regizor", "Quentin Tarantino", "Nolan", "Spielberg", "Scorsese"});
//        filterPanel.add(directorComboBox, gbc);
////        filterPanel.add(Box.createRigidArea(new Dimension(0, 5)));
//        JTextField ratingField = new JTextField(5);
//        ratingField.setMaximumSize(new Dimension(50, 20));
//        filterPanel.add(new JLabel("Rating minim:"), gbc);
//        filterPanel.add(ratingField, gbc);
//        filterPanel.add(Box.createRigidArea(new Dimension(0, 5)));
//        JButton applyFiltersButton = new JButton("Aplică Filtrele");
//        filterPanel.add(applyFiltersButton, gbc);
//        JButton resetFiltersButton = new JButton("Resetează Filtrele");
//        filterPanel.add(resetFiltersButton, gbc);
//
//        // Adaugă listener pentru butonul de resetare
//        resetFiltersButton.addActionListener(e -> {
//            try {
//                tableModel = new ProductionTableModel(imdb.getProductionList());
//                // ... additional logic if necessary
//                productionsTable.setModel(tableModel);
//                // reseteaza casutele de filtrare
//                genreComboBox.setSelectedIndex(0);
//                directorComboBox.setSelectedIndex(0);
//                ratingField.setText("");
//                productionsTable.revalidate();
//                productionsTable.repaint();
//            } catch (Exception ex) {
//                ex.printStackTrace(); // Log the exception to the console
//            }
//        });
//
//        // Adaugă listener pentru butonul de filtrare
//        applyFiltersButton.addActionListener(e -> {
//            List<Production> filteredProductions = imdb.getProductionList();
//            System.out.println(filteredProductions.size());
//            // Aplică filtrele
//            Genre selectedGenre = (Genre) genreComboBox.getSelectedItem();
//            if (selectedGenre != null) {
//                filteredProductions = imdb.filterByGenre(selectedGenre.toString(), filteredProductions);
//            }
//            String selectedDirector = (String) directorComboBox.getSelectedItem();
//            if (selectedDirector != null && !selectedDirector.equals("Orice Regizor")) {
//                filteredProductions = imdb.filterByDirector(selectedDirector, filteredProductions);
//            }
//            String ratingText = ratingField.getText();
//            if (!ratingText.isEmpty()) {
//                try {
//                    double rating = Double.parseDouble(ratingText);
//                    filteredProductions = imdb.filterByRating(rating, filteredProductions);
//                } catch (NumberFormatException ex) {
//                    JOptionPane.showMessageDialog(this, "Rating-ul trebuie să fie un număr!");
//                }
//            }
//            // Actualizează tabelul
//            tableModel.setProductions(filteredProductions);
//            tableModel.fireTableDataChanged();
//            tableModel.fireTableStructureChanged();
//        });
//        filterPanel.add(Box.createVerticalGlue()); // Adaugă un spațiu gol pentru a poziționa butonul în partea de jos
//        return filterPanel;
//    }
//
//
//    private JPanel createContentPanel() {
//        JPanel contentPanel = new JPanel(new BorderLayout());
//        contentPanel.add(new JLabel("Conținut Recomandat"), BorderLayout.NORTH);
//
//        // Inițializează tabelul cu producții
//        resetTableModel();
//        productionsTable = new JTable(tableModel);
//        contentPanel.add(new JScrollPane(productionsTable), BorderLayout.CENTER);
//
//        // Adauga zona de filtrare
//        contentPanel.add(createFilterPanel(), BorderLayout.EAST);
//
//        // Listener pentru dublu-clic pe tabel
//        productionsTable.addMouseListener(new MouseAdapter() {
//            public void mouseClicked(MouseEvent e) {
//                if (e.getClickCount() == 2) {
//                    int selectedRow = productionsTable.getSelectedRow();
//                    if (selectedRow >= 0) {
//                        Production selectedProduction = tableModel.getProductionAt(selectedRow);
//                        openProductionDetails(selectedProduction);
//                    }
//                }
//            }
//        });
//
//        return contentPanel;
//    }
//
//    private void resetTableModel() {
//        tableModel = new ProductionTableModel(imdb.getProductionList());
//    }
//

}
