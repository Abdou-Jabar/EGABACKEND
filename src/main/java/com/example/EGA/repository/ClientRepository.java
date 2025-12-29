package com.example.EGA.repository;

import com.example.EGA.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client,Long> {
    @Query("""
   SELECT DISTINCT c\s
   FROM Client c\s
   LEFT JOIN FETCH c.comptes cp\s
   WHERE c.estSupprime = false\s
     AND cp.estSupprime = false
""")
    List<Client> findClientsWithActiveComptes();
    Optional<Client> findByIdAndEstSupprimeFalse(Long id);
}
