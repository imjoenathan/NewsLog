/**
 * Created by mishamalik on 10/10/15.
 */
public class User {
    private String email;
    private String password;
    private String username;

    public User( String email, String password ) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public String getUsername() {
        return username;
    }
    public void setEmail( String email ) {
        this.email = email;
    }

    public void setPassword( String password ) {
        this.password = password;
    }

    public void setUsername( String username ) {
        this.username = username;
    }




}
