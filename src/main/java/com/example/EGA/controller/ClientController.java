package com.example.EGA.controller;

import com.example.EGA.dto.AjouterClientDTO;
import com.example.EGA.dto.ModifierClientDTO;
import com.example.EGA.entity.Client;
import com.example.EGA.repository.ClientRepository;
import com.example.EGA.service.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ClientController {
    private final ClientRepository clientRepository;

    private final ClientService clientService;
    public ClientController(ClientService clientService, ClientRepository clientRepository) {
        this.clientService = clientService;
        this.clientRepository = clientRepository;
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

    @PostMapping("/client/ajouter")
    public Client ajouter(@RequestBody AjouterClientDTO dto){
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
        clientExistants.setSexe(dto.getSexe());
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