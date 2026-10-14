package com.dbw.spring_boot.model;

import java.util.List;

public class Bestellung {

    private Long bestellungId;
    private Long kundeId;
    private Long personalnr;
    private String datum;
    private String status;
    private List<Bestellposition> positionen;

    public Bestellung() {}

    public Bestellung(Long bestellungId, Long kundeId, Long personalnr, String datum, String status) {
        this.bestellungId = bestellungId;
        this.kundeId = kundeId;
        this.personalnr = personalnr;
        this.datum = datum;
        this.status = status;
    }

    public Long getBestellungId() { return bestellungId; }
    public void setBestellungId(Long bestellungId) { this.bestellungId = bestellungId; }

    public Long getKundeId() { return kundeId; }
    public void setKundeId(Long kundeId) { this.kundeId = kundeId; }

    public Long getPersonalnr() { return personalnr; }
    public void setPersonalnr(Long personalnr) { this.personalnr = personalnr; }

    public String getDatum() { return datum; }
    public void setDatum(String datum) { this.datum = datum; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<Bestellposition> getPositionen() { return positionen; }
    public void setPositionen(List<Bestellposition> positionen) { this.positionen = positionen; }
}