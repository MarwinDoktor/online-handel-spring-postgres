package com.dbw.spring_boot.repositories;

import com.dbw.spring_boot.model.Adresse;
import com.dbw.spring_boot.model.Kunde;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class KundeRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private List<Kunde> queryKunden(String whereClause, Object... params) {
        String sql = "SELECT k.kunde_id, k.email, k.vorname, k.nachname, k.passwort, " +
                "       kha.typ, " +
                "       a.adresse_id, a.aktiv, a.strasse, a.hausnummer, a.plz, a.ort, a.land " +
                "FROM kunde k " +
                "LEFT JOIN kunde_hat_adressen kha ON k.kunde_id = kha.kunde_id " +
                "LEFT JOIN adresse a ON kha.adresse_id = a.adresse_id " +
                whereClause;

        Map<Long, Kunde> kundenMap = new HashMap<>();

        jdbcTemplate.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                Long kId = rs.getLong("kunde_id");

                Kunde k = kundenMap.get(kId);
                if (k == null) {
                    k = new Kunde(
                            kId,
                            rs.getString("email"),
                            rs.getString("vorname"),
                            rs.getString("nachname"),
                            rs.getString("passwort")
                    );
                    kundenMap.put(kId, k);
                }

                Long aId = rs.getLong("adresse_id");
                if (!rs.wasNull()) {
                    Adresse a = new Adresse(
                            aId,
                            rs.getBoolean("aktiv"),
                            rs.getString("strasse"),
                            rs.getString("hausnummer"),
                            rs.getLong("plz"),
                            rs.getString("ort"),
                            rs.getString("land")
                    );
                    String typ = rs.getString("typ");
                    k.addAdresse(a, typ);
                }
            }
        }, params);

        return new ArrayList<>(kundenMap.values());
    }

    public List<Kunde> findAll() {
        return queryKunden("");
    }

    public Kunde findById(Long id) {
        List<Kunde> result = queryKunden("WHERE k.kunde_id = ?", id);
        return result.isEmpty() ? null : result.get(0);
    }

    public Kunde findByEmail(String email) {
        List<Kunde> result = queryKunden("WHERE k.email = ?", email);
        return result.isEmpty() ? null : result.get(0);
    }

    public int save(Kunde k) {
        String sql = "INSERT INTO kunde (email, vorname, nachname, passwort) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, k.getEmail(), k.getVorname(), k.getNachname(), k.getPasswort());
    }

    public int update(Long id, Kunde k) {
        String sql = "UPDATE kunde SET email=?, vorname=?, nachname=?, passwort=? WHERE kunde_id=?";
        return jdbcTemplate.update(sql, k.getEmail(), k.getVorname(), k.getNachname(), k.getPasswort(), id);
    }

    public int deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM kunde_hat_adressen WHERE kunde_id = ?", id);
        return jdbcTemplate.update("DELETE FROM kunde WHERE kunde_id = ?", id);
    }
}