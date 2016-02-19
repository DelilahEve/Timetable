package ca.delilaheve.timetable.data;

public class User {

    private int id;
    private String name;
    private String password;
    private String accountType;

    public User(int id, String name, String password, String accountType) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.accountType = accountType;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getAccountType() {
        return accountType;
    }
}
