package com.example.EGA.dto;

import com.example.EGA.model.Sexe;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
//DTOClient pour modifier un client sans toucher à l'attribut estSupprime
public class ModifierClientDTO {
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String nationalite;
}
