package com.example.EGA.service;

import com.example.EGA.entity.Client;
import com.example.EGA.entity.Compte;
import com.example.EGA.model.Type;
import com.example.EGA.repository.ClientRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ClientService {
    private final ClientRepository clientRepository;
    private final CompteService compteService;

    public ClientService(ClientRepository clientRepository,
                         CompteService compteService) {
        this.clientRepository = clientRepository;
        this.compteService = compteService;
    }
    @Transactional
    public void supprimerClient(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));
        for (Compte c : client.getComptes()) {
            c.setEstSupprime(true);
        }
        client.setEstSupprime(true);
        clientRepository.save(client);
    }

    @Transactional
    public Client creerClientAvecCompte(Client client, Type typeCompte) {

        // Sauvegarder le client
        Client savedClient = clientRepository.save(client);

        // Créer automatiquement un compte pour lui
        compteService.creerCompte(savedClient.getId(), typeCompte);

        return savedClient;
    }
}
