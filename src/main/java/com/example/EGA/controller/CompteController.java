package com.example.EGA.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.EGA.dto.ModifierCompteDTO;
import com.example.EGA.entity.Compte;
import com.example.EGA.model.Type;
import com.example.EGA.repository.CompteRepository;
import com.example.EGA.service.CompteService;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "http://localhost:4200")
public class CompteController {
    private final CompteRepository compteRepository;

    private final CompteService compteService;
    public CompteController(CompteService compteService, CompteRepository compteRepository) {
        this.compteService = compteService;
        this.compteRepository = compteRepository;
    }

    //Lister tous les comptes
    @GetMapping("/compte")
    public List<Compte> findAll(){
        return compteRepository.findAllByClient();
    }
    
    //Récupérer un compte via son Id
    @GetMapping("/compte/{id}")
    public Compte findById(@PathVariable String id){
        return compteRepository
                .findByIdAndEstSupprimeFalseAndClientEstSupprimeFalse(id)
                .orElseThrow(() ->
                        new RuntimeException("Compte supprimé ou inexistant"));
    }

    //Ajouter un compte
    @PostMapping("/compte/ajouter")
    public ResponseEntity<Compte> creerCompte(
            @RequestParam Long id,
            @RequestParam Type type) {
        Compte compte = compteService.creerCompte(id, type);
        return ResponseEntity.status(HttpStatus.CREATED).body(compte);
    }

    //Modifier un compte
    @PutMapping("/compte/modifier/{id}")
    public Compte modifier(@RequestBody ModifierCompteDTO dto, @PathVariable String id) {
        Compte compte = compteRepository
                .findByIdAndEstSupprimeFalseAndClientEstSupprimeFalse(id)
                .orElseThrow(() ->
                        new RuntimeException("Compte supprimé ou inexistant"));
        compte.setType(dto.getType());

        return compteRepository.save(compte);
    }

    //Supprimer logiquement un compte
    @PutMapping("/compte/supprimer/{id}")
    public ResponseEntity<String> supprimer(@PathVariable String id) {
        compteService.supprimerCompte(id);
        return ResponseEntity.ok("Compte supprimé avec succès");
    }
}
