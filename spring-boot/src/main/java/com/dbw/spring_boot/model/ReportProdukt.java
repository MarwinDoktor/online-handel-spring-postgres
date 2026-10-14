package com.dbw.spring_boot.model;

import java.math.BigDecimal;

public class ReportProdukt {

    private String sku;
    private String name;
    private Integer gesamtVerkaufteMenge;
    private BigDecimal umsatz;
    private Integer anzahlBestellungen;

    public ReportProdukt() {}

    public ReportProdukt(String sku, String name, Integer gesamtVerkaufteMenge, BigDecimal umsatz, Integer anzahlBestellungen) {
        this.sku = sku;
        this.name = name;
        this.gesamtVerkaufteMenge = gesamtVerkaufteMenge;
        this.umsatz = umsatz;
        this.anzahlBestellungen = anzahlBestellungen;
    }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getGesamtVerkaufteMenge() { return gesamtVerkaufteMenge; }
    public void setGesamtVerkaufteMenge(Integer gesamtVerkaufteMenge) { this.gesamtVerkaufteMenge = gesamtVerkaufteMenge; }

    public BigDecimal getUmsatz() { return umsatz; }
    public void setUmsatz(BigDecimal umsatz) { this.umsatz = umsatz; }

    public Integer getAnzahlBestellungen() { return anzahlBestellungen; }
    public void setAnzahlBestellungen(Integer anzahlBestellungen) { this.anzahlBestellungen = anzahlBestellungen; }
}