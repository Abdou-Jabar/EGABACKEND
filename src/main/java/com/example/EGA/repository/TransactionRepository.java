package com.example.EGA.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.EGA.entity.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("""
           SELECT t FROM Transaction t\s
           WHERE (t.compteSource.id = :num OR t.compteDestination.id = :num)
           ORDER BY t.dateTransaction DESC
          \s""")
    List<Transaction> findReleve(String num); 

    @Query("SELECT t FROM Transaction t ORDER BY t.dateTransaction DESC")
    List<Transaction> findSorted();

    @Query("""
           SELECT t FROM Transaction t\s
           WHERE (t.compteSource.id = :num OR t.compteDestination.id = :num)
           AND t.dateTransaction BETWEEN :debut AND :fin
           ORDER BY t.dateTransaction DESC
          \s""")
    List<Transaction> findReleveByPeriod(String num, LocalDateTime debut, LocalDateTime fin);

    @Query("SELECT SUM(t.montant) FROM Transaction t")
    Double sumAllMontants();

    List<Transaction> findTop4ByOrderByDateTransactionDesc();

    long countByDateTransactionAfter(LocalDateTime date);

    long countByDateTransactionBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT SUM(t.montant) FROM Transaction t WHERE t.dateTransaction > :date")
    Double sumVolumeAfter(LocalDateTime date);

    @Query("SELECT SUM(t.montant) FROM Transaction t WHERE t.dateTransaction BETWEEN :start AND :end")
    Double sumMontantBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT t FROM Transaction t " +
            "LEFT JOIN FETCH t.compteSource cs " +
            "LEFT JOIN FETCH cs.client " +
            "LEFT JOIN FETCH t.compteDestination cd " +
            "LEFT JOIN FETCH cd.client " +
            "ORDER BY t.dateTransaction DESC " +
            "LIMIT 3")
    List<Transaction> findTop4WithClientInfo();
}
