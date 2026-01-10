package com.example.EGA.controller;

import com.example.EGA.dto.DashboardDTO;
import com.example.EGA.entity.Transaction;
import com.example.EGA.model.Type;
import com.example.EGA.repository.ClientRepository;
import com.example.EGA.repository.CompteRepository;
import com.example.EGA.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final ClientRepository clientRepository;
    private final CompteRepository compteRepository;
    private final TransactionRepository transactionRepository;

    @GetMapping
    public DashboardDTO getStats() {
        long totalClients = clientRepository.countByEstSupprimeFalse();
        long totalComptes = compteRepository.countByEstSupprimeFalse();
        long totalTransactions = transactionRepository.count();

        Double volume = transactionRepository.sumAllMontants();

        long nbCourant = compteRepository.countByTypeAndEstSupprimeFalse(Type.Courant);
        Double soldeCourant = compteRepository.sumSoldeByType(Type.Courant);

        long nbEpargne = compteRepository.countByTypeAndEstSupprimeFalse(Type.Epargne);
        Double soldeEpargne = compteRepository.sumSoldeByType(Type.Epargne);

        List<Transaction> latest = transactionRepository.findTop5ByOrderByDateTransactionDesc();

        return new DashboardDTO(
                totalClients,
                totalComptes,
                totalTransactions,
                volume != null ? volume : 0.0,
                nbCourant,
                soldeCourant != null ? soldeCourant : 0.0,
                nbEpargne,
                soldeEpargne != null ? soldeEpargne : 0.0,
                latest
        );
    }
}