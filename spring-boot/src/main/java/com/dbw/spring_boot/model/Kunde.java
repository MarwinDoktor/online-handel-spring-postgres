package com.dbw.spring_boot.model;

import java.util.ArrayList;
import java.util.List;

public class Kunde {

    private Long kundeId;
    private String email;
    private String vorname;
    private String nachname;
    private String passwort;

    private List<KundenAdresse> adressen = new ArrayList<>();

    public Kunde() {}

    public Kunde(Long kundeId, String email, String vorname, String nachname, String passwort) {
        this.kundeId = kundeId;
        this.email = email;
        this.vorname = vorname;
        this.nachname = nachname;
        this.passwort = passwort;
    }

    public Long getKundeId() {
        return kundeId;
    }

    public void setKundeId(Long kundeId) {
        this.kundeId = kundeId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getPasswort() {
        return passwort;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }

    public List<KundenAdresse> getAdressen() {
        return adressen;
    }

    public void setAdressen(List<KundenAdresse> adressen) {
        this.adressen = adressen;
    }

    public void addAdresse(Adresse adresse, String typ) {
        this.adressen.add(new KundenAdresse(adresse, typ));
    }
}
