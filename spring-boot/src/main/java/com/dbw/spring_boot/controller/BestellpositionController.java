package com.dbw.spring_boot.controller;

import com.dbw.spring_boot.model.Bestellposition;
import com.dbw.spring_boot.repositories.BestellpositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class BestellpositionController {

    @Autowired
    private BestellpositionRepository repository;

    @GetMapping("/bestellpositionen")
    public ResponseEntity<?> getPositionen(@RequestParam(required = false) Long id) {
        if (id != null) {
            Bestellposition bp = repository.findById(id);
            if (bp != null) return ResponseEntity.ok(bp);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bestellposition nicht gefunden");
        } else {
            return ResponseEntity.ok(repository.findAll());
        }
    }

    @PostMapping("/bestellpositionen")
    public ResponseEntity<String> createPosition(@RequestBody Bestellposition bp) {
        try {
            repository.save(bp);
            return ResponseEntity.status(HttpStatus.CREATED).body("Bestellposition erfolgreich erstellt.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fehler: " + e.getMessage());
        }
    }

    @DeleteMapping("/bestellpositionen")
    public ResponseEntity<String> deletePosition(@RequestParam Long id) {
        if (repository.deleteById(id) > 0) return ResponseEntity.ok("Bestellposition gelöscht.");
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bestellposition nicht gefunden.");
    }
}
