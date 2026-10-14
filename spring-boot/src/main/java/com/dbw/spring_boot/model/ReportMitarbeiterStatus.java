package com.dbw.spring_boot.model;

public class ReportMitarbeiterStatus {

    private Long personalNr;
    private String status;
    private Integer anzahlBestellungen;

    public ReportMitarbeiterStatus() {}

    public ReportMitarbeiterStatus(Long personalNr, String status, Integer anzahlBestellungen) {
        this.personalNr = personalNr;
        this.status = status;
        this.anzahlBestellungen = anzahlBestellungen;
    }

    public Long getPersonalNr() { return personalNr; }
    public void setPersonalNr(Long personalNr) { this.personalNr = personalNr; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getAnzahlBestellungen() { return anzahlBestellungen; }
    public void setAnzahlBestellungen(Integer anzahlBestellungen) { this.anzahlBestellungen = anzahlBestellungen; }
}