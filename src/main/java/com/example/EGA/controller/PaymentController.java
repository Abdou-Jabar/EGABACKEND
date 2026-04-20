package com.example.EGA.controller;

import com.example.EGA.dto.PaymentDTO;
import com.example.EGA.dto.WebhookPayload;
import com.example.EGA.service.CompteService;
import com.example.EGA.service.TransactionService;
import com.example.EGA.service.WebhookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PaymentController {

    private final CompteService compteService;
    private final TransactionService transactionService;
    private final WebhookService webhookService;

    public PaymentController(CompteService compteService,
                             TransactionService transactionService,
                             WebhookService webhookService) {
        this.compteService = compteService;
        this.transactionService = transactionService;
        this.webhookService = webhookService;
    }

    /**
     * Effectue un paiement (virement) entre deux comptes.
     * Appelé exclusivement par Laravel via X-API-KEY.
     *
     * Corps attendu :
     * {
     *   "compteSource": "GB33BUKB...",
     *   "compteDestination": "FR76...",
     *   "montant": 5000.0,
     *   "motDePasse": "1234"
     * }
     */
    @PostMapping("/payment")
    public ResponseEntity<?> effectuerPaiement(@RequestBody @Valid PaymentDTO dto) {

        // 1. Vérifier le mot de passe du compte source
        boolean motDePasseValide = compteService.verifierMotDePasse(
                dto.getCompteSource(), dto.getMotDePasse()
        );

        if (!motDePasseValide) {
            WebhookPayload echecPayload = new WebhookPayload(
                    "ECHEC",
                    dto.getCompteSource(),
                    dto.getCompteDestination(),
                    dto.getMontant(),
                    LocalDateTime.now(),
                    "Mot de passe incorrect"
            );
            webhookService.envoyer(echecPayload);

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("erreur", "Mot de passe incorrect"));
        }

        // 2. Effectuer le virement
        try {
            transactionService.effectuerVirement(
                    dto.getCompteSource(),
                    dto.getCompteDestination(),
                    dto.getMontant()
            );
        } catch (RuntimeException e) {
            WebhookPayload echecPayload = new WebhookPayload(
                    "ECHEC",
                    dto.getCompteSource(),
                    dto.getCompteDestination(),
                    dto.getMontant(),
                    LocalDateTime.now(),
                    e.getMessage()
            );
            webhookService.envoyer(echecPayload);

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("erreur", e.getMessage()));
        }

        // 3. Notifier Laravel du succès
        WebhookPayload succesPayload = new WebhookPayload(
                "SUCCES",
                dto.getCompteSource(),
                dto.getCompteDestination(),
                dto.getMontant(),
                LocalDateTime.now(),
                "Virement effectué avec succès"
        );
        webhookService.envoyer(succesPayload);

        return ResponseEntity.ok(Map.of(
                "statut", "SUCCES",
                "message", "Virement effectué avec succès",
                "montant", dto.getMontant(),
                "compteSource", dto.getCompteSource(),
                "compteDestination", dto.getCompteDestination(),
                "dateTransaction", LocalDateTime.now().toString()
        ));
    }
}
