package com.example.EGA.dto;

import com.example.EGA.model.Type;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//DTOCompte pour ne modifier que le type du compte
public class ModifierCompteDTO {
    private Type type;
}
