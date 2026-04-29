package hms.models;

public class Patient extends Person {

    private String bloodGroup;
    private String address;

    public Patient(int id, String name, int age, String gender,
                   String phone, String email,
                   String bloodGroup, String address) {
        super(id, name, age, gender, phone, email);
        this.bloodGroup = bloodGroup;
        this.address    = address;
    }

    @Override
    public String getRole() { return "Patient"; }

    public String getBloodGroup() { return bloodGroup; }
    public String getAddress()    { return address; }

    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }
    public void setAddress(String address)       { this.address = address; }

    @Override
    public String toString() {
        return super.toString()
            + " | Blood: " + bloodGroup
            + " | Address: " + address;
    }
}