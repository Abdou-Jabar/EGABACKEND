package com.example.EGA.controller;

import com.example.EGA.dto.TransactionDTO;
import com.example.EGA.entity.Transaction;
import com.example.EGA.service.PdfGeneratorService;
import com.example.EGA.service.TransactionService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
    private final TransactionService transactionService;
    private final PdfGeneratorService pdfGeneratorService;

    public TransactionController(TransactionService transactionService, PdfGeneratorService pdfGeneratorService) {
        this.transactionService = transactionService;
        this.pdfGeneratorService = pdfGeneratorService;
    }

    //Faire un dépôt sur un compte
    @PostMapping("/deposer")
    public ResponseEntity<?> deposer(@RequestBody TransactionDTO dto) {
        transactionService.effectuerDepot(dto.getNumeroCompteDestination(), dto.getSolde());
        return ResponseEntity.ok("Dépôt réussi");
    }

    //Faire un retrait d'un compte
    @PostMapping("/retirer")
    public ResponseEntity<?> retirer(@RequestBody TransactionDTO dto) {
        transactionService.effectuerRetrait(dto.getNumeroCompteSource(), dto.getSolde());
        return ResponseEntity.ok("Retrait réussi");
    }

    //Faire un virement d'un compte source à un compte destinataire
    @PostMapping("/virement")
    public ResponseEntity<?> virement(@RequestBody TransactionDTO dto) {
        transactionService.effectuerVirement(
                dto.getNumeroCompteSource(),
                dto.getNumeroCompteDestination(),
                dto.getSolde()
        );
        return ResponseEntity.ok("Virement réussi");
    }

    @GetMapping("/releve/pdf")
    public void imprimerRelevePdf(
            @RequestParam String numeroCompte,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin,
            HttpServletResponse response) throws IOException {

        // Configuration du téléchargement
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=releve_" + numeroCompte + ".pdf";
        response.setHeader(headerKey, headerValue);

        // Récupération des données
        List<Transaction> transactions = transactionService.obtenirReleve(numeroCompte, debut, fin);

        // Génération du fichier
        pdfGeneratorService.generateRelevePdf(transactions, numeroCompte, response);
    }
}
