package hms.models;

public abstract class Person {

    private int id;
    private String name;
    private int age;
    private String gender;
    private String phone;
    private String email;

    public Person(int id, String name, int age, String gender, String phone, String email) {
        this.id     = id;
        this.name   = name;
        this.age    = age;
        this.gender = gender;
        this.phone  = phone;
        this.email  = email;
    }

    // Abstract method — every Person subclass must implement this
    public abstract String getRole();

    // Getters
    public int getId()       { return id; }
    public String getName()  { return name; }
    public int getAge()      { return age; }
    public String getGender(){ return gender; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }

    // Setters
    public void setName(String name)   { this.name = name; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return "[" + getRole() + "] ID: " + id + " | Name: " + name + " | Age: " + age;
    }
}