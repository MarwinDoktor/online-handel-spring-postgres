package com.dbw.spring_boot.model;

public class ReportMitarbeiter {

    private Long personalNr;
    private Integer anzahlVerwalteterBestellungen;
    private Integer anzahlAngelegterProdukte;

    public ReportMitarbeiter() {}

    public ReportMitarbeiter(Long personalNr, Integer anzahlVerwalteterBestellungen, Integer anzahlAngelegterProdukte) {
        this.personalNr = personalNr;
        this.anzahlVerwalteterBestellungen = anzahlVerwalteterBestellungen;
        this.anzahlAngelegterProdukte = anzahlAngelegterProdukte;
    }

    public Long getPersonalNr() { return personalNr; }
    public void setPersonalNr(Long personalNr) { this.personalNr = personalNr; }

    public Integer getAnzahlVerwalteterBestellungen() { return anzahlVerwalteterBestellungen; }
    public void setAnzahlVerwalteterBestellungen(Integer anzahlVerwalteterBestellungen) { this.anzahlVerwalteterBestellungen = anzahlVerwalteterBestellungen; }

    public Integer getAnzahlAngelegterProdukte() { return anzahlAngelegterProdukte; }
    public void setAnzahlAngelegterProdukte(Integer anzahlAngelegterProdukte) { this.anzahlAngelegterProdukte = anzahlAngelegterProdukte; }
}