package com.example.EGA.repository;

import java.util.List;
import java.util.Optional;

import com.example.EGA.dto.ListeClientDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.EGA.entity.Client;

public interface ClientRepository extends JpaRepository<Client,Long> {
        @Query("""
        SELECT new com.example.EGA.dto.ListeClientDTO(c, COUNT(cp))
        FROM Client c
        LEFT JOIN c.comptes cp
        WHERE c.estSupprime = false
        AND (cp.id IS NULL OR cp.estSupprime = false)
        GROUP BY c
    """)
    List<ListeClientDTO> findClientsWithActiveComptes();
    Optional<Client> findByIdAndEstSupprimeFalse(Long id);

    long countByEstSupprimeFalse();
}
