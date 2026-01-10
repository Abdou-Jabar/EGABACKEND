package com.example.EGA.dto;
import com.example.EGA.entity.Transaction;
import java.util.List;

public record DashboardDTO(
        long totalClients,
        long totalComptes,
        long totalTransactions,
        double volumeTransactions,
        long nbComptesCourant,
        double soldeTotalCourant,
        long nbComptesEpargne,
        double soldeTotalEpargne,
        List<Transaction> dernieresTransactions
) {}
