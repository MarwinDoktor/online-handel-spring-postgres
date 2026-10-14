package com.dbw.spring_boot.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Bestellposition {

    private Long positionsId;
    private Long bestellungId;
    private String produktSku;
    private Integer menge;
    private BigDecimal gesamtpreis;
    private Produkt produkt;

    public Bestellposition() {}

    public Bestellposition(Long positionsId, Long bestellungId, String produktSku, Integer menge, BigDecimal gesamtpreis) {
        this.positionsId = positionsId;
        this.bestellungId = bestellungId;
        this.produktSku = produktSku;
        this.menge = menge;
        this.gesamtpreis = gesamtpreis;
    }

    public Long getPositionsId() { return positionsId; }
    public void setPositionsId(Long positionsId) { this.positionsId = positionsId; }

    public Long getBestellungId() { return bestellungId; }
    public void setBestellungId(Long bestellungId) { this.bestellungId = bestellungId; }

    public String getProduktSku() { return produktSku; }
    public void setProduktSku(String produktSku) { this.produktSku = produktSku; }

    public Integer getMenge() { return menge; }
    public void setMenge(Integer menge) { this.menge = menge; }

    public BigDecimal getGesamtpreis() { return gesamtpreis; }
    public void setGesamtpreis(BigDecimal gesamtpreis) { this.gesamtpreis = gesamtpreis; }

    public Produkt getProdukt() { return produkt; }
    public void setProdukt(Produkt produkt) { this.produkt = produkt; }
}