package com.example.EGA.service;

import com.example.EGA.entity.Compte;
import com.example.EGA.entity.Transaction;
import com.example.EGA.model.TypeTransaction;
import com.example.EGA.repository.CompteRepository;
import com.example.EGA.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {

    private final CompteRepository compteRepository;
    private final TransactionRepository transactionRepository;
    private final EmailService emailService;

    public TransactionService(CompteRepository compteRepository,
                              TransactionRepository transactionRepository,
                              EmailService emailService) {
        this.compteRepository = compteRepository;
        this.transactionRepository = transactionRepository;
        this.emailService = emailService;
    }

    @Transactional
    public void effectuerDepot(String numDest, Double montant) {
        if (montant < 500) throw new RuntimeException("Le montant minimal pour un dépôt est de 500 F");

        Compte dest = compteRepository.findByIdAndEstSupprimeFalse(numDest)
                .orElseThrow(() -> new RuntimeException("Compte destination introuvable ou supprimé"));

        dest.setSolde(dest.getSolde() + montant);

        Transaction t = new Transaction();
        t.setType(TypeTransaction.Depot);
        t.setMontant(montant);
        t.setCompteDestination(dest);
        t.setDateTransaction(LocalDateTime.now());

        transactionRepository.save(t);
        compteRepository.save(dest);
        emailService.envoyerDepot(dest, montant);
    }

    @Transactional
    public void effectuerRetrait(String numSource, Double montant) {
        if (montant < 500) throw new RuntimeException("Le montant minimal pour un retrait est de 500 F");

        Compte source = compteRepository.findByIdAndEstSupprimeFalse(numSource)
                .orElseThrow(() -> new RuntimeException("Compte source introuvable"));

        if (source.getSolde() < montant) throw new RuntimeException("Solde insuffisant");

        source.setSolde(source.getSolde() - montant);

        Transaction t = new Transaction();
        t.setType(TypeTransaction.Retrait);
        t.setMontant(montant);
        t.setCompteSource(source);
        t.setDateTransaction(LocalDateTime.now());

        transactionRepository.save(t);
        compteRepository.save(source);
        emailService.envoyerRetrait(source, montant);
    }

    @Transactional
    public void effectuerVirement(String numSource, String numDest, Double montant) {
        if (montant < 500) throw new RuntimeException("Le montant minimal pour un virement est de 500 F");

        Compte source = compteRepository.findByIdAndEstSupprimeFalse(numSource)
                .orElseThrow(() -> new RuntimeException("Compte source introuvable"));

        Compte dest = compteRepository.findByIdAndEstSupprimeFalseAndClientEstSupprimeFalse(numDest)
                .orElseThrow(() -> new RuntimeException("Compte destination introuvable"));

        if (source.getSolde() < montant) throw new RuntimeException("Solde insuffisant");

        source.setSolde(source.getSolde() - montant);
        dest.setSolde(dest.getSolde() + montant);

        Transaction t = new Transaction();
        t.setType(TypeTransaction.Virement);
        t.setMontant(montant);
        t.setCompteSource(source);
        t.setCompteDestination(dest);
        t.setDateTransaction(LocalDateTime.now());

        transactionRepository.save(t);
        compteRepository.save(source);
        compteRepository.save(dest);
        emailService.envoyerVirement(source, dest, montant);
    }

    public List<Transaction> obtenirReleve(String numeroCompte) {
        compteRepository.findByIdAndEstSupprimeFalseAndClientEstSupprimeFalse(numeroCompte)
                .orElseThrow(() -> new RuntimeException("Compte introuvable"));

        return transactionRepository.findReleve(numeroCompte);
    }

    public List<Transaction> obtenirReleveParPeriode(String numeroCompte, LocalDateTime debut, LocalDateTime fin) {
        compteRepository.findByIdAndEstSupprimeFalseAndClientEstSupprimeFalse(numeroCompte)
                .orElseThrow(() -> new RuntimeException("Compte introuvable"));

        return transactionRepository.findReleveByPeriod(numeroCompte, debut, fin);
    }

    public List<Transaction> obtenirTransactionsParCompte(String numCompte) {
        compteRepository.findByIdAndEstSupprimeFalseAndClientEstSupprimeFalse(numCompte)
                .orElseThrow(() -> new RuntimeException("Compte introuvable ou supprimé"));

        return transactionRepository.findReleve(numCompte);
    }

    public List<Transaction> obtenirTransactionsParPeriode(String num, LocalDateTime debut, LocalDateTime fin) {
        compteRepository.findByIdAndEstSupprimeFalse(num)
                .orElseThrow(() -> new RuntimeException("Compte introuvable ou supprimé"));

        return transactionRepository.findReleveByPeriod(num, debut, fin);
    }
}
