package com.dbw.spring_boot.repositories;

import com.dbw.spring_boot.model.Produkt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ProduktRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final RowMapper<Produkt> ROW_MAPPER = new RowMapper<Produkt>() {
        @Override
        public Produkt mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Produkt(
                    rs.getString("sku"),
                    rs.getString("name"),
                    rs.getBigDecimal("preis"),
                    rs.getInt("lagerbestand"),
                    rs.getLong("angelegt_von")
            );
        }
    };

    public List<Produkt> findAll() {
        String sql = "SELECT * FROM produkt";
        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    public Produkt findBySku(String sku) {
        String sql = "SELECT * FROM produkt WHERE sku = ?";
        List<Produkt> result = jdbcTemplate.query(sql, ROW_MAPPER, sku);
        return result.isEmpty() ? null : result.get(0);
    }

    public int save(Produkt p) {
        String sql = "INSERT INTO produkt (sku, name, preis, lagerbestand, angelegt_von) VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, p.getSku(), p.getName(), p.getPreis(), p.getLagerbestand(), p.getAngelegtVon());
    }

    public int updateLagerbestand(String sku, int neuerBestand) {
        String sql = "UPDATE produkt SET lagerbestand = ? WHERE sku = ?";
        return jdbcTemplate.update(sql, neuerBestand, sku);
    }

    public int deleteBySku(String sku) {
        String sql = "DELETE FROM produkt WHERE sku = ?";
        return jdbcTemplate.update(sql, sku);
    }
}