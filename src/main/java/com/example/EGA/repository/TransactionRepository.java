package com.example.EGA.repository;

import com.example.EGA.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("""
           SELECT t FROM Transaction t\s
           WHERE (t.compteSource.id = :num OR t.compteDestination.id = :num)
           ORDER BY t.dateTransaction DESC
          \s""")
    List<Transaction> findReleve(String num);

    @Query("""
           SELECT t FROM Transaction t\s
           WHERE (t.compteSource.id = :num OR t.compteDestination.id = :num)
           AND t.dateTransaction BETWEEN :debut AND :fin
           ORDER BY t.dateTransaction DESC
          \s""")
    List<Transaction> findReleveByPeriod(String num, LocalDateTime debut, LocalDateTime fin);

    @Query("SELECT SUM(t.montant) FROM Transaction t")
    Double sumAllMontants();

    List<Transaction> findTop5ByOrderByDateTransactionDesc();

}
