package com.dbw.spring_boot.controller;

import com.dbw.spring_boot.model.ReportKunde;
import com.dbw.spring_boot.model.ReportMitarbeiter;
import com.dbw.spring_boot.model.ReportMitarbeiterStatus; // NEU
import com.dbw.spring_boot.model.ReportProdukt;
import com.dbw.spring_boot.repositories.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ReportController {

    @Autowired
    private ReportRepository repository;

    @GetMapping("/report/kunde/summe-anzahl-bestellungen")
    public ResponseEntity<List<ReportKunde>> getKundenReport() {
        return ResponseEntity.ok(repository.getKundenReport());
    }

    @GetMapping("/report/produkt/verkaufszahlen")
    public ResponseEntity<List<ReportProdukt>> getProduktReport() {
        return ResponseEntity.ok(repository.getProduktReport());
    }

    @GetMapping("/report/mitarbeiter/uebersicht")
    public ResponseEntity<List<ReportMitarbeiter>> getMitarbeiterReport() {
        return ResponseEntity.ok(repository.getMitarbeiterReport());
    }

    @GetMapping("/report/mitarbeiter/bestellstatus-uebersicht")
    public ResponseEntity<List<ReportMitarbeiterStatus>> getMitarbeiterStatusReport() {
        return ResponseEntity.ok(repository.getMitarbeiterStatusReport());
    }
}