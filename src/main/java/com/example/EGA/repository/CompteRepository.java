package com.example.EGA.repository;

import com.example.EGA.entity.Compte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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


}
