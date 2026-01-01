package com.example.EGA.controller;

import com.example.EGA.dto.TransactionDTO;
import com.example.EGA.entity.Transaction;
import com.example.EGA.repository.TransactionRepository;
import com.example.EGA.service.PdfGeneratorService;
import com.example.EGA.service.TransactionService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
    private final TransactionService transactionService;
    private final PdfGeneratorService pdfGeneratorService;
    private final TransactionRepository transactionRepository;

    public TransactionController(TransactionService transactionService, PdfGeneratorService pdfGeneratorService, TransactionRepository transactionRepository) {
        this.transactionService = transactionService;
        this.pdfGeneratorService = pdfGeneratorService;
        this.transactionRepository = transactionRepository;
    }

    //Lister toutes les transactions
    @GetMapping("/all")
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    //Lister toutes les transactions d'un compte
    @GetMapping("/compte/{num}")
    public ResponseEntity<?> getTransactions(@PathVariable String num) {
        try {
            List<Transaction> transactions = transactionService.obtenirTransactionsParCompte(num);
            return transactions.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(transactions);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Lister les transactions d'un compte sur une période donnée
    @GetMapping("/compte/{num}/periode")
    public ResponseEntity<List<Transaction>> getTransactionsByPeriode(
            @PathVariable String num,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {

        List<Transaction> transactions = transactionService.obtenirTransactionsParPeriode(num, debut, fin);

        if (transactions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(transactions);
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

    //Imprimer le releve d'un compte
    @GetMapping("/releve/pdf")
    public void imprimerRelevePdf(
            @RequestParam String numeroCompte,
            HttpServletResponse response) throws IOException {

        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=releve_" + numeroCompte + ".pdf";
        response.setHeader(headerKey, headerValue);

        List<Transaction> transactions = transactionService.obtenirReleve(numeroCompte);

        pdfGeneratorService.generateRelevePdf(transactions, numeroCompte, response);
    }

    //Imprimer le relevé d'un compte au cours d'une période donné
    @GetMapping("/releveperiod/pdf")
    public void imprimerRelevePdf(
            @RequestParam String numeroCompte,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin,
            HttpServletResponse response) throws IOException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String debutStr = formatter.format(debut);
        String finStr = formatter.format(fin);

        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=releve de" + numeroCompte + "du " + debutStr + " au " + finStr +".pdf";
        response.setHeader(headerKey, headerValue);

        List<Transaction> transactions = transactionService.obtenirReleveParPeriode(numeroCompte, debut, fin);

        pdfGeneratorService.generateRelevePdfByPeriod(transactions, numeroCompte, response);
    }
}
