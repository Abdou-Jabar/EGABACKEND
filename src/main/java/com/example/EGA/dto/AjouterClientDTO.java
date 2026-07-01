package com.example.EGA.dto;

import java.time.LocalDate;

import com.example.EGA.model.Sexe;
import com.example.EGA.model.Type;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

// DTO d'entrée : on n'expose JAMAIS l'entité Client en @RequestBody
// pour éviter qu'un client malveillant envoie un mot de passe, un id, etc.
@Getter
@Setter
public class AjouterClientDTO {

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    @NotNull(message = "La date de naissance est obligatoire")
    @Past(message = "La date de naissance doit être dans le passé")
    private LocalDate dateNaissance;

    @NotNull(message = "Le sexe est obligatoire")
    private Sexe sexe;

    @NotBlank(message = "Le téléphone est obligatoire")
    @Pattern(regexp = "^(?:\\+228)?\\s?\\d{8}$", message = "Numéro de téléphone invalide")
    private String telephone;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Email invalide")
    private String email;

    @NotBlank(message = "La nationalité est obligatoire")
    private String nationalite;

    @NotNull(message = "Le type de compte est obligatoire")
    private Type typeCompte;
}
