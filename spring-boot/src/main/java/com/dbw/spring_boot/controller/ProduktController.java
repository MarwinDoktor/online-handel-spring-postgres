package com.dbw.spring_boot.controller;

import com.dbw.spring_boot.model.Produkt;
import com.dbw.spring_boot.repositories.ProduktRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProduktController {

    @Autowired
    private ProduktRepository repository;

    @GetMapping("/produkte")
    public ResponseEntity<?> getProdukte(@RequestParam(required = false) String sku) {
        if (sku != null) {
            Produkt p = repository.findBySku(sku);
            if (p != null) return ResponseEntity.ok(p);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produkt mit SKU " + sku + " nicht gefunden");
        } else {
            return ResponseEntity.ok(repository.findAll());
        }
    }

    @PostMapping("/produkte")
    public ResponseEntity<String> createProdukt(@RequestBody Produkt p) {
        try {
            repository.save(p);
            return ResponseEntity.status(HttpStatus.CREATED).body("Produkt erfolgreich erstellt.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fehler beim Erstellen: " + e.getMessage());
        }
    }

    @PutMapping("/produkte")
    public ResponseEntity<String> updateProdukt(@RequestParam String sku, @RequestBody Produkt p) {
        int rows = repository.updateLagerbestand(sku, p.getLagerbestand());

        if (rows > 0) {
            return ResponseEntity.ok("Lagerbestand aktualisiert.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produkt nicht gefunden.");
        }
    }

    @DeleteMapping("/produkte")
    public ResponseEntity<String> deleteProdukt(@RequestParam String sku) {
        int rows = repository.deleteBySku(sku);
        if (rows > 0) {
            return ResponseEntity.ok("Produkt gelöscht.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produkt nicht gefunden.");
        }
    }
}