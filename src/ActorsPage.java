import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.List;

public class ActorsPage extends JFrame {
    private JList<String> actorList;
    private IMDB imdb = IMDB.getInstance(); // O instanță a clasei IMDB pentru a accesa datele
    private String userAccountType;

    public ActorsPage(String userAccountType) {
        setTitle("Actori");
        setSize(400, 600);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setLayout(new BorderLayout());
        userAccountType = userAccountType;

        initializeActorList();
    }

    private void initializeActorList() {
        List<String> actorNames = getSortedActorNames();  // Obține și sortează numele actorilor
        actorList = new JList<>(actorNames.toArray(new String[0]));
        actorList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(actorList);
        add(scrollPane, BorderLayout.CENTER);

        actorList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int index = actorList.locationToIndex(evt.getPoint());
                    if (index >= 0) {
                        String selectedActor = actorList.getModel().getElementAt(index);
                        openActorDetails(selectedActor);
                    }
                }
            }
        });
    }

    private List<String> getSortedActorNames() {
        // Implementează logica pentru a obține și sorta numele actorilor
        List<Actor> actors = imdb.getActorList();
        List<String> actorNames = new java.util.ArrayList<>(actors.stream().map(Actor::getName).toList());
        Collections.sort(actorNames);
        return actorNames;
    }

    private void openActorDetails(String actorName) {
        // Implementează logica pentru a deschide detalii despre actorul selectat
        JOptionPane.showMessageDialog(this, "Deschide detaliile pentru: " + actorName);
        JDialog actorDetailsDialog = new JDialog(this, "Detalii actor", true);
        actorDetailsDialog.setSize(400, 600);
        actorDetailsDialog.setLocationRelativeTo(this);
        actorDetailsDialog.setVisible(true);
        actorDetailsDialog.setLayout(new BorderLayout());
        // butonul de inchidere
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> actorDetailsDialog.dispose());
        actorDetailsDialog.add(closeButton, BorderLayout.PAGE_END);
    }
}
