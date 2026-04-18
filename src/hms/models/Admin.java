package hms.models;

public class Admin {

    private int    adminId;
    private String username;
    private String password;

    public Admin(int adminId, String username, String password) {
        this.adminId  = adminId;
        this.username = username;
        this.password = password;
    }

    public int    getAdminId()  { return adminId; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }

    public boolean checkPassword(String input) {
        return this.password.equals(input);
    }
}