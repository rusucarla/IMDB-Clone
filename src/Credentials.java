public class Credentials {
    private String email, password;
    public Credentials(String email, String password) {
        this.email = email;
        this.password = password;
    }
    public Credentials() {
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    // setter
    public void setEmail(String new_email) {
        this.email = new_email;
    }
    public void setPassword(String new_password) {
        this.password = new_password;
    }
}
