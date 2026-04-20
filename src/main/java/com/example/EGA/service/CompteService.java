package com.example.EGA.service;

import com.example.EGA.entity.Client;
import com.example.EGA.entity.Compte;
import com.example.EGA.model.Type;
import com.example.EGA.repository.ClientRepository;
import com.example.EGA.repository.CompteRepository;
import jakarta.transaction.Transactional;
import org.iban4j.Iban;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CompteService {

    private final CompteRepository compteRepository;
    private final ClientRepository clientRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public CompteService(CompteRepository compteRepository,
                         ClientRepository clientRepository,
                         EmailService emailService,
                         PasswordEncoder passwordEncoder) {
        this.compteRepository = compteRepository;
        this.clientRepository = clientRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    public Compte creerCompte(Long clientId, Type typeCompte, String motDePasse) {

        Client client = clientRepository
                .findByIdAndEstSupprimeFalse(clientId)
                .orElseThrow(() -> new RuntimeException("Client introuvable"));

        Compte compte = new Compte();
        compte.setType(typeCompte);
        compte.setClient(client);
        compte.setMotDePasse(passwordEncoder.encode(motDePasse));

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

    /**
     * Vérifie que le mot de passe fourni correspond au hash stocké pour ce compte.
     * Utilisé lors d'un paiement pour authentifier le titulaire du compte source.
     */
    public boolean verifierMotDePasse(String numeroCompte, String motDePasse) {
        Compte compte = compteRepository
                .findByIdAndEstSupprimeFalse(numeroCompte)
                .orElseThrow(() -> new RuntimeException("Compte introuvable"));
        return passwordEncoder.matches(motDePasse, compte.getMotDePasse());
    }

    @Transactional
    public void supprimerCompte(String id) {
        Compte compte = compteRepository
                .findByIdAndEstSupprimeFalseAndClientEstSupprimeFalse(id)
                .orElseThrow(() -> new RuntimeException("Compte actif non trouvé"));

        compte.setEstSupprime(true);
        compteRepository.save(compte);
    }
}
