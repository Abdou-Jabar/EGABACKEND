package com.example.EGA.dto;

import com.example.EGA.entity.Client;
import com.example.EGA.model.Type;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AjouterCompteDTO {
    private Client client;
    private Type typeCompte;
}
