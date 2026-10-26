# Beat_Manager_Niveau_4

## Ziel der Applikation
Ich entwickle eine Java-Konsolenapplikation, mit der Musikproduzenten ihre Beat-Projekte übersichtlich verwalten können. Es geht dabei nicht um die Verarbeitung echter Audiodateien, sondern um die Organisation der Projektdaten (BPM, Tonart, Status), der verwendeten Spuren und die automatische Berechnung von Verkaufspreisen für verschiedene Lizenzmodelle.

## Muss Kriterien
* **Projekte verwalten:** Neue Beat-Projekte mit Name, BPM und Tonart erstellen, anzeigen und löschen.
* **Spuren hinzufügen:** Verschiedene Spur Typen einem Projekt zuweisen, wobei jeder Typ eigene Eigenschaften hat.
* **Lizenzpreise berechnen:** Automatische Preisberechnung für einen Beat je nach Lizenzmodell und den enthaltenen Spuren.
* **Eingaben prüfen:** Ungültige Eingaben oder nicht freigegebene Samples beim Exklusiv Verkauf mit eigenen Fehlermeldungen abfangen.
* **Speichern und Laden:** Alle Projekte lokal in einer Datei speichern, damit die Daten beim nächsten Programmstart wieder da sind.

## Nice-to-Have
* **Such und Filterfunktion:** Beats gezielt nach BPM-Bereichen, Tonart oder Genre durchsuchen.
* **Royalty Split Rechner:** Aufteilung der Einnahmen in Prozent berechnen, wenn mehrere Produzenten an einem Beat mitgearbeitet haben.
* **Vertrags Export:** Eine kurze Textdatei mit den Lizenz und Preisdetails als Kaufübersicht für Kunden generieren.


## KI Prompt
_Sie erstellen eine Skizze der geplanten Architektur (UML Klassendiagramm). Zu meinem Projekt: einen Beat und Musikprojekt Manager als Konsolen Applikation in Java umsetzen.   Dabei geht es nicht um echte Audiodateien, sondern um ein Verwaltungstool für Musikproduzenten: Man kann Beat-Projekte (mit BPM, Tonart etc.) erfassen, verschiedene Spur Typen wie Samples, Drums oder MIDI-Spuren hinzufügen (Vererbung/Polymorphismus) und automatisch Verkaufspreise für verschiedene Lizenzmodelle berechnen lassen (Design Patterns). Auch eigene Exceptions, eine eigene generische Klasse (z. B. für die Datenverwaltung/Suche) und Speicherung via JSON/CSV_

![ULM-Klassendiagramm](beatmanager.drawio.png)
