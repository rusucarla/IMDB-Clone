import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class MainPage extends JFrame {
    private IMDB imdb = IMDB.getInstance(); // O instanță a clasei IMDB pentru a accesa datele
    private JTable productionsTable;
    private ProductionTableModel tableModel;
    private String userAccountType;

    public MainPage(String userAccountType) {
        this.userAccountType = userAccountType;
        initUI();
    }

    private void initUI() {
        setTitle("Pagina Principală - IMDB");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Setează layout-ul principal
        setLayout(new BorderLayout());

        // Adaugă bara de meniu
        setJMenuBar(createMenuBar());

        // Adaugă panoul cu filtre și conținutul central
        add(createFilterPanel(), BorderLayout.WEST);
        add(createContentPanel(), BorderLayout.CENTER);
    }


    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Meniu File
        JMenu fileMenu = new JMenu("Get Out");
        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.addActionListener((event) -> {
            // Logică de delogare
            System.exit(0); // sau arată din nou fereastra de login
        });
        fileMenu.add(logoutItem);

        // Meniu Navigare
        JMenu navigateMenu = new JMenu("Navigate");
        JMenuItem actorsPageItem = new JMenuItem("Actors Page");
        actorsPageItem.addActionListener((event) -> {
            // Logică pentru a deschide pagina actorilor
            openActorsPage();
        });
        navigateMenu.add(actorsPageItem);

        menuBar.add(fileMenu);
        menuBar.add(navigateMenu);
        // Adaugă alte meniuri după necesitate

        return menuBar;
    }

    private void openActorsPage() {
        ActorsPage actorsPage = new ActorsPage(userAccountType);
        actorsPage.setVisible(true);
    }

    private JPanel createFilterPanel() {
        JPanel filterPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;  // Componentele se termină la sfârșitul rândului
        gbc.anchor = GridBagConstraints.CENTER;  // Ancorează componentele în centrul spațiului lor
        gbc.insets = new Insets(5, 0, 5, 0);  // Adaugă un spațiu vertical între componente
        // Adaugă filtre (gen, rating etc.)
        filterPanel.add(new JLabel("Filtru dupa Gen:"), gbc);
        JComboBox<Genre> genreComboBox = new JComboBox<>(Genre.values());
//        genreComboBox.setMaximumSize(new Dimension(150, 20));
        filterPanel.add(genreComboBox, gbc);
        filterPanel.add(new JLabel("Filtru dupa Regizor:"), gbc);
        JComboBox<String> directorComboBox = new JComboBox<>(new String[]{"Orice Regizor", "Quentin Tarantino", "Nolan", "Spielberg", "Scorsese"});
        filterPanel.add(directorComboBox, gbc);
//        filterPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        JTextField ratingField = new JTextField(5);
        ratingField.setMaximumSize(new Dimension(50, 20));
        filterPanel.add(new JLabel("Rating minim:"), gbc);
        filterPanel.add(ratingField, gbc);
        filterPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        JButton applyFiltersButton = new JButton("Aplică Filtrele");
        filterPanel.add(applyFiltersButton, gbc);

        // Adaugă listener pentru butonul de filtrare
        applyFiltersButton.addActionListener(e -> {
            List<Production> filteredProductions = imdb.getProductionList();
            System.out.println(filteredProductions.size());
        });
        filterPanel.add(Box.createVerticalGlue()); // Adaugă un spațiu gol pentru a poziționa butonul în partea de jos
        return filterPanel;
    }


    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(new JLabel("Conținut Recomandat"), BorderLayout.NORTH);

        // Inițializează tabelul cu producții
        resetTableModel();
        productionsTable = new JTable(tableModel);
        contentPanel.add(new JScrollPane(productionsTable), BorderLayout.CENTER);

        // Listener pentru dublu-clic pe tabel
        productionsTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = productionsTable.getSelectedRow();
                    if (selectedRow >= 0) {
                        Production selectedProduction = tableModel.getProductionAt(selectedRow);
                        openProductionDetails(selectedProduction);
                    }
                }
            }
        });

        return contentPanel;
    }

    private void resetTableModel() {
        tableModel = new ProductionTableModel(imdb.getProductionList());
    }

    private void openProductionDetails(Production production) {
        // Crează și afișează o nouă fereastră sau un dialog cu detaliile producției
        JDialog detailsDialog = new JDialog(this, "Detalii Producție", true);
        detailsDialog.setSize(400, 300);
        detailsDialog.setLayout(new BorderLayout());

        // Formatează textul folosind HTML
        JLabel detailsLabel = new JLabel("<html><b>Titlu:</b> " + production.getTitlu() + "<br><b>Gen:</b> " + production.getGenreList() + "<br><b>Rating:</b> " + production.getNotaFilm() + "</html>");
        JScrollPane scrollPane = new JScrollPane(detailsLabel);  // Folosește JScrollPane pentru a gestiona textul lung
        detailsDialog.add(scrollPane, BorderLayout.CENTER);

        // Adaugă informații despre producție în dialog
//        detailsDialog.add(new JLabel("Titlu: " + production.getTitlu()), BorderLayout.NORTH);
//        detailsDialog.add(new JLabel("Gen: " + production.getGenreList()), BorderLayout.CENTER);
//        detailsDialog.add(new JLabel("Rating: " + production.getNotaFilm()), BorderLayout.SOUTH);

        // Adaugă un buton pentru închidere
        JButton closeButton = new JButton("Inchide");
        closeButton.addActionListener(e -> detailsDialog.dispose());
        detailsDialog.add(closeButton, BorderLayout.PAGE_END);

        // Afișează dialogul
        detailsDialog.setLocationRelativeTo(this);
        detailsDialog.setVisible(true);
    }

}
