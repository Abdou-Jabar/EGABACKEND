package com.example.EGA.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.EGA.dto.AjouterCompteDTO;
import com.example.EGA.dto.ModifierClientDTO;
import com.example.EGA.entity.Client;
import com.example.EGA.entity.Compte;
import com.example.EGA.repository.ClientRepository;
import com.example.EGA.repository.CompteRepository;
import com.example.EGA.service.ClientService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class ClientController {
    private final ClientRepository clientRepository;
    private final CompteRepository compteRepository;
    private final ClientService clientService;
    public ClientController(ClientService clientService, ClientRepository clientRepository, CompteRepository compteRepository) {
        this.clientService = clientService;
        this.clientRepository = clientRepository;
        this.compteRepository = compteRepository;
    }

    //Lister tous les clients
    @GetMapping("/client")
    public List<Client> findAll(){
        return clientRepository.findClientsWithActiveComptes();
    }

    //Récupérer un client via son Id
    @GetMapping("/client/{id}")
    public Client findById(@PathVariable Long id){
        return clientRepository.findById(id).orElseThrow(() -> new RuntimeException("Client non trouvé avec id = " + id));
    }

    @GetMapping("/client/{id}/comptes")
    public List<Compte> getComptesByClient(@PathVariable Long id) {
        return compteRepository.findActiveComptesByClientId(id);
    }

    @PostMapping("/client/ajouter")
    public Client ajouter(@RequestBody AjouterCompteDTO dto){
        return clientService.creerClientAvecCompte(
                dto.getClient(),
                dto.getTypeCompte()
        );
    }

    //Modifier un client
    @PutMapping("/client/modifier/{id}")
    public ResponseEntity<Client> update(@PathVariable Long id, @RequestBody ModifierClientDTO dto) {
        Client clientExistants = clientRepository.findById(id).orElseThrow();
        clientExistants.setNom(dto.getNom());
        clientExistants.setPrenom(dto.getPrenom());
        clientExistants.setEmail(dto.getEmail());
        clientExistants.setTelephone(dto.getTelephone());
        clientExistants.setNationalite(dto.getNationalite());

        return ResponseEntity.ok(clientRepository.save(clientExistants));
    }

    //Supprimer logiquement un client
    @PutMapping("client/supprimer/{id}")
    public ResponseEntity<String> supprimer(@PathVariable Long id) {
        clientService.supprimerClient(id);
        return ResponseEntity.ok("Client supprimé avec succès");
    }
}