import java.util.ArrayList;
import java.util.List;

public class RequestsHolder {

        private static List<Request> listaCereri = new ArrayList<>();

        private RequestsHolder() {
            // Constructor privat pentru a preveni instantierea clasei
        }

        public static List<Request> getListaCereri() {
            return listaCereri;
        }

        public static void adaugaCerere(Request cerere) {
            listaCereri.add(cerere);
        }

        public static void stergeCerere(Request cerere) {
            listaCereri.remove(cerere);
        }

        // Alte metode pentru gestionarea listei de cereri, dacă este necesar
        // ...
}
