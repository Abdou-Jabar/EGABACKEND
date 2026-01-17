package com.example.EGA.service;

import com.example.EGA.entity.Transaction;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PdfGeneratorService {

    public void generateRelevePdf(List<Transaction> transactions, String numeroCompte, HttpServletResponse response) throws IOException {
        PdfWriter writer = new PdfWriter(response.getOutputStream());
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // En-tête du document
        document.add(new Paragraph("EGA BANK - RELEVE DE COMPTE").setBold().setFontSize(18));
        document.add(new Paragraph("Numéro de Compte : " + numeroCompte));
        document.add(new Paragraph("Historique des transactions"));
        document.add(new Paragraph("\n"));

        // Création du tableau avec des largeurs de colonnes relatives
        float[] columnWidths = {2, 2, 2, 4};
        Table table = new Table(UnitValue.createPercentArray(columnWidths));
        table.setWidth(UnitValue.createPercentValue(100));

        // En-têtes du tableau
        table.addHeaderCell("Date");
        table.addHeaderCell("Type");
        table.addHeaderCell("Montant");
        table.addHeaderCell("Détails");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (Transaction t : transactions) {
            // 1. Date
            table.addCell(t.getDateTransaction().format(formatter));

            // 2. Type (Depot, Retrait, Virement)
            table.addCell(t.getType().toString());

            // 3. Montant avec distinction Débit/Crédit
            // Si le compte est source = l'argent sort (négatif pour le relevé)
            if (t.getCompteSource() != null && t.getCompteSource().getId().equals(numeroCompte)) {
                table.addCell("-" + t.getMontant() + " F");
            } else {
                table.addCell("+" + t.getMontant() + " F");
            }

            // 4. Détails (Gestion des comptes tiers)
            String detail;
            if (t.getCompteSource() != null && t.getCompteDestination() != null) {
                // C'est un virement
                if (t.getCompteSource().getId().equals(numeroCompte)) {
                    detail = "Virement vers " + t.getCompteDestination().getId();
                } else {
                    detail = "Virement reçu de " + t.getCompteSource().getId();
                }
            } else {
                detail = t.getType().toString() + " au guichet";
            }
            table.addCell(detail);
        }

        document.add(table);
        document.close();
    }

    public void generateRelevePdfByPeriod(List<Transaction> transactions, String numeroCompte, HttpServletResponse response) throws IOException {
        PdfWriter writer = new PdfWriter(response.getOutputStream());
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // En-tête du document
        document.add(new Paragraph("EGA BANK - RELEVE DE COMPTE").setBold().setFontSize(18));
        document.add(new Paragraph("Numéro de Compte : " + numeroCompte));
        document.add(new Paragraph("Historique des transactions"));
        document.add(new Paragraph("\n"));

        // Création du tableau avec des largeurs de colonnes relatives
        float[] columnWidths = {2, 2, 2, 4};
        Table table = new Table(UnitValue.createPercentArray(columnWidths));
        table.setWidth(UnitValue.createPercentValue(100));

        // En-têtes du tableau
        table.addHeaderCell("Date");
        table.addHeaderCell("Type");
        table.addHeaderCell("Montant");
        table.addHeaderCell("Détails");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (Transaction t : transactions) {
            // 1. Date
            table.addCell(t.getDateTransaction().format(formatter));

            // 2. Type (Depot, Retrait, Virement)
            table.addCell(t.getType().toString());

            // 3. Montant avec distinction Débit/Crédit
            // Si le compte est source = l'argent sort (négatif pour le relevé)
            if (t.getCompteSource() != null && t.getCompteSource().getId().equals(numeroCompte)) {
                table.addCell("-" + t.getMontant() + " F");
            } else {
                table.addCell("+" + t.getMontant() + " F");
            }

            // 4. Détails (Gestion des comptes tiers)
            String detail;
            if (t.getCompteSource() != null && t.getCompteDestination() != null) {
                // C'est un virement
                if (t.getCompteSource().getId().equals(numeroCompte)) {
                    detail = "Virement vers " + t.getCompteDestination().getId();
                } else {
                    detail = "Virement reçu de " + t.getCompteSource().getId();
                }
            } else {
                detail = t.getType().toString() + " au guichet";
            }
            table.addCell(detail);
        }

        document.add(table);
        document.close();
    }

    public byte[] generateRelevePdfBytes(List<Transaction> transactions, String numeroCompte) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // En-tête du document
        document.add(new Paragraph("EGA BANK - RELEVE DE COMPTE").setBold().setFontSize(18));
        document.add(new Paragraph("Numéro de Compte : " + numeroCompte));
        document.add(new Paragraph("Historique des transactions"));
        document.add(new Paragraph("\n"));

        // Création du tableau avec des largeurs de colonnes relatives
        float[] columnWidths = {2, 2, 2, 4};
        Table table = new Table(UnitValue.createPercentArray(columnWidths));
        table.setWidth(UnitValue.createPercentValue(100));

        // En-têtes du tableau
        table.addHeaderCell("Date");
        table.addHeaderCell("Type");
        table.addHeaderCell("Montant");
        table.addHeaderCell("Détails");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (Transaction t : transactions) {
            // 1. Date
            table.addCell(t.getDateTransaction().format(formatter));

            // 2. Type (Depot, Retrait, Virement)
            table.addCell(t.getType().toString());

            // 3. Montant avec distinction Débit/Crédit
            // Si le compte est source = l'argent sort (négatif pour le relevé)
            if (t.getCompteSource() != null && t.getCompteSource().getId().equals(numeroCompte)) {
                table.addCell("-" + t.getMontant() + " F");
            } else {
                table.addCell("+" + t.getMontant() + " F");
            }

            // 4. Détails (Gestion des comptes tiers)
            String detail;
            if (t.getCompteSource() != null && t.getCompteDestination() != null) {
                // C'est un virement
                if (t.getCompteSource().getId().equals(numeroCompte)) {
                    detail = "Virement vers " + t.getCompteDestination().getId();
                } else {
                    detail = "Virement reçu de " + t.getCompteSource().getId();
                }
            } else {
                detail = t.getType().toString() + " au guichet";
            }
            table.addCell(detail);
        }

        document.add(table);
        document.close();

        return baos.toByteArray();
    }
}