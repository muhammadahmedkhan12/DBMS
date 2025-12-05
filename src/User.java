
public class User {
    protected String username;
    protected String password;
    private static int num;
    private int id;
    private String contactno;
    private String email;

    public User(){}
    public User(String username, String password,String email, String contactno){
        this.username = username;
        this.password = password;
        this.id = num++;
        this.email = email;
        this.contactno = contactno;
    }

    public void setPassword(String newpassword){
        this.password = newpassword;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getContactno() {
        return this.contactno;
    }

    public String getEmail() {
        return this.email;
    }
}
