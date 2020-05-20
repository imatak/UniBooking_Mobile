package unipu.hr.unibooking;

public class Rezervacija {
    private String EmailUsera;
    private String UserTekst;
    private String Datum;
    private String Razlog;
    private String Termin;
    private String ID;
    private String Status;
    private String Fakultet;
    private String TimeStamp;
    private String Komentar;
    private String Uredio;

    public Rezervacija() {
    }

    public String getFakultet() { return Fakultet; }

    public void setFakultet(String fakultet) { Fakultet = fakultet; }

    public String getEmailUsera() {
        return EmailUsera;
    }

    public void setEmailUsera(String emailUsera) {
        EmailUsera = emailUsera;
    }

    public String getUserTekst() {
        return UserTekst;
    }

    public void setUserTekst(String userTekst) {
        UserTekst = userTekst;
    }

    public String getDatum() {
        return Datum;
    }

    public void setDatum(String datum) {
        Datum = datum;
    }

    public String getRazlog() {
        return Razlog;
    }

    public void setRazlog(String razlog) {
        Razlog = razlog;
    }

    public String getTermin() {
        return Termin;
    }

    public void setTermin(String termin) {
        Termin = termin;
    }

    public String getID() { return ID; }

    public void setID(String ID) { this.ID = ID; }

    public String getStatus() { return Status; }

    public void setStatus(String status) { Status = status; }

    public String getTimeStamp() { return TimeStamp; }

    public void setTimeStamp(String timeStamp) { TimeStamp = timeStamp; }

    public String getKomentar() { return Komentar; }

    public void setKomentar(String komentar) { Komentar = komentar; }

    public String getUredio() { return Uredio; }

    public void setUredio(String uredio) { Uredio = uredio; }
}
