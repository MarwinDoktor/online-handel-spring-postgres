package com.dbw.spring_boot.repositories;

import com.dbw.spring_boot.model.ReportKunde;
import com.dbw.spring_boot.model.ReportMitarbeiter;
import com.dbw.spring_boot.model.ReportMitarbeiterStatus; // NEU
import com.dbw.spring_boot.model.ReportProdukt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ReportRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final RowMapper<ReportKunde> KUNDE_MAPPER = new RowMapper<ReportKunde>() {
        @Override
        public ReportKunde mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new ReportKunde(
                    rs.getLong("kunde_id"),
                    rs.getString("email"),
                    rs.getInt("anzahl_bestellungen"),
                    rs.getBigDecimal("gesamtsumme")
            );
        }
    };

    private static final RowMapper<ReportProdukt> PRODUKT_MAPPER = new RowMapper<ReportProdukt>() {
        @Override
        public ReportProdukt mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new ReportProdukt(
                    rs.getString("sku"),
                    rs.getString("name"),
                    rs.getInt("gesamt_verkaufte_menge"),
                    rs.getBigDecimal("umsatz"),
                    rs.getInt("anzahl_bestellungen")
            );
        }
    };

    private static final RowMapper<ReportMitarbeiter> MITARBEITER_MAPPER = new RowMapper<ReportMitarbeiter>() {
        @Override
        public ReportMitarbeiter mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new ReportMitarbeiter(
                    rs.getLong("personal_nr"),
                    rs.getInt("anzahl_verwalteter_bestellungen"),
                    rs.getInt("anzahl_angelegter_produkte")
            );
        }
    };

    private static final RowMapper<ReportMitarbeiterStatus> STATUS_MAPPER = new RowMapper<ReportMitarbeiterStatus>() {
        @Override
        public ReportMitarbeiterStatus mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new ReportMitarbeiterStatus(
                    rs.getLong("personal_nr"),
                    rs.getString("status"),
                    rs.getInt("anzahl_bestellungen")
            );
        }
    };

    public List<ReportKunde> getKundenReport() {
        return jdbcTemplate.query("SELECT * FROM v_kunde_summe_anzahl_bestellungen", KUNDE_MAPPER);
    }

    public List<ReportProdukt> getProduktReport() {
        return jdbcTemplate.query("SELECT * FROM v_produkt_verkaufszahlen", PRODUKT_MAPPER);
    }

    public List<ReportMitarbeiter> getMitarbeiterReport() {
        return jdbcTemplate.query("SELECT * FROM v_mitarbeiter_uebersicht", MITARBEITER_MAPPER);
    }

    public List<ReportMitarbeiterStatus> getMitarbeiterStatusReport() {
        return jdbcTemplate.query("SELECT * FROM v_mitarbeiter_bestellstatus_uebersicht", STATUS_MAPPER);
    }
}