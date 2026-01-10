package com.example.EGA.dto;

import com.example.EGA.entity.Client;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListeClientDTO {
    private Client client;
    Long nombreCompte;
    public ListeClientDTO(Client client, Long nombreCompte) {
        this.client = client;
        this.nombreCompte = nombreCompte;
    }
}
