# Online-Handel mit PostgreSQL & Spring Boot

Die Anwendung bildet ein vereinfachtes Online-Handelssystem ab (Kunden, Mitarbeiter, Produkte, Bestellungen, Adressen) und stellt dafür eine REST-API bereit. Die Datenbank enthält neben dem Schema auch Trigger und Views, die Geschäftslogik und Auswertungen abbilden.

## Technologie-Stack

- **Backend:** Java 17, Spring Boot 3.5, Spring Web, Spring JDBC (JdbcTemplate)
- **Datenbank:** PostgreSQL 17
- **Build:** Maven
- **Container:** Docker & Docker Compose
- **API-Dokumentation:** springdoc-openapi (Swagger UI)

## Architektur

```
Controller  →  Repository (JdbcTemplate)  →  PostgreSQL
     ↑
   Model (POJOs)
```

- **Controller:** REST-Endpoints für CRUD und Reports
- **Repository:** Datenbankzugriffe mit JdbcTemplate und RowMapper
- **Model:** Java-Klassen, die die Entitäten abbilden
- **schema.sql:** Tabellen, Constraints, Trigger, Funktionen und Views
- **data.sql:** Testdaten

## Datenbank-Features

- Tabellen mit CHECK-Constraints (u. a. Regex für Namen, E-Mail, Passwort-Komplexität, SKU)
- Fremdschlüssel und automatische IDs (`GENERATED ALWAYS AS IDENTITY`)
- Trigger und PL/pgSQL-Funktionen für:
  - Lagerbestandsverwaltung bei Bestellpositionen (Insert/Update/Delete)
  - Statuswechsel-Logik bei Bestellungen (inkl. Rückbuchung bei Storno)
- Views für Auswertungen (Kundenumsatz, Produktverkaufszahlen, Mitarbeiterübersicht)

## Voraussetzungen

- Java 17
- Maven
- Docker & Docker Compose
- (Optional) IntelliJ IDEA oder VS Code mit Java-Support

## Starten

1. Repository klonen und in das Projektverzeichnis wechseln.

2. Datenbank starten:
   ```bash
   docker compose up -d
   ```
   (Für persistente Datenbank: `docker compose -f docker-compose-persistent.yml up -d`)

3. Anwendung starten:
   ```bash
   cd spring-boot
   mvn clean install
   mvn spring-boot:run
   ```

4. Anwendung öffnen:
   - Startseite: http://localhost:8080
   - Swagger UI: http://localhost:8080/swagger-ui/index.html

## Hinweise

- Die Zugangsdaten für die Datenbank (`lordoftherows` / `rows123`) und die Testdaten in `data.sql` sind **Demo-Daten** und ausschließlich für Testzwecke gedacht.
- Passwörter werden in diesem Projekt bewusst im Klartext gespeichert und verglichen (In der Praxis würde man natürlich Hashing verwenden).
- Das Projekt ist für den lokalen Betrieb ausgelegt und nicht für den Produktiv-Einsatz gedacht.

## Autor

Marwin Doktor  
Heinrich-Heine-Universität Düsseldorf  
B.Sc. Informatik
