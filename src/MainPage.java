import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

        add(cardPanel, BorderLayout.CENTER);
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
        List<Actor> initialActors = imdb.getActorList();
        for (Actor actor : initialActors) {
            actorListModel.addElement(actor);
        }
        JScrollPane scrollPane = new JScrollPane(actorList);
        actorsPagePanel.add(scrollPane, BorderLayout.CENTER);
        actorList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Actor selectedActor = actorList.getSelectedValue();
                    openActorDetails(selectedActor, username, userAccountType);
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
        if (user.getUserType() == AccountType.REGULAR) {
            JButton add_or_removeButton = new JButton("");
            checkifabutton(add_or_removeButton, selectedActor, user);
            buttonPanel.add(add_or_removeButton);
        }

        // verific daca utilizatorul curent este admin sau contributor
        // pentru a putea modifica actorul
        if (user.getUserType() == AccountType.ADMIN || user.getUserType() == AccountType.CONTRIBUTOR) {
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
                            actorListModel.clear();
                            List<Actor> initialActors = imdb.getActorList();
                            for (Actor actor1 : initialActors) {
                                actorListModel.addElement(actor1);
                            }
                        } else {
                            staff.removeActorSystem(selectedActor.getName());
                            staff.addActorSystem(actor);
                            //afisez lista de actori
                            actorListModel.clear();
                            List<Actor> initialActors = imdb.getActorList();
                            for (Actor actor1 : initialActors) {
                                actorListModel.addElement(actor1);
                            }
                        }
                        // inchid fereastra de modificare
                        modifyDialog.dispose();
                        // inchid fereastra de detalii
                        detailsDialog.dispose();
                        // deschid fereastra de detalii cu actorul modificat
                    }
                });
                buttonPanel2.add(saveButton);
                modifyDialog.add(buttonPanel2, BorderLayout.SOUTH);
                // Show the dialog
                modifyDialog.setVisible(true);
            });
            buttonPanel.add(modifyButton);
        }

        detailsDialog.add(buttonPanel, BorderLayout.SOUTH);
        detailsDialog.setVisible(true);
        detailsDialog.revalidate();
        detailsDialog.repaint();
    }

    private void checkifabutton(JButton addOrRemoveButton, Actor selectedActor, User user) {
        if (user.getUserType() == AccountType.REGULAR) {
            if (user.getFavoriteActors().contains(selectedActor)) {
                addOrRemoveButton.setText("Sterge Actor");
                addOrRemoveButton.addActionListener(e -> {
                    // sterge actorul din lista de actori a utilizatorului curent
                    Regular regular = (Regular) user;
                    regular.remove_favorite_actor(selectedActor);
                });
            } else {
                addOrRemoveButton.setText("Adauga Actor");
                addOrRemoveButton.addActionListener(e -> {
                    // adauga actorul in lista de actori a utilizatorului curent
                    Regular regular = (Regular) user;
                    regular.add_favorite_actor(selectedActor);
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
        JButton resetFiltersButton = new JButton("Resetează Lista");
        filterPanel.add(resetFiltersButton);

        resetFiltersButton.addActionListener(e -> {
            List<Production> initialProductions = imdb.getProductionList();
            productionListModel.clear();
            for (Production production : initialProductions) {
                productionListModel.addElement(production);
            }
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
        productionListModel = new DefaultListModel<>();
        JList<Production> productionList = new JList<>(productionListModel);
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
            System.exit(0); // sau arată din nou fereastra de login
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
        // buton de producții
        JButton productionsPageButton = new JButton("Productions Page");
        productionsPageButton.addActionListener(e -> {
            cardLayout.show(cardPanel, "productions");
        });
        sidebarPanel.add(productionsPageButton);
        return sidebarPanel;
    }

    private void openProductionDetails(Production production, User user) {
        // Crează și afișează o nouă fereastră sau un dialog cu detaliile producției
        JDialog detailsDialog = new JDialog(this, "Detalii Producție", true);
        detailsDialog.setSize(400, 300);
        detailsDialog.setLayout(new BorderLayout());

        // Productia are un titlu, regizori, actori, genuri, rating, descriere
        // Create a panel for the title and description
        JPanel textPanel = new JPanel(new GridLayout(0, 2));
        textPanel.add(new JLabel("Titlu: " + production.getTitlu()));
        // adauga nota filmului
        textPanel.add(new JLabel("Nota: " + production.getNotaFilm()));
        if (production instanceof Series){
            // Add release year to the text panel
            textPanel.add(new JLabel("An Lansare: " + ((Series)production).getReleaseAn()));
            textPanel.add(new JLabel("Numar Sezoane: " + ((Series)production).getNumarSezoane()));
        } else {
            // add runtime and release year to the text panel
            textPanel.add(new JLabel("Durata: " + ((Movie) production).getRunTimp() + " minute"));
            textPanel.add(new JLabel("An Lansare: " + ((Movie) production).getReleaseAn()));
        }
        JTextArea descriptionArea = new JTextArea(5, 20); // 5 rows, 20 columns
        descriptionArea.setText(production.getDescriereFilm());
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setLineWrap(true);
        descriptionArea.setCaretPosition(0);
        descriptionArea.setEditable(false);
        JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);
        textPanel.add(descriptionScrollPane);
        // Create a model and list for the directors
        DefaultListModel<String> directorModel = new DefaultListModel<>();
        for (String director : production.getRegizoriList()) {
            directorModel.addElement(director);
        }
        JList<String> directorList = new JList<>(directorModel);
        directorList.setBorder(BorderFactory.createTitledBorder("Regizori"));
        // Create a model and list for the actors
        DefaultListModel<String> actorModel = new DefaultListModel<>();
        for (String actor : production.getActoriList()) {
            actorModel.addElement(actor);
        }
        JList<String> actorList = new JList<>(actorModel);
        actorList.setBorder(BorderFactory.createTitledBorder("Actori"));
        // Create a model and list for the genres
        DefaultListModel<String> genreModel = new DefaultListModel<>();
        for (String genre : production.getGenreList()) {
            genreModel.addElement(genre);
        }
        JList<String> genreList = new JList<>(genreModel);
        genreList.setBorder(BorderFactory.createTitledBorder("Genuri"));
        // Create a model and list for the ratings
        DefaultListModel<String> ratingModel = new DefaultListModel<>();
        for (Rating rating : production.getRatingList()) {
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
        if (production instanceof Series) {
            Series series = (Series) production;
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
        if (user.getUserType() == AccountType.REGULAR) {
            JButton addOrRemoveButton = new JButton("");
            checkifabuttonP(addOrRemoveButton, production, user);
            buttonPanel.add(addOrRemoveButton);
        }
        // verific daca utilizatorul curent este admin sau contributor
        // pentru a putea modifica productia
        if (user.getUserType() == AccountType.ADMIN || user.getUserType() == AccountType.CONTRIBUTOR) {
            JButton modifyButton = new JButton("Modifica Productie");
            modifyButton.addActionListener(e -> {
                // deschide fereastra de modificare
                JDialog modifyDialog = new JDialog(this, "Modifica Productie", true);
                modifyDialog.setLayout(new BorderLayout());
                modifyDialog.setSize(400, 600);
                modifyDialog.setLocationRelativeTo(this); // Center on parent (this)
                modifyDialog.setModal(true); // Block input to other windows
                // Create a panel for the title and description
                JPanel modifyPanel = new JPanel(new GridLayout(0, 1));
                JTextField titleField = new JTextField(production.getTitlu());
                modifyPanel.add(new JLabel("Titlu: "));
                modifyPanel.add(titleField);
                JTextArea descriptionArea2 = new JTextArea(5, 20); // 5 rows, 20 columns
                descriptionArea2.setText(production.getDescriereFilm());
                descriptionArea2.setWrapStyleWord(true);
                descriptionArea2.setLineWrap(true);
                descriptionArea2.setCaretPosition(0);
                modifyPanel.add(new JLabel("Descriere: "));
                JScrollPane descriptionScrollPane2 = new JScrollPane(descriptionArea2);
                modifyPanel.add(descriptionScrollPane2);
                // Create a text filed for the rating
                JTextField ratingField2 = new JTextField(production.getNotaFilm() + "");
                ratingField2.setMaximumSize(new Dimension(50, 20));
                modifyPanel.add(new JLabel("Rating: "));
                modifyPanel.add(ratingField2);
                // create a text area for the directors
                JTextArea directorsArea = new JTextArea(5, 20); // 5 rows, 20 columns
                StringBuilder directorsTextFill = new StringBuilder();
                for (String director : production.getRegizoriList()) {
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
                for (String actor : production.getActoriList()) {
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
                for (String genre : production.getGenreList()) {
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
                for (Rating rating : production.getRatingList()) {
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
                if (production instanceof Series) {
                    Series series = (Series) production;
                    // Create a model and list for the seasons
                    JTextArea seasonsArea = new JTextArea(5, 20); // 5 rows, 20 columns
                    StringBuilder seasonsTextFill = new StringBuilder();
                    for (String season : series.getMapSerial().keySet()) {
                        seasonsTextFill.append(season).append("\n");
                    }
                    seasonsArea.setText(seasonsTextFill.toString());
                    seasonsArea.setWrapStyleWord(true);
                    seasonsArea.setLineWrap(true);
                    seasonsArea.setCaretPosition(0);
                    seasonsArea.setEditable(true);
                    modifyPanel.add(new JLabel("Sezoane: "));
                    JScrollPane seasonsScrollPane = new JScrollPane(seasonsArea);
                    modifyPanel.add(seasonsScrollPane);
                    // Create a model and list for the episodes
                    JTextArea episodesArea = new JTextArea(5, 20); // 5 rows, 20 columns
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
                    modifyPanel.add(new JLabel("Episoade: "));
                    JScrollPane episodesScrollPane = new JScrollPane(episodesArea);
                    modifyPanel.add(episodesScrollPane);
                    JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, seasonsScrollPane, episodesScrollPane);
                    modifyPanel.add(splitPane);
                }
                // Add components to the dialog without overlapping others
                modifyDialog.add(modifyPanel, BorderLayout.NORTH);
                // Adauga butoane pentru salvare modificari
                JPanel buttonPanel2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                JButton saveButton = new JButton("Salveaza");
                saveButton.addActionListener(e2 -> {
                    // salveaza modificarile
                    String title = titleField.getText();
                    String description = descriptionArea2.getText();
                    String ratingText = ratingField2.getText();
                    String directorsText = directorsArea.getText();
                    String actorsText = actorsArea.getText();
                    String genresText = genresArea.getText();
                    String ratingsText = ratingsArea.getText();
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
//                        if (production instanceof Movie) {
//                            Movie movie = new Movie(title, directors, actors, genres, ratings, description, );
//                            if (Objects.equals(movie.getTitlu(), production.getTitlu())) {
//                                staff.updateProduction(movie);
//                                //afisez lista de productii
//                                productionListModel.clear();
//                                List<Production> initialProductions = imdb.getProductionList();
//                                for (Production production1 : initialProductions) {
//                                    productionListModel.addElement(production1);
//                                }
//                            } else {
//                                staff.removeProductionSystem(production.getTitlu());
//                                staff.addProductionSystem(movie);
//                                //afisez lista de productii
//                                productionListModel.clear();
//                                List<Production> initialProductions = imdb.getProductionList();
//                                for (Production production1 : initialProductions) {
//                                    productionListModel.addElement(production1);
//                                }
//                            }
//                        } else {
//                            Series
//                            if (Objects.equals(series.getTitlu(), production.getTitlu())) {
//                                staff.updateProduction(series);
//                                //afisez lista de productii
//                                productionListModel.clear();
//                                List<Production> initialProductions = imdb.getProductionList();
//                                for (Production production1 : initialProductions) {
//                                    productionListModel.addElement(production1);
//                                }
//                            } else {
//                                staff.removeProductionSystem(production.getTitlu());
//                                staff.addProductionSystem(series);
//                                //afisez lista de productii
//                                productionListModel.clear();
//                                List<Production> initialProductions = imdb.getProductionList();
//                                for (Production production1 : initialProductions) {
//                                    productionListModel.addElement(production1);
//                                }
//                            }
//                        }
                        // inchid fereastra de modificare
                        modifyDialog.dispose();
                        // inchid fereastra de detalii
                        detailsDialog.dispose();
                        // deschid fereastra de detalii cu productia modificata
                    }
                });
                // Show the dialog
                modifyDialog.setVisible(true);

            });
            buttonPanel.add(modifyButton);
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
        if (user.getUserType() == AccountType.REGULAR) {
            if (user.getFavoriteProductions().contains(production)) {
                addOrRemoveButton.setText("Sterge Productie din Favorite");
                addOrRemoveButton.addActionListener(e -> {
                    // sterge productia din lista de productii a utilizatorului curent
                    Regular regular = (Regular) user;
                    regular.remove_favorite_production(production);
                });
            } else {
                addOrRemoveButton.setText("Adauga Productie la Favorite");
                addOrRemoveButton.addActionListener(e -> {
                    // adauga productia in lista de productii a utilizatorului curent
                    Regular regular = (Regular) user;
                    regular.add_favorite_production(production);
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
