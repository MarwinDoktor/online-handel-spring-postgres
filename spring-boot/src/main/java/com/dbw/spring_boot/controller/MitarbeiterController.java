package com.dbw.spring_boot.controller;

import com.dbw.spring_boot.model.Mitarbeiter;
import com.dbw.spring_boot.repositories.MitarbeiterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class MitarbeiterController {

    @Autowired
    private MitarbeiterRepository repository;

    @GetMapping("/mitarbeiter")
    public ResponseEntity<?> getMitarbeiter(@RequestParam(required = false) Long id) {
        if (id != null) {
            Mitarbeiter m = repository.findById(id);
            if (m != null) {
                return ResponseEntity.ok(m);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Mitarbeiter nicht gefunden");
            }
        } else {
            List<Mitarbeiter> alle = repository.findAll();
            return ResponseEntity.ok(alle);
        }
    }

    @PostMapping("/mitarbeiter")
    public ResponseEntity<String> createMitarbeiter(@RequestBody Mitarbeiter m) {
        try {
            repository.save(m);
            return ResponseEntity.status(HttpStatus.CREATED).body("Mitarbeiter erfolgreich erstellt.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fehler beim Erstellen: " + e.getMessage());
        }
    }

    @DeleteMapping("/mitarbeiter")
    public ResponseEntity<String> deleteMitarbeiter(@RequestParam Long id) {
        int rowsAffected = repository.deleteById(id);
        if (rowsAffected > 0) {
            return ResponseEntity.ok("Mitarbeiter gelöscht.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Mitarbeiter mit ID " + id + " nicht gefunden.");
        }
    }
}