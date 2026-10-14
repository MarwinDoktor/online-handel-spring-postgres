package com.dbw.spring_boot.repositories;

import com.dbw.spring_boot.model.Bestellposition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class BestellpositionRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final RowMapper<Bestellposition> ROW_MAPPER = new RowMapper<Bestellposition>() {
        @Override
        public Bestellposition mapRow(ResultSet rs, int rowNum) throws SQLException {
            BigDecimal einzelpreis = rs.getBigDecimal("preis");
            int menge = rs.getInt("menge");

            BigDecimal berechneterGesamtpreis = einzelpreis.multiply(BigDecimal.valueOf(menge));

            return new Bestellposition(
                    rs.getLong("position_id"),
                    rs.getLong("bestellung_id"),
                    rs.getString("sku"),
                    menge,
                    berechneterGesamtpreis
            );
        }
    };

    public List<Bestellposition> findAll() {
        String sql = "SELECT bp.position_id, bp.bestellung_id, bp.sku, bp.menge, p.preis " +
                "FROM bestellposition bp " +
                "JOIN produkt p ON bp.sku = p.sku";
        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    public Bestellposition findById(Long id) {
        String sql = "SELECT bp.position_id, bp.bestellung_id, bp.sku, bp.menge, p.preis " +
                "FROM bestellposition bp " +
                "JOIN produkt p ON bp.sku = p.sku " +
                "WHERE bp.position_id = ?";
        List<Bestellposition> result = jdbcTemplate.query(sql, ROW_MAPPER, id);
        return result.isEmpty() ? null : result.get(0);
    }

    public int save(Bestellposition bp) {
        String sql = "INSERT INTO bestellposition (bestellung_id, sku, menge) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql, bp.getBestellungId(), bp.getProduktSku(), bp.getMenge());
    }

    public int deleteById(Long id) {
        return jdbcTemplate.update("DELETE FROM bestellposition WHERE position_id = ?", id);
    }
}
