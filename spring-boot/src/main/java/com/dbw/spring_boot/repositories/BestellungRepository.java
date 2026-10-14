package com.dbw.spring_boot.repositories;

import com.dbw.spring_boot.model.Bestellung;
import com.dbw.spring_boot.model.Bestellposition;
import com.dbw.spring_boot.model.Produkt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class BestellungRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ProduktRepository produktRepository;

    private static final RowMapper<Bestellung> ROW_MAPPER = new RowMapper<Bestellung>() {
        @Override
        public Bestellung mapRow(ResultSet rs, int rowNum) throws SQLException {
            String rawDate = rs.getString("datum");
            String formattedDate = rawDate;
            if (rawDate != null && rawDate.contains(" ")) {
                formattedDate = rawDate.replace(" ", "T") + "Z";
            }

            return new Bestellung(
                    rs.getLong("bestellung_id"),
                    rs.getLong("kunde_id"),
                    rs.getLong("mitarbeiterzuweis"),
                    formattedDate,
                    rs.getString("status")
            );
        }
    };

    private void ladeDetails(Bestellung b) {
        String sql = "SELECT bp.position_id, bp.bestellung_id, bp.sku, bp.menge, p.preis " +
                "FROM bestellposition bp " +
                "JOIN produkt p ON bp.sku = p.sku " +
                "WHERE bp.bestellung_id = ?";

        List<Bestellposition> positionen = jdbcTemplate.query(sql, (rs, rowNum) -> {
            Bestellposition bp = new Bestellposition(
                    rs.getLong("position_id"),
                    rs.getLong("bestellung_id"),
                    rs.getString("sku"),
                    rs.getInt("menge"),
                    rs.getBigDecimal("preis").multiply(java.math.BigDecimal.valueOf(rs.getInt("menge")))
            );

            Produkt p = produktRepository.findBySku(bp.getProduktSku());
            bp.setProdukt(p);
            bp.setProduktSku(null);
            return bp;
        }, b.getBestellungId());

        b.setPositionen(positionen);
    }

    public List<Bestellung> findAll() {
        List<Bestellung> bestellungen = jdbcTemplate.query("SELECT * FROM bestellung", ROW_MAPPER);
        for (Bestellung b : bestellungen) {
            ladeDetails(b);
        }
        return bestellungen;
    }

    public Bestellung findById(Long id) {
        String sql = "SELECT * FROM bestellung WHERE bestellung_id = ?";
        List<Bestellung> result = jdbcTemplate.query(sql, ROW_MAPPER, id);
        if (result.isEmpty()) return null;

        Bestellung b = result.get(0);
        ladeDetails(b);
        return b;
    }

    public int save(Bestellung b) {
        String sql = "INSERT INTO bestellung (kunde_id, mitarbeiterzuweis, datum, status) VALUES (?, ?, CAST(? AS TIMESTAMP), ?)";
        return jdbcTemplate.update(sql, b.getKundeId(), b.getPersonalnr(), b.getDatum(), b.getStatus());
    }

    public int deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM bestellposition WHERE bestellung_id = ?", id);
        return jdbcTemplate.update("DELETE FROM bestellung WHERE bestellung_id = ?", id);
    }
}