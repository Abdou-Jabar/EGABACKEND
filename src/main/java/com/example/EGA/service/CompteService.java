package com.example.EGA.service;

import com.example.EGA.entity.Client;
import com.example.EGA.entity.Compte;
import com.example.EGA.model.Type;
import com.example.EGA.repository.ClientRepository;
import com.example.EGA.repository.CompteRepository;
import jakarta.transaction.Transactional;
import org.iban4j.Iban;
import org.springframework.stereotype.Service;

@Service
public class CompteService {
    private final CompteRepository compteRepository;
    private final ClientRepository clientRepository;
    private final EmailService emailService;

    public CompteService(CompteRepository compteRepository,
                         ClientRepository clientRepository,
                         EmailService emailService) {
        this.compteRepository = compteRepository;
        this.clientRepository = clientRepository;
        this.emailService = emailService;
    }

    public Compte creerCompte(Long clientId, Type typeCompte) {

        Client client = clientRepository
                .findByIdAndEstSupprimeFalse(clientId)
                .orElseThrow(() -> new RuntimeException("Client introuvable"));

        Compte compte = new Compte();
        compte.setType(typeCompte);
        compte.setClient(client);

        String iban = Iban.random().toString();
        compte.setId(iban);

        Compte saved = compteRepository.save(compte);

        if (client.getEmail() != null && !client.getEmail().isBlank()) {
            emailService.envoyerCreationCompte(
                    client.getEmail(),
                    client.getPrenom(),
                    iban
            );
        }

        return saved;
    }

    @Transactional
    public void supprimerCompte(String id) {

        Compte compte = compteRepository
                .findByIdAndEstSupprimeFalseAndClientEstSupprimeFalse(id)
                .orElseThrow(() ->new RuntimeException("Compte actif non trouvé"));

        compte.setEstSupprime(true);
        compteRepository.save(compte);
    }
}

