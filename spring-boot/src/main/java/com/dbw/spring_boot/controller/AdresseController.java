package com.dbw.spring_boot.controller;

import com.dbw.spring_boot.model.Adresse;
import com.dbw.spring_boot.repositories.AdresseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AdresseController {

    @Autowired
    private AdresseRepository repository;

    @GetMapping("/adressen")
    public ResponseEntity<?> getAdressen(@RequestParam(required = false) Long id) {
        if (id != null) {
            Adresse a = repository.findById(id);
            if (a != null) {
                return ResponseEntity.ok(a);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Adresse nicht gefunden");
            }
        } else {
            return ResponseEntity.ok(repository.findAll());
        }
    }

    @PostMapping("/adressen")
    public ResponseEntity<String> createAdresse(@RequestBody Adresse a) {
        try {
            repository.save(a);
            return ResponseEntity.status(HttpStatus.CREATED).body("Adresse erfolgreich erstellt.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fehler: " + e.getMessage());
        }
    }

    @PutMapping("/adressen")
    public ResponseEntity<String> updateAdresse(@RequestParam Long id, @RequestBody Adresse a) {
        int rows = repository.update(id, a);
        if (rows > 0) {
            return ResponseEntity.ok("Adresse aktualisiert.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Adresse nicht gefunden.");
        }
    }
}