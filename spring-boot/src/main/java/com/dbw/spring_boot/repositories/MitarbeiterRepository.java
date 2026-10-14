package com.dbw.spring_boot.repositories;

import com.dbw.spring_boot.model.Mitarbeiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class MitarbeiterRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final RowMapper<Mitarbeiter> ROW_MAPPER = new RowMapper<Mitarbeiter>() {
        @Override
        public Mitarbeiter mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Mitarbeiter(
                    rs.getLong("personal_nr"),
                    rs.getString("vorname"),
                    rs.getString("nachname"),
                    rs.getString("email"),
                    rs.getString("passwort")
            );
        }
    };

    public List<Mitarbeiter> findAll() {
        String sql = "SELECT * FROM mitarbeiter";
        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    public Mitarbeiter findById(Long id) {
        String sql = "SELECT * FROM mitarbeiter WHERE personal_nr = ?";
        List<Mitarbeiter> result = jdbcTemplate.query(sql, ROW_MAPPER, id);
        return result.isEmpty() ? null : result.get(0);
    }

    public int save(Mitarbeiter m) {
        String sql = "INSERT INTO mitarbeiter (vorname, nachname, email, passwort) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, m.getVorname(), m.getNachname(), m.getEmail(), m.getPasswort());
    }

    public int deleteById(Long id) {
        String sql = "DELETE FROM mitarbeiter WHERE personal_nr = ?";
        return jdbcTemplate.update(sql, id);
    }
}