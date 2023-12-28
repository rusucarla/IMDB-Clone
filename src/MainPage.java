import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MainPage extends JFrame {
    // Constructor
    public MainPage() {
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

        // Adaugă componentele UI
        add(createSearchPanel(), BorderLayout.NORTH);
        add(createFilterPanel(), BorderLayout.WEST);
        add(createContentPanel(), BorderLayout.CENTER);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Meniu File
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener((event) -> System.exit(0));
        fileMenu.add(exitItem);

        menuBar.add(fileMenu);

        // Adaugă alte meniuri după necesitate

        return menuBar;
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel label = new JLabel("Caută film/serial/actor:");
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Caută");
        panel.add(label);
        panel.add(searchField);
        panel.add(searchButton);

        // Adaugă funcționalitatea pentru butonul de căutare

        return panel;
    }

    private JPanel createFilterPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("Filtre"));

        // Adaugă opțiuni de filtrare (gen, rating etc.)
        JCheckBox actionCheckBox = new JCheckBox("Action");
        // Adaugă alte checkbox-uri sau componente pentru filtre

        panel.add(actionCheckBox);
        // Adaugă alte filtre

        return panel;
    }

    private JPanel createContentPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Panel pentru filtre
        JPanel filtersPanel = new JPanel();
        JComboBox<String> genreComboBox = new JComboBox<>(new String[]{"Toate", "Action", "Drama", "Comedy"});
        JTextField ratingField = new JTextField(5);
        JButton filterButton = new JButton("Filtrează");
        filtersPanel.add(new JLabel("Gen:"));
        filtersPanel.add(genreComboBox);
        filtersPanel.add(new JLabel("Rating minim:"));
        filtersPanel.add(ratingField);
        filtersPanel.add(filterButton);

        // Tabel pentru afișarea producțiilor
        String[] columnNames = {"Titlu", "Gen", "Rating"};
        JTable table = new JTable(new DefaultTableModel(columnNames, 0));
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(filtersPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Adaugă funcționalitatea butonului de filtrare
        filterButton.addActionListener(e -> {
            String selectedGenre = (String) genreComboBox.getSelectedItem();
            double minRating = Double.parseDouble(ratingField.getText());
            List<Production> filteredProductions = IMDB.getInstance().getProductionList(); // Începe cu toate producțiile

//            // Aplică filtrul de gen
//            if (selectedGenre != null && !selectedGenre.equals("Toate")) {
//                filteredProductions = IMDB.getInstance().filterByGenre(selectedGenre);
//            }

            // Aplică filtrul de rating
            filteredProductions = IMDB.getInstance().filterByRating(minRating);

            // Actualizează tabelul cu producțiile filtrate
            updateTableWithProductions(table, filteredProductions);
        });

        // Încarcă și afișează toate producțiile la început
        updateTableWithProductions(table, IMDB.getInstance().getProductionList());

        return panel;
    }

    // Metodă pentru actualizarea tabelului cu o listă de producții
    private void updateTableWithProductions(JTable table, List<Production> productions) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Curăță tabelul existent
        for (Production p : productions) {
            model.addRow(new Object[]{p.getTitlu(), p.getGenreList(), p.getNotaFilm()});
        }
    }

    // Metode suplimentare pentru gestionarea evenimentelor și actualizarea UI
    // ...
}
