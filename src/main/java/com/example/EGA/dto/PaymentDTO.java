package com.example.EGA.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentDTO {

    @NotBlank(message = "Le numéro du compte source est obligatoire")
    private String compteSource;

    @NotBlank(message = "Le numéro du compte destination est obligatoire")
    private String compteDestination;

    @NotNull(message = "Le montant est obligatoire")
    @Positive(message = "Le montant doit être positif")
    private Double montant;

    @NotBlank(message = "Le mot de passe est obligatoire")
    private String motDePasse;
}
