package com.dbw.spring_boot.controller;

import com.dbw.spring_boot.model.Bestellung;
import com.dbw.spring_boot.repositories.BestellungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class BestellungController {

    @Autowired
    private BestellungRepository repository;

    @GetMapping("/bestellungen")
    public ResponseEntity<?> getBestellungen(@RequestParam(required = false) Long id) {
        if (id != null) {
            Bestellung b = repository.findById(id);
            if (b != null) return ResponseEntity.ok(b);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bestellung nicht gefunden");
        } else {
            return ResponseEntity.ok(repository.findAll());
        }
    }

    @PostMapping("/bestellungen")
    public ResponseEntity<String> createBestellung(@RequestBody Bestellung b) {
        try {
            repository.save(b);
            return ResponseEntity.status(HttpStatus.CREATED).body("Bestellung erfolgreich erstellt.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fehler: " + e.getMessage());
        }
    }

    @DeleteMapping("/bestellungen")
    public ResponseEntity<String> deleteBestellung(@RequestParam Long id) {
        int rows = repository.deleteById(id);
        if (rows > 0) {
            return ResponseEntity.ok("Bestellung gelöscht.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bestellung nicht gefunden.");
        }
    }
}
