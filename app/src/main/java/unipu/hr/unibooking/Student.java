package unipu.hr.unibooking;

public class Student {

    private String UserID;
    private String email;
    private String lozinka;
    private String Fakultet;

    public Student() {
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    public String getFakultet() {
        return Fakultet;
    }

    public void setFakultet(String fakultet) {
        Fakultet = fakultet;
    }

}
