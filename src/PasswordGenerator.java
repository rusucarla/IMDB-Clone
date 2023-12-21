import java.security.SecureRandom;
import java.util.Random;

public class PasswordGenerator {
    private static final String LitereLowercase = "abcdefghijklmnopqrstuvwxyz";
    private static final String LitereUppercase = LitereLowercase.toUpperCase();

    private static final String Cifre = "0123456789";
    private static final String CaractereSpeciale = "!@#$%&*()_+-=[]|,./?><";
    private static final String ElementeTotale = LitereLowercase
            + LitereUppercase + Cifre + CaractereSpeciale;
    private static final SecureRandom secureRandom = new SecureRandom();

    public static String generatePassword() {
        StringBuilder password = new StringBuilder();
        int nrCaractere = new Random().nextInt(13) + 1;
        for (int i = 0; i < nrCaractere; i++) {
            int index = secureRandom.nextInt(ElementeTotale.length());
            password.append(ElementeTotale.charAt(index));
        }
        return password.toString();
    }
}
