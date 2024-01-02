import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RequestsHolder {

    private static final List<Request> listaCereri = Collections.synchronizedList(new ArrayList<>());


    private RequestsHolder() {
        // Constructor privat pentru a preveni instantierea clasei
    }

    public static List<Request> getListaCereri() {
        synchronized (listaCereri) {
            return listaCereri;
        }
    }

    public static void adaugaCerere(Request cerere) {
        synchronized (listaCereri) {
            listaCereri.add(cerere);
        }
    }

    public static void stergeCerere(Request cerere) {
        synchronized (listaCereri) {
            System.out.println("Înainte de ștergere: " + listaCereri);
            listaCereri.remove(cerere);
            System.out.println("Cerere stearsa");
            System.out.println("După ștergere: " + listaCereri);
        }
    }

    // Alte metode pentru gestionarea listei de cereri, dacă este necesar
    // ...
}
