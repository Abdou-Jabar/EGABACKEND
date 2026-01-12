package com.example.EGA.dto;
import java.util.List;

import com.example.EGA.entity.Transaction;

public record DashboardDTO(
        long totalClients,
        double diffClients,
        long totalComptes,
        double diffComptes,
        long totalTransactions,
        double diffTransactions,
        double volume,
        double diffVolume,
        long nbCourant,
        double soldeCourant,
        long nbEpargne,
        double soldeEpargne,
        List<Transaction> dernieresTransactions
) {}
