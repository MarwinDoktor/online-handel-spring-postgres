-- Aufgabe 4a:
CREATE TABLE adresse (
    adresse_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    aktiv BOOLEAN NOT NULL,
    strasse VARCHAR(60) NOT NULL,
    hausnummer VARCHAR(10) NOT NULL,
    plz BIGINT NOT NULL,
    ort VARCHAR(255) NOT NULL,
    land VARCHAR(255) NOT NULL,

    CONSTRAINT chk_strasse_format CHECK (strasse ~ '^[A-ZÄÖÜ][a-zäöüß]*$'),
    CONSTRAINT chk_hausnummer_format CHECK (hausnummer ~ '^[0-9]+[a-z]?$'),
    CONSTRAINT chk_plz_length CHECK (plz < 1000000000000));

-- Aufgabe 4b:
CREATE TABLE kunde (
    kunde_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email VARCHAR(256) NOT NULL UNIQUE,
    vorname VARCHAR(32) NOT NULL,
    nachname VARCHAR(32) NOT NULL,
    passwort VARCHAR(20) NOT NULL,

    CONSTRAINT chk_kunde_vorname CHECK (vorname ~ '^[a-zA-ZÄäÖöÜüß]+$'),
    CONSTRAINT chk_kunde_nachname CHECK (nachname ~ '^[a-zA-ZÄäÖöÜüß]+$'),
    CONSTRAINT chk_kunde_email_format CHECK (email LIKE '%@%.%' AND position('.' in substring(email from position('@' in email))) > 1),
    CONSTRAINT chk_kunde_passwort_len CHECK (LENGTH(passwort) BETWEEN 5 AND 20),
    CONSTRAINT chk_kunde_passwort_complex CHECK (passwort ~ '[A-Za-z]' AND passwort ~ '[0-9]' AND passwort ~ '[^A-Za-z0-9]'));

-- Aufgabe 4e:
CREATE TABLE mitarbeiter (
    personal_nr BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    vorname VARCHAR(255) NOT NULL,
    nachname VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    passwort VARCHAR(20) NOT NULL,

    CONSTRAINT chk_ma_passwort_len CHECK (LENGTH(passwort) BETWEEN 5 AND 20),
    CONSTRAINT chk_ma_passwort_complex CHECK (passwort ~ '[A-Za-z]' AND passwort ~ '[0-9]' AND passwort ~ '[^A-Za-z0-9]'));

-- Aufgabe 4c:
CREATE TABLE kunde_hat_adressen (
    adresse_id BIGINT NOT NULL,
    kunde_id BIGINT NOT NULL,
    typ VARCHAR(255) NOT NULL,

    PRIMARY KEY (adresse_id, kunde_id),
    FOREIGN KEY (adresse_id) REFERENCES adresse(adresse_id),
    FOREIGN KEY (kunde_id) REFERENCES kunde(kunde_id),

    CONSTRAINT chk_adress_typ CHECK (typ IN ('Lieferadresse', 'Rechnungsadresse')));

-- Aufgabe 4d:
CREATE TABLE bestellung (
    bestellung_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    datum TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL,
    mitarbeiterzuweis BIGINT NOT NULL,
    kunde_id BIGINT NOT NULL,

    FOREIGN KEY (mitarbeiterzuweis) REFERENCES mitarbeiter(personal_nr),
    FOREIGN KEY (kunde_id) REFERENCES kunde(kunde_id),

    CONSTRAINT chk_status_values CHECK (status IN ('neu', 'bezahlt', 'versendet', 'abgeschlossen', 'storniert')));

-- Aufgabe 4f:
CREATE TABLE produkt (
    sku VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    preis DECIMAL(10,2) NOT NULL,
    lagerbestand INTEGER NOT NULL,
    angelegt_von BIGINT NOT NULL,

    FOREIGN KEY (angelegt_von) REFERENCES mitarbeiter(personal_nr),

    CONSTRAINT chk_sku_alphanumeric CHECK (sku ~ '^[A-Za-z0-9\-]+$'),
    CONSTRAINT chk_lagerbestand_pos CHECK (lagerbestand >= 0),
    CONSTRAINT chk_preis_format CHECK (preis < 100000000.00));

-- Aufgabe 4g:
CREATE TABLE bestellposition (
    position_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    menge INTEGER NOT NULL,
    sku VARCHAR(255) NOT NULL,
    bestellung_id BIGINT NOT NULL,

    FOREIGN KEY (sku) REFERENCES produkt(sku),
    FOREIGN KEY (bestellung_id) REFERENCES bestellung(bestellung_id),

    CONSTRAINT chk_menge_pos CHECK (menge > 0));

-- TRIGGER & FUNKTIONEN

-- Trigger für 4d (Statuswechsel) & 4g (Bestand zurückbuchen bei Storno)
CREATE OR REPLACE FUNCTION check_bestellung_status() RETURNS TRIGGER AS $$
BEGIN
    -- 4d: Statuswechsel Prüfung
    IF NEW.status = 'storniert' AND OLD.status NOT IN ('neu', 'bezahlt') THEN
        RAISE EXCEPTION 'Statuswechsel auf storniert nur von neu oder bezahlt erlaubt.';
    END IF;

    -- 4g: Storno-Logik (Lagerbestand zurückbuchen)
    IF NEW.status = 'storniert' AND OLD.status != 'storniert' THEN
        UPDATE produkt p
        SET lagerbestand = lagerbestand + bp.menge
        FROM bestellposition bp
        WHERE bp.sku = p.sku AND bp.bestellung_id = NEW.bestellung_id;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_bestellung_status_update
    BEFORE UPDATE ON bestellung
    FOR EACH ROW
    EXECUTE FUNCTION check_bestellung_status();


-- Trigger für 4g (Lagerbestand bei Insert/Update/Delete von Positionen)
CREATE OR REPLACE FUNCTION manage_bestellposition() RETURNS TRIGGER AS $$
DECLARE
    current_lagerbestand INTEGER;
    bestellung_status VARCHAR;
BEGIN
    IF TG_OP = 'DELETE' THEN
        SELECT status INTO bestellung_status FROM bestellung WHERE bestellung_id = OLD.bestellung_id;
    ELSE
        SELECT status INTO bestellung_status FROM bestellung WHERE bestellung_id = NEW.bestellung_id;
    END IF;

    IF bestellung_status = 'storniert' THEN
        RAISE EXCEPTION 'Änderungen an stornierten Bestellungen sind nicht erlaubt.';
    END IF;

    IF TG_OP = 'INSERT' THEN
        SELECT lagerbestand INTO current_lagerbestand FROM produkt WHERE sku = NEW.sku;

    IF current_lagerbestand < NEW.menge THEN
            RAISE EXCEPTION 'Lagerbestand zu niedrig für Produkt %', NEW.sku;
    ELSE
        UPDATE produkt SET lagerbestand = lagerbestand - NEW.menge WHERE sku = NEW.sku;
    END IF;
    RETURN NEW;

-- UPDATE Logik
    ELSIF TG_OP = 'UPDATE' THEN
        IF NEW.menge > OLD.menge THEN
            SELECT lagerbestand INTO current_lagerbestand FROM produkt WHERE sku = NEW.sku;

    IF current_lagerbestand < (NEW.menge - OLD.menge) THEN
        RAISE EXCEPTION 'Lagerbestand reicht nicht für Erhöhung der Menge.';
    END IF;

    UPDATE produkt SET lagerbestand = lagerbestand - (NEW.menge - OLD.menge) WHERE sku = NEW.sku;
    ELSIF NEW.menge < OLD.menge THEN
        UPDATE produkt SET lagerbestand = lagerbestand + (OLD.menge - NEW.menge) WHERE sku = NEW.sku;
    END IF;
    RETURN NEW;

    ELSIF TG_OP = 'DELETE' THEN
        UPDATE produkt SET lagerbestand = lagerbestand + OLD.menge WHERE sku = OLD.sku;
        RETURN OLD;
    END IF;

    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_bestellposition_change
    BEFORE INSERT OR UPDATE OR DELETE ON bestellposition
    FOR EACH ROW
    EXECUTE FUNCTION manage_bestellposition();


-- Aufgabe 6a:
CREATE OR REPLACE VIEW v_kunde_summe_anzahl_bestellungen AS
SELECT
    k.kunde_id,
    k.email,

    COUNT(DISTINCT b.bestellung_id) AS anzahl_bestellungen,

    COALESCE(SUM(bp.menge * p.preis), 0) AS gesamtsumme
FROM kunde k
         LEFT JOIN bestellung b ON k.kunde_id = b.kunde_id
         LEFT JOIN bestellposition bp ON b.bestellung_id = bp.bestellung_id
         LEFT JOIN produkt p ON bp.sku = p.sku
GROUP BY k.kunde_id, k.email
ORDER BY k.kunde_id ASC;


-- Aufgabe 6b:
CREATE OR REPLACE VIEW v_produkt_verkaufszahlen AS
SELECT
    p.sku,
    p.name,
    COALESCE(SUM(CASE WHEN b.status NOT IN ('neu', 'storniert') THEN bp.menge ELSE 0 END), 0) AS gesamt_verkaufte_menge,
    COALESCE(SUM(CASE WHEN b.status NOT IN ('neu', 'storniert') THEN bp.menge * p.preis ELSE 0 END), 0) AS umsatz,
    COUNT(DISTINCT CASE WHEN b.status NOT IN ('neu', 'storniert') THEN b.bestellung_id END) AS anzahl_bestellungen
FROM produkt p
         LEFT JOIN bestellposition bp ON p.sku = bp.sku
         LEFT JOIN bestellung b ON bp.bestellung_id = b.bestellung_id
GROUP BY p.sku, p.name
ORDER BY gesamt_verkaufte_menge DESC;


-- Aufgabe 6c:
CREATE OR REPLACE VIEW v_mitarbeiter_uebersicht AS
SELECT
    m.personal_nr,
    COUNT(DISTINCT b.bestellung_id) AS anzahl_verwalteter_bestellungen,
    COUNT(DISTINCT p.sku) AS anzahl_angelegter_produkte
FROM mitarbeiter m
         LEFT JOIN bestellung b ON m.personal_nr = b.mitarbeiterzuweis
         LEFT JOIN produkt p ON m.personal_nr = p.angelegt_von
GROUP BY m.personal_nr
ORDER BY m.personal_nr ASC;


-- Aufgabe 6d:
CREATE OR REPLACE VIEW v_mitarbeiter_bestellstatus_uebersicht AS
SELECT
    m.personal_nr,
    s.status,
    COUNT(b.bestellung_id) AS anzahl_bestellungen
FROM mitarbeiter m
         CROSS JOIN (
    VALUES ('neu'), ('bezahlt'), ('versendet'), ('abgeschlossen'), ('storniert')
) AS s(status)
         LEFT JOIN bestellung b
                   ON m.personal_nr = b.mitarbeiterzuweis
                       AND s.status = b.status
GROUP BY m.personal_nr, s.status
ORDER BY m.personal_nr ASC, s.status ASC;