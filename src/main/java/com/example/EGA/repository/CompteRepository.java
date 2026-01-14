package com.example.EGA.repository;

import com.example.EGA.entity.Compte;
import com.example.EGA.model.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CompteRepository extends JpaRepository<Compte, String> {
    @Query("""
           SELECT c FROM Compte c
           WHERE c.estSupprime = false
           AND c.client.estSupprime = false
           """)
    List<Compte> findAllByClient();

    Optional<Compte> findByIdAndEstSupprimeFalse(String id);

    Optional<Compte> findByIdAndEstSupprimeFalseAndClientEstSupprimeFalse(String id);

    @Query("""
        SELECT COUNT(cp) 
        FROM Compte cp 
        JOIN cp.client c 
        WHERE cp.estSupprime = false 
        AND c.estSupprime = false
    """)
    long countByEstSupprimeFalseAndClientEstSupprimeFalse();

    @Query("""
        SELECT cp 
        FROM Compte cp 
        WHERE cp.client.id = :clientId 
        AND cp.estSupprime = false
    """)
    List<Compte> findActiveComptesByClientId(@Param("clientId") Long clientId);

    long countByTypeAndEstSupprimeFalse(Type type);

    @Query("SELECT SUM(c.solde) FROM Compte c WHERE c.type = :type AND c.estSupprime = false")
    Double sumSoldeByType(@Param("type") Type type);

    long countByDateCreationBefore(LocalDateTime date);

    // Pour l'aperçu des comptes
    long countByType(Type type);
}
