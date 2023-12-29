import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private IMDB imdb = IMDB.getInstance();
    public LoginFrame() {
        setTitle("Login Page");
        setSize(300, 150);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Panel pentru formularul de login
        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        // Label și text field pentru username
        JLabel userLabel = new JLabel("User");
        userLabel.setBounds(10, 10, 80, 25);
        panel.add(userLabel);
        JTextField userText = new JTextField(20);
        userText.setBounds(100, 10, 160, 25);
        panel.add(userText);

        // Label și password field pentru parola
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(10, 40, 80, 25);
        panel.add(passwordLabel);
        JPasswordField passwordText = new JPasswordField(20);
        passwordText.setBounds(100, 40, 160, 25);
        panel.add(passwordText);

        // Buton de login
        JButton loginButton = new JButton("Login");
        loginButton.setBounds(10, 80, 80, 25);
        panel.add(loginButton);

        // Adaugă un listener pentru butonul de login
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Verifică corectitudinea parolei
                String user = userText.getText();
                String password = new String(passwordText.getPassword());
                if (imdb.checkLogin(user, password)) {
                    // Continuă în funcție de alegerea utilizatorului
                    String[] options = {"GUI", "CLI"};
                    int response = JOptionPane.showOptionDialog(null, "Continuă cu:", "Alegere Mod",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                            null, options, options[0]);

                    if (response == 0) {
                        // Continuă în GUI
                        startGUI(user);
                    } else if (response == 1) {
                        // Continuă în CLI
                        startCLI(user);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Username sau parolă incorectă!");
                }
            }
        });
    }

    // Metodă pentru inițializarea GUI
    private void startGUI(String userType) {
        // Cod pentru inițializarea interfeței grafice
        JOptionPane.showMessageDialog(null, "Bun venit în GUI!");
        // Ascunde sau închide fereastra de login
        LoginFrame.this.setVisible(false);
        LoginFrame.this.dispose();

        // Creează și afișează fereastra principală
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MainPage mainPage = new MainPage(userType);
                mainPage.setVisible(true);
            }
        });
    }

    // Metodă pentru inițializarea CLI
    private void startCLI(String userType) {
        // Cod pentru inițializarea CLI
        JOptionPane.showMessageDialog(null, "Continuă în CLI!");
    }

    public static void main(String[] args) {
        // Rulează interfața de login
        LoginFrame frame = new LoginFrame();
        frame.setVisible(true);
    }
}

