package com.dbw.spring_boot.controller;

import com.dbw.spring_boot.model.Kunde;
import com.dbw.spring_boot.repositories.KundeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class KundeController {

    @Autowired
    private KundeRepository repository;

    @GetMapping("/kunden")
    public ResponseEntity<?> getKunden(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String email) {

        if (id != null) {
            Kunde k = repository.findById(id);
            if (k != null) return ResponseEntity.ok(k);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Kunde mit ID " + id + " nicht gefunden");
        }
        else if (email != null) {
            Kunde k = repository.findByEmail(email);
            if (k != null) return ResponseEntity.ok(k);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Kunde mit Email " + email + " nicht gefunden");
        }
        else {
            return ResponseEntity.ok(repository.findAll());
        }
    }

    @PostMapping("/kunden")
    public ResponseEntity<String> createKunde(@RequestBody Kunde k) {
        try {
            repository.save(k);
            return ResponseEntity.status(HttpStatus.CREATED).body("Kunde erfolgreich erstellt.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fehler: " + e.getMessage());
        }
    }

    @PutMapping("/kunden")
    public ResponseEntity<String> updateKunde(@RequestParam Long id, @RequestBody Kunde k) {
        int rows = repository.update(id, k);
        if (rows > 0) {
            return ResponseEntity.ok("Kunde aktualisiert.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Kunde nicht gefunden.");
        }
    }

    @DeleteMapping("/kunden")
    public ResponseEntity<String> deleteKunde(@RequestParam Long id) {
        if (repository.deleteById(id) > 0) return ResponseEntity.ok("Kunde gelöscht.");
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Kunde nicht gefunden.");
    }
}