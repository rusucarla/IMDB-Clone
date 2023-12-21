public class Rating {
    String usernameRater;
    int notaRating;
    String comentariiRater;

    public Rating(String username, int nota, String comentarii){
        this.comentariiRater = comentarii;
        this.usernameRater = username;
        int rezultat = validareNota(nota);
        this.notaRating = nota;
    }
    public String getUsernameRater(){
        return usernameRater;
    }

    public int getNotaRating() {
        return this.notaRating;
    }
    private int validareNota(int notaRating){
        return Math.max(1, Math.min(10, notaRating));
    }
}
