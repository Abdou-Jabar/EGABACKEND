package com.example.EGA.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//DTOTransaction pour effectuer les différentes transactions
public class TransactionDTO {
    private String numeroCompteSource;
    private String numeroCompteDestination;
    private Double solde;
}
