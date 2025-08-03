package PDF;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import java.awt.Color;
import java.io.FileOutputStream;

/**
 * Diese Java-Anwendung erstellt ein PDF, das den Anmeldebildschirm
 * einer DATEV-App simulieren soll. Erstellt mit der OpenPDF-Bibliothek.
 */
public class PDFApp {

    public static void main(String[] args) {
        Document document = new Document(PageSize.A4);

        try {
            // PDF-Datei erstellen
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("datev_app.pdf"));
            document.open();

            // Gestaltungsfläche für Inhalte und Hintergrund
            PdfContentByte canvas = writer.getDirectContent();         // für Texte, Rahmen, Felder
            PdfContentByte bg = writer.getDirectContentUnder();        // für Hintergrundflächen

            // Seitenmaße (A4)
            float pageWidth = PageSize.A4.getWidth();
            float pageHeight = PageSize.A4.getHeight();

            // Handygröße und Positionierung
            float phoneWidth = 200;
            float phoneHeight = 400;
            float phoneX = (pageWidth - phoneWidth) / 2;                // horizontal zentriert
            float phoneY = (pageHeight - phoneHeight) / 2;              // vertikal zentriert

            // Handy-Hintergrund (hellgrün, mit abgerundeten Ecken)
            bg.setColorFill(new Color(224, 242, 233));                 // #E0F2E9
            bg.roundRectangle(phoneX, phoneY, phoneWidth, phoneHeight, 20);
            bg.fill();

            // Handy-Rahmen (schwarz)
            canvas.setColorStroke(Color.BLACK);
            canvas.setLineWidth(2f);
            canvas.roundRectangle(phoneX, phoneY, phoneWidth, phoneHeight, 20);
            canvas.stroke();

            // Logo einfügen
            // Hinweis: Logo-Datei "DATEV Logo.jpg" muss im Verzeichnis "resources" vorhanden sein
            Image logo = Image.getInstance("resources/DATEV Logo.jpg");
            logo.scaleToFit(100, 100);
            float logoX = phoneX + (phoneWidth - logo.getScaledWidth()) / 2;
            float logoY = phoneY + phoneHeight - 110;
            logo.setAbsolutePosition(logoX, logoY);
            document.add(logo);

            // Eingabefelder vorbereiten
            String[] labels = {"Nachname", "Benutzer-ID", "Passwort"};
            float fieldWidth = 160;
            float fieldHeight = 30;
            float spacing = 50;
            float startY = logoY - 60; // Abstand zum Logo

            // Schriftart setzen
            BaseFont baseFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
            canvas.setFontAndSize(baseFont, 12);

            // Eingabefelder zeichnen
            for (int i = 0; i < labels.length; i++) {
                float fieldX = phoneX + (phoneWidth - fieldWidth) / 2;
                float fieldY = startY - (i * spacing);

                // Feld-Hintergrund (weiß, abgerundet)
                canvas.setColorFill(Color.WHITE);
                canvas.roundRectangle(fieldX, fieldY, fieldWidth, fieldHeight, 10);
                canvas.fill();

                // Platzhalter-Text (grau, zentriert)
                float textX = fieldX + fieldWidth / 2;
                float textY = fieldY + fieldHeight / 2 - 4;
                canvas.beginText();
                canvas.setColorFill(Color.GRAY);
                canvas.showTextAligned(PdfContentByte.ALIGN_CENTER, labels[i], textX, textY, 0);
                canvas.endText();
            }

            // "Anmelden"-Button
            float buttonWidth = 100;
            float buttonHeight = 30;
            float buttonX = phoneX + (phoneWidth - buttonWidth) / 2;
            float buttonY = startY - labels.length * spacing - 20;

            // Button zeichnen (grün, abgerundet)
            canvas.setColorFill(new Color(76, 175, 80)); // #4CAF50
            canvas.roundRectangle(buttonX, buttonY, buttonWidth, buttonHeight, 10);
            canvas.fill();

            // Button-Beschriftung (weiß, zentriert)
            canvas.beginText();
            canvas.setColorFill(Color.WHITE);
            canvas.setFontAndSize(baseFont, 12);
            canvas.showTextAligned(PdfContentByte.ALIGN_CENTER, "Anmelden",
                    buttonX + buttonWidth / 2, buttonY + buttonHeight / 2 - 4, 0);
            canvas.endText();

            System.out.println("PDF \"datev_app.pdf\" wurde erfolgreich erstellt.");

        } catch (Exception e) {
            System.err.println("Fehler beim Erstellen des PDFs: " + e.getMessage());
        } finally {
            document.close();
        }
    }
}
