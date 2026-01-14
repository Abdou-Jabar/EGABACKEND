package com.example.EGA.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.EGA.entity.Client;

public interface    ClientRepository extends JpaRepository<Client,Long> {
        @Query("""
        SELECT c
        FROM Client c
        LEFT JOIN c.comptes cp
        WHERE c.estSupprime = false
        AND (cp.id IS NULL OR cp.estSupprime = false)
        GROUP BY c
    """)
    List<Client> findClientsWithActiveComptes();
    Optional<Client> findByIdAndEstSupprimeFalse(Long id);

    long countByEstSupprimeFalse();

    long countByDateInscriptionBeforeAndEstSupprimeFalse(LocalDateTime date);

}

