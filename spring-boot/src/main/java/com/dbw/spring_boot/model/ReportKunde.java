package com.dbw.spring_boot.model;

import java.math.BigDecimal;

public class ReportKunde {

    private Long kundeId;
    private String email;
    private Integer anzahlBestellungen;
    private BigDecimal gesamtsumme;

    public ReportKunde() {}

    public ReportKunde(Long kundeId, String email, Integer anzahlBestellungen, BigDecimal gesamtsumme) {
        this.kundeId = kundeId;
        this.email = email;
        this.anzahlBestellungen = anzahlBestellungen;
        this.gesamtsumme = gesamtsumme;
    }

    public Long getKundeId() { return kundeId; }
    public void setKundeId(Long kundeId) { this.kundeId = kundeId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Integer getAnzahlBestellungen() { return anzahlBestellungen; }
    public void setAnzahlBestellungen(Integer anzahlBestellungen) { this.anzahlBestellungen = anzahlBestellungen; }

    public BigDecimal getGesamtsumme() { return gesamtsumme; }
    public void setGesamtsumme(BigDecimal gesamtsumme) { this.gesamtsumme = gesamtsumme; }
}