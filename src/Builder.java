import java.time.LocalDateTime;

public class Builder {
    private Credentials userCredentials;
    private String userNume;
    private int userAge;
    private LocalDateTime userBirthday;
    public Builder setCredentials(Credentials c) {
        this.userCredentials = c;
        return this;
    }
    public Builder setDOB(LocalDateTime DOB){
        this.userBirthday = DOB;
        return this;
    }
    public Builder setAge(int age){
        this.userAge = age;
        return this;
    }
    public Builder setUsername(String username) {
        this.userNume = username;
        return this;
    }
//    public User.Information build() {
//        return new User.Information(this);
//    }
}
