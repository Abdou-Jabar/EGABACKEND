package com.example.EGA.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WebhookPayload {

    private String statut;           // "SUCCES" ou "ECHEC"
    private String compteSource;
    private String compteDestination;
    private Double montant;
    private LocalDateTime dateTransaction;
    private String message;
}
