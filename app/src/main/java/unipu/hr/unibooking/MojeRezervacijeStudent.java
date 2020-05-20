package unipu.hr.unibooking;

import java.io.Serializable;

public class MojeRezervacijeStudent implements Serializable {
    private String Datum;
    private String Vrijeme;
    private String Status;
    private String Razlog;
    private String ID;
    private String Email;
    private String Fakultet;
    private String Komentar;
    private String Uredio;

    public MojeRezervacijeStudent(String datum, String vrijeme, String status, String razlog, String ID, String email, String fakultet, String komentar, String uredio) {
        this.Datum = datum;
        this.Vrijeme = vrijeme;
        this.Status = status;
        this.Razlog = razlog;
        this.ID = ID;
        this.Email= email;
        this.Fakultet = fakultet;
        this.Komentar = komentar;
        this.Uredio = uredio;
    }

    public String getFakultet() { return Fakultet; }

    public void setFakultet(String fakultet) { Fakultet = fakultet; }

    public String getEmail() { return Email; }

    public void setEmail(String email) { Email = email; }

    public String getDatum() {
        return Datum;
    }

    public void setDatum(String datum) {
        this.Datum = datum;
    }

    public String getVrijeme() {
        return Vrijeme;
    }

    public void setVrijeme(String vrijeme) {
        this.Vrijeme = vrijeme;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        this.Status = status;
    }

    public String getRazlog() {
        return Razlog;
    }

    public void setRazlog(String razlog) {
        this.Razlog = razlog;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getKomentar() { return Komentar; }

    public void setKomentar(String komentar) { Komentar = komentar; }

    public String getUredio() { return Uredio; }

    public void setUredio(String uredio) { Uredio = uredio; }

}
