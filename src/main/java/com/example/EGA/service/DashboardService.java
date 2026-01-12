package com.example.EGA.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.EGA.dto.DashboardDTO;
import com.example.EGA.entity.Transaction;
import com.example.EGA.model.Type;
import com.example.EGA.repository.ClientRepository;
import com.example.EGA.repository.CompteRepository;
import com.example.EGA.repository.TransactionRepository;

@Service
public class DashboardService {

    private final ClientRepository clientRepository;
    private final CompteRepository compteRepository;
    private final TransactionRepository transactionRepository;

    public DashboardService(ClientRepository clientRepository, CompteRepository compteRepository, TransactionRepository transactionRepository) {
        this.clientRepository = clientRepository;
        this.compteRepository = compteRepository;
        this.transactionRepository = transactionRepository;
    }

    public DashboardDTO getDashboardData() {
        // Définition des périodes (J à J-30 et J-30 à J-60)
        LocalDateTime maintenant = LocalDateTime.now();
        LocalDateTime ilYa30Jours = maintenant.minusDays(30);
        LocalDateTime ilYa60Jours = maintenant.minusDays(60);

        // --- 1. CLIENTS ---
        long totalClients = clientRepository.count();
        long clientsIlYa30J = clientRepository.countByDateInscriptionBeforeAndEstSupprimeFalse(ilYa30Jours);
        double diffClients = calculerVariation(clientsIlYa30J, totalClients);

        // --- 2. COMPTES ---
        long totalComptes = compteRepository.count();
        long comptesIlYa30J = compteRepository.countByDateCreationBefore(ilYa30Jours);
        double diffComptes = calculerVariation(comptesIlYa30J, totalComptes);

        // --- 3. TRANSACTIONS (Nombre) ---
        long txMoisActuel = transactionRepository.countByDateTransactionBetween(ilYa30Jours, maintenant);
        long txMoisPrecedent = transactionRepository.countByDateTransactionBetween(ilYa60Jours, ilYa30Jours);
        double diffTx = calculerVariation(txMoisPrecedent, txMoisActuel);

        // --- 4. VOLUME (Somme des montants) ---
        Double volumeActuel = transactionRepository.sumMontantBetween(ilYa30Jours, maintenant);
        Double volumePrecedent = transactionRepository.sumMontantBetween(ilYa60Jours, ilYa30Jours);

        volumeActuel = (volumeActuel != null) ? volumeActuel : 0.0;
        volumePrecedent = (volumePrecedent != null) ? volumePrecedent : 0.0;
        double diffVolume = calculerVariationDouble(volumePrecedent, volumeActuel);

        // --- 5. APERÇU COMPTES ---
        long nbCourant = compteRepository.countByType(Type.Courant);
        Double soldeCourant = compteRepository.sumSoldeByType(Type.Courant);

        long nbEpargne = compteRepository.countByType(Type.Epargne);
        Double soldeEpargne = compteRepository.sumSoldeByType(Type.Epargne);

        // --- 6. DERNIERES TRANSACTIONS ---
        List<Transaction> latest = transactionRepository.findTop4WithClientInfo();

        return new DashboardDTO(
                totalClients, diffClients,
                totalComptes, diffComptes,
                txMoisActuel, diffTx,
                volumeActuel, diffVolume,
                nbCourant, (soldeCourant != null ? soldeCourant : 0.0),
                nbEpargne, (soldeEpargne != null ? soldeEpargne : 0.0),
                latest
        );
    }

    /**
     * Calcule le pourcentage de variation entre deux valeurs long
     */
    private double calculerVariation(long ancienneValeur, long nouvelleValeur) {
        if (ancienneValeur == 0) return nouvelleValeur > 0 ? 100.0 : 0.0;
        return ((double) (nouvelleValeur - ancienneValeur) / ancienneValeur) * 100;
    }

    /**
     * Calcule le pourcentage de variation entre deux valeurs double
     */
    private double calculerVariationDouble(double ancienneValeur, double nouvelleValeur) {
        if (ancienneValeur == 0) return nouvelleValeur > 0 ? 100.0 : 0.0;
        return ((nouvelleValeur - ancienneValeur) / ancienneValeur) * 100;
    }
}