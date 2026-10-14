package com.dbw.spring_boot.repositories;

import com.dbw.spring_boot.model.Adresse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class AdresseRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final RowMapper<Adresse> ROW_MAPPER = new RowMapper<Adresse>() {
        @Override
        public Adresse mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Adresse(
                    rs.getLong("adresse_id"),
                    rs.getBoolean("aktiv"),
                    rs.getString("strasse"),
                    rs.getString("hausnummer"),
                    rs.getLong("plz"),
                    rs.getString("ort"),
                    rs.getString("land")
            );
        }
    };

    public List<Adresse> findAll() {
        return jdbcTemplate.query("SELECT * FROM adresse", ROW_MAPPER);
    }

    public Adresse findById(Long id) {
        String sql = "SELECT * FROM adresse WHERE adresse_id = ?";
        List<Adresse> result = jdbcTemplate.query(sql, ROW_MAPPER, id);
        return result.isEmpty() ? null : result.get(0);
    }

    public int save(Adresse a) {
        String sql = "INSERT INTO adresse (aktiv, strasse, hausnummer, plz, ort, land) VALUES (?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, a.isAktiv(), a.getStrasse(), a.getHausnummer(), a.getPlz(), a.getOrt(), a.getLand());
    }

    public int update(Long id, Adresse a) {
        String sql = "UPDATE adresse SET aktiv=?, strasse=?, hausnummer=?, plz=?, ort=?, land=? WHERE adresse_id=?";
        return jdbcTemplate.update(sql, a.isAktiv(), a.getStrasse(), a.getHausnummer(), a.getPlz(), a.getOrt(), a.getLand(), id);
    }
}