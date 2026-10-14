package com.dbw.spring_boot.controller;

import com.dbw.spring_boot.model.Kunde;
import com.dbw.spring_boot.model.Mitarbeiter;
import com.dbw.spring_boot.repositories.KundeRepository;
import com.dbw.spring_boot.repositories.MitarbeiterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;

@RestController
public class LoginController {

    @Autowired
    private MitarbeiterRepository mitarbeiterRepository;

    @Autowired
    private KundeRepository kundeRepository;

    @PostMapping("/login/mitarbeiter")
    public ResponseEntity<Mitarbeiter> loginMitarbeiter(@RequestBody Map<String, Object> payload) {
        Object idObj = payload.get("personalNr");
        String passwortInput = (String) payload.get("passwort");

        Long personalNr = null;
        if (idObj != null) {
            try {
                personalNr = Long.parseLong(idObj.toString());
            } catch (NumberFormatException e) {
                // ID war keine Zahl
            }
        }

        Mitarbeiter m = null;
        if (personalNr != null) {
            m = mitarbeiterRepository.findById(personalNr);
        }

        if (m != null && m.getPasswort() != null && m.getPasswort().equals(passwortInput)) {
            m.setPasswort(null);
            return ResponseEntity.ok(m);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Mitarbeiter());
        }
    }


    @PostMapping("/login/kunde")
    public ResponseEntity<Kunde> loginKunde(@RequestBody Map<String, String> payload) {

        String email = payload.get("email");
        String passwort = payload.get("passwort");

        Kunde k = kundeRepository.findByEmail(email);

        if (k != null && k.getPasswort() != null && k.getPasswort().equals(passwort)) {
            k.setPasswort(null);
            return ResponseEntity.ok(k);
        } else {
            Kunde emptyKunde = new Kunde();
            emptyKunde.setAdressen(new ArrayList<>());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(emptyKunde);
        }
    }
}