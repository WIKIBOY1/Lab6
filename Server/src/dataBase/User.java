package dataBase;

public class User {
    public final String username;
    public final String password;
    public boolean authFlag = false;

    public User(String username, String password, boolean authFlag) {
        this.username = username;
        this.password = password;
        this.authFlag = authFlag;
    }

    public String getPassword() {
        return password;
    }
    public boolean getAuth(){
        return authFlag;
    }

    public String getUsername() {
        return username;
    }
}
